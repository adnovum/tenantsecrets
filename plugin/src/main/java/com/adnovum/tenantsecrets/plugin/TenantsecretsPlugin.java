/*
 * Copyright 2019 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* Taken and adapted from github.com/gocd-contrib/secrets-skeleton-plugin. */

package com.adnovum.tenantsecrets.plugin;

import com.adnovum.tenantsecrets.plugin.executors.SecretConfigLookupExecutor;
import com.adnovum.tenantsecrets.plugin.models.SecretConfig;
import com.adnovum.tenantsecrets.plugin.validators.SecretConfigValidator;
import com.github.bdpiparva.plugin.base.dispatcher.BaseBuilder;
import com.github.bdpiparva.plugin.base.dispatcher.RequestDispatcher;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import static java.util.Collections.singletonList;

@Extension
public class TenantsecretsPlugin implements GoPlugin {
    private RequestDispatcher requestDispatcher;

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
        requestDispatcher = BaseBuilder
                .forSecrets()
                .v1()
                .icon("/plugin-icon.svg", "image/svg+xml")
                .configMetadata(SecretConfig.class, true)
                .configView("/secrets.template.html")
                .validateSecretConfig(new SecretConfigValidator())
                .lookup(new SecretConfigLookupExecutor())
                .build();
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest requestMessage) throws UnhandledRequestTypeException {
        return requestDispatcher.dispatch(requestMessage);
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return new GoPluginIdentifier("secrets", singletonList("1.0"));
    }
}
