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

package de.bixilon.minosoft.gui.rendering.gui.elements.layout

import de.bixilon.minosoft.gui.rendering.gui.elements.Element
import de.bixilon.minosoft.gui.rendering.gui.hud.HUDRenderer
import de.bixilon.minosoft.gui.rendering.gui.mesh.GUIVertexConsumer
import de.bixilon.minosoft.gui.rendering.gui.mesh.GUIVertexOptions
import de.bixilon.minosoft.gui.rendering.util.vec.vec2.Vec2iUtil.EMPTY
import de.bixilon.minosoft.gui.rendering.util.vec.vec2.Vec2iUtil.max
import de.bixilon.minosoft.gui.rendering.util.vec.vec4.Vec4iUtil.offset
import de.bixilon.minosoft.gui.rendering.util.vec.vec4.Vec4iUtil.spaceSize
import de.bixilon.minosoft.util.KUtil.synchronizedListOf
import de.bixilon.minosoft.util.KUtil.toSynchronizedList
import glm_.vec2.Vec2i

class ZLayout(hudRenderer: HUDRenderer) : Element(hudRenderer) {
    private val children: MutableList<Element> = synchronizedListOf()
    override var cacheEnabled: Boolean = false // ToDo: Cache

    override fun forceRender(offset: Vec2i, z: Int, consumer: GUIVertexConsumer, options: GUIVertexOptions?): Int {
        var zOffset = 0
        for (child in children.toSynchronizedList()) {
            zOffset += child.render(margin.offset + offset, z + zOffset, consumer, options)
        }
        return zOffset
    }

    override fun forceSilentApply() {
        var size = Vec2i.EMPTY
        for (child in children.toSynchronizedList()) {
            child.silentApply()
            size = size.max(child.size)
        }
        this.size = size + margin.spaceSize
    }

    fun append(child: Element) {
        children += child
        forceApply()
    }

    fun add(index: Int, child: Element) {
        children.add(index, child)
        forceApply()
    }

    operator fun plusAssign(child: Element) = append(child)
}