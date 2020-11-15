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

package de.bixilon.minosoft.data.entities.entities.monster.piglin;

import de.bixilon.minosoft.data.entities.EntityMetaDataFields;
import de.bixilon.minosoft.data.entities.EntityRotation;
import de.bixilon.minosoft.data.entities.Location;
import de.bixilon.minosoft.data.entities.entities.monster.Monster;
import de.bixilon.minosoft.protocol.network.Connection;

import java.util.UUID;

public abstract class AbstractPiglin extends Monster {
    public AbstractPiglin(Connection connection, int entityId, UUID uuid, Location location, EntityRotation rotation) {
        super(connection, entityId, uuid, location, rotation);
    }

    public boolean isImmuneToZombification() {
        return metaData.getSets().getBoolean(EntityMetaDataFields.ABSTRACT_PIGLIN_IMMUNE_TO_ZOMBIFICATION);
    }

    public boolean isBaby() {
        return metaData.getSets().getBoolean(EntityMetaDataFields.PIGLIN_IS_BABY);
    }

    public boolean isChargingCrossbow() {
        return metaData.getSets().getBoolean(EntityMetaDataFields.PIGLIN_IS_CHARGING_CROSSBOW);
    }

    public boolean isDancing() {
        return metaData.getSets().getBoolean(EntityMetaDataFields.PIGLIN_IS_DANCING);
    }

}
