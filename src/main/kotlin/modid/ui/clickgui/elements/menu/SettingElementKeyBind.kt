package modid.ui.clickgui.elements.menu

import modid.features.settings.impl.KeybindSetting
import modid.font.FontRenderer
import modid.ui.ColorPalette.TEXT_OFFSET
import modid.ui.ColorPalette.elementBackground
import modid.ui.ColorPalette.textColor
import modid.ui.clickgui.elements.ElementType
import modid.ui.clickgui.elements.SettingElement
import modid.ui.util.elements.KeybindElement
import modid.utils.render.TextAlign
import modid.utils.render.roundedRectangle
import modid.utils.render.text
import net.minecraft.client.renderer.GlStateManager

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