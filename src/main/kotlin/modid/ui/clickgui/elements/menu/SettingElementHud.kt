package modid.ui.clickgui.elements.menu

import modid.Core
import modid.features.impl.render.ClickGUIModule
import modid.features.settings.impl.HudSetting
import modid.font.FontRenderer
import modid.ui.ColorPalette.TEXT_OFFSET
import modid.ui.ColorPalette.buttonColor
import modid.ui.ColorPalette.clickGUIColor
import modid.ui.ColorPalette.elementBackground
import modid.ui.ColorPalette.textColor
import modid.ui.clickgui.elements.ElementType
import modid.ui.clickgui.elements.SettingElement
import modid.ui.hud.EditHUDGui
import modid.ui.util.animations.impl.ColorAnimation
import modid.ui.util.animations.impl.LinearAnimation
import modid.utils.render.*
import modid.utils.render.ColorUtil.darker
import modid.utils.render.ColorUtil.darkerIf
import modid.utils.render.RenderUtils.loadBufferedImage
import net.minecraft.client.renderer.texture.DynamicTexture


/**
 * Renders all the modules.
 *
 * Backend made by Aton, with some changes
 * Design mostly made by Stivais
 *
 * @author Stivais, Aton
 * @see [SettingElement]
 */
class SettingElementHud(setting: HudSetting) : SettingElement<HudSetting>(
    setting, ElementType.DUAL
) {
    override val isHovered: Boolean
        get() = setting.displayToggle && isAreaHovered(x + w - 30f, y + 5f, 21f, 20f)

    private val isShortcutHovered: Boolean
        get() {
            return if (setting.displayToggle) isAreaHovered(x + w - 60f, y + 5f, 21f, 20f)
            else isAreaHovered(x + w - 30f, y + 5f, 21f, 20f)
        }

    private val movementIcon = DynamicTexture(loadBufferedImage("/assets/ui/MoveHudIcon.png"))
    private val colorAnim = ColorAnimation(250)
    private val linearAnimation = LinearAnimation<Float>(200)

    override fun draw() {
        roundedRectangle(x, y, w, h, elementBackground)
        text(name, x + TEXT_OFFSET, y + 18f, textColor, 12f, FontRenderer.REGULAR)

        var offset = 30f
        if (setting.displayToggle) {
            val color = colorAnim.get(clickGUIColor, buttonColor, setting.enabled)//.brighter()
            if (!ClickGUIModule.switchType) {
                roundedRectangle(x + w - offset, y + 5f, 21f, 20f, color, 5f)
                rectangleOutline(x + w - offset, y + 5f, 21f, 20f, clickGUIColor, 5f, 3f)
                offset = 60f
            } else {

                roundedRectangle(x + w - 43f, y + 4f, 34f, 20f, buttonColor, 9f)
                if (setting.enabled || linearAnimation.isAnimating()) roundedRectangle(x + w - 43f, y + 4f, linearAnimation.get(34f, 9f, setting.enabled), 20f, color, 9f)

                if (isHovered) rectangleOutline(x + w - 43f, y + 4f, 34f, 20f, color.darker(.85f), 9f, 3f)
                circle(x + w - linearAnimation.get(33f, 17f, !setting.enabled), y + 14f, 6f,
                    Color(220, 220, 220).darkerIf(isHovered, 0.9f)
                )
                offset = 70f
            }
        }
        drawDynamicTexture(
            movementIcon, x + w - offset, y + 5f, 20f, 20f
        )
    }

    override fun mouseClicked(mouseButton: Int): Boolean {
        if (mouseButton == 0) {
            when {
                isHovered -> if (colorAnim.start()) {
                    setting.enabled = !setting.enabled
                    setting.value.enabledSetting.value = setting.enabled
                }
                isShortcutHovered -> Core.display = EditHUDGui
                else -> return false
            }
            return true
        }
        return false
    }
}