package noobestroutes.ui.blockgui.blockeditor.elements

import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.IBlockState
import noobestroutes.ui.ColorPalette.TEXT_OFFSET
import noobestroutes.ui.ColorPalette.buttonColor
import noobestroutes.ui.ColorPalette.textColor
import noobestroutes.ui.blockgui.blockeditor.BlockEditor
import noobestroutes.ui.blockgui.blockeditor.Element
import noobestroutes.ui.util.MouseUtils.isAreaHovered
import noobestroutes.ui.util.animations.impl.EaseInOut
import noobestroutes.utils.capitalizeFirst
import noobestroutes.utils.render.ColorUtil.darkerIf
import noobestroutes.utils.render.TextAlign
import noobestroutes.utils.render.roundedRectangle
import noobestroutes.utils.render.text

class ElementSelector(private val property: PropertyEnum<*>, val block: IBlockState) : Element(0f, 0f) {
    private val name = property.name.toString()
    private val options = mutableListOf<String>()

    private val posAnim = EaseInOut(250)
    companion object {
        const val WIDTH = 533f
        const val HEIGHT = 25f
        const val SCALE = 15f
    }
    private inline val xLeftBound get() = BlockEditor.originX + x + TEXT_OFFSET
    private inline val yBound get() = y + 30f

    private var extended = false

    init {
        property.allowedValues.forEach {
            //options.add(it.name)
        }

    }

    fun findHoveredOptions(): List<Boolean> {
        val hoveredList = mutableListOf<Boolean>()
        if (extended) {
            options.forEachIndexed { index, _ ->
                hoveredList.add(
                    isAreaHovered(
                        xLeftBound,
                        yBound + index * (HEIGHT * 1.1f),
                        WIDTH,
                        HEIGHT,
                    )
                )
            }
        } else {
            hoveredList.add(
                isAreaHovered(
                    xLeftBound,
                    yBound,
                    WIDTH,
                    HEIGHT,
                )
            )
        }
        return hoveredList
    }

    override fun draw() {
        text(name.capitalizeFirst(), xLeftBound, y + 17.75f, textColor, 20f)
        val optionsHovered = findHoveredOptions()
        if (extended) options.forEachIndexed { index, name ->
            roundedRectangle(
                xLeftBound,
                yBound + index * (HEIGHT * 1.1f),
                WIDTH,
                HEIGHT,
                buttonColor, radius = 10
            )
            text(
                name,
                xLeftBound + (WIDTH * 0.5),
                HEIGHT * 0.5 + yBound + index * (HEIGHT * 1.1f),
                textColor.darkerIf(optionsHovered[index]),
                SCALE,
                align = TextAlign.Middle
            )
        } else {
            roundedRectangle(
                xLeftBound,
                yBound,
                WIDTH,
                HEIGHT,
                buttonColor, radius = 10
            )
            text(
                options.first(),
                xLeftBound + (WIDTH * 0.5),
                HEIGHT * 0.5 + yBound,
                textColor.darkerIf(optionsHovered.first()),
                SCALE,
                align = TextAlign.Middle
            )
        }



    }

    override fun getElementHeight(): Float {
        return 104f + if (extended) ((options.size - 1) * (HEIGHT * 1.1f)) else 0f
    }
}