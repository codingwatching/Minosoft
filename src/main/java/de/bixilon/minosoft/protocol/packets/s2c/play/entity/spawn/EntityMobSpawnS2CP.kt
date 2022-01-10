/*
 * Minosoft
 * Copyright (C) 2020-2022 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */
package de.bixilon.minosoft.protocol.packets.s2c.play.entity.spawn

import de.bixilon.minosoft.data.entities.EntityRotation
import de.bixilon.minosoft.data.entities.entities.Entity
import de.bixilon.minosoft.data.entities.meta.EntityData
import de.bixilon.minosoft.modding.event.events.EntitySpawnEvent
import de.bixilon.minosoft.protocol.network.connection.play.PlayConnection
import de.bixilon.minosoft.protocol.packets.factory.LoadPacket
import de.bixilon.minosoft.protocol.packets.s2c.PlayS2CPacket
import de.bixilon.minosoft.protocol.protocol.PlayInByteBuffer
import de.bixilon.minosoft.protocol.protocol.ProtocolVersions
import de.bixilon.minosoft.terminal.RunConfiguration
import de.bixilon.minosoft.util.logging.Log
import de.bixilon.minosoft.util.logging.LogLevels
import de.bixilon.minosoft.util.logging.LogMessageType
import glm_.vec3.Vec3d
import java.util.*

@LoadPacket(threadSafe = false)
class EntityMobSpawnS2CP(buffer: PlayInByteBuffer) : PlayS2CPacket {
    val entityId: Int = buffer.readEntityId()
    val entityUUID: UUID? = if (buffer.versionId >= ProtocolVersions.V_15W31A) {
        buffer.readUUID()
    } else {
        null
    }
    val entity: Entity

    init {
        val typeId: Int = if (buffer.versionId < ProtocolVersions.V_16W32A) {
            buffer.readUnsignedByte()
        } else {
            buffer.readVarInt()
        }
        val position: Vec3d = if (buffer.versionId < ProtocolVersions.V_16W06A) {
            Vec3d(buffer.readFixedPointNumberInt(), buffer.readFixedPointNumberInt(), buffer.readFixedPointNumberInt())
        } else {
            buffer.readVec3d()
        }
        val rotation = EntityRotation(buffer.readAngle().toDouble(), buffer.readAngle().toDouble())
        val headYaw = buffer.readAngle()
        val velocity = buffer.readVelocity()

        val data: EntityData? = if (buffer.versionId < ProtocolVersions.V_19W34A) {
            buffer.readMetaData()
        } else {
            null
        }
        val entityType = buffer.connection.registries.entityTypeRegistry[typeId]
        entity = entityType.build(buffer.connection, position, rotation, data, buffer.versionId)!!
        entity.setHeadRotation(headYaw)
        entity.velocity = velocity
        data?.let {
            entity.data.sets.putAll(it.sets)
            if (RunConfiguration.VERBOSE_ENTITY_META_DATA_LOGGING) {
                Log.log(LogMessageType.OTHER, LogLevels.VERBOSE) { "Entity data (entityId=$entityId): ${entity.entityMetaDataAsString}" }
            }
        }
    }

    override fun handle(connection: PlayConnection) {
        connection.world.entities.add(entityId, entityUUID, entity)

        connection.fireEvent(EntitySpawnEvent(connection, this))
    }

    override fun log(reducedLog: Boolean) {
        Log.log(LogMessageType.NETWORK_PACKETS_IN, LogLevels.VERBOSE) { "Mob spawn (entityId=$entityId, entityUUID=$entityUUID, entity=$entity)" }
    }
}