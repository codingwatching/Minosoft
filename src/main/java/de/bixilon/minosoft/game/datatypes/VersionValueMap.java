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

public class VersionValueMap<V> {
    TreeMap<Integer, V> values = new TreeMap<>();

    public VersionValueMap() {
    }

    public VersionValueMap(MapSet<Integer, V>[] sets, boolean unused) {
        for (MapSet<Integer, V> set : sets) {
            values.put(set.getKey(), set.getValue());
        }
    }

    public VersionValueMap(V value) {
        values.put(Versions.getLowestVersionSupported().getProtocolVersion(), value);
    }

    public V get(int protocolId) {
        return values.lowerEntry(protocolId).getValue();
    }

    public TreeMap<Integer, V> getAll() {
        return values;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        VersionValueMap<V> their = (VersionValueMap<V>) obj;
        return getAll().equals(their.getAll());
    }
}
