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
package de.bixilon.minosoft.protocol.packets.s2c.play

import de.bixilon.minosoft.Minosoft
import de.bixilon.minosoft.data.entities.block.BlockEntity
import de.bixilon.minosoft.data.world.ChunkData
import de.bixilon.minosoft.data.world.biome.source.SpatialBiomeArray
import de.bixilon.minosoft.datafixer.BlockEntityFixer.fix
import de.bixilon.minosoft.gui.rendering.util.VecUtil.of
import de.bixilon.minosoft.gui.rendering.util.vec.vec3.Vec3iUtil.EMPTY
import de.bixilon.minosoft.protocol.network.connection.play.PlayConnection
import de.bixilon.minosoft.protocol.packets.s2c.PlayS2CPacket
import de.bixilon.minosoft.protocol.protocol.PlayInByteBuffer
import de.bixilon.minosoft.protocol.protocol.ProtocolVersions.*
import de.bixilon.minosoft.util.KUtil.toInt
import de.bixilon.minosoft.util.KUtil.toResourceLocation
import de.bixilon.minosoft.util.Util
import de.bixilon.minosoft.util.chunk.ChunkUtil
import de.bixilon.minosoft.util.logging.Log
import de.bixilon.minosoft.util.logging.LogLevels
import de.bixilon.minosoft.util.logging.LogMessageType
import de.bixilon.minosoft.util.nbt.tag.NBTUtil.asCompound
import de.bixilon.minosoft.util.nbt.tag.NBTUtil.compoundCast
import glm_.vec2.Vec2i
import glm_.vec3.Vec3i
import java.util.*

class ChunkDataS2CP(buffer: PlayInByteBuffer) : PlayS2CPacket() {
    val chunkPosition: Vec2i
    val chunkData: ChunkData = ChunkData()
    var unloadChunk: Boolean = false
        private set
    var heightMap: Map<String, Any>? = null
        private set
    private var isFullChunk = false

    init {
        val dimension = buffer.connection.world.dimension!!
        chunkPosition = buffer.readChunkPosition()
        if (buffer.versionId < V_20W45A) {
            isFullChunk = !buffer.readBoolean()
        }
        if (buffer.versionId < V_14W26A) { // ToDo
            val sectionBitMask = BitSet.valueOf(buffer.readByteArray(2))
            val addBitMask = BitSet.valueOf(buffer.readByteArray(2))

            // decompress chunk data
            val decompressed: PlayInByteBuffer = if (buffer.versionId < V_14W28A) {
                Util.decompress(buffer.readByteArray(buffer.readInt()), buffer.connection)
            } else {
                buffer
            }
            val chunkData = ChunkUtil.readChunkPacket(decompressed, dimension, sectionBitMask, addBitMask, !isFullChunk, dimension.hasSkyLight)
            if (chunkData == null) {
                unloadChunk = true
            } else {
                this.chunkData.replace(chunkData)
            }
        } else {
            if (buffer.versionId in V_1_16_PRE7 until V_1_16_2_PRE2) {
                buffer.readBoolean() // ToDo: ignore old data???
            }
            val sectionBitMask = when {
                buffer.versionId < V_15W34C -> BitSet.valueOf(buffer.readByteArray(2))
                buffer.versionId < V_15W36D -> BitSet.valueOf(buffer.readByteArray(4))
                buffer.versionId < V_21W03A -> BitSet.valueOf(longArrayOf(buffer.readVarInt().toLong()))
                buffer.versionId < V_21W37A -> BitSet.valueOf(buffer.readLongArray())
                else -> null
            }
            if (buffer.versionId >= V_18W44A) {
                heightMap = buffer.readNBT()?.compoundCast()
            }
            if (!isFullChunk && buffer.versionId < V_21W37A) {
                this.chunkData.biomeSource = SpatialBiomeArray(buffer.readBiomeArray())
            }
            val size = buffer.readVarInt()
            val lastBufferPosition = buffer.pointer

            if (buffer.versionId < V_21W37A) {
                val chunkData = ChunkUtil.readChunkPacket(buffer, dimension, sectionBitMask!!, null, !isFullChunk, dimension.hasSkyLight)
                if (chunkData == null) {
                    unloadChunk = true
                } else {
                    this.chunkData.replace(chunkData)
                }
            } else {
                this.chunkData.replace(ChunkUtil.readPaletteChunk(buffer, dimension, null, isFullChunk = true, containsSkyLight = false))
            }

            // set position to expected read positions; the server sometimes sends a bunch of useless zeros (~ 190k), thanks @pokechu22
            buffer.pointer = size + lastBufferPosition

            // block entities
            when {
                buffer.versionId < V_1_9_4 -> {
                }
                buffer.versionId < V_21W37A -> {
                    val blockEntities: MutableMap<Vec3i, BlockEntity> = mutableMapOf()
                    val positionOffset = Vec3i.of(chunkPosition, dimension.lowestSection, Vec3i.EMPTY)
                    for (i in 0 until buffer.readVarInt()) {
                        val nbt = buffer.readNBT().asCompound()
                        val position = Vec3i(nbt["x"]?.toInt() ?: continue, nbt["y"]?.toInt() ?: continue, nbt["z"]?.toInt() ?: continue) - positionOffset
                        val resourceLocation = (nbt["id"]?.toResourceLocation() ?: continue).fix()
                        val type = buffer.connection.registries.blockEntityTypeRegistry[resourceLocation] ?: let {
                            Log.log(LogMessageType.NETWORK_PACKETS_IN, level = LogLevels.WARN) { "Unknown block entity: $resourceLocation" }
                            null
                        } ?: continue
                        val entity = type.build(buffer.connection)
                        entity.updateNBT(nbt)
                        blockEntities[position] = entity
                    }
                    this.chunkData.blockEntities = blockEntities
                }
                else -> {
                    val blockEntities: MutableMap<Vec3i, BlockEntity> = mutableMapOf()

                    for (i in 0 until buffer.readVarInt()) {
                        val xz = buffer.readUnsignedByte()
                        val y = buffer.readShort()
                        val type = buffer.connection.registries.blockEntityTypeRegistry[buffer.readVarInt()]
                        val nbt = buffer.readNBT()?.asCompound() ?: continue
                        val entity = type.build(buffer.connection)
                        entity.updateNBT(nbt)
                        blockEntities[Vec3i(xz shr 4, y, xz and 0x0F)] = entity
                    }
                    this.chunkData.blockEntities = blockEntities
                }
            }

            if (buffer.versionId >= V_21W37A) {
                this.chunkData.replace(ChunkLightDataS2CP(buffer) { chunkPosition }.chunkData)
            }
        }
    }

    override fun handle(connection: PlayConnection) {
        if (unloadChunk) {
            connection.world.unloadChunk(chunkPosition)
            return
        }
        val chunk = connection.world.getOrCreateChunk(chunkPosition)
        chunk.setData(chunkData)
    }

    override fun log() {
        if (Minosoft.config.config.general.reduceProtocolLog) {
            return
        }
        Log.log(LogMessageType.NETWORK_PACKETS_IN, level = LogLevels.VERBOSE) { "Chunk data (chunkPosition=$chunkPosition)" }
    }
}