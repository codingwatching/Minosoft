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

package de.bixilon.minosoft.game.datatypes.entities.objects;

import de.bixilon.minosoft.game.datatypes.entities.Location;
import de.bixilon.minosoft.game.datatypes.entities.ObjectInterface;
import de.bixilon.minosoft.game.datatypes.entities.Velocity;
import de.bixilon.minosoft.game.datatypes.entities.meta.EntityMetaData;


public class MinecartTNT extends Minecart implements ObjectInterface {

    public MinecartTNT(int entityId, UUID uuid, Location location, short yaw, short pitch, int additionalInt) {
        super(entityId, location, yaw, pitch, additionalInt, null);
    }

    public MinecartTNT(int entityId, UUID uuid, Location location, short yaw, short pitch, int additionalInt, Velocity velocity) {
        super(entityId, location, yaw, pitch, additionalInt, velocity);
    }

    public MinecartTNT(int entityId, UUID uuid, Location location, short yaw, short pitch, Velocity velocity, EntityMetaData.MetaDataHashMap sets, int protocolId) {
        super(entityId, location, yaw, pitch, 0, velocity);
    }

    @Override
    public MinecartType getType() {
        return MinecartType.TNT;
    }
}
