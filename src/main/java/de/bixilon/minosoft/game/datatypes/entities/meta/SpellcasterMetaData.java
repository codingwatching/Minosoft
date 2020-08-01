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
package de.bixilon.minosoft.game.datatypes.entities.meta;


public class SpellcasterMetaData extends LivingMetaData {

    public SpellcasterMetaData(MetaDataHashMap sets, int protocolId) {
        super(sets, protocolId);
    }

    public SpellTypes getSpell() {
        final int defaultValue = SpellTypes.NONE.getId();
        if (version.getVersionNumber() < ProtocolVersion.VERSION_1_11_2.getVersionNumber()) {
            return SpellTypes.byId(defaultValue);
        }
        return SpellTypes.byId(sets.getInt(super.getLastDataIndex() + 1, defaultValue));
    }

    @Override
    protected int getLastDataIndex() {
        return super.getLastDataIndex() + 1;
    }

    public enum SpellTypes {
        NONE(0),
        SUMMON_VEX(1),
        ATTACK(2),
        WOLOLO(3),
        DISAPPEAR(3),
        BLINDNESS(3);

        final int id;

        SpellTypes(int id) {
            this.id = id;
        }

        public static SpellTypes byId(int id) {
            for (SpellTypes type : values()) {
                if (type.getId() == id) {
                    return type;
                }
            }
            return null;
        }

        public int getId() {
            return id;
        }
    }
}
