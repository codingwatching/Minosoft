/*
 * Minosoft
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

package de.bixilon.minosoft.protocol.packets.clientbound.play;

import de.bixilon.minosoft.data.entities.Location;
import de.bixilon.minosoft.logging.Log;
import de.bixilon.minosoft.protocol.packets.ClientboundPacket;
import de.bixilon.minosoft.protocol.protocol.InByteBuffer;
import de.bixilon.minosoft.protocol.protocol.PacketHandler;

public class PacketFacePlayer implements ClientboundPacket {
    PlayerFaces face;
    Location location;
    int entityId = -1;
    PlayerFaces entityFace;

    @Override
    public boolean read(InByteBuffer buffer) {
        face = PlayerFaces.byId(buffer.readVarInt());
        location = buffer.readLocation();
        if (buffer.readBoolean()) {
            // entity present
            entityId = buffer.readVarInt();
            entityFace = PlayerFaces.byId(buffer.readVarInt());
        }
        return true;
    }

    @Override
    public void handle(PacketHandler h) {
        h.handle(this);
    }

    @Override
    public void log() {
        Log.protocol(String.format("Received face player packet (face=%s, location=%s, entityId=%d, entityFace=%s)", face, location, entityId, entityFace));
    }

    public PlayerFaces getFace() {
        return face;
    }

    public Location getLocation() {
        return location;
    }

    public int getEntityId() {
        return entityId;
    }

    public PlayerFaces getEntityFace() {
        return entityFace;
    }

    public enum PlayerFaces {
        FEET,
        EYES;

        public static PlayerFaces byId(int id) {
            return values()[id];
        }
    }
}