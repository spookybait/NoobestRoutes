package noobestroutes.ui.clickgui.elements.menu

import net.minecraft.client.renderer.GlStateManager
import noobestroutes.features.settings.impl.SelectorSetting
import noobestroutes.font.FontRenderer
import noobestroutes.ui.ColorPalette.TEXT_OFFSET
import noobestroutes.ui.ColorPalette.buttonColor
import noobestroutes.ui.ColorPalette.clickGUIColor
import noobestroutes.ui.ColorPalette.elementBackground
import noobestroutes.ui.ColorPalette.textColor
import noobestroutes.ui.clickgui.elements.ElementType
import noobestroutes.ui.clickgui.elements.SettingElement
import noobestroutes.ui.util.animations.impl.CubicBezierAnimation
import noobestroutes.utils.capitalizeFirst
import noobestroutes.utils.render.*
import noobestroutes.utils.render.ColorUtil.brighterIf
import noobestroutes.utils.render.ColorUtil.darker

/**
 * Renders all the modules.
 *
 * Backend made by Aton, with some changes
 * Design mostly made by Stivais
 *
 * @author Stivais, Aton
 * @see [SettingElement]
 */
class SettingElementSelector(setting: SelectorSetting) :
    SettingElement<SelectorSetting>(setting, ElementType.SELECTOR) {

    override val isHovered: Boolean
        get() = isAreaHovered(0f, 0f, w, DEFAULT_HEIGHT)

    val display: String
        inline get() = setting.selected

    inline val size: Int
        get () = setting.options.size

    private val settingAnim = CubicBezierAnimation(200, 0.4, 0, 0.2, 1)

    private val isSettingHovered: (Int) -> Boolean = {
        isAreaHovered(0f, 38f + 32f * it, w, 32f)
    }

    private val color: Color
        get() = buttonColor.brighterIf(isHovered)

    override fun draw() {
        GlStateManager.pushMatrix()
        translate(x, y)
        h = settingAnim.get(32f, size * 36f + DEFAULT_HEIGHT, !extended)

        roundedRectangle(0f, 0f, w, h, elementBackground)
        val width = getTextWidth(display, 12f)

        roundedRectangle(w - 20f - width, 4f, width + 12f, 22f, color, 5f)

        text(name, TEXT_OFFSET, 16f, textColor, 12f, FontRenderer.REGULAR)
        text(display, w - 14f - width, 8f, textColor, 12f, FontRenderer.REGULAR, TextAlign.Left, TextPos.Top)

        if (!extended && !settingAnim.isAnimating()) {
            GlStateManager.popMatrix()
            return
        }

        rectangleOutline(w - 20f - width, 4f, width + 12f, 22f, clickGUIColor, 5f, 1.5f)

        val scissor = scissor(getEffectiveX() + x, getEffectiveY(), w, h)

        roundedRectangle(TEXT_OFFSET, 37f, w - 12f, size * 32f, buttonColor, 5f)

        for (i in 0 until size) {
            val y = 38 + 32 * i
            text(setting.options[i].lowercase().capitalizeFirst(),w * 0.5f, y + 6f, textColor, 12f, FontRenderer.REGULAR, TextAlign.Middle, TextPos.Top)
            if (isSettingHovered(i)) rectangleOutline(5, y - 1f, w - 11.5f, 32.5f, clickGUIColor.darker(), 4f, 3f)
        }
        resetScissor(scissor)
        GlStateManager.popMatrix()
    }

    override fun mouseClicked(mouseButton: Int): Boolean {
        if (mouseButton == 0) {
            if (isHovered) {
                if (settingAnim.start()) extended = !extended
                return true
            }
            if (!extended) return false
            for (index in 0 until setting.options.size) {
                if (isSettingHovered(index)) {
                    if (settingAnim.start()) {
                        setting.selected = setting.options[index]
                        extended = false
                    }
                    return true
                }
            }
        } else if (mouseButton == 1) {
            if (isHovered) {
                setting.index += 1
                return true
            }
        }
        return false
    }
}