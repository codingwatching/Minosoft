/*
 * Minosoft
 * Copyright (C) 2020 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */

package de.bixilon.minosoft.data.entities.entities.animal;

import de.bixilon.minosoft.data.entities.EntityMetaDataFields;
import de.bixilon.minosoft.data.entities.EntityRotation;
import de.bixilon.minosoft.data.entities.Location;
import de.bixilon.minosoft.protocol.network.Connection;

import java.util.UUID;

public class Panda extends Animal {
    public Panda(Connection connection, int entityId, UUID uuid, Location location, EntityRotation rotation) {
        super(connection, entityId, uuid, location, rotation);
    }

    public int getUnhappyTimer() {
        return metaData.getSets().getInt(EntityMetaDataFields.PANDA_UNHAPPY_TIMER);
    }

    public int getSneezeTimer() {
        return metaData.getSets().getInt(EntityMetaDataFields.PANDA_SNEEZE_TIMER);
    }

    public int getEatTimer() {
        return metaData.getSets().getInt(EntityMetaDataFields.PANDA_EAT_TIMER);
    }

    public Gene getMainGene() {
        return Gene.values()[metaData.getSets().getInt(EntityMetaDataFields.PANDA_MAIN_GENE)];
    }

    public Gene getHiddenGene() {
        return Gene.values()[metaData.getSets().getInt(EntityMetaDataFields.PANDA_HIDDEN_GAME)];
    }

    private boolean getPandaFlag(int bitMask) {
        return metaData.getSets().getBitMask(EntityMetaDataFields.PANDA_FLAGS, bitMask);
    }

    public boolean isSneezing() {
        return getPandaFlag(0x02);
    }

    public boolean isRolling() {
        return getPandaFlag(0x04);
    }

    public boolean isSitting() {
        return getPandaFlag(0x08);
    }

    public boolean isOnBack() {
        return getPandaFlag(0x10);
    }

    public enum Gene {
        NORMAL,
        LAZY,
        WORRIED,
        PLAYFUL,
        BROWN,
        WEAK,
        AGGRESSIVE
    }
}
