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

import de.bixilon.minosoft.logging.Log;
import de.bixilon.minosoft.protocol.packets.ClientboundPacket;
import de.bixilon.minosoft.protocol.protocol.InByteBuffer;
import de.bixilon.minosoft.protocol.protocol.PacketHandler;

public class PacketCraftRecipeResponse implements ClientboundPacket {
    byte windowId;
    int recipeId;
    String recipeName;

    @Override
    public boolean read(InByteBuffer buffer) {
        if (buffer.getProtocolId() < 346) { //ToDo: was this really in 346?
            windowId = buffer.readByte();
            recipeId = buffer.readVarInt();
            return true;
        }
        windowId = buffer.readByte();
        recipeName = buffer.readString();
        return true;
    }
    @Override
    public void log() {
        Log.protocol(String.format("Received Crafting recipe response (windowId=%d, recipeId=%d)", windowId, recipeId));
    }

    @Override
    public void handle(PacketHandler h) {
        h.handle(this);
    }

    public byte getWindowId() {
        return windowId;
    }

    public int getRecipeId() {
        return recipeId;
    }
}
