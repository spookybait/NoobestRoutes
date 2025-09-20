package noobestroutes.ui.clickgui.elements

import noobestroutes.Core
import noobestroutes.features.Category
import noobestroutes.features.ModuleManager.modules
import noobestroutes.features.impl.render.ClickGUIModule
import noobestroutes.font.FontRenderer
import noobestroutes.ui.ColorPalette
import noobestroutes.ui.ColorPalette.TEXT_OFFSET
import noobestroutes.ui.ColorPalette.clickGUIColor
import noobestroutes.ui.ColorPalette.moduleButtonColor
import noobestroutes.ui.ColorPalette.titlePanelColor
import noobestroutes.ui.util.MouseUtils.mouseX
import noobestroutes.ui.util.MouseUtils.mouseY
import noobestroutes.ui.util.UiElement
import noobestroutes.ui.util.animations.impl.ColorAnimation
import noobestroutes.ui.util.animations.impl.EaseInOut
import noobestroutes.ui.util.animations.impl.LinearAnimation
import noobestroutes.utils.render.*
import noobestroutes.utils.render.ColorUtil.brighter
import noobestroutes.utils.render.ColorUtil.withAlpha
import noobestroutes.utils.round
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.texture.DynamicTexture
import org.lwjgl.input.Keyboard
import kotlin.math.floor

class Panel(val name: String, val category: Category, val icon: DynamicTexture) : UiElement(ClickGUIModule.panelX[category]!!.value, ClickGUIModule.panelY[category]!!.value) {

    companion object {
        const val WIDTH = 240f
        const val HEIGHT = 40f
        const val HALF_HEIGHT = HEIGHT * 0.5f
        const val BORDER_THICKNESS = 3f
        const val DOUBLE_BORDER_THICKNESS = BORDER_THICKNESS * 2f
        const val HITBOX_WIDTH = WIDTH + DOUBLE_BORDER_THICKNESS
        const val HITBOX_HEIGHT = HEIGHT + DOUBLE_BORDER_THICKNESS

        const val BOTTOM_SEGMENT_HEIGHT = 10f
        const val PANEL_RADIUS = 10f
        const val HIGHLIGHT_THICKNESS = 2f

        private const val IMAGE_SIZE = 32f
        private const val IMAGE_X = WIDTH * 0.9f - IMAGE_SIZE * 0.5f
        private const val IMAGE_Y = HEIGHT * 0.5f - IMAGE_SIZE * 0.5f
    }

    fun updatingModuleButtons(){
        uiChildren.forEach {
            (it as ModuleButton).updateElements()
        }
    }

    private fun drawIcon(){
        drawDynamicTexture(icon, IMAGE_X, IMAGE_Y, IMAGE_SIZE, IMAGE_SIZE)
    }


    private var dragging = false

    var extended: Boolean = ClickGUIModule.panelExtended[category]!!.enabled

    private var length = 0f

    private var x2 = 0f
    private var y2 = 0f

    private var scrollTarget = 0f
    private var scrollOffset = 0f
    private val scrollAnimation = LinearAnimation<Float>(200)
    private val extendAnim = EaseInOut(250)
    private val colorAnimation = ColorAnimation(200)
    private inline val bottomSegmentColor get() = colorAnimation.get(titlePanelColor, moduleButtonColor, !extended)
    private inline val separatorColor get() = colorAnimation.get(titlePanelColor.withAlpha(0f) , clickGUIColor.brighter(1.65f), !extended)

    init {
        for (module in modules.sortedByDescending { getTextWidth(it.name, 18f) }) {
            if (module.category != this@Panel.category) continue
            if (module.devOnly && !Core.DEV_MODE) {
                if (module.enabled) module.onDisable()
                module.keybinding?.key = Keyboard.KEY_NONE
                module.notPersistent = true
                continue
            }
            addChild(ModuleButton(0f, module))
        }
        //if (uiChildren.isEmpty()) ClickGUIBase.removePanel(this)
    }
    val isNotEmpty = uiChildren.isNotEmpty()


    private inline val isHovered get() = isAreaHovered(-BORDER_THICKNESS, -BORDER_THICKNESS, HITBOX_WIDTH, HITBOX_HEIGHT)

