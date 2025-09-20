package noobestroutes.ui.hud

import net.minecraft.client.renderer.GlStateManager
import noobestroutes.config.Config
import noobestroutes.features.ModuleManager.huds
import noobestroutes.font.FontRenderer
import noobestroutes.ui.ColorPalette.textColor
import noobestroutes.ui.Screen
import noobestroutes.ui.util.MouseUtils
import noobestroutes.ui.util.MouseUtils.isAreaHovered
import noobestroutes.ui.util.animations.impl.EaseInOut
import noobestroutes.utils.clock.Executor
import noobestroutes.utils.clock.Executor.Companion.register
import noobestroutes.utils.render.*
import org.lwjgl.opengl.Display
import kotlin.math.sign

/**
 * Screen that renders all your active Hud's
 *
 * @author Stivais
 */
object EditHUDGui : Screen() {

    var dragging: HudElement? = null

    private var startX: Float = 0f
    private var startY: Float = 0f

    var open = false

    private val openAnim = EaseInOut(600)
    private val resetAnim = EaseInOut(1000)

    /** Code is horrible ngl but it looks nice */
    override fun draw() {
        mc.mcProfiler.startSection("noobestroutes Example Hud")
        dragging?.let {
            it.x = MouseUtils.mouseX - startX
            it.y = MouseUtils.mouseY - startY
        }
        GlStateManager.pushMatrix()
        scale(mc.displayWidth / 1920f, mc.displayHeight / 1080f)
        scale(1f / scaleFactor, 1f / scaleFactor, 1f)

        if (openAnim.isAnimating()) {
            val animVal = openAnim.get(0f, 1f, !open)
            scale(animVal, animVal)
        }

        //dropShadow(-100f, -25f, 200f, 50f, 10f, 1f)
        roundedRectangle(Display.getWidth() * 0.5f - 75, Display.getHeight() * 0.86f - 30, 150f, 40f, color, 9f)

        text(
            "Reset",
            Display.getWidth() * 0.5f,
            Display.getHeight() * 0.86f,
            textColor,
            18f,
            FontRenderer.REGULAR,
            TextAlign.Middle,
            TextPos.Bottom
        )

        if (openAnim.isAnimating()) {
            val animVal = openAnim.get(0f, 1f, !open)
            scale(1 / animVal, 1 / animVal)
        }
        scale(scaleFactor, scaleFactor, 1f)

        GlStateManager.popMatrix()

        if (!open) return
        for (i in 0 until huds.size) {
            huds[i].draw(example = true)
        }

        mc.mcProfiler.endSection()
    }

    private val color = Color(0f, 0.75f, 0.75f, 0.75f)
        get() {
            field.brightness = (0.75f).coerceAtMost(1f)
            return field
        }

    override fun onScroll(amount: Int) {
        for (i in huds.size - 1 downTo 0) {
            if (huds[i].accept()) {
                huds[i].scale += amount.sign * 0.05f
            }
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (isAreaHovered(Display.getWidth() * 0.5f - 75, Display.getHeight() * 0.86f - 90f, 150f, 40f)) {
            resetHUDs()
            return
        }

        for (i in huds.size - 1 downTo 0) {
            if (huds[i].accept()) {
                dragging = huds[i]
                huds[i].anim2.start()

                startX = MouseUtils.mouseX - huds[i].x
                startY = MouseUtils.mouseY - huds[i].y
                return
            }
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        dragging?.anim2?.start(true)
        dragging = null
        super.mouseReleased(mouseX, mouseY, state)
    }

    override fun initGui() {
        openAnim.start()
        open = true
    }

    override fun onGuiClosed() {
        open = false
        openAnim.start(true)

        Executor(0) {
            if (!openAnim.isAnimating()) destroyExecutor()
            drawScreen(0, 0, 0f)
        }.register()

        for (i in 0 until huds.size) {
            huds[i]
        }
        Config.save()
    }

    /**
     * Creates an Executor that slowly resets hud's position
     */
    fun resetHUDs() {
        if (resetAnim.start()) {
            for (i in huds) {
                i.resetX = i.x
                i.resetY = i.y
                i.resetScale = i.scale
            }
            Executor(0) {
                if (!resetAnim.isAnimating()) {
                    Config.save()
                    destroyExecutor()
                }
                for (hud in huds) {
                    hud.x = resetAnim.get(hud.resetX, hud.xSetting.default)
                    hud.y = resetAnim.get(hud.resetY, hud.ySetting.default)
                    hud.scale = resetAnim.get(hud.resetScale, hud.scaleSetting.default)
                }
            }.register()
        }
    }
}