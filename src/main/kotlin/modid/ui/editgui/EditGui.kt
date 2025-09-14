package modid.ui.editgui

import modid.Core
import modid.config.Config
import modid.features.impl.render.ClickGUIModule
import modid.ui.Screen
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.input.Mouse
import kotlin.math.sign

object EditGui : Screen() {

    private var activeEditGuiBase: EditGuiBase? = null

    init {
        EditGuiBase.Companion.editGuiBaseX = ClickGUIModule.editGuiX.value
        EditGuiBase.Companion.editGuiBaseY = ClickGUIModule.editGuiY.value
    }


    fun openEditGui(editGuiBase: EditGuiBase){
        activeEditGuiBase = editGuiBase
        Core.display = this
    }

    override fun draw() {
        GlStateManager.pushMatrix()
        scaleUI()
        setupBlending()
        backgroundCapture()
        activeEditGuiBase?.doHandleDraw()
        backgroundCleanup()
        GlStateManager.popMatrix()
    }

    override fun onScroll(amount: Int) {
        if (Mouse.getEventDWheel() == 0) return
        activeEditGuiBase?.handleScroll(amount.sign * 16)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        activeEditGuiBase?.handleMouseClicked(mouseButton)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (activeEditGuiBase?.doHandleKeyTyped(typedChar, keyCode) == true) return
        super.keyTyped(typedChar, keyCode)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        if (state != 0) return
        activeEditGuiBase?.handleMouseReleased()
    }

    override fun initGui() {
        activeEditGuiBase?.onOpen
    }

    override fun onGuiClosed() {
        activeEditGuiBase?.onClose
        activeEditGuiBase = null
        ClickGUIModule.editGuiX.value = EditGuiBase.Companion.editGuiBaseX
        ClickGUIModule.editGuiY.value = EditGuiBase.Companion.editGuiBaseY
        Config.save()
    }
}