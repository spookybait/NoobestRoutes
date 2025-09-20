package noobestroutes.ui.util.elements.colorelement

import net.minecraft.client.renderer.GlStateManager
import noobestroutes.ui.util.ElementValue
import noobestroutes.ui.util.UiElement
import noobestroutes.ui.util.elements.colorelement.ColorElement.ColorElementsConstants
import noobestroutes.utils.render.*
import noobestroutes.utils.render.ColorUtil.hsbMax
import noobestroutes.utils.render.ColorUtil.withAlpha

class ColorBoxElement(
    x: Float, y: Float,
    override var elementValue: Color
) : UiElement(x, y), ElementValue<Color> {
    override val elementValueChangeListeners = mutableListOf<(Color) -> Unit>()

    override fun draw() {
        GlStateManager.pushMatrix()
        translate(x - ColorElementsConstants.COLOR_BOX_SIZE_HALF, y - ColorElementsConstants.COLOR_BOX_SIZE_HALF)

        GlStateManager.translate(0f, 0f, 1f)
        circle(
            elementValue.saturation * ColorElementsConstants.COLOR_BOX_SIZE,
            (1 - elementValue.brightness) * ColorElementsConstants.COLOR_BOX_SIZE,
            ColorElementsConstants.COLOR_BOX_CIRCLE_RADIUS,
            elementValue.withAlpha(1f),
            Color.Companion.WHITE,
            ColorElementsConstants.COLOR_BOX_CIRCLE_THICKNESS
        )
        GlStateManager.translate(0f, 0f, -1f)

        stencilRoundedRectangle(
            0f,
            0f,
            ColorElementsConstants.COLOR_BOX_SIZE,
            ColorElementsConstants.COLOR_BOX_SIZE,
            ColorElementsConstants.COLOR_BOX_RADIUS
        )
        drawHSBBox(
            0f,
            0f,
            ColorElementsConstants.COLOR_BOX_SIZE,
            ColorElementsConstants.COLOR_BOX_SIZE,
            elementValue.hsbMax()
        )
        popStencil()


        GlStateManager.popMatrix()
        if (dragging) {
            elementValue.saturation = getMouseXPercentageInBounds(
                0f,
                ColorElementsConstants.COLOR_BOX_SIZE
            )
            elementValue.brightness = getMouseYPercentageInBounds(
                0f,
                ColorElementsConstants.COLOR_BOX_SIZE, true
            )
            invokeValueChangeListeners()
        }

    }

    var dragging = false
    val isHovered get() = isAreaHovered(
        0f,
        0f,
        ColorElementsConstants.COLOR_BOX_SIZE,
        ColorElementsConstants.COLOR_BOX_SIZE
    )
    override fun mouseClicked(mouseButton: Int): Boolean {
        if (mouseButton != 0) return false
        if (isHovered) {
            dragging = true
            return true
        }
        return false
    }

    override fun mouseReleased(): Boolean {
        dragging = false
        return false
    }
}