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

package de.bixilon.minosoft.gui.rendering.gui

import de.bixilon.kotlinglm.vec2.Vec2
import de.bixilon.kutil.latch.AbstractLatch
import de.bixilon.kutil.observer.DataObserver.Companion.observe
import de.bixilon.kutil.observer.DataObserver.Companion.observed
import de.bixilon.minosoft.config.key.KeyCodes
import de.bixilon.minosoft.data.registries.identified.Namespaces.minosoft
import de.bixilon.minosoft.gui.rendering.RenderContext
import de.bixilon.minosoft.gui.rendering.events.ResizeWindowEvent
import de.bixilon.minosoft.gui.rendering.framebuffer.IntegratedFramebuffer
import de.bixilon.minosoft.gui.rendering.gui.atlas.AtlasManager
import de.bixilon.minosoft.gui.rendering.gui.gui.GUIManager
import de.bixilon.minosoft.gui.rendering.gui.gui.dragged.DraggedManager
import de.bixilon.minosoft.gui.rendering.gui.gui.popper.PopperManager
import de.bixilon.minosoft.gui.rendering.gui.hud.HUDManager
import de.bixilon.minosoft.gui.rendering.gui.input.ModifierKeys
import de.bixilon.minosoft.gui.rendering.input.InputHandler
import de.bixilon.minosoft.gui.rendering.renderer.drawable.Drawable
import de.bixilon.minosoft.gui.rendering.renderer.renderer.AsyncRenderer
import de.bixilon.minosoft.gui.rendering.renderer.renderer.RendererBuilder
import de.bixilon.minosoft.gui.rendering.system.base.BlendingFunctions
import de.bixilon.minosoft.gui.rendering.system.window.KeyChangeTypes
import de.bixilon.minosoft.gui.rendering.util.vec.vec2.Vec2Util.EMPTY
import de.bixilon.minosoft.modding.event.listener.CallbackEventListener.Companion.listen
import de.bixilon.minosoft.protocol.network.session.play.PlaySession
import de.bixilon.minosoft.util.delegate.RenderingDelegate.observeRendering

class GUIRenderer(
    val session: PlaySession,
    override val context: RenderContext,
) : AsyncRenderer, InputHandler, Drawable {
    private val profile = session.profiles.gui
    override val renderSystem = context.system
    var scaledSize: Vec2 by observed(Vec2(context.window.size))
    val gui = GUIManager(this)
    val hud = HUDManager(this)
    val popper = PopperManager(this)
    val dragged = DraggedManager(this)
    var halfSize: Vec2 = Vec2()
        private set
    var resolutionUpdate = true
    override val framebuffer: IntegratedFramebuffer get() = context.framebuffer.gui
    val shader = context.system.createShader(minosoft("gui")) { GUIShader(it) }
    val atlas = AtlasManager(context)

    var currentMousePosition: Vec2 by observed(Vec2.EMPTY)
        private set

    override fun init(latch: AbstractLatch) {
        gui.init()
        hud.init()
        popper.init()
    }

    override fun postInit(latch: AbstractLatch) {
        shader.load()

        session.events.listen<ResizeWindowEvent> { updateResolution(Vec2(it.size)) }
        context.window::systemScale.observe(this) { updateResolution(systemScale = it) }
        profile::scale.observeRendering(this) { updateResolution(scale = it) }

        gui.postInit()
        hud.postInit()
        popper.postInit()
    }

    private fun updateResolution(windowSize: Vec2 = Vec2(context.window.size), scale: Float = profile.scale, systemScale: Vec2 = context.window.systemScale) {
        scaledSize = Vec2(windowSize.scale(systemScale, scale)) + 0.01f
        halfSize = scaledSize / 2.0f
        resolutionUpdate = true

        gui.onScreenChange()
        hud.onScreenChange()
        popper.onScreenChange()
        dragged.onScreenChange()
    }

    fun setup() {
        renderSystem.reset(
            blending = true,
            depthTest = false,
            sourceRGB = BlendingFunctions.SOURCE_ALPHA,
            destinationRGB = BlendingFunctions.ONE_MINUS_SOURCE_ALPHA,
            sourceAlpha = BlendingFunctions.ONE,
            destinationAlpha = BlendingFunctions.ONE_MINUS_SOURCE_ALPHA,
        )
        shader.use()
    }

    override fun onMouseMove(position: Vec2): Boolean {
        val scaledPosition = position.scale()
        currentMousePosition = scaledPosition
        return popper.onMouseMove(scaledPosition) || dragged.onMouseMove(scaledPosition) || gui.onMouseMove(scaledPosition)
    }

    override fun onCharPress(char: Int): Boolean {
        return popper.onCharPress(char) || dragged.onCharPress(char) || gui.onCharPress(char)
    }

    override fun onKey(code: KeyCodes, change: KeyChangeTypes): Boolean {
        return popper.onKey(code, change) || dragged.onKey(code, change) || gui.onKey(code, change)
    }

    override fun onScroll(scrollOffset: Vec2): Boolean {
        return popper.onScroll(scrollOffset) || dragged.onScroll(scrollOffset) || gui.onScroll(scrollOffset)
    }

    override fun prepareDrawAsync() {
        hud.drawAsync()
        gui.drawAsync()
        popper.drawAsync()
        dragged.drawAsync()
    }

    override fun draw() {
        hud.draw()
        gui.draw()
        popper.draw()
        dragged.draw()
        if (this.resolutionUpdate) {
            this.resolutionUpdate = false
        }
    }

    fun Vec2.scale(systemScale: Vec2 = context.window.systemScale, scale: Float = profile.scale): Vec2 {
        val totalScale = systemScale * scale
        return this / totalScale
    }

    fun isKeyDown(vararg keyCodes: KeyCodes): Boolean {
        return context.input.isKeyDown(*keyCodes)
    }

    fun isKeyDown(modifier: ModifierKeys): Boolean {
        return context.input.isKeyDown(modifier)
    }

    companion object : RendererBuilder<GUIRenderer> {

        override fun build(session: PlaySession, context: RenderContext): GUIRenderer {
            return GUIRenderer(session, context)
        }
    }
}
