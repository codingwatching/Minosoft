/*
 * Minosoft
 * Copyright (C) 2020 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */

package de.bixilon.minosoft.protocol.packets.s2c.play;

import de.bixilon.minosoft.modding.event.events.OpenSignEditorEvent;
import de.bixilon.minosoft.protocol.network.connection.PlayConnection;
import de.bixilon.minosoft.protocol.packets.s2c.PlayS2CPacket;
import de.bixilon.minosoft.protocol.protocol.PlayInByteBuffer;
import de.bixilon.minosoft.util.logging.Log;
import glm_.vec3.Vec3i;

import static de.bixilon.minosoft.protocol.protocol.ProtocolVersions.V_14W03B;

public class PacketOpenSignEditor extends PlayS2CPacket {
    private final Vec3i position;

    public PacketOpenSignEditor(PlayInByteBuffer buffer) {
        if (buffer.getVersionId() < V_14W03B) {
            this.position = buffer.readIntBlockPosition();
            return;
        }
        this.position = buffer.readBlockPosition();
    }

    @Override
    public void handle(PlayConnection connection) {
        OpenSignEditorEvent event = new OpenSignEditorEvent(connection, this);
        if (connection.fireEvent(event)) {
            return;
        }
    }

    @Override
    public void log() {
        Log.protocol(String.format("[IN] Opening sign editor: %s", this.position));
    }

    public Vec3i getPosition() {
        return this.position;
    }
}
