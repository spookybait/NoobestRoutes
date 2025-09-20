package noobestroutes.ui.util.shader

import gg.essential.elementa.components.UIBlock
import gg.essential.universal.UMatrixStack
import gg.essential.universal.shader.*
import noobestroutes.Core.logger
import noobestroutes.utils.Utils.COLOR_NORMALIZER
import noobestroutes.utils.render.Color
import noobestroutes.utils.render.RenderUtils.createLegacyShader

/**
 * Taken from Odin
 */
object RoundedRect {
    fun initShaders() {
        logger.info("initing rounded rect shader")
        RoundedRectangle.initShader()
        HSBBox.initShader()
        Circle.initShader()
        DropShadow.initShader()
    }

    fun drawRectangle(
        matrixStack: UMatrixStack, x: Float, y: Float, width: Float, height: Float,
        color: Color, borderColor: Color, shadowColor: Color,
        borderThickness: Float, topL: Float, topR: Float, botL: Float, botR: Float, edgeSoftness: Float,
        color2: Color, gradientDir: Int, shadowSoftness: Float = 0f
    ) {
        if (!RoundedRectangle.isInitialized() || !RoundedRectangle.shader.usable) return


        RoundedRectangle.shader.bind()
        RoundedRectangle.shaderCenterUniform.setValue(x + (width * 0.5f), y + (height * 0.5f))
        RoundedRectangle.shaderSizeUniform.setValue(width, height)
        RoundedRectangle.shaderRadiusUniform.setValue(botR, topR, botL, topL)
        RoundedRectangle.shaderBorderThicknessUniform.setValue(borderThickness)
        RoundedRectangle.shaderEdgeSoftnessUniform.setValue(edgeSoftness)
        RoundedRectangle.shaderColorUniform.setValue(color.r * COLOR_NORMALIZER, color.g * COLOR_NORMALIZER, color.b * COLOR_NORMALIZER, color.alpha)
        RoundedRectangle.shaderColor2Uniform.setValue(color2.r * COLOR_NORMALIZER, color2.g * COLOR_NORMALIZER, color2.b * COLOR_NORMALIZER, color2.alpha)
        val direction = RoundedRectangle.directionVecs[gradientDir]
        RoundedRectangle.shaderGradientDir.setValue(direction.first, direction.second)
        RoundedRectangle.shaderBorderColorUniform.setValue(
            borderColor.r * COLOR_NORMALIZER,
            borderColor.g * COLOR_NORMALIZER,
            borderColor.b * COLOR_NORMALIZER,
            borderColor.alpha
        )
        RoundedRectangle.shaderShadowColorUniform.setValue(
            shadowColor.r * COLOR_NORMALIZER,
            shadowColor.g * COLOR_NORMALIZER,
            shadowColor.b * COLOR_NORMALIZER,
            shadowColor.alpha
        )
        RoundedRectangle.shaderShadowSoftness.setValue(shadowSoftness)

        UIBlock.drawBlockWithActiveShader(
            matrixStack,
            color.javaColor,
            x.toDouble(),
            y.toDouble(),
            x.toDouble() + width.toDouble(),
            y.toDouble() + height.toDouble()
        )

        RoundedRectangle.shader.unbind()
    }

    fun drawHSBBox(matrixStack: UMatrixStack, x: Float, y: Float, width: Float, height: Float, color: Color) {
        if (!HSBBox.isInitialized() || !HSBBox.shader.usable) return

        HSBBox.shader.bind()
        HSBBox.shaderCenterUniform.setValue(x + (width * 0.5f), y + (height * 0.5f))
        HSBBox.shaderSizeUniform.setValue(width, height)
        HSBBox.shaderColorUniform.setValue(color.r * COLOR_NORMALIZER, color.g * COLOR_NORMALIZER, color.b * COLOR_NORMALIZER, color.alpha)

        UIBlock.drawBlockWithActiveShader(
            matrixStack,
            color.javaColor,
            x.toDouble(),
            y.toDouble(),
            x.toDouble() + width.toDouble(),
            y.toDouble() + height.toDouble()
        )

        HSBBox.shader.unbind()
    }

