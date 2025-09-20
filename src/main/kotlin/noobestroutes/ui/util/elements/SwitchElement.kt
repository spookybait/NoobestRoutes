package noobestroutes.ui.util.elements


import noobestroutes.ui.ColorPalette
import noobestroutes.ui.ColorPalette.buttonColor
import noobestroutes.ui.util.ElementValue
import noobestroutes.ui.util.UiElement
import noobestroutes.ui.util.animations.impl.ColorAnimation
import noobestroutes.ui.util.animations.impl.LinearAnimation
import noobestroutes.utils.render.Color
import noobestroutes.utils.render.ColorUtil.brighterIf
import noobestroutes.utils.render.circle
import noobestroutes.utils.render.roundedRectangle
import net.minecraft.client.renderer.GlStateManager


/**
 * Drawn from the center
 */
class SwitchElement(
    val scale: Float,
    override var elementValue: Boolean,
    x: Float,
    y: Float,
) : UiElement(x, y), ElementValue<Boolean> {

    companion object {
        const val SWITCH_WIDTH = 34f
        const val SWITCH_HEIGHT = 20f
        const val SWITCH_WIDTH_HALF = SWITCH_WIDTH * 0.5f
        const val SWITCH_HEIGHT_HALF = SWITCH_HEIGHT * 0.5f
        const val SWITCH_CIRCLE_RADIUS = 6f
        const val SWITCH_CIRCLE_OFFSET = SWITCH_CIRCLE_RADIUS * 1.5f
        const val SWITCH_CIRCLE_START = -SWITCH_WIDTH_HALF + SWITCH_CIRCLE_OFFSET
        const val SWITCH_CIRCLE_END = SWITCH_WIDTH_HALF - SWITCH_CIRCLE_OFFSET
    }

    override val elementValueChangeListeners = mutableListOf<(Boolean) -> Unit>()
    private val colorAnimation = ColorAnimation(250)
    private val linearAnimation = LinearAnimation<Float>(200)

    internal inline val isHovered
        get() =
            isAreaHovered(-SWITCH_WIDTH_HALF, -SWITCH_HEIGHT_HALF, SWITCH_WIDTH, SWITCH_HEIGHT)


    override fun draw() {
        GlStateManager.pushMatrix()
        translate(x, y)
        scale(scale, scale)
        val isHovered = this.isHovered
        val backgroundColor = colorAnimation.get(
            ColorPalette.clickGUIColor,
            buttonColor,
            elementValue
        )//.darkerIf(isHovered, 0.9f)

        roundedRectangle(
            -SWITCH_WIDTH_HALF,
            -SWITCH_HEIGHT_HALF,
            SWITCH_WIDTH,
            SWITCH_HEIGHT,
            buttonColor,
            9f
        )

        if (elementValue || linearAnimation.isAnimating()) {
            roundedRectangle(
                -SWITCH_WIDTH_HALF,
                -SWITCH_HEIGHT_HALF,
                linearAnimation.get(SWITCH_WIDTH, 9f, elementValue),
                SWITCH_HEIGHT,
                backgroundColor,
                9f
            )
        }

        circle(
            linearAnimation.get(SWITCH_CIRCLE_START, SWITCH_CIRCLE_END, !elementValue), 0, SWITCH_CIRCLE_RADIUS,
            Color(220, 220, 220).brighterIf(isHovered)
        )
        GlStateManager.popMatrix()
    }

    override fun mouseClicked(mouseButton: Int): Boolean {
        if (!isHovered || mouseButton != 0) return false
        if (colorAnimation.start()) {
            linearAnimation.start()
            setValue(!elementValue)
            return true
        }
        return false

    }
}