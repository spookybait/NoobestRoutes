package noobestroutes.ui.util.elements.colorelement

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.texture.DynamicTexture
import noobestroutes.ui.ColorPalette
import noobestroutes.ui.util.ElementValue
import noobestroutes.ui.util.UiElement
import noobestroutes.ui.util.elements.colorelement.ColorElement.ColorElementsConstants.TEXT_BOX_THICKNESS
import noobestroutes.utils.render.Color
import noobestroutes.utils.render.RenderUtils
import noobestroutes.utils.render.roundedRectangle

class ColorElement(
    x: Float,
    y: Float,
    val w: Float,
    val h: Float,
    val radius: Float,
    override var elementValue: Color,
    val alphaEnabled: Boolean
) : UiElement(x, y), ElementValue<Color> {

    override val elementValueChangeListeners = mutableListOf<(Color) -> Unit>()
    var isOpen = false
    private inline val isHovered get() = isAreaHovered(x, y, w, h)

    object ColorElementsConstants {
        const val COLOR_POPOUT_GAP = 15f

        const val COLOR_BOX_SIZE = 150f
        const val COLOR_BOX_SIZE_HALF = COLOR_BOX_SIZE * 0.5f
        const val COLOR_BOX_RADIUS = 10f
        const val COLOR_BOX_CIRCLE_RADIUS = 8f
        const val COLOR_BOX_CIRCLE_THICKNESS = COLOR_BOX_CIRCLE_RADIUS * 0.225f

        val ALPHA_BACKGROUND = DynamicTexture(RenderUtils.loadBufferedImage("/assets/ui/AlphaBackground.png"))
        val HUE_GRADIENT = DynamicTexture(RenderUtils.loadBufferedImage("/assets/ui/HueGradient.png"))
        const val COLOR_SLIDER_HEIGHT = 150f
        const val COLOR_SLIDER_WIDTH = 14f
        const val COLOR_SLIDER_HEIGHT_HALF = COLOR_SLIDER_HEIGHT * 0.5f
        const val COLOR_SLIDER_WIDTH_HALF = COLOR_SLIDER_WIDTH * 0.5f
        const val COLOR_SLIDER_CIRCLE_RADIUS = COLOR_SLIDER_WIDTH * 0.55f
        const val COLOR_SLIDER_CIRCLE_BORDER_THICKNESS = COLOR_SLIDER_CIRCLE_RADIUS * 0.3f

        const val COLOR_POPOUT_WIDTH = COLOR_POPOUT_GAP * 3f + COLOR_BOX_SIZE + COLOR_SLIDER_WIDTH
        const val COLOR_POPOUT_ALPHA_WIDTH = COLOR_POPOUT_GAP + COLOR_SLIDER_WIDTH

        const val TEXT_BOX_HEIGHT = 35f
        const val TEXT_BOX_THICKNESS = 3f

        //idk why I had to subtract 4, but I did
        const val TEXT_BOX_WIDTH = (COLOR_BOX_SIZE + COLOR_SLIDER_WIDTH * 2 - COLOR_POPOUT_GAP * 2f) / 3
        const val TEXT_BOX_WIDTH_WITH_GAP = (COLOR_BOX_SIZE + COLOR_SLIDER_WIDTH * 2 - TEXT_BOX_THICKNESS) / 3
        const val COLOR_POPOUT_GAP_THIRD = COLOR_POPOUT_GAP / 3

        const val COLOR_POPOUT_HEIGHT = COLOR_BOX_SIZE + COLOR_POPOUT_GAP * 4f + TEXT_BOX_HEIGHT * 2

        //based off of image size
        const val ALPHA_BACKGROUND_WIDTH = 20f
        const val ALPHA_BACKGROUND_HEIGHT = 155f
    }

    init {
        addChild(
            ColorPopoutElement(0f, 0f, alphaEnabled, elementValue)
        )

    }

    override fun draw() {
        GlStateManager.pushMatrix()
        roundedRectangle(
            x,
            y,
            w,
            h,
            elementValue,
            ColorPalette.elementBackground,
            Color.Companion.TRANSPARENT,
            TEXT_BOX_THICKNESS,
            radius,
            radius,
            radius,
            radius,
            0.5f
        )

        uiChildren[0].updatePosition(x + 300f, y)
        uiChildren[0].visible = isOpen

        GlStateManager.popMatrix()
    }

    override fun mouseClicked(mouseButton: Int): Boolean {
        if (isHovered) {
            isOpen = !isOpen
            return true
        }
        return false
    }
}