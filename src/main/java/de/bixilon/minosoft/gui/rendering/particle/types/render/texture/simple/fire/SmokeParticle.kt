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

package de.bixilon.minosoft.gui.rendering.particle.types.render.texture.simple.fire

import de.bixilon.kotlinglm.vec3.Vec3d
import de.bixilon.minosoft.data.registries.identified.ResourceLocation
import de.bixilon.minosoft.data.registries.particle.data.ParticleData
import de.bixilon.minosoft.gui.rendering.particle.ParticleFactory
import de.bixilon.minosoft.gui.rendering.particle.types.render.texture.simple.AscendingParticle
import de.bixilon.minosoft.protocol.network.session.play.PlaySession
import de.bixilon.minosoft.util.KUtil.toResourceLocation

class SmokeParticle(session: PlaySession, position: Vec3d, velocity: Vec3d, data: ParticleData? = null, scaleMultiplier: Float = 1.0f) : AscendingParticle(session, position, velocity, Vec3d(0.1), scaleMultiplier, 0.3f, 8, -0.1f, true, data) {


    companion object : ParticleFactory<SmokeParticle> {
        override val identifier: ResourceLocation = "minecraft:smoke".toResourceLocation()

        override fun build(session: PlaySession, position: Vec3d, velocity: Vec3d, data: ParticleData): SmokeParticle {
            return SmokeParticle(session, position, velocity, data)
        }
    }
}
