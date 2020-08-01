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

package de.bixilon.minosoft.protocol.packets.clientbound.play;

import de.bixilon.minosoft.game.datatypes.entities.EntityProperty;
import de.bixilon.minosoft.game.datatypes.entities.EntityPropertyKeys;
import de.bixilon.minosoft.logging.Log;
import de.bixilon.minosoft.protocol.packets.ClientboundPacket;
import de.bixilon.minosoft.protocol.protocol.InByteBuffer;
import de.bixilon.minosoft.protocol.protocol.PacketHandler;

import java.util.HashMap;
import java.util.UUID;

public class PacketEntityProperties implements ClientboundPacket {
    final HashMap<EntityPropertyKeys, EntityProperty> properties = new HashMap<>();
    int entityId;

    @Override
    public boolean read(InByteBuffer buffer) {
        if (buffer.getProtocolId() < 7) {
            entityId = buffer.readInt();
            int count = buffer.readInt();
            for (int i = 0; i < count; i++) {
                EntityPropertyKeys key = EntityPropertyKeys.byName(buffer.readString(), buffer.getProtocolId());
                double value = buffer.readDouble();
                short listLength = buffer.readShort();
                for (int ii = 0; ii < listLength; ii++) {
                    UUID uuid = buffer.readUUID();
                    double amount = buffer.readDouble();
                    ModifierAction operation = ModifierAction.byId(buffer.readByte());
                    // ToDo: modifiers
                }
                properties.put(key, new EntityProperty(value));
            }
            return true;
        }
        entityId = buffer.readVarInt();
        int count = buffer.readInt();
        for (int i = 0; i < count; i++) {
            EntityPropertyKeys key = EntityPropertyKeys.byName(buffer.readString(), buffer.getProtocolId());
            double value = buffer.readDouble();
            int listLength = buffer.readVarInt();
            for (int ii = 0; ii < listLength; ii++) {
                UUID uuid = buffer.readUUID();
                double amount = buffer.readDouble();
                ModifierAction operation = ModifierAction.byId(buffer.readByte());
                // ToDo: modifiers
            }
            properties.put(key, new EntityProperty(value));
        }
        return true;
    }

    @Override
    public void log() {
        Log.protocol(String.format("Received entity properties (entityId=%d)", entityId));
    }

    @Override
    public void handle(PacketHandler h) {
        h.handle(this);
    }

    public int getEntityId() {
        return entityId;
    }

    public enum ModifierAction {
        ADD(0),
        ADD_PERCENT(1),
        MULTIPLY(2);

        final int id;

        ModifierAction(int id) {
            this.id = id;
        }

        public static ModifierAction byId(int id) {
            for (ModifierAction a : values()) {
                if (a.getId() == id) {
                    return a;
                }
            }
            return null;
        }

        public int getId() {
            return id;
        }
    }
}
