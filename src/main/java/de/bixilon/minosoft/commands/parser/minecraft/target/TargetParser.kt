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

package de.bixilon.minosoft.commands.parser.minecraft.target

import de.bixilon.kutil.bit.BitByte.isBitMask
import de.bixilon.kutil.uuid.UUIDUtil.toUUID
import de.bixilon.minosoft.commands.errors.ExpectedArgumentError
import de.bixilon.minosoft.commands.parser.ArgumentParser
import de.bixilon.minosoft.commands.parser.factory.ArgumentParserFactory
import de.bixilon.minosoft.commands.parser.minecraft.target.targets.CommandEntityTarget
import de.bixilon.minosoft.commands.parser.minecraft.target.targets.identifier.name.InvalidNameError
import de.bixilon.minosoft.commands.parser.minecraft.target.targets.identifier.name.NameEntityTarget
import de.bixilon.minosoft.commands.parser.minecraft.target.targets.identifier.uuid.UUIDEntityTarget
import de.bixilon.minosoft.commands.parser.minecraft.target.targets.selector.SelectorEntityTarget
import de.bixilon.minosoft.commands.parser.minecraft.target.targets.selector.error.InvalidSelectorKeyError
import de.bixilon.minosoft.commands.parser.minecraft.target.targets.selector.error.InvalidTargetSelector
import de.bixilon.minosoft.commands.parser.minecraft.target.targets.selector.properties.EntityTargetProperties
import de.bixilon.minosoft.commands.parser.minecraft.target.targets.selector.properties.EntityTargetProperty
import de.bixilon.minosoft.commands.parser.minecraft.uuid.InvalidUUIDError
import de.bixilon.minosoft.commands.util.CommandReader
import de.bixilon.minosoft.commands.util.ReadResult
import de.bixilon.minosoft.data.registries.identified.ResourceLocation
import de.bixilon.minosoft.protocol.protocol.ProtocolDefinition
import de.bixilon.minosoft.protocol.protocol.buffers.play.PlayInByteBuffer
import de.bixilon.minosoft.util.KUtil.toResourceLocation

class TargetParser(
    val single: Boolean = false,
    val onlyPlayers: Boolean = false,
    val playerName: String = DEFAULT_PLAYER_NAME,
) : ArgumentParser<CommandEntityTarget> {
    override val examples: List<Any?> = listOf(playerName, "@a", "@p")

    override fun parse(reader: CommandReader): CommandEntityTarget {
        if (!reader.canPeek()) {
            throw ExpectedArgumentError(reader)
        }
        return if (reader.peek() == '@'.code) {
            reader.parseSelector()
        } else {
            parseEntityIdentifier(reader)
        }
    }

    fun CommandReader.parseSelector(): SelectorEntityTarget {
        unsafeRead('@'.code)
        val selectorChar = readNext() ?: throw ExpectedArgumentError(this)
        val selector = TargetSelectors.BY_CHAR[selectorChar.toChar()] ?: throw InvalidTargetSelector(this)

        val properties: Map<String, EntityTargetProperty> = readMap({ readKey() }, { readValue(it) }) ?: emptyMap()

        return SelectorEntityTarget(selector, properties)
    }

    private fun CommandReader.readKey(): String? {
        if (peek() == '"'.code) {
            return readUnquotedString()
        }
        return readUntil('='.code)
    }

    private fun CommandReader.readValue(key: ReadResult<String>): EntityTargetProperty {
        val target = EntityTargetProperties[key.result] ?: throw InvalidSelectorKeyError(this, key)
        return target.read(this)
    }

    fun parseEntityIdentifier(reader: CommandReader): CommandEntityTarget {
        val result = reader.readResult { reader.readUnquotedString() }
        if (result.result == null) {
            throw ExpectedArgumentError(reader)
        }
        if (result.result.length < 16) {
            val name = result.result
            if (ProtocolDefinition.MINECRAFT_NAME_VALIDATOR.matcher(name).matches()) {
                return NameEntityTarget(name)
            }
            throw InvalidNameError(reader, result)
        }

        try {
            return UUIDEntityTarget(result.result.toUUID())
        } catch (ignored: Throwable) {
            throw InvalidUUIDError(reader, result)
        }
    }

    companion object : ArgumentParserFactory<TargetParser> {
        override val identifier: ResourceLocation = "minecraft:entity".toResourceLocation()
        const val DEFAULT_PLAYER_NAME = "Bixilon"

        override fun read(buffer: PlayInByteBuffer): TargetParser {
            val flags = buffer.readUnsignedByte()
            val single = flags.isBitMask(0x01)
            val onlyPlayers = flags.isBitMask(0x02)
            return TargetParser(single, onlyPlayers, buffer.session.player.additional.name)
        }
    }
}
