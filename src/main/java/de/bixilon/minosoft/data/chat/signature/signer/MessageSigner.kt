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

package de.bixilon.minosoft.data.chat.signature.signer

import de.bixilon.kutil.exception.Broken
import de.bixilon.minosoft.data.chat.signature.LastSeenMessageList
import de.bixilon.minosoft.data.text.ChatComponent
import de.bixilon.minosoft.protocol.network.session.play.PlaySession
import de.bixilon.minosoft.protocol.protocol.ProtocolVersions
import de.bixilon.minosoft.protocol.versions.Version
import java.security.PrivateKey
import java.time.Instant
import java.util.*

interface MessageSigner {

    fun reset(): Unit = Broken("Not yet implemented")

    fun signMessage(privateKey: PrivateKey, message: String, preview: ChatComponent?, salt: Long, sender: UUID, time: Instant, lastSeen: LastSeenMessageList): ByteArray


    companion object {

        fun forVersion(version: Version, session: PlaySession): MessageSigner {
            if (version < ProtocolVersions.V_1_19_1_PRE4) {
                return MessageSigner1(version)
            }
            if (version < ProtocolVersions.V_22W42A) {
                return MessageSigner2(version)
            }
            return MessageSigner3(version, session.sessionId)
        }
    }
}
