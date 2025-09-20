package noobestroutes.ui.blockgui.blockselector

import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import noobestroutes.features.dungeon.brush.BrushModule
import noobestroutes.ui.blockgui.blockeditor.BlockEditor
import noobestroutes.utils.render.RenderUtils.drawItem

class BlockItemStackElement(x: Int, y: Int, private val blockItem: ItemStack) : BlockElement(x, y) {
    override var block: Block? = Block.getBlockFromItem(blockItem.item)
    override var displayName: String = blockItem.displayName

    override fun draw() {
        super.draw()
        blockItem.drawItem(x * 50f + BlockSelector.originX + 50f, y * 50f + BlockSelector.scrollOffset + BlockSelector.originY, scale = 2.3f)
    }

    override fun mouseClicked() {
        if (isHovered) {
            BrushModule.selectedBlockState = block?.getStateFromMeta(blockItem.metadata) ?: return
            BlockEditor.currentBlockName = displayName
        }
    }


}