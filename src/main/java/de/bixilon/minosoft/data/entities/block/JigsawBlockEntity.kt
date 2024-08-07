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

package de.bixilon.minosoft.data.entities.block

import de.bixilon.minosoft.data.registries.identified.Namespaces.minecraft
import de.bixilon.minosoft.data.registries.identified.ResourceLocation
import de.bixilon.minosoft.protocol.network.session.play.PlaySession
import de.bixilon.minosoft.util.KUtil.toResourceLocation

class JigsawBlockEntity(session: PlaySession) : BlockEntity(session) {
    var joint: String = "rollable"
        private set
    var name: ResourceLocation = minecraft("empty")
        private set
    var pool: ResourceLocation = minecraft("empty")
        private set
    var finalState: ResourceLocation = minecraft("empty")
        private set
    var target: ResourceLocation = minecraft("empty")
        private set


    override fun updateNBT(nbt: Map<String, Any>) {
        nbt["joint"]?.let { joint = it.toString() }
        nbt["name"]?.let { name = it.toResourceLocation() }
        nbt["pool"]?.let { pool = it.toResourceLocation() }
        nbt["finalState"]?.let { finalState = it.toResourceLocation() }
        nbt["target"]?.let { target = it.toResourceLocation() }
    }

    companion object : BlockEntityFactory<JigsawBlockEntity> {
        override val identifier: ResourceLocation = minecraft("jigsaw")

        override fun build(session: PlaySession): JigsawBlockEntity {
            return JigsawBlockEntity(session)
        }
    }
}
