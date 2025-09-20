package noobestroutes.ui.clickgui.elements.menu

import noobestroutes.features.settings.impl.NumberSetting
import noobestroutes.ui.ColorPalette.TEXT_OFFSET
import noobestroutes.ui.ColorPalette.elementBackground
import noobestroutes.ui.ColorPalette.textColor
import noobestroutes.ui.clickgui.elements.ElementType
import noobestroutes.ui.clickgui.elements.SettingElement
import noobestroutes.ui.util.elements.SliderElement
import noobestroutes.ui.util.elements.textElements.NumberBoxElement
import noobestroutes.ui.util.elements.textElements.TextBoxElement
import noobestroutes.utils.render.ColorUtil.darker
import noobestroutes.utils.render.TextAlign
import noobestroutes.utils.render.roundedRectangle
import noobestroutes.utils.render.text
import noobestroutes.utils.round
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
class SettingElementSlider(setting: NumberSetting<*>) :
    SettingElement<NumberSetting<*>>(setting, ElementType.SLIDER) {

    companion object {
        private const val TEXT_BOX_HEIGHT = 21.5f
        private const val HALF_TEXT_BOX_HEIGHT = TEXT_BOX_HEIGHT * 0.5f
        private const val SLIDER_HEIGHT = 7f
        private const val Y_PADDING = 12f
        private const val NUMBER_BOX_MIN_WIDTH = 28f
        private const val NUMBER_BOX_RADIUS = 6f
        private const val NUMBER_BOX_PADDING = 9f
        private const val TEXT_BOX_THICKNESS = 2f
    }

    val sliderElement = SliderElement(
        BORDER_OFFSET,
        40f,
        w - BORDER_OFFSET * 2,
        SLIDER_HEIGHT,
        setting.valueDouble,
        setting.min,
        setting.max,
        setting.increment,
        if (setting.value is Int) 0 else 2
    ).apply {
        addValueChangeListener { sliderValue ->
            setting.setValueFromNumber(sliderValue)
            updateValues(sliderValue)
        }
    }
    val numberBoxElement = NumberBoxElement(
        "",
        w - BORDER_OFFSET,
        Y_PADDING - TEXT_BOX_HEIGHT * 0.5f + 6f,
        NUMBER_BOX_MIN_WIDTH,
        TEXT_BOX_HEIGHT,
        12f,
        TextAlign.Right,
        NUMBER_BOX_RADIUS,
        NUMBER_BOX_PADDING,
        textColor.darker(),
        8,
        TextBoxElement.TextBoxType.NORMAL,
        TEXT_BOX_THICKNESS,
        setting.roundTo,
        setting.min,
        setting.max,
        setting.valueDouble
    ).apply {
        addValueChangeListener { boxValue ->
            setting.setValueFromNumber(boxValue)
            updateValues(boxValue)
        }
    }

    init {
        addChildren(numberBoxElement, sliderElement)
    }

    fun updateValues(sliderValue: Double) {
        sliderElement.elementValue = sliderValue
        numberBoxElement.apply {
            elementValue = setting.valueDouble.round(roundTo).toDouble()
            updateTextBoxValue()
        }
    }

    override fun draw() {
        GlStateManager.pushMatrix()
        translate(x, y)
        roundedRectangle(0f, 0f, w, h, elementBackground)
        roundedRectangle(0f, 0f, w, h, elementBackground)

        text(name, TEXT_OFFSET, Y_PADDING + HALF_TEXT_BOX_HEIGHT * 0.5f, textColor, 12f)
        GlStateManager.popMatrix()
    }
}