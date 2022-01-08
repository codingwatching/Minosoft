/*
 * Minosoft
 * Copyright (C) 2021 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */
package de.bixilon.minosoft.protocol.packets.s2c.play


import de.bixilon.minosoft.data.world.ChunkData
import de.bixilon.minosoft.protocol.network.connection.play.PlayConnection
import de.bixilon.minosoft.protocol.packets.s2c.PlayS2CPacket
import de.bixilon.minosoft.protocol.protocol.PlayInByteBuffer
import de.bixilon.minosoft.protocol.protocol.ProtocolVersions
import de.bixilon.minosoft.util.KUtil
import de.bixilon.minosoft.util.chunk.LightUtil.readLightPacket
import de.bixilon.minosoft.util.logging.Log
import de.bixilon.minosoft.util.logging.LogLevels
import de.bixilon.minosoft.util.logging.LogMessageType
import glm_.vec2.Vec2i
import java.util.*

class ChunkLightDataS2CP(buffer: PlayInByteBuffer, chunkPositionGetter: () -> Vec2i = { Vec2i(buffer.readVarInt(), buffer.readVarInt()) }) : PlayS2CPacket {
    val chunkPosition: Vec2i = chunkPositionGetter()
    var trustEdges: Boolean = false
        private set
    val chunkData: ChunkData

    init {
        if (buffer.versionId >= ProtocolVersions.V_1_16_PRE3) {
            trustEdges = buffer.readBoolean()
        }

        val skyLightMask: BitSet
        val blockLightMask: BitSet

        if (buffer.versionId < ProtocolVersions.V_20W49A) {
            skyLightMask = KUtil.bitSetOf(buffer.readVarLong())
            blockLightMask = KUtil.bitSetOf(buffer.readVarLong())
            buffer.readVarLong() // emptyBlockLightMask
            buffer.readVarLong() // emptySkyLightMask
        } else {
            skyLightMask = BitSet.valueOf(buffer.readLongArray())
            blockLightMask = BitSet.valueOf(buffer.readLongArray())
            buffer.readLongArray() // emptySkyLightMask
            buffer.readLongArray() // emptyBlockLightMask
        }

        chunkData = readLightPacket(buffer, skyLightMask, blockLightMask, buffer.connection.world.dimension!!)
    }

    override fun log(reducedLog: Boolean) {
        if (reducedLog) {
            return
        }
        Log.log(LogMessageType.NETWORK_PACKETS_IN, level = LogLevels.VERBOSE) { "Chunk light data (position=$chunkPosition)" }
    }

    override fun handle(connection: PlayConnection) {
        val chunk = connection.world.getOrCreateChunk(chunkPosition)
        chunk.setData(chunkData)
    }
}
