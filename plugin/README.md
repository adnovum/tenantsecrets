# Skeleton Secrets Plugin

This is a skeleton plugin which implements the GoCD [Secret Plugin](https://plugin-api.gocd.org/current/secrets) endpoint. This plugin allows retrieving of secrets that are stored in encrypted files. This is merely a skeleton plugin that plugin developers can fork to get quickly started with writing notification plugins for GoCD.

## Getting started

* Edit the file `build.gradle`
* Edit the `SecretConfig.java` class to add any configuration fields that is needed for the secret manager of your choice.
* Edit the `secrets.template.html` file which contains the view for the configuration required to access secrets.
* Implement the `SecretConfigValidator.java` class to add validations for the secret configs.
* Implement the `SecretConfigLookupExecutor.java` class to return the resolved secrets on the basis of keys received in the request.

## Building the code base

To build the jar, run `./gradlew clean test assemble`

## License

```plain
Copyright 2019 ThoughtWorks, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## About the license and releasing your plugin under a different license

The skeleton code in this repository is licensed under the Apache 2.0 license. The license itself specifies the terms
under which derivative works may be distributed (the license also defines derivative works). The Apache 2.0 license is a
permissive open source license that has minimal requirements for downstream licensors/licensees to comply with.

This does not prevent your plugin from being licensed under a different license as long as you comply with the relevant
clauses of the Apache 2.0 license (especially section 4). Typically, you clone this repository and keep the existing
copyright notices. You are free to add your own license and copyright notice to any modifications.

This is not legal advice. Please contact your lawyers if needed.
