package modid.ui.clickgui.elements

import modid.features.Category
import modid.features.impl.render.ClickGUIModule
import modid.font.FontRenderer.wrappedTextBounds
import modid.ui.ColorPalette.elementBackground
import modid.ui.ColorPalette.textColor
import modid.ui.ColorPalette.titlePanelColor
import modid.ui.util.UiElement
import modid.utils.capitalizeFirst
import modid.utils.render.RenderUtils.loadBufferedImage
import modid.utils.render.rectangleOutline
import modid.utils.render.roundedRectangle
import modid.utils.render.wrappedText
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.texture.DynamicTexture

object ClickGUIBase : UiElement(0f, 0f) {
    private val moveIcon = DynamicTexture(loadBufferedImage("/assets/ui/MovementIcon.png"))
    private val dungeonIcon = DynamicTexture(loadBufferedImage("/assets/ui/DungeonIcon.png"))
    private val floor7Icon = DynamicTexture(loadBufferedImage("/assets/ui/Floor7Icon.png"))
    private val renderIcon = DynamicTexture(loadBufferedImage("/assets/ui/RenderIcon.png"))
    private val miscIcon = DynamicTexture(loadBufferedImage("/assets/ui/MiscIcon.png"))
    private val routesIcon = DynamicTexture(loadBufferedImage("/assets/ui/RoutesIcon.png"))


//if (category == Category.FLOOR7) "Floor 7" else
    init {
        addChild(SearchBar)
        for (category in Category.entries) {
            val name = category.name.lowercase().capitalizeFirst()
            val icon = when (name) {
                "Render" -> renderIcon
                "Floor 7" -> floor7Icon
                "Misc" -> miscIcon
                "Move" -> moveIcon
                "Dungeon" -> dungeonIcon
                "Routes" -> routesIcon
                else -> moveIcon
            }
            val panel = Panel(name, category, icon)
            if (panel.isNotEmpty) uiChildren.add(panel)
        }
    }

    fun removePanel(panel: Panel) {
        uiChildren.remove(panel)
    }

    override fun doDrawChildren() {
        for (i in uiChildren.indices) {
            uiChildren[i].doHandleDraw()
            GlStateManager.translate(0f, 0f, -8f)
        }
    }

    fun onGuiInit(){
        for (i in uiChildren.indices) {
            val panel = uiChildren[i] as? Panel ?: continue
            panel.updatePosition(ClickGUIModule.panelX[panel.category]!!.value, ClickGUIModule.panelY[panel.category]!!.value)
            panel.extended = ClickGUIModule.panelExtended[panel.category]!!.enabled
            panel.updatingModuleButtons()
        }
        SearchBar.updatePosition(ClickGUIModule.searchBarX.value, ClickGUIModule.searchBarY.value)

    }

    fun wipeDescription(){
        description = ""
        descriptionX = -1f
        descriptionY = -1f
    }

    fun setDescription(description: String, x: Float, y: Float) {
        ClickGUIBase.description = description
        descriptionX = x + Panel.WIDTH + 10f
        descriptionY = y
    }


    private const val TEXT_PADDING_X = 7f
    private const val TEXT_PADDING_Y = 9f
    private const val OUTLINE_THICKNESS = 3f
    /**
     * draws description
     */
    override fun draw() {
        if (descriptionX == -1f || descriptionY == -1f || description.isBlank()) return
        val area = wrappedTextBounds(description, 300f, 12f)
        blurRoundedRectangle(descriptionX, descriptionY, area.first + TEXT_PADDING_X, area.second + TEXT_PADDING_Y, 5f, 5f, 5f, 5f, 0.5f)
        roundedRectangle(descriptionX, descriptionY, area.first + TEXT_PADDING_X, area.second + TEXT_PADDING_Y, elementBackground, 5f, edgeSoftness = 1.8f)
        rectangleOutline(
            descriptionX - OUTLINE_THICKNESS,
            descriptionY - OUTLINE_THICKNESS,
            area.first + TEXT_PADDING_X + OUTLINE_THICKNESS,
            area.second + TEXT_PADDING_Y + OUTLINE_THICKNESS,
            titlePanelColor,
            5f,
            OUTLINE_THICKNESS,
        )
        wrappedText(description, descriptionX + TEXT_PADDING_X, descriptionY + TEXT_PADDING_X, 300f, textColor, 12f)
    }

    private var description = ""
    private var descriptionX = -1f
    private var descriptionY = -1f

}