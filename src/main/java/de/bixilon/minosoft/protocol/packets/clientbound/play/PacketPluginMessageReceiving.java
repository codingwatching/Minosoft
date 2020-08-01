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

public class PacketPluginMessageReceiving implements ClientboundPacket {
    String channel;
    byte[] data;


    @Override
    public boolean read(InByteBuffer buffer) {
        channel = buffer.readString();
        if (buffer.getProtocolId() < 32) {
            data = buffer.readBytes(buffer.readShort()); // first read length, then the data
        } else {
            data = buffer.readBytesLeft();
        }
        return true;
    }

    @Override
    public void log() {
        Log.protocol(String.format("Plugin message received in channel \"%s\" with %s bytes of data", channel, data.length));
    }

    @Override
    public void handle(PacketHandler h) {
        h.handle(this);
    }

    public String getChannel() {
        return channel;
    }

    public byte[] getData() {
        return data;
    }
}
