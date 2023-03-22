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

package de.bixilon.minosoft.gui.rendering.world.entities

import de.bixilon.minosoft.data.entities.block.BlockEntity
import de.bixilon.minosoft.gui.rendering.RenderContext
import de.bixilon.minosoft.gui.rendering.models.raw.block.legacy.SingleBlockRenderable

interface MeshedBlockEntityRenderer<E : BlockEntity> : BlockEntityRenderer<E>, SingleBlockRenderable {
    override var light: Int
        get() = 0
        set(value) {}

    override fun draw(context: RenderContext) = Unit
    override fun load() = Unit
    override fun unload() = Unit

}
