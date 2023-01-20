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

repositories {
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.0.0")
    implementation("de.themoep:minedown-adventure:1.7.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.0.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    shadowJar {
        dependencies {
            include(dependency("de.themoep:minedown-adventure"))
        }

        relocate("de.themoep.minedown", "net.minecrell.serverlistplus.velocity.minedown")
    }
}