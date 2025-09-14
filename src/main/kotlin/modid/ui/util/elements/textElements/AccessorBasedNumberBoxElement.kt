package modid.ui.util.elements.textElements

import modid.Core
import modid.ui.util.UiElement
import modid.ui.util.elements.textElements.NumberBoxElement.Companion.numberKeyWhiteList
import modid.utils.render.Color
import modid.utils.render.TextAlign
import modid.utils.round
import modid.utils.skyblock.modMessage

class AccessorBasedNumberBoxElement(
    val name: String,
    x: Float,
    y: Float,
    minWidth: Float,
    var h: Float,
    textScale: Float,
    textAlign: TextAlign,
    val radius: Float,
    textPadding: Float,
    boxColor: Color,
    maxCharacters: Int,
    boxType: TextBoxElement.TextBoxType,
    boxThickness: Float = 3f,
    val roundTo: Int,
    val min: Double,
    val max: Double,
    val getter: () -> Number,
    val setter: (Double) -> Unit
) : UiElement(x, y) {

    inline var elementValue
        get() = getter.invoke().toDouble()
        set(value) {
            setter(value)
        }

    val textBox = TextBoxElement(
        name,
        x,
        y,
        minWidth,
        h,
        textScale,
        textAlign,
        radius,
        textPadding,
        boxColor,
        maxCharacters,
        boxType,
        boxThickness,
        if (roundTo == 0) this.elementValue.toInt().toString() else this.elementValue.toString(),
        numberKeyWhiteList,
        ""
    ).apply {
        addValueChangeListener {
            textUnlisten(it)
        }
    }


    override fun draw() {
        updateTextBoxValue()
    }

    init {
        addChild(
            textBox
        )
    }


    private fun textUnlisten(text: String) {
        if (text.isEmpty()) {
            elementValue = min
            updateTextBoxValue()
            return
        }
        elementValue =
            try {
                text.toDouble().round(roundTo).toDouble()
            } catch (e: NumberFormatException) {
                modMessage("Invalid number! Defaulting to previous value.")
                Core.logger.error(text, e)
                elementValue
            }

        updateTextBoxValue()
    }

    var lastValue = Double.MAX_VALUE
    fun updateTextBoxValue() {
        val get = elementValue
        if (lastValue == get) return
        lastValue = get
        textBox.elementValue = if (roundTo == 0) this.elementValue.toInt().toString() else this.elementValue.toString()
    }


}