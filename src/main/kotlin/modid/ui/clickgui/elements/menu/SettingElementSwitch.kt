package modid.ui.clickgui.elements.menu
import modid.features.settings.impl.BooleanSetting
import modid.font.FontRenderer
import modid.ui.ColorPalette.TEXT_OFFSET
import modid.ui.ColorPalette.elementBackground
import modid.ui.ColorPalette.textColor
import modid.ui.clickgui.elements.ElementType
import modid.ui.clickgui.elements.SettingElement
import modid.ui.util.elements.SwitchElement
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
class SettingElementSwitch(setting: BooleanSetting) : SettingElement<BooleanSetting>(
    setting, ElementType.CHECK_BOX
) {

    val switchElement =
        SwitchElement(1f, setting.enabled, w - SwitchElement.SWITCH_WIDTH_HALF - BORDER_OFFSET, h * 0.5f).apply {
            addValueChangeListener {
                setting.enabled = it
            }
        }

    init {
        addChild(switchElement)
    }

    override fun draw() {
        GlStateManager.pushMatrix()
        translate(x, y)
        roundedRectangle(0f, 0f, w, h, elementBackground)
        text(name, TEXT_OFFSET, h * 0.5, textColor, 12f, FontRenderer.REGULAR)
        GlStateManager.popMatrix()
    }
}