    fun drawCircle(
        matrixStack: UMatrixStack,
        x: Float,
        y: Float,
        radius: Float,
        color: Color,
        borderColor: Color,
        borderThickness: Float
    ) {
        if (!Circle.isInitialized() || !Circle.shader.usable) return

        Circle.shader.bind()
        Circle.shaderCenterUniform.setValue(x, y)
        Circle.shaderRadiusUniform.setValue(radius)
        Circle.shaderColorUniform.setValue(color.r * COLOR_NORMALIZER, color.g * COLOR_NORMALIZER, color.b * COLOR_NORMALIZER, color.alpha)
        Circle.shaderBorderColorUniform.setValue(
            borderColor.r * COLOR_NORMALIZER,
            borderColor.g * COLOR_NORMALIZER,
            borderColor.b * COLOR_NORMALIZER,
            borderColor.alpha
        )
        Circle.shaderBorderThicknessUniform.setValue(borderThickness)

        UIBlock.drawBlockWithActiveShader(
            matrixStack,
            color.javaColor,
            x.toDouble() - radius,
            y.toDouble() - radius,
            x.toDouble() + radius,
            y.toDouble() + radius
        )

        Circle.shader.unbind()
    }

    fun drawDropShadow(
        matrixStack: UMatrixStack,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        shadowColor: Color,
        topL: Float,
        topR: Float,
        botL: Float,
        botR: Float,
        shadowSoftness: Float
    ) {
        if (!DropShadow.isInitialized() || !DropShadow.shader.usable) return

        DropShadow.shader.bind()
        DropShadow.shaderCenterUniform.setValue(x + (width * 0.5f), y + (height * 0.5f))
        DropShadow.shaderSizeUniform.setValue(width, height)
        DropShadow.shaderRadiusUniform.setValue(botR, topR, botL, topL)
        DropShadow.shaderShadowColorUniform.setValue(
            shadowColor.r * COLOR_NORMALIZER,
            shadowColor.g * COLOR_NORMALIZER,
            shadowColor.b * COLOR_NORMALIZER,
            shadowColor.alpha
        )
        DropShadow.shaderShadowSoftness.setValue(shadowSoftness)

        UIBlock.drawBlockWithActiveShader(
            matrixStack,
            Color.WHITE.javaColor,
            x.toDouble() - shadowSoftness,
            y.toDouble() - shadowSoftness,
            x.toDouble() + width.toDouble() + shadowSoftness * 2,
            y.toDouble() + height.toDouble() + shadowSoftness * 2
        )

        DropShadow.shader.unbind()
    }


    object RoundedRectangle {
        lateinit var shader: UShader
        lateinit var shaderCenterUniform: Float2Uniform
        lateinit var shaderSizeUniform: Float2Uniform
        lateinit var shaderRadiusUniform: Float4Uniform
        lateinit var shaderBorderThicknessUniform: FloatUniform
        lateinit var shaderEdgeSoftnessUniform: FloatUniform
        lateinit var shaderColorUniform: Float4Uniform
        lateinit var shaderColor2Uniform: Float4Uniform
        lateinit var shaderGradientDir: Float2Uniform
        lateinit var shaderBorderColorUniform: Float4Uniform
        lateinit var shaderShadowColorUniform: Float4Uniform
        lateinit var shaderShadowSoftness: FloatUniform
        val directionVecs = listOf(Pair(1f, 0f), Pair(0f, 1f), Pair(-1f, 0f), Pair(0f, -1f))

        fun isInitialized() = RoundedRectangle::shader.isInitialized

