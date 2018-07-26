/*
 * ServerListPlus - https://git.io/slp
 * Copyright (C) 2014 Minecrell (https://github.com/Minecrell)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.minecrell.serverlistplus.core.config.yaml;

import static net.minecrell.serverlistplus.core.logging.Logger.WARN;

import net.minecrell.serverlistplus.core.ServerListPlusCore;
import net.minecrell.serverlistplus.core.config.io.IOHelper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;

public final class ServerListPlusYAML {
    private ServerListPlusYAML() {}

    private static final String HEADER_FILENAME = "header.txt";

    public static YAMLWriter createWriter(ServerListPlusCore core) {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // Configuration style
        dumperOptions.setWidth(Integer.MAX_VALUE);

        // Plugin classes are loaded from a different class loader, that's why we need this
        Constructor constructor = new UnknownConfigurationConstructor(core);
        Representer representer = new ConfigurationRepresenter(); // Skip null properties

        boolean outdatedYaml = false;

        try { // Try setting the newer property utils, but will fail on CraftBukkit
            representer.setPropertyUtils(new ConfigurationPropertyUtils(core));
        } catch (Throwable e) {
            outdatedYaml = true; // Meh, CraftBukkit is using an outdated SnakeYAML version
            core.getLogger().log(WARN, "Your server is using an outdated YAML version. The configuration might " +
                    "not work correctly.");
            // Use old fallback versions and apply fixes!
            representer = new OutdatedConfigurationRepresenter();
            representer.setPropertyUtils(new OutdatedConfigurationPropertyUtils(core));
        }

        String[] header = null;
        try { // Try loading the configuration header from the JAR file
            header = IOHelper.readLineArray(core.getClass().getResourceAsStream(HEADER_FILENAME));
        } catch (IOException e) {
            core.getLogger().log(WARN, e, "Unable to read configuration header!");
        }

        return new YAMLWriter(new SnakeYAML(dumperOptions, constructor, representer, outdatedYaml), header);
    }
}
