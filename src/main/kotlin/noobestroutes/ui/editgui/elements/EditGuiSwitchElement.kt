package noobestroutes.ui.editgui.elements

import noobestroutes.ui.ColorPalette
import noobestroutes.ui.clickgui.elements.SettingElement.Companion.BORDER_OFFSET
import noobestroutes.ui.editgui.EditGuiElement
import noobestroutes.ui.util.UiElement
import noobestroutes.ui.util.elements.SwitchElement
import noobestroutes.utils.render.text
import net.minecraft.client.renderer.GlStateManager

class EditGuiSwitchElement(
    val name: String, val getter: () -> Boolean, val setter: (Boolean) -> Unit
) : UiElement(0f, 0f), EditGuiElement {
    override val priority: Int = 1
    override val isDoubleWidth: Boolean = false
    override val height: Float = 50f


    inline var value
        get() = getter.invoke()
        set(value) = setter(value)

    val switch = SwitchElement(2f, value, 0f, 0f)

    init {
        switch.addValueChangeListener {
           value = it
        }
        addChild(switch)
    }

    override fun draw() {
        GlStateManager.pushMatrix()
        translate(x + BORDER_OFFSET, y)
        switch.x = SwitchElement.SWITCH_WIDTH + 120f
        text(name, 0f, 0f, ColorPalette.textColor, 16f)
        GlStateManager.popMatrix()
    }

}