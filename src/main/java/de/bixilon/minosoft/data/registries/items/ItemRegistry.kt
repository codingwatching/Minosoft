/*
 * Minosoft
 * Copyright (C) 2020-2022 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */

package de.bixilon.minosoft.data.registries.items

import de.bixilon.minosoft.data.registries.ResourceLocation
import de.bixilon.minosoft.data.registries.registries.Registries
import de.bixilon.minosoft.data.registries.registries.registry.AbstractRegistry
import de.bixilon.minosoft.data.registries.registries.registry.Registry
import de.bixilon.minosoft.data.registries.registries.registry.ResourceLocationDeserializer

class ItemRegistry(
    parent: Registry<Item>? = null,
) : Registry<Item>(parent = parent) {
    private var flattened = false

    override fun getOrNull(id: Int): Item? {
        return if (!flattened) {
            val itemId = id ushr 16
            val itemMeta = id and 0xFFFF

            var versionItemId = itemId shl 16
            if (itemMeta > 0 && itemMeta < Short.MAX_VALUE) {
                versionItemId = versionItemId or itemMeta
            }
            return super.getOrNull(versionItemId) ?: super.getOrNull(itemId shl 16) // ignore meta data ?
        } else {
            super.getOrNull(id)
        }
    }

    override fun initialize(data: Map<ResourceLocation, Any>?, registries: Registries?, deserializer: ResourceLocationDeserializer<Item>?, flattened: Boolean, metaType: MetaTypes, alternative: AbstractRegistry<Item>?): Registry<Item> {
        this.flattened = flattened
        return super.initialize(data, registries, deserializer, flattened, metaType, alternative)
    }
}
