package noobestroutes.ui.clickgui.elements.menu

import net.minecraft.client.renderer.GlStateManager
import noobestroutes.features.settings.impl.KeybindSetting
import noobestroutes.font.FontRenderer
import noobestroutes.ui.ColorPalette.TEXT_OFFSET
import noobestroutes.ui.ColorPalette.elementBackground
import noobestroutes.ui.ColorPalette.textColor
import noobestroutes.ui.clickgui.elements.ElementType
import noobestroutes.ui.clickgui.elements.SettingElement
import noobestroutes.ui.util.elements.KeybindElement
import noobestroutes.utils.render.TextAlign
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
class SettingElementKeyBind(setting: KeybindSetting) :
    SettingElement<KeybindSetting>(setting, ElementType.KEY_BIND) {

    val keybind = KeybindElement(setting.value, w - BORDER_OFFSET, h * 0.5f, 1f, 1f, TextAlign.Right).apply {
        addValueChangeListener {
            setting.value.key = it.key
        }
    }
    init {
        addChild(keybind)
    }


    override fun draw() {
        GlStateManager.pushMatrix()
        translate(x, y)
        roundedRectangle(0f, 0f, w, h, elementBackground)
        text(name,  x + TEXT_OFFSET, h * 0.5f, textColor, 12f, FontRenderer.REGULAR)
        GlStateManager.popMatrix()
    }
}