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

package de.bixilon.minosoft.data.mappings.entities

import de.bixilon.minosoft.data.entities.EntityRotation
import de.bixilon.minosoft.data.entities.entities.Entity
import de.bixilon.minosoft.data.entities.meta.EntityMetaData
import de.bixilon.minosoft.data.mappings.ResourceLocation
import de.bixilon.minosoft.protocol.network.connection.PlayConnection
import de.bixilon.minosoft.util.ResourceLocationAble
import glm_.vec3.Vec3

interface EntityFactory<T : Entity> : ResourceLocationAble {

    /**
     * Tweaks the entity resource location. Used for pre flattening versions.
     */
    fun tweak(connection: PlayConnection, entityMetaData: EntityMetaData?, versionId: Int): ResourceLocation {
        return RESOURCE_LOCATION
    }

    fun build(connection: PlayConnection, entityType: EntityType, position: Vec3, rotation: EntityRotation): T? {
        return null
    }
}