    private val isMouseOverExtended
        get() = extended && isAreaHovered(-BORDER_THICKNESS, -BORDER_THICKNESS, HITBOX_WIDTH, length.coerceAtLeast(HEIGHT) + DOUBLE_BORDER_THICKNESS)

    private fun getTotalHeight(offset: Float): Float {
        var y = offset
        uiChildren.forEach { y += (it as ModuleButton).getHeight() }
        return y
    }


    override fun doHandleDraw() {
        GlStateManager.pushMatrix()
        scrollOffset = scrollAnimation.get(scrollOffset, scrollTarget).round(0).toFloat()
        val offset = floor(extendAnim.get(0f, getTotalHeight(scrollOffset), !extended))
        if (dragging) {
            updatePosition(floor(x2 + mouseX), floor(y2 + mouseY))
        }
        translate(x, y)
        blurRoundedRectangle(-BORDER_THICKNESS, -BORDER_THICKNESS, WIDTH + DOUBLE_BORDER_THICKNESS, offset + HEIGHT + BOTTOM_SEGMENT_HEIGHT + DOUBLE_BORDER_THICKNESS, PANEL_RADIUS, PANEL_RADIUS, PANEL_RADIUS, PANEL_RADIUS, 0.5f)

        rectangleOutline(
            -BORDER_THICKNESS,
            -BORDER_THICKNESS,
            WIDTH + DOUBLE_BORDER_THICKNESS,
            offset + HEIGHT + BOTTOM_SEGMENT_HEIGHT + DOUBLE_BORDER_THICKNESS,
            titlePanelColor,
            10f,
            3f
        )



        roundedRectangle(
            0f, 0f, WIDTH, HEIGHT,
            titlePanelColor, Color.TRANSPARENT, Color.TRANSPARENT,
            0f, PANEL_RADIUS, PANEL_RADIUS, 0f, 0f, 2.6f
        )
        text(name, TEXT_OFFSET, HALF_HEIGHT, ColorPalette.textColor, 16f, FontRenderer.BOLD,TextAlign.Left)
        drawIcon()


        if (extended || extendAnim.isAnimating()) {
            stencilRoundedRectangle(-3f, HEIGHT, WIDTH + 3f, offset)
            var startY = scrollOffset + HEIGHT
            if (uiChildren.isNotEmpty()) {
                for (button in uiChildren) {
                    button.visible = true
                    button.updatePosition(0f, startY)
                    button.doHandleDraw()
                    startY += (button as ModuleButton).getHeight()
                }
                length = startY + 5f
            }
            popStencil()
            roundedRectangle(-BORDER_THICKNESS, HEIGHT - HIGHLIGHT_THICKNESS, WIDTH + DOUBLE_BORDER_THICKNESS, HIGHLIGHT_THICKNESS, separatorColor)
        } else {
            uiChildren.forEach {
                it.visible = false
            }
        }

        roundedRectangle(
            0f, offset + HEIGHT, WIDTH, BOTTOM_SEGMENT_HEIGHT, bottomSegmentColor, bottomSegmentColor, bottomSegmentColor,
            0f, 0f, 0f, PANEL_RADIUS, PANEL_RADIUS, 3f
        )


        GlStateManager.popMatrix()
    }

    override fun mouseClicked(mouseButton: Int): Boolean {
        if (!isHovered) return false
        if (mouseButton == 0) {
            x2 = x - mouseX
            y2 = y - mouseY
            dragging = true
            return true
        }
        if (mouseButton == 1) {
            if (extendAnim.start()) {
                extended = !extended
                colorAnimation.start()
            }
            return true
        }
        return false
    }

    override fun onScroll(amount: Int): Boolean {
        if (!isMouseOverExtended) return false
        scrollTarget = (scrollTarget + amount).coerceIn((-length + scrollOffset + 72f).coerceAtMost(0f), 0f)
        scrollAnimation.start(true)
        return true
    }


    override fun mouseReleased(): Boolean {
        dragging = false

        ClickGUIModule.panelX[category]!!.value = x
        ClickGUIModule.panelY[category]!!.value = y
        ClickGUIModule.panelExtended[category]!!.enabled = extended
        /*
        if (extended) {
            moduleButtons.filter { it.module.name.contains(currentSearch, true) }.reversed().forEach {
                it.mouseReleased(state)
            }
        }

 */     return false
    }



}