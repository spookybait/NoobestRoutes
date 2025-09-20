package noobestroutes.ui.blockgui.blockselector

import net.minecraft.block.Block
import noobestroutes.ui.ColorPalette.buttonColor
import noobestroutes.ui.util.MouseUtils.isAreaHovered
import noobestroutes.utils.render.ColorUtil.darker
import noobestroutes.utils.render.ColorUtil.multiplyAlpha
import noobestroutes.utils.render.roundedRectangle

abstract class BlockElement(var x: Int, var y: Int) {

    abstract var block: Block?
    abstract var displayName: String

    val isHovered: Boolean get() = isAreaHovered(
        x * 50f + BlockSelector.originX + 45f,
        y * 50f + BlockSelector.scrollOffset + BlockSelector.originY - 5,
        48f,
        48f
    )

    open fun draw() {
        if (isHovered) {
            roundedRectangle(
                x * 50f + BlockSelector.originX + 45f,
                y * 50f + BlockSelector.scrollOffset + BlockSelector.originY - 5,
                48f, 48f,
                buttonColor.darker(0.4f).multiplyAlpha(1.3f)
            )
        }
    }

    abstract fun mouseClicked()


}