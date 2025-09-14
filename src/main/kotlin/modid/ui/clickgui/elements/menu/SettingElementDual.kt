package modid.ui.clickgui.elements.menu

import modid.features.settings.impl.DualSetting
import modid.ui.ColorPalette.elementBackground
import modid.ui.clickgui.elements.ElementType
import modid.ui.clickgui.elements.SettingElement
import modid.ui.util.elements.DualElement
import modid.utils.render.roundedRectangle
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
class SettingElementDual(setting: DualSetting) : SettingElement<DualSetting>(
    setting, ElementType.DUAL
) {
    val dualElement = DualElement(setting.left, setting.right, w * 0.5f, h * 0.5f, 0.9f, 0.9f, setting.enabled).apply {
        addValueChangeListener { setting.enabled = it }
    }

    init {
        addChild(dualElement)
    }

    override fun draw() {
        GlStateManager.pushMatrix()
        translate(x, y)
        roundedRectangle(0f, 0f, w, h, elementBackground)
        GlStateManager.popMatrix()
    }
}