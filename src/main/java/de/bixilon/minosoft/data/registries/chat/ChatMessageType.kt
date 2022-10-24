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

package de.bixilon.minosoft.data.registries.chat

import de.bixilon.kutil.json.JsonObject
import de.bixilon.kutil.json.JsonUtil.asJsonObject
import de.bixilon.minosoft.data.chat.ChatTextPositions
import de.bixilon.minosoft.data.registries.ResourceLocation
import de.bixilon.minosoft.data.registries.registries.Registries
import de.bixilon.minosoft.data.registries.registries.registry.RegistryItem
import de.bixilon.minosoft.data.registries.registries.registry.codec.ResourceLocationCodec

class ChatMessageType(
    override val resourceLocation: ResourceLocation,
    val chat: TypeProperties,
    val narration: TypeProperties?,
    val position: ChatTextPositions,
) : RegistryItem() {

    override fun toString(): String {
        return resourceLocation.toString()
    }

    companion object : ResourceLocationCodec<ChatMessageType> {

        override fun deserialize(registries: Registries?, resourceLocation: ResourceLocation, data: JsonObject): ChatMessageType {
            return ChatMessageType(
                resourceLocation = resourceLocation,
                chat = data["chat"].asJsonObject().let { TypeProperties.deserialize(it) },
                narration = data["chat"]?.asJsonObject()?.let { TypeProperties.deserialize(it) },
                position = data["position"]?.let { ChatTextPositions[it] } ?: ChatTextPositions.CHAT,
            )
        }
    }
}