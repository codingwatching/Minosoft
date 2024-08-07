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

package de.bixilon.minosoft.terminal

import de.bixilon.kutil.observer.DataObserver.Companion.observe
import de.bixilon.kutil.shutdown.AbstractShutdownReason
import de.bixilon.kutil.shutdown.ShutdownManager
import de.bixilon.minosoft.config.profile.profiles.account.AccountProfileManager
import de.bixilon.minosoft.data.accounts.Account
import de.bixilon.minosoft.data.registries.blocks.types.building.stone.Bedrock
import de.bixilon.minosoft.data.registries.blocks.types.building.stone.StoneBlock
import de.bixilon.minosoft.local.LocalConnection
import de.bixilon.minosoft.local.generator.DebugGenerator
import de.bixilon.minosoft.local.generator.flat.FlatGenerator
import de.bixilon.minosoft.local.storage.DebugStorage
import de.bixilon.minosoft.protocol.address.ServerAddress
import de.bixilon.minosoft.protocol.network.NetworkConnection
import de.bixilon.minosoft.protocol.network.session.play.PlaySession
import de.bixilon.minosoft.protocol.network.session.play.PlaySessionStates.Companion.disconnected
import de.bixilon.minosoft.protocol.network.session.status.StatusSession
import de.bixilon.minosoft.protocol.versions.Version
import de.bixilon.minosoft.protocol.versions.Versions
import de.bixilon.minosoft.util.DNSUtil
import de.bixilon.minosoft.util.logging.Log
import de.bixilon.minosoft.util.logging.LogLevels
import de.bixilon.minosoft.util.logging.LogMessageType
import kotlin.system.exitProcess

object AutoConnect {

    private fun connect(session: PlaySession) {
        if (RunConfiguration.DISABLE_EROS) {
            session::state.observe(this) {
                if (it.disconnected) {
                    Log.log(LogMessageType.AUTO_CONNECT, LogLevels.INFO) { "Disconnected from server, exiting..." }
                    ShutdownManager.shutdown()
                }
            }
        }
        session::error.observe(this) { ShutdownManager.shutdown(reason = AbstractShutdownReason.CRASH) }
        session.connect()
    }


    private fun autoConnect(address: ServerAddress, version: Version, account: Account) {
        val session = PlaySession(
            connection = NetworkConnection(address, true), // TODO: native network
            account = account,
            version = version,
        )
        connect(session)
        Log.log(LogMessageType.AUTO_CONNECT, LogLevels.INFO) { "Connecting to $address, with version $version using account $account..." }
    }

    private fun debug(version: Version, account: Account) {
        val flat = let@{ it: PlaySession ->
            val stone = it.registries.block[StoneBlock.Block]?.states?.default

            return@let FlatGenerator(it.registries.biome["plains"], arrayOf(
                it.registries.block[Bedrock]?.states?.default,
                stone,
                stone,
                stone,
                stone,
                stone,
            ))
        }
        val session = PlaySession(
            connection = LocalConnection(::DebugGenerator, ::DebugStorage),
            account = account,
            version = version,
        )
        connect(session)
        Log.log(LogMessageType.AUTO_CONNECT, LogLevels.INFO) { "Connecting to debug, with version $version using account $account..." }
    }

    fun autoConnect(connectString: String) {
        // ToDo: Show those connections in eros
        val split = connectString.split(',')
        val address = split[0]
        val versionName = split.getOrNull(1) ?: "automatic"
        val version = Versions[versionName] ?: throw IllegalArgumentException("Auto connect: Version ($versionName) not found!")
        val accountProfile = AccountProfileManager.selected
        val account = accountProfile.entries[split.getOrNull(2)] ?: accountProfile.selected ?: throw RuntimeException("Auto connect: Account not found! Have you started normal before or added an account?")

        Log.log(LogMessageType.AUTO_CONNECT, LogLevels.INFO) { "Checking account..." }
        account.tryCheck(null)

        if (version == Versions.AUTOMATIC) {
            Log.log(LogMessageType.AUTO_CONNECT, LogLevels.INFO) { "Pinging server to get version..." }
            val ping = StatusSession(address)
            ping::status.observe(this) { autoConnect(ping.connection!!.address, ping.serverVersion ?: throw IllegalArgumentException("Could not determinate server's version!"), account) }
            ping::error.observe(this) { exitProcess(1) }
            ping.ping()
            return
        }

        // debug(version, account)

        autoConnect(DNSUtil.resolveServerAddress(address).first(), version, account)
    }
}
