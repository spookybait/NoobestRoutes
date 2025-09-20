package noobestroutes.ui.clickgui.elements.menu

import net.minecraft.client.renderer.GlStateManager
import noobestroutes.features.settings.impl.ActionSetting
import noobestroutes.font.FontRenderer
import noobestroutes.ui.ColorPalette.elementBackground
import noobestroutes.ui.ColorPalette.textColor
import noobestroutes.ui.clickgui.elements.ElementType
import noobestroutes.ui.clickgui.elements.SettingElement
import noobestroutes.utils.render.ColorUtil.darker
import noobestroutes.utils.render.TextAlign
import noobestroutes.utils.render.TextPos
import noobestroutes.utils.render.roundedRectangle
import noobestroutes.utils.render.text

/**
 * Renders all the modules.
 *
 * Backend made by Aton, with some changes
 * Design mostly made by Stivais
 *
 * @author Stivais, Aton
 * @see [SettingElement]
 */
class SettingElementAction(setting: ActionSetting) : SettingElement<ActionSetting>(setting, ElementType.ACTION) {
    override val isHovered: Boolean
        get() = isAreaHovered(20f, 0f, w - 40f, h - 10f)

    override fun draw() {
        GlStateManager.pushMatrix()
        translate(x, y)
        roundedRectangle(0f, 0f, w, h, elementBackground)
        text(name, w * 0.5,h * 0.5, if (isHovered) textColor.darker() else textColor, 12f , FontRenderer.REGULAR, TextAlign.Middle, TextPos.Middle)
        GlStateManager.popMatrix()

    }

    override fun mouseClicked(mouseButton: Int): Boolean {
        if (mouseButton == 0 && isHovered) {
            setting.action()
            return true
        }
        return false
    }
}