package noobestroutes.ui.clickgui.elements.menu

import net.minecraft.client.renderer.GlStateManager
import noobestroutes.features.settings.impl.DualSetting
import noobestroutes.ui.ColorPalette.elementBackground
import noobestroutes.ui.clickgui.elements.ElementType
import noobestroutes.ui.clickgui.elements.SettingElement
import noobestroutes.ui.util.elements.DualElement
import noobestroutes.utils.render.roundedRectangle

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