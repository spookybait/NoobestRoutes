package noobestroutes.ui.blockgui.blockeditor.elements


import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.IBlockState
import noobestroutes.features.dungeon.brush.BrushModule
import noobestroutes.ui.ColorPalette.TEXT_OFFSET
import noobestroutes.ui.ColorPalette.buttonColor
import noobestroutes.ui.ColorPalette.clickGUIColor
import noobestroutes.ui.ColorPalette.textColor
import noobestroutes.ui.blockgui.blockeditor.BlockEditor.originX
import noobestroutes.ui.blockgui.blockeditor.Element
import noobestroutes.ui.util.MouseUtils.isAreaHovered
import noobestroutes.ui.util.MouseUtils.mouseX
import noobestroutes.ui.util.animations.impl.ColorAnimation
import noobestroutes.utils.render.*
import noobestroutes.utils.render.ColorUtil.brighterIf
import noobestroutes.utils.render.ColorUtil.darkerIf
import noobestroutes.utils.skyblock.modMessage
import org.lwjgl.input.Keyboard
import kotlin.math.roundToInt


class ElementSlider(
    val name: String,
    val property: PropertyInteger,
    val block: IBlockState
) : Element(0f, 0f) {

    private val w = 550
    private val h = 104f
    private var listeningText = false
    private var listening: Boolean = false
    private var valueDouble: Double = block.getValue(property).toDouble()
    private val min: Double = property.allowedValues.min().toDouble()
    private val max: Double = property.allowedValues.max().toDouble()


    //55
    private val isHovered: Boolean
        get() = isAreaHovered(originX + x,y + 21.5f, w - 15f, 33.5f)

    private val isHoveredBox: Boolean
        get() = isAreaHovered(
            originX + x + w - TEXT_OFFSET - 30 - getTextWidth(getDisplay(), 16f),
             + y + 5f,
            16f + getTextWidth(getDisplay(), 16f),
            21.5f
        )


    private val colorAnim = ColorAnimation(100)

    /** Used to make slider smoother and not jittery (doesn't change value.) */
    private var sliderPercentage: Float = ((valueDouble - min) / (max - min)).toFloat().coerceAtMost(1f)

    private inline val color: Color
        get() = clickGUIColor.brighterIf(isHovered)


    private fun getDisplay(): String {
        if (listeningText) {
            return listeningTextField.ifEmpty { " " }
        }
        return "${valueDouble.roundToInt()}"
    }

    override fun draw() {

        val textWidth = getTextWidth(getDisplay(), 16f)

        roundedRectangle(
            originX + x + w - TEXT_OFFSET - 30 - textWidth,
            y,
            16f + textWidth,
            26.5f,
            buttonColor,
            4f,
            edgeSoftness = 1f
        )
        rectangleOutline(
            originX + x + w - TEXT_OFFSET - 30 - textWidth,
            y,
            16f + textWidth,
            26.5f,
            colorAnim.get(
                buttonColor.darkerIf(isHoveredBox, 0.8f),
                clickGUIColor.darkerIf(isHoveredBox, 0.8f),
                !listeningText
            ),
            4f,
            3f
        )

        if (listening) {
            val sliderCalculation = ((mouseX + 10.6f - (originX + x + TEXT_OFFSET)) / (w - 15f)).coerceIn(0f, 1f)
            sliderPercentage = sliderCalculation
            val diff = max - min
            val newVal = min + sliderCalculation * diff
            valueDouble = newVal
        }
        //roundedRectangle(originX + x + w - 4, y + , 2, h, clickGUIColor.brighter(1.6f), 0f, edgeSoftness = 0)

        text(name, originX + x + TEXT_OFFSET, y + 17.75f, textColor, 20f)
        text(
            getDisplay(),
            originX + x + w - TEXT_OFFSET - 22 - textWidth,
            y + 15.75f,
            textColor.darkerIf(isHoveredBox),
            16f
        )

        //draw slider
        roundedRectangle(originX + x + TEXT_OFFSET, y + 37f, w - 30f, 7f, sliderBGColor, 3f)
        roundedRectangle(originX + x + TEXT_OFFSET, y + 37f, sliderPercentage * (w - 30f), 7f, color, 3f)

    }

    override fun getElementHeight(): Float {
        return 75f
    }

    override fun mouseClicked() {
        when {
            isHoveredBox -> {
                if (listeningText) {
                    textUnlisten()
                } else {
                    listeningText = true
                    listeningTextField = valueDouble.roundToInt().toString()
                }
            }
            listeningText -> {
                textUnlisten()
                listeningText = false
                if (isHovered) { listening = true }
            }
            isHovered -> listening = true
        }
    }

    override fun mouseReleased() {
        listening = false
        val sliderCalculation = ((mouseX + 10.6f - (originX + x + TEXT_OFFSET)) / (w - 15f)).coerceIn(0f, 1f)
        val diff = max - min
        val newVal = min + sliderCalculation * diff
        setter(newVal)
    }

    private fun updateSlider() {
        sliderPercentage = ((valueDouble - min) / (max - min)).toFloat().coerceAtMost(1f)
    }


    private fun textUnlisten() {
        val input = listeningTextField.trimEnd('.')
        val newValue = if (input.isEmpty()) min else {
            val double = input.toDoubleOrNull()
            if (double == null) {
                modMessage("Invalid Number! Defaulting to previous value")
                valueDouble
            } else double
        }
        setter(newValue)
        updateSlider()
        listeningText = false
    }

    private fun handleText(input: String): String {
        val cleaned = buildString {
            input.forEachIndexed { index, char ->
                when {
                    char.isDigit() -> append(char)
                    char == '-' && index == 0 && !contains('-') -> append(char)
                    char == '.' && !contains('.') -> append(char)
                }
            }
        }
        return cleaned
    }

    private var listeningTextField: String = ""

    override fun mouseClickedAnywhere(mouseButton: Int): Boolean {
        if (mouseButton == 0 && listeningText && !isHovered && !isHoveredBox) {
            textUnlisten()
            return true
        }
        return false
    }


    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (listeningText) {
            var text = listeningTextField
            when (keyCode) {
                Keyboard.KEY_ESCAPE, Keyboard.KEY_NUMPADENTER, Keyboard.KEY_RETURN -> {
                    textUnlisten()
                    return
                }

                Keyboard.KEY_PERIOD -> {
                    if (listeningTextField.contains('.')) return
                    listeningTextField += '.'
                    return
                }

                Keyboard.KEY_DELETE -> {
                    listeningTextField = handleText(text.dropLast(1))
                    return
                }

                Keyboard.KEY_BACK -> {
                    listeningTextField =
                        if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            ""
                        } else {
                            handleText(text.dropLast(1))
                        }
                    return
                }

                in keyWhiteList -> {
                    text += typedChar.toString()
                    listeningTextField = handleText(text)
                    return
                }
            }
        }
        if (isHovered || isHoveredBox) {
            val amount = when (keyCode) {
                Keyboard.KEY_RIGHT -> 1
                Keyboard.KEY_LEFT -> -1
                else -> return
            }
            setter(amount + valueDouble)
            return
        }
    }
    private fun setter(newVal: Double) {
        BrushModule.selectedBlockState = BrushModule.selectedBlockState.withProperty(property, newVal.roundToInt().coerceIn(min.toInt(), max.toInt()))
    }


    private companion object {
        val sliderBGColor = Color(-0xefeff0)
    }

    private val keyWhiteList = listOf(
        Keyboard.KEY_0,
        Keyboard.KEY_1,
        Keyboard.KEY_2,
        Keyboard.KEY_3,
        Keyboard.KEY_4,
        Keyboard.KEY_5,
        Keyboard.KEY_6,
        Keyboard.KEY_7,
        Keyboard.KEY_8,
        Keyboard.KEY_9,
        Keyboard.KEY_NUMPAD0,
        Keyboard.KEY_NUMPAD1,
        Keyboard.KEY_NUMPAD2,
        Keyboard.KEY_NUMPAD3,
        Keyboard.KEY_NUMPAD4,
        Keyboard.KEY_NUMPAD5,
        Keyboard.KEY_NUMPAD6,
        Keyboard.KEY_NUMPAD7,
        Keyboard.KEY_NUMPAD8,
        Keyboard.KEY_NUMPAD9,
        Keyboard.KEY_MINUS
    )
}