package de.bixilon.minosoft.gui.rendering.gui.hud.elements.hotbar

import de.bixilon.minosoft.data.text.ChatComponent
import de.bixilon.minosoft.data.text.RGBColor.Companion.asColor
import de.bixilon.minosoft.gui.rendering.gui.elements.Element
import de.bixilon.minosoft.gui.rendering.gui.elements.primitive.ImageElement
import de.bixilon.minosoft.gui.rendering.gui.elements.text.TextElement
import de.bixilon.minosoft.gui.rendering.gui.hud.HUDRenderer
import de.bixilon.minosoft.gui.rendering.gui.hud.atlas.HUDAtlasElement
import de.bixilon.minosoft.gui.rendering.gui.mesh.GUIVertexConsumer
import de.bixilon.minosoft.gui.rendering.gui.mesh.GUIVertexOptions
import de.bixilon.minosoft.util.MMath.ceil
import glm_.vec2.Vec2i

abstract class AbstractHotbarHealthElement(hudRenderer: HUDRenderer) : Element(hudRenderer) {
    abstract val totalHealth: Float
    abstract val totalMaxHealth: Float
    var totalMaxHearts = 0
    var rows = 0
    var text = false
    private var textElement = TextElement(hudRenderer, "", parent = this)


    override fun forceSilentApply() {
        totalMaxHearts = (totalMaxHealth / 2).ceil

        text = totalMaxHearts > HP_TEXT_LIMIT
        if (text) {
            textElement.text = createText()

            _size = textElement.size
        } else {
            rows = totalMaxHearts / HEARTS_PER_ROW
            if (totalMaxHearts % HEARTS_PER_ROW != 0) {
                rows++
            }

            _size = Vec2i(HEARTS_PER_ROW, rows) * HEART_SIZE + Vec2i(1, 0) // 1 pixel is overlapping, so we have one more for the heart
        }

        cacheUpToDate = false
    }

    protected fun drawCanisters(offset: Vec2i, z: Int, consumer: GUIVertexConsumer, options: GUIVertexOptions?, atlasElement: HUDAtlasElement) {
        for (heart in 0 until totalMaxHearts) {
            val row = heart / HEARTS_PER_ROW
            val column = heart % HEARTS_PER_ROW

            val image = ImageElement(hudRenderer, atlasElement)

            image.render(offset + Vec2i(column, (rows - 1) - row) * HEART_SIZE, z, consumer, options)
        }
    }

    override fun forceRender(offset: Vec2i, z: Int, consumer: GUIVertexConsumer, options: GUIVertexOptions?): Int {
        return textElement.render(offset, z, consumer, options)
    }

    abstract fun createText(): ChatComponent


    companion object {
        val NORMAL_TEXT_COLOR = "#ff1313".asColor()
        private const val HP_PER_ROW = 20
        const val HEARTS_PER_ROW = HP_PER_ROW / 2
        val HEART_SIZE = Vec2i(8, 9)
        const val LAYERS = 2
        const val HP_TEXT_LIMIT = 40
    }
}