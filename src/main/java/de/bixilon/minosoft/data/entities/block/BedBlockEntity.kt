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

import de.bixilon.kutil.primitive.IntUtil.toInt
import de.bixilon.minosoft.data.colors.DyeColors
import de.bixilon.minosoft.data.registries.identified.Namespaces.minecraft
import de.bixilon.minosoft.data.registries.identified.ResourceLocation
import de.bixilon.minosoft.protocol.network.session.play.PlaySession

class BedBlockEntity(session: PlaySession) : BlockEntity(session) {
    var color = DyeColors.RED
        private set


    override fun updateNBT(nbt: Map<String, Any>) {
        color = DyeColors.getOrNull(nbt["color"]?.toInt()) ?: DyeColors.RED
    }

    companion object : BlockEntityFactory<BedBlockEntity> {
        override val identifier: ResourceLocation = minecraft("bed")

        override fun build(session: PlaySession): BedBlockEntity {
            return BedBlockEntity(session)
        }
    }
}
