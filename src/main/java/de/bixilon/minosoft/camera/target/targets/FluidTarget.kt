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

package de.bixilon.minosoft.camera.target.targets

import de.bixilon.kotlinglm.vec3.Vec3d
import de.bixilon.minosoft.data.direction.Directions
import de.bixilon.minosoft.data.registries.blocks.state.BlockState
import de.bixilon.minosoft.data.registries.blocks.state.PropertyBlockState
import de.bixilon.minosoft.data.registries.fluid.Fluid
import de.bixilon.minosoft.data.text.BaseComponent
import de.bixilon.minosoft.data.text.ChatComponent
import de.bixilon.minosoft.data.text.formatting.TextFormattable
import de.bixilon.minosoft.data.world.positions.BlockPosition
import java.util.*

class FluidTarget(
    position: Vec3d,
    distance: Double,
    direction: Directions,
    val state: BlockState,
    val blockPosition: BlockPosition,
    val fluid: Fluid,
) : GenericTarget(position, distance, direction), TextFormattable {

    override fun toString(): String {
        return toText().legacy
    }

    override fun toText(): ChatComponent {
        val text = BaseComponent()

        text += "Fluid target "
        text += blockPosition
        text += ": "
        text += fluid.identifier

        text += "\n"

        if (state is PropertyBlockState) {
            text += state.withProperties()
        }

        return text
    }

    override fun hashCode(): Int {
        return Objects.hash(blockPosition, state, distance, fluid)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is FluidTarget) return false
        return distance == other.distance && blockPosition == other.blockPosition && state == other.state && fluid == other.fluid
    }
}
