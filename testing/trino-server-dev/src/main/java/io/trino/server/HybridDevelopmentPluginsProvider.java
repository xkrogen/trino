/*
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
package io.trino.server;

import com.google.inject.Inject;
import io.trino.server.PluginManager.PluginsProvider;

import static java.util.Objects.requireNonNull;

/**
 * Supports loading internal and external plugins simultaneously during development.
 * The `plugin.dir` value in `config.properties` is relative to `trino-server-dev` module.
 */
public class HybridDevelopmentPluginsProvider
        implements PluginsProvider
{
    private final DevelopmentPluginsProvider developmentPluginsProvider;
    private final ServerPluginsProvider serverPluginsProvider;

    @Inject
    public HybridDevelopmentPluginsProvider(DevelopmentPluginsProvider developmentPluginsProvider, ServerPluginsProvider serverPluginsProvider)
    {
        this.developmentPluginsProvider = requireNonNull(developmentPluginsProvider, "developmentPluginsProvider is null");
        this.serverPluginsProvider = requireNonNull(serverPluginsProvider, "serverPluginsProvider is null");
    }

    @Override
    public void loadPlugins(Loader loader, ClassLoaderFactory createClassLoader)
    {
        developmentPluginsProvider.loadPlugins(loader, createClassLoader);
        serverPluginsProvider.loadPlugins(loader, createClassLoader);
    }
}