        fun initShader() {
            if (RoundedRectangle::shader.isInitialized) return

            shader = createLegacyShader("rectangle", "roundedrectangle", BlendState.NORMAL)
            if (!shader.usable) {
                logger.error("Failed to load noobestroutes rounded rectangle shader")
                return
            }
            shaderCenterUniform = shader.getFloat2Uniform("u_rectCenter")
            shaderSizeUniform = shader.getFloat2Uniform("u_rectSize")
            shaderRadiusUniform = shader.getFloat4Uniform("u_Radii")
            shaderBorderThicknessUniform = shader.getFloatUniform("u_borderThickness")
            shaderEdgeSoftnessUniform = shader.getFloatUniform("u_edgeSoftness")
            shaderColorUniform = shader.getFloat4Uniform("u_colorRect")
            shaderColor2Uniform = shader.getFloat4Uniform("u_colorRect2")
            shaderGradientDir = shader.getFloat2Uniform("u_gradientDirectionVector")
            shaderBorderColorUniform = shader.getFloat4Uniform("u_colorBorder")
            shaderShadowColorUniform = shader.getFloat4Uniform("u_colorShadow")
            shaderShadowSoftness = shader.getFloatUniform("u_shadowSoftness")
        }
    }

    object HSBBox {
        lateinit var shader: UShader
        lateinit var shaderCenterUniform: Float2Uniform
        lateinit var shaderSizeUniform: Float2Uniform
        lateinit var shaderColorUniform: Float4Uniform

        fun isInitialized() = HSBBox::shader.isInitialized

        fun initShader() {
            if (HSBBox::shader.isInitialized) return

            shader = createLegacyShader("rectangle", "hsbbox", BlendState.NORMAL)
            if (!shader.usable) {
                println("Failed to load Odin HSBBox shader")
                return
            }
            shaderCenterUniform = shader.getFloat2Uniform("u_rectCenter")
            shaderSizeUniform = shader.getFloat2Uniform("u_rectSize")
            shaderColorUniform = shader.getFloat4Uniform("u_colorRect")
        }
    }

    object Circle {
        lateinit var shader: UShader
        lateinit var shaderCenterUniform: Float2Uniform
        lateinit var shaderRadiusUniform: FloatUniform
        lateinit var shaderColorUniform: Float4Uniform
        lateinit var shaderBorderColorUniform: Float4Uniform
        lateinit var shaderBorderThicknessUniform: FloatUniform

        fun isInitialized() = Circle::shader.isInitialized

        fun initShader() {
            if (Circle::shader.isInitialized) return

            shader = createLegacyShader("rectangle", "circleFragment", BlendState.NORMAL)
            if (!shader.usable) {
                println("Failed to load Odin circle shader")
                return
            }
            shaderCenterUniform = shader.getFloat2Uniform("u_circleCenter")
            shaderRadiusUniform = shader.getFloatUniform("u_circleRadius")
            shaderColorUniform = shader.getFloat4Uniform("u_colorCircle")
            shaderBorderColorUniform = shader.getFloat4Uniform("u_colorBorder")
            shaderBorderThicknessUniform = shader.getFloatUniform("u_borderThickness")

        }
    }

    object DropShadow {
        lateinit var shader: UShader
        lateinit var shaderCenterUniform: Float2Uniform
        lateinit var shaderSizeUniform: Float2Uniform
        lateinit var shaderRadiusUniform: Float4Uniform
        lateinit var shaderShadowColorUniform: Float4Uniform
        lateinit var shaderShadowSoftness: FloatUniform

        fun isInitialized() = DropShadow::shader.isInitialized

        fun initShader() {
            if (DropShadow::shader.isInitialized) return

            shader = createLegacyShader("rectangle", "dropShadow", BlendState.NORMAL)
            if (!shader.usable) {
                println("Failed to load Odin drop shadow shader")
                return
            }
            shaderCenterUniform = shader.getFloat2Uniform("u_rectCenter")
            shaderSizeUniform = shader.getFloat2Uniform("u_rectSize")
            shaderRadiusUniform = shader.getFloat4Uniform("u_Radii")
            shaderShadowColorUniform = shader.getFloat4Uniform("u_colorShadow")
            shaderShadowSoftness = shader.getFloatUniform("u_shadowSoftness")

        }
    }
}