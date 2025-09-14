package modid.ui.clickgui.elements.menu

import modid.features.settings.impl.DropdownSetting
import modid.font.FontRenderer
import modid.ui.ColorPalette.TEXT_OFFSET
import modid.ui.ColorPalette.elementBackground
import modid.ui.ColorPalette.textColor
import modid.ui.clickgui.elements.ElementType
import modid.ui.clickgui.elements.SettingElement
import modid.ui.util.animations.impl.LinearAnimation
import modid.utils.render.drawArrow
import modid.utils.render.roundedRectangle
import modid.utils.render.text

/**
 * Renders all the modules.
 *
 * Backend made by Aton, with some changes
 * Design mostly made by Stivais
 *
 * @author Stivais, Aton
 * @see [SettingElement]
 */
class SettingElementDropdown(setting: DropdownSetting) : SettingElement<DropdownSetting>(
    setting, ElementType.DROPDOWN
) {
    private val linearAnimation = LinearAnimation<Float>(200)

    override val isHovered: Boolean get() =
        isAreaHovered(x, y, w, h)

    override fun draw() {
        roundedRectangle(x, y, w, h, elementBackground)
        text(name, x + TEXT_OFFSET, y + h  * 0.5f, textColor, 12f, FontRenderer.REGULAR)

        val rotation = linearAnimation.get(0f, 90f, !setting.value)
        drawArrow(x + w - 12f, y + 16, rotation, scale = 0.8f)
    }

    override fun mouseClicked(mouseButton: Int): Boolean {
        if (isHovered) {
            if (linearAnimation.start()) {
                setting.enabled = !setting.enabled
                return true
            }
        }
        return false
    }

}