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

package de.bixilon.minosoft.gui.rendering.gui.gui.screen.container

import de.bixilon.minosoft.config.key.KeyActions
import de.bixilon.minosoft.config.key.KeyBinding
import de.bixilon.minosoft.config.key.KeyCodes
import de.bixilon.minosoft.data.container.Container
import de.bixilon.minosoft.gui.rendering.gui.GUIRenderer
import de.bixilon.minosoft.gui.rendering.gui.gui.LayoutedGUIElement
import de.bixilon.minosoft.gui.rendering.gui.gui.screen.container.inventory.InventoryScreen
import de.bixilon.minosoft.gui.rendering.gui.gui.screen.container.inventory.LocalInventoryScreen
import de.bixilon.minosoft.modding.event.events.container.ContainerCloseEvent
import de.bixilon.minosoft.modding.event.events.container.ContainerOpenEvent
import de.bixilon.minosoft.modding.event.listener.CallbackEventListener.Companion.listen
import de.bixilon.minosoft.util.KUtil.toResourceLocation

object ContainerGUIManager {

    private fun registerLocalContainerEvent(guiRenderer: GUIRenderer) {
        guiRenderer.context.input.bindings.register("minosoft:local_inventory".toResourceLocation(), KeyBinding(
            KeyActions.PRESS to setOf(KeyCodes.KEY_E),
        )) { guiRenderer.gui.open(LocalInventoryScreen) }
    }

    fun open(guiRenderer: GUIRenderer, container: Container) {
        val screen = ContainerGUIFactories.build(guiRenderer, container) ?: throw Exception("Can not open $container: No factory! (Probably not yet implemented)")
        for (element in guiRenderer.gui.elementOrder.toList()) {
            if (element !is LayoutedGUIElement<*>) {
                continue
            }
            if (element.element !is ContainerScreen<*>) {
                continue
            }
            guiRenderer.gui.pop(element)
        }
        guiRenderer.gui.push(screen)
    }

    fun close(guiRenderer: GUIRenderer, container: Container) {
        for (element in guiRenderer.gui.elementOrder) {
            if (element !is LayoutedGUIElement<*>) {
                continue
            }
            if (element.element !is ContainerScreen<*>) {
                continue
            }
            if (element.element.container == container) {
                guiRenderer.gui.pop(element)
                break
            }
        }
    }

    fun register(guiRenderer: GUIRenderer) {
        guiRenderer.atlas.load(InventoryScreen.ATLAS)

        for (factory in ContainerGUIFactories) {
            factory.register(guiRenderer)
        }

        registerLocalContainerEvent(guiRenderer)

        val queue = guiRenderer.context.queue
        guiRenderer.session.events.listen<ContainerOpenEvent> { queue += { open(guiRenderer, it.container) } }
        guiRenderer.session.events.listen<ContainerCloseEvent> { queue += { close(guiRenderer, it.container) } }
    }
}
