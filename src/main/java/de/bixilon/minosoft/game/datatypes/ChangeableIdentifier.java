/*
 * Codename Minosoft
 * Copyright (C) 2020 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *  This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */

package de.bixilon.minosoft.game.datatypes;

import de.bixilon.minosoft.game.datatypes.objectLoader.versions.Versions;

import java.util.TreeMap;

import static de.bixilon.minosoft.protocol.protocol.ProtocolDefinition.FLATTING_VERSION_ID;

public class ChangeableIdentifier extends VersionValueMap<String> {
    String mod = "minecraft";

    public ChangeableIdentifier(String legacy, String water) {
        values.put(Versions.getLowestVersionSupported().getProtocolVersion(), legacy);
        values.put(FLATTING_VERSION_ID, water);
    }

    public ChangeableIdentifier(String legacy, String water, String mod) {
        values.put(Versions.getLowestVersionSupported().getProtocolVersion(), legacy);
        values.put(FLATTING_VERSION_ID, water);
        this.mod = mod;
    }

    public ChangeableIdentifier(TreeMap<Integer, String> values) {
        this.values = values;
    }

    public ChangeableIdentifier(IdentifierSet... sets) {
        super(sets, true);
    }

    public ChangeableIdentifier(String name) {
        super(name);
    }

    public boolean isValidName(String name, int protocolId) {
        name = name.toLowerCase();
        if (name.indexOf(":") != 0) {
            String[] splittedName = name.split(":", 2);
            if (!mod.equals(splittedName[0])) {
                // mod is not correct
                return false;
            }
            name = splittedName[1];
            // split and check mod
        }

        return get(protocolId).equals(name);
    }
}
