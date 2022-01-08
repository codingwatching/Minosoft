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

package de.bixilon.minosoft.protocol.packets.s2c.play.bossbar

import de.bixilon.minosoft.data.bossbar.BossbarColors
import de.bixilon.minosoft.data.bossbar.BossbarNotches
import de.bixilon.minosoft.modding.event.EventInitiators
import de.bixilon.minosoft.modding.event.events.bossbar.BossbarStyleSetEvent
import de.bixilon.minosoft.protocol.network.connection.play.PlayConnection
import de.bixilon.minosoft.protocol.packets.s2c.PlayS2CPacket
import de.bixilon.minosoft.protocol.protocol.InByteBuffer
import de.bixilon.minosoft.util.logging.Log
import de.bixilon.minosoft.util.logging.LogLevels
import de.bixilon.minosoft.util.logging.LogMessageType
import java.util.*

class BossbarStyleSetS2CP(
    val uuid: UUID,
    buffer: InByteBuffer,
) : PlayS2CPacket {
    val color = BossbarColors[buffer.readVarInt()]
    val notches = BossbarNotches[buffer.readVarInt()]

    override fun handle(connection: PlayConnection) {
        val bossbar = connection.bossbarManager.bossbars[uuid] ?: return

        var changes = 0

        if (bossbar.color != color) {
            bossbar.color = color
            changes++
        }
        if (bossbar.notches != notches) {
            bossbar.notches = notches
            changes++
        }
        if (changes == 0) {
            return
        }

        connection.fireEvent(BossbarStyleSetEvent(connection, EventInitiators.SERVER, uuid, bossbar))
    }

    override fun log(reducedLog: Boolean) {
        Log.log(LogMessageType.NETWORK_PACKETS_IN, LogLevels.VERBOSE) { "Bossbar style set (uuid=$uuid, color=$color, notches=$notches)" }
    }
}
