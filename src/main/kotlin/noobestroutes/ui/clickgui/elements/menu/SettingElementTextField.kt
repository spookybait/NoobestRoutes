package noobestroutes.ui.clickgui.elements.menu

import noobestroutes.features.settings.impl.StringSetting
import noobestroutes.ui.ColorPalette.TEXT_OFFSET
import noobestroutes.ui.ColorPalette.elementBackground
import noobestroutes.ui.ColorPalette.textColor
import noobestroutes.ui.clickgui.elements.ElementType
import noobestroutes.ui.clickgui.elements.SettingElement
import noobestroutes.ui.util.elements.textElements.TextBoxElement
import noobestroutes.utils.render.ColorUtil.darker
import noobestroutes.utils.render.TextAlign
import noobestroutes.utils.render.roundedRectangle
import noobestroutes.utils.render.text
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
class SettingElementTextField(setting: StringSetting) :
    SettingElement<StringSetting>(setting, ElementType.TEXT_FIELD) {

    companion object {
        private const val TEXT_BOX_ELEMENT_HEIGHT = 24f
        private const val TEXT_BOX_ELEMENT_HEIGHT_HALF = TEXT_BOX_ELEMENT_HEIGHT * 0.5f
    }

    val textElement = TextBoxElement(
        "",
        w - BORDER_OFFSET,
        h * 0.5f - TEXT_BOX_ELEMENT_HEIGHT_HALF,
        36f,
        TEXT_BOX_ELEMENT_HEIGHT,
        12f,
        TextAlign.Right,
        6f,
        9f,
        textColor.darker(),
        12,
        TextBoxElement.TextBoxType.NORMAL,
        2f,
        setting.text
    ).apply {
        addValueChangeListener {
            setting.text = it
        }
    }

    init {
        addChild(textElement)
    }

    override fun draw() {
        GlStateManager.pushMatrix()
        translate(x, y)
        roundedRectangle(0f, 0f, w, h, elementBackground)
        text(name, TEXT_OFFSET, h * 0.5f, textColor, 12f)
        GlStateManager.popMatrix()
    }



}