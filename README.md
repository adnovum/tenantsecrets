# GoCD tenantsecrets plugin

![CI](https://github.com/sandro-h/tenantsecrets/workflows/CI/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sandro-h_tenantsecrets&metric=alert_status)](https://sonarcloud.io/dashboard?id=sandro-h_tenantsecrets)

GoCD secrets plugin that provides multi-tenant secrets.

It allows generating GoCD secrets that are bound to a pipeline group. A bound secret
cannot be decrypted in any other pipeline group. This prevents someone with access to
other pipeline configs (especially in the pipeline-as-code context) from copying the
secret and making GoCD decrypt it in their own pipeline.

**Work in progress**
