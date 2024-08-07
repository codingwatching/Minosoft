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
package de.bixilon.minosoft.data.entities.entities.monster.piglin

import de.bixilon.kotlinglm.vec3.Vec3d
import de.bixilon.minosoft.data.entities.EntityRotation
import de.bixilon.minosoft.data.entities.data.EntityData
import de.bixilon.minosoft.data.entities.data.EntityDataField
import de.bixilon.minosoft.data.entities.entities.SynchronizedEntityData
import de.bixilon.minosoft.data.registries.entities.EntityFactory
import de.bixilon.minosoft.data.registries.entities.EntityType
import de.bixilon.minosoft.data.registries.identified.Namespaces.minecraft
import de.bixilon.minosoft.data.registries.identified.ResourceLocation
import de.bixilon.minosoft.protocol.network.session.play.PlaySession
import de.bixilon.minosoft.protocol.protocol.ProtocolVersions

class Piglin(session: PlaySession, entityType: EntityType, data: EntityData, position: Vec3d, rotation: EntityRotation) : AbstractPiglin(session, entityType, data, position, rotation) {

    @get:SynchronizedEntityData
    override val isImmuneToZombification: Boolean
        get() = if (session.version.versionId < ProtocolVersions.V_20W27A) {
            super.isImmuneToZombification
        } else {
            data.getBoolean(IMMUNE_TO_ZOMBIFICATION_DATA, false)
        }

    @get:SynchronizedEntityData
    val isBaby: Boolean
        get() = data.getBoolean(IS_BABY_DATA, false)

    @get:SynchronizedEntityData
    val isChargingCrossbow: Boolean
        get() = data.getBoolean(IS_CHARGING_CROSSBOW_DATA, false)

    @get:SynchronizedEntityData
    val isDancing: Boolean
        get() = data.getBoolean(IS_DANCING_DATA, false)


    companion object : EntityFactory<Piglin> {
        override val identifier: ResourceLocation = minecraft("piglin")
        private val IMMUNE_TO_ZOMBIFICATION_DATA = EntityDataField("PIGLIN_IMMUNE_TO_ZOMBIFICATION")
        private val IS_BABY_DATA = EntityDataField("PIGLIN_IS_BABY")
        private val IS_CHARGING_CROSSBOW_DATA = EntityDataField("PIGLIN_IS_CHARGING_CROSSBOW")
        private val IS_DANCING_DATA = EntityDataField("PIGLIN_IS_DANCING")


        override fun build(session: PlaySession, entityType: EntityType, data: EntityData, position: Vec3d, rotation: EntityRotation): Piglin {
            return Piglin(session, entityType, data, position, rotation)
        }
    }
}
