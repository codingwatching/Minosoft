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

package de.bixilon.minosoft.protocol.packets.serverbound.play;

import de.bixilon.minosoft.logging.Log;
import de.bixilon.minosoft.protocol.network.Connection;
import de.bixilon.minosoft.protocol.packets.ServerboundPacket;
import de.bixilon.minosoft.protocol.protocol.OutPacketBuffer;
import de.bixilon.minosoft.protocol.protocol.Packets;

public class PacketWindowClickButton implements ServerboundPacket {

    final byte windowId;
    final byte buttonId; // up, middle, bottom (0, 1, 2); in later versions: lectern page, etc

    public PacketWindowClickButton(byte windowId, byte buttonId) {
        this.windowId = windowId;
        this.buttonId = buttonId;
    }

    @Override
    public OutPacketBuffer write(Connection connection) {
        OutPacketBuffer buffer = new OutPacketBuffer(connection, Packets.Serverbound.PLAY_CLICK_WINDOW_BUTTON);
        buffer.writeByte(windowId);
        buffer.writeByte(buttonId);
        return buffer;
    }

    @Override
    public void log() {
        Log.protocol(String.format("Sending Click Window Packet (windowId=%d, buttonId=%d)", windowId, buttonId));
    }
}