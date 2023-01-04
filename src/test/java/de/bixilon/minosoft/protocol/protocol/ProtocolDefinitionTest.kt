/*
 * Minosoft
 * Copyright (C) 2020-2023 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */
package de.bixilon.minosoft.protocol.protocol

import de.bixilon.minosoft.data.registries.ResourceLocation
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ProtocolDefinitionTest {

    @Test
    fun testTickTime() {
        assertEquals(ProtocolDefinition.TICK_TIME, 50)
    }

    @Test
    fun testTicksPerSecond() {
        assertEquals(ProtocolDefinition.TICKS_PER_SECOND, 20)
    }

    @Test
    fun testDefaultNamespace() {
        assertEquals(ProtocolDefinition.DEFAULT_NAMESPACE, "minecraft")
    }

    @Test
    fun testSectionSize() {
        assertEquals(ProtocolDefinition.BLOCKS_PER_SECTION, 4096)
    }

    /**
     * @see [de.bixilon.minosoft.data.registries.ResourceLocation]
     */
    @Test
    fun testAllowedNamespaces() {
        // Should Pass
        assertEquals(ResourceLocation.ALLOWED_NAMESPACE_PATTERN.matches("minecraft"), true)
        assertEquals(ResourceLocation.ALLOWED_NAMESPACE_PATTERN.matches("min1234567890craft"), true)
        assertEquals(ResourceLocation.ALLOWED_NAMESPACE_PATTERN.matches("mine-craft"), true)
        assertEquals(ResourceLocation.ALLOWED_NAMESPACE_PATTERN.matches("mine_craft"), true)
        assertEquals(ResourceLocation.ALLOWED_NAMESPACE_PATTERN.matches("mine.craft"), true)
        // Should Fail
        assertEquals(ResourceLocation.ALLOWED_NAMESPACE_PATTERN.matches("MineCraft"), false)
        assertEquals(ResourceLocation.ALLOWED_NAMESPACE_PATTERN.matches("mine craft"), false)
        assertEquals(ResourceLocation.ALLOWED_NAMESPACE_PATTERN.matches("minecraft!"), false)
        assertEquals(ResourceLocation.ALLOWED_NAMESPACE_PATTERN.matches("^minecraft"), false)
        assertEquals(ResourceLocation.ALLOWED_NAMESPACE_PATTERN.matches("mine/craft"), false)
    }

    /**
     * @see [de.bixilon.minosoft.data.registries.ResourceLocation]
     */
    @Test
    fun testAllowedResourceLocationPaths() {
        // Should Pass
        assertEquals(ResourceLocation.ALLOWED_PATH_PATTERN.matches("minecraft"), true)
        assertEquals(ResourceLocation.ALLOWED_PATH_PATTERN.matches("min1234567890craft"), true)
        assertEquals(ResourceLocation.ALLOWED_PATH_PATTERN.matches("mine-craft"), true)
        assertEquals(ResourceLocation.ALLOWED_PATH_PATTERN.matches("mine_craft"), true)
        assertEquals(ResourceLocation.ALLOWED_PATH_PATTERN.matches("mine.craft"), true)
        assertEquals(ResourceLocation.ALLOWED_PATH_PATTERN.matches("mine/craft"), true)
        // Should Fail
        assertEquals(ResourceLocation.ALLOWED_PATH_PATTERN.matches("MineCraft"), false)
        assertEquals(ResourceLocation.ALLOWED_PATH_PATTERN.matches("mine craft"), false)
        assertEquals(ResourceLocation.ALLOWED_PATH_PATTERN.matches("minecraft!"), false)
        assertEquals(ResourceLocation.ALLOWED_PATH_PATTERN.matches("^minecraft"), false)
        assertEquals(ResourceLocation.ALLOWED_PATH_PATTERN.matches("mine//craft"), false)
        assertEquals(ResourceLocation.ALLOWED_PATH_PATTERN.matches("mine///craft"), false)
    }

}
