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
package de.bixilon.minosoft.protocol.packets.s2c.play.entity

import de.bixilon.kutil.cast.CastUtil.unsafeCast
import de.bixilon.minosoft.data.entities.entities.Entity
import de.bixilon.minosoft.data.entities.event.EntityEvent
import de.bixilon.minosoft.data.entities.event.EntityEvents
import de.bixilon.minosoft.protocol.network.session.play.PlaySession
import de.bixilon.minosoft.protocol.packets.s2c.PlayS2CPacket
import de.bixilon.minosoft.protocol.protocol.buffers.play.PlayInByteBuffer
import de.bixilon.minosoft.util.logging.Log
import de.bixilon.minosoft.util.logging.LogLevels
import de.bixilon.minosoft.util.logging.LogMessageType

class EntityEventS2CP(buffer: PlayInByteBuffer) : PlayS2CPacket {
    private val entityId: Int = buffer.readInt()
    private val eventId: Int = buffer.readUnsignedByte()

    override fun handle(session: PlaySession) {
        val entity = session.world.entities[entityId] ?: return
        val event = EntityEvents.get(session.version, entity, eventId)
        if (event == null) {
            Log.log(LogMessageType.NETWORK_IN, LogLevels.VERBOSE) { "Unknown entity event (entity=${entity.type.identifier}, id=$eventId)" }
            return
        }
        event.unsafeCast<EntityEvent<Entity>>().handle(entity)
    }

    override fun log(reducedLog: Boolean) {
        Log.log(LogMessageType.NETWORK_IN, level = LogLevels.VERBOSE) { "Entity event (entityId=$entityId, event=$eventId)" }
    }
}
