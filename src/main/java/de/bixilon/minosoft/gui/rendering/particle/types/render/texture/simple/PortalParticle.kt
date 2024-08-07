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

package de.bixilon.minosoft.gui.rendering.particle.types.render.texture.simple

import de.bixilon.kotlinglm.pow
import de.bixilon.kotlinglm.vec3.Vec3d
import de.bixilon.minosoft.data.registries.identified.ResourceLocation
import de.bixilon.minosoft.data.registries.particle.data.ParticleData
import de.bixilon.minosoft.data.text.formatting.color.RGBColor
import de.bixilon.minosoft.gui.rendering.particle.ParticleFactory
import de.bixilon.minosoft.gui.rendering.util.vec.vec3.Vec3dUtil.EMPTY
import de.bixilon.minosoft.protocol.network.session.play.PlaySession
import de.bixilon.minosoft.util.KUtil.toResourceLocation

class PortalParticle(session: PlaySession, position: Vec3d, velocity: Vec3d, data: ParticleData? = null) : SimpleTextureParticle(session, Vec3d(position), Vec3d.EMPTY, data) {
    private val startPosition = Vec3d(position)

    override var scale: Float
        get() = super.scale * (1.0f - (1.0f - (floatAge / maxAge)).pow(2))
        set(value) {
            super.scale = value
        }

    init {
        this.velocity(velocity)
        this.position(position)

        this.scale = 0.1f * (random.nextFloat() * 0.2f + 0.5f)

        (random.nextFloat() * 0.6f + 0.4f).let {
            this.color = RGBColor(it * 0.9f, it * 0.3f, it)
        }

        this.maxAge = (random.nextInt(10) + 40)
        movement = false
    }

    override fun move(velocity: Vec3d) {
        forceMove(velocity)
    }

    override fun tick() {
        super.tick()
        if (dead) {
            return
        }

        val lifeTime = floatAge / maxAge
        val velocityMultiplier = 1.0f - (-lifeTime + lifeTime * lifeTime * 2.0f)

        this.position(startPosition + velocity * velocityMultiplier)
        this.position.y += 1.0 - lifeTime
    }


    companion object : ParticleFactory<PortalParticle> {
        override val identifier: ResourceLocation = "minecraft:portal".toResourceLocation()

        override fun build(session: PlaySession, position: Vec3d, velocity: Vec3d, data: ParticleData): PortalParticle {
            return PortalParticle(session, position, velocity, data)
        }
    }
}
