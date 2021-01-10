# GoCD tenantsecrets plugin

GoCD secrets plugin that provides multi-tenant secrets.

It allows generating GoCD secrets that are bound to a pipeline group. A bound secret
cannot be decrypted in any other pipeline group. This prevents someone with access to
other pipeline configs (especially in the pipeline-as-code context) from copying the
secret and making GoCD decrypt it in their own pipeline.

**Work in progress**
