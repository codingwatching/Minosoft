/*
 * Minosoft
 * Copyright (C) 2020-2025 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */
package de.bixilon.minosoft.protocol.packets.s2c.play.block

import de.bixilon.minosoft.data.world.positions.BlockPosition
import de.bixilon.minosoft.modding.event.events.BlockBreakAnimationEvent
import de.bixilon.minosoft.protocol.network.session.play.PlaySession
import de.bixilon.minosoft.protocol.packets.s2c.PlayS2CPacket
import de.bixilon.minosoft.protocol.protocol.ProtocolVersions
import de.bixilon.minosoft.protocol.protocol.buffers.play.PlayInByteBuffer
import de.bixilon.minosoft.util.logging.Log
import de.bixilon.minosoft.util.logging.LogLevels
import de.bixilon.minosoft.util.logging.LogMessageType

class BlockBreakAnimationS2CP(buffer: PlayInByteBuffer) : PlayS2CPacket {
    /**
     * Entity id of the entity who is breaking the block
     */
    val entityId: Int = buffer.readVarInt()
    var blockPosition: BlockPosition = if (buffer.versionId < ProtocolVersions.V_14W03B) {
        buffer.readIntBlockPosition()
    } else {
        buffer.readBlockPosition()
    }
    val stage: Int = let {
        val value = buffer.readUnsignedByte()
        when {
            value < 0 -> -1
            value > 8 -> -1
            else -> value
        }
    }

    override fun handle(session: PlaySession) {
        val event = BlockBreakAnimationEvent(session, this)
        if (session.events.fire(event)) {
            return
        }
    }

    override fun log(reducedLog: Boolean) {
        Log.log(LogMessageType.NETWORK_IN, level = LogLevels.VERBOSE) { "Block break animation (entityId=$entityId, blockPosition=$blockPosition, stage=$stage)" }
    }
}
