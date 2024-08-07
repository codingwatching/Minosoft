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

package de.bixilon.minosoft.data.registries.dimension.effects.minecraft

import de.bixilon.minosoft.data.registries.dimension.DimensionProperties
import de.bixilon.minosoft.data.registries.dimension.effects.DimensionEffects
import de.bixilon.minosoft.data.registries.dimension.effects.FogEffects
import de.bixilon.minosoft.data.registries.identified.Namespaces.minecraft
import de.bixilon.minosoft.protocol.network.session.play.PlaySession

object OverworldEffects : DimensionEffects {
    override val identifier = minecraft("overworld")

    override val daylightCycle: Boolean get() = true
    override val skyLight: Boolean get() = true

    override val weather: Boolean get() = true
    override val sun: Boolean get() = true
    override val moon: Boolean get() = true
    override val stars: Boolean get() = true

    override val clouds: Boolean get() = true
    override fun getCloudHeight(session: PlaySession): IntRange {
        val height = session.world.dimension.height
        if (height > DimensionProperties.DEFAULT_HEIGHT) {
            return 192..196
        }
        return 128..132
    }

    override val fog = FogEffects()

    override fun toString(): String {
        return identifier.toString()
    }
}
