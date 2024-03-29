# GoCD tenantsecrets plugin

![CI](https://github.com/adnovum/tenantsecrets/workflows/CI/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=adnovum_tenantsecrets&metric=alert_status)](https://sonarcloud.io/dashboard?id=adnovum_tenantsecrets)

GoCD secrets plugin that provides multi-tenant secrets.

It allows generating GoCD secrets that are bound to a tenant. A bound secret
cannot be decrypted by any other tenant using the same GoCD instance.
This prevents someone with access to other pipeline configs (especially in the pipeline-as-code context) from copying the
secret and making GoCD decrypt it in their own pipeline.

## Installation

### Install plugin

* Download the latest version of the plugin from the [releases page](https://github.com/adnovum/tenantsecrets/releases)
* Install it on the GoCD server as described in the [GoCD Plugin User Guide](https://docs.gocd.org/current/extension_points/plugin_user_guide.html).

### Install webservice

For users to generate secrets, there is a webservice that provides a UI and a REST endpoint.

The webservice can be run as a docker container. It is meant to be deployed on the same host as the GoCD server, for access to
its encryption key.

```shell
docker run -d -p 1717:1717 -v /path/to/gocd/cipher.aes:/config/cipher.aes:ro ghcr.io/adnovum/tenantsecrets:latest
```

It provides the following entrypoints:

* GUI: <http://localhost:1717/secrets>
* REST: <http://localhost:1717/secrets/api>

See "Generating secrets" on how to use these entrypoints.

Note: The webservice only provides a HTTP endpoint. You're encouraged to use the same HTTPS termination as for the GoCD server (e.g. nginx).

The following environment variables can be used to configure the webservice:

| Env. variable | Default | Description |
| ------------- | ------- | ----------- |
| `TENANTSECRETS_CIPHERFILE` | `/config/cipher.aes` | Path to cipher file for secret encryption. |
| `TENANTSECRETS_DOCLINK` | `https://github.com/adnovum/tenantsecrets` | The documentation link shown after encrypting a secret in the UI. |

## Administration

### Creating secret configs

GoCD admins have to create a secret configuration for each tenant that
wishes to use tenant secrets:

* **Id**: The identifier of the secret configuration, which will be [used by pipelines](#using-secrets). It is 
  recommended that you set it to the same value as the tenant identifier, because the plugin will not know about 
  this ID, so it is error prone if the two are not aligned.
* **Tenant identifier**: The unique identifier of the tenant to which the secrets will be bound. The plugin will use
  this ID during [secret generation](#generating-secrets) and lookups.
* **Cipher file**: Path to the master key for the encryption. Defaults to `/godata/config/cipher.aes`, which is the
  cipher file used by the gocd-server docker image. To generate one, you can use e.g., command `openssl rand -hex 16`.
* **Rules**: Make sure to restrict the secret config to the pipeline group or environment that only the tenant has access to.

![secret configuration](docs/secret_configuration.png)

### Tenant separation

The security of tenant secrets relies on properly configured authorization. Each tenant should have an
exclusive pipeline group or environment assigned to them.   
Otherwise an adversary can just place his pipeline into another tenant's pipeline group or environment and read the secrets bound
to that tenant.

* For pipelines configured via UI: users should not be allowed to create their own pipelines in arbitrary pipeline groups or environments.
* For pipeline-as-code configuration: Make sure to restrict config repos to specific pipeline groups and/or environments.

![config repo configuration](docs/restrict_config_repo.png)

## Generating secrets

### Via GUI

Once the webservice has been installed (see above), visit `http://localhost:1717/secrets` and
follow the instructions in the form.

### Via REST

POST your secret to `http://localhost:1717/secrets/api/<tenant identifier>`.

Example curl call:

```shell
curl 'http://localhost:1717/secrets/api/tenant1' -s -H 'Content-Type: text/plain' -d "my secret"
```

## Using secrets

The secret can be referenced in pipelines using the [secret param syntax](https://docs.gocd.org/current/configuration/secrets_management.html#step-4---define-secret-params).

```text
{{SECRET:[tenant1][AES:YZEax9ra1bkL6Td9:5dgUOEIZACwfZGcZoRu5]}}
```

For example in a YAML configuration:

```yaml
pipelines:
  mypipeline:
    environment_variables:
      LE_SECRET: "{{SECRET:[tenant1][AES:YZEax9ra1bkL6Td9:5dgUOEIZACwfZGcZoRu5]}}"
```

## Development

Build and run unit tests:

```shell
./gradlew build
```

To run the webservice locally in the IDE, you need to declare a cipher file. Easiest is to do so
in an `webservice/src/main/resources/application-default.yaml` file, which is never committed:

```yaml
tenantsecrets:
  cipherFile: /local/path/to/cipher.aes
```

Build docker image:

```shell
./gradlew bootBuildImage
```

Trigger a release on GitHub:

* Bump the `baseVersion` in `build.gradle`
* Push a release tag to trigger the release CI:

```shell
./gradlew release
```

# Contribution Guide

This project uses default IntelliJ IDEA code style settings, with overrides from EditorConfig.
