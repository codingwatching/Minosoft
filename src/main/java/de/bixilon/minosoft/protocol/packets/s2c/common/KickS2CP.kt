/*
 * Minosoft
 * Copyright (C) 2020-2024 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */
package de.bixilon.minosoft.protocol.packets.s2c.common

import de.bixilon.kutil.cast.CastUtil.unsafeCast
import de.bixilon.minosoft.data.text.ChatComponent
import de.bixilon.minosoft.modding.event.events.KickEvent
import de.bixilon.minosoft.protocol.network.NetworkConnection
import de.bixilon.minosoft.protocol.network.session.play.PlaySession
import de.bixilon.minosoft.protocol.network.session.play.PlaySessionStates
import de.bixilon.minosoft.protocol.packets.s2c.PlayS2CPacket
import de.bixilon.minosoft.protocol.protocol.ProtocolStates
import de.bixilon.minosoft.protocol.protocol.ProtocolVersions.V_23W42A
import de.bixilon.minosoft.protocol.protocol.buffers.play.PlayInByteBuffer
import de.bixilon.minosoft.util.logging.Log
import de.bixilon.minosoft.util.logging.LogLevels
import de.bixilon.minosoft.util.logging.LogMessageType

class KickS2CP(buffer: PlayInByteBuffer) : PlayS2CPacket {
    val reason: ChatComponent = if (buffer.session.connection.unsafeCast<NetworkConnection>().state == ProtocolStates.LOGIN && buffer.versionId >= V_23W42A) buffer.readChatComponent() else buffer.readNbtChatComponent()

    override fun handle(session: PlaySession) {
        if (!session.connection.active) {
            return // already disconnected, maybe timed out?
        }
        session.events.fire(KickEvent(session, reason))
        // got kicked
        session.terminate()
        if (session.connection.unsafeCast<NetworkConnection>().state == ProtocolStates.LOGIN) {
            session.state = PlaySessionStates.ERROR
        } else {
            session.state = PlaySessionStates.KICKED
        }
        Log.log(LogMessageType.NETWORK, LogLevels.WARN) { "Kicked from ${session.connection.identifier}: $reason" }
    }

    override fun log(reducedLog: Boolean) {
        Log.log(LogMessageType.NETWORK_IN, level = LogLevels.VERBOSE) { "Login kick (reason=$reason)" }
    }
}
