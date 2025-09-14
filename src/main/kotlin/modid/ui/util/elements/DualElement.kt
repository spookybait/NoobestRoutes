package modid.ui.util.elements

import modid.ui.ColorPalette
import modid.ui.ColorPalette.buttonColor
import modid.ui.ColorPalette.clickGUIColor
import modid.ui.util.ElementValue
import modid.ui.util.UiElement
import modid.ui.util.animations.impl.EaseInOut
import modid.utils.render.ColorUtil.darker
import modid.utils.render.ColorUtil.darkerIf
import modid.utils.render.TextAlign
import modid.utils.render.roundedRectangle
import modid.utils.render.text
import net.minecraft.client.renderer.GlStateManager


class DualElement(
    val left: String,
    val right: String,
    x: Float,
    y: Float,
    val xScale: Float,
    val yScale: Float,
    override var elementValue: Boolean
) : UiElement(x, y), ElementValue<Boolean> {
    private val posAnim = EaseInOut(250)

    private inline val isRightHovered: Boolean
        get() = isAreaHovered(
            0f,
            -DUAL_ELEMENT_HALF_HEIGHT,
            DUAL_ELEMENT_HALF_WIDTH,
            DUAL_ELEMENT_HEIGHT
        )

    private inline val isLeftHovered: Boolean
        get() = isAreaHovered(
            -DUAL_ELEMENT_HALF_WIDTH,
            -DUAL_ELEMENT_HALF_HEIGHT,
            DUAL_ELEMENT_HALF_WIDTH,
            DUAL_ELEMENT_HEIGHT
        )


    companion object {
        const val DUAL_ELEMENT_WIDTH = 240f
        const val DUAL_ELEMENT_HEIGHT = 28f
        const val DUAL_ELEMENT_HALF_WIDTH = DUAL_ELEMENT_WIDTH * 0.5f
        const val DUAL_ELEMENT_HALF_HEIGHT = DUAL_ELEMENT_HEIGHT * 0.5f
        const val DUAL_LEFT_TEXT_POSITION = (DUAL_ELEMENT_WIDTH * 0.25) - DUAL_ELEMENT_HALF_WIDTH
        const val DUAL_RIGHT_TEXT_POSITION = (DUAL_ELEMENT_WIDTH * 0.75) - DUAL_ELEMENT_HALF_WIDTH

    }

    override val elementValueChangeListeners = mutableListOf<(Boolean) -> Unit>()

    override fun draw() {
        GlStateManager.pushMatrix()
        translate(x, y)
        scale(xScale, yScale)
        roundedRectangle(
            -DUAL_ELEMENT_HALF_WIDTH,
            -DUAL_ELEMENT_HALF_HEIGHT,
            DUAL_ELEMENT_WIDTH,
            DUAL_ELEMENT_HEIGHT,
            buttonColor,
            radius = 5f
        )
        val pos = posAnim.get(0f, DUAL_ELEMENT_HALF_WIDTH, !elementValue)
        roundedRectangle(
            -DUAL_ELEMENT_HALF_WIDTH + pos,
            -DUAL_ELEMENT_HALF_HEIGHT,
            DUAL_ELEMENT_HALF_WIDTH,
            DUAL_ELEMENT_HEIGHT,
            clickGUIColor.darker(0.8f),
            radius = 5f
        )

        text(
            left,
            DUAL_LEFT_TEXT_POSITION,
            0f,
            ColorPalette.textColor.darkerIf(isLeftHovered),
            12f,
            align = TextAlign.Middle
        )
        text(
            right,
            DUAL_RIGHT_TEXT_POSITION,
            0f,
            ColorPalette.textColor.darkerIf(isRightHovered),
            12f,
            align = TextAlign.Middle
        )

        GlStateManager.popMatrix()
    }

    override fun mouseClicked(mouseButton: Int): Boolean {
        if (mouseButton != 0) return false
        if (isLeftHovered && elementValue) {
            if (posAnim.start()) setValue(false)
            return true
        } else if (isRightHovered && !elementValue) {
            if (posAnim.start()) setValue(true)
            return true
        }
        return false
    }

}