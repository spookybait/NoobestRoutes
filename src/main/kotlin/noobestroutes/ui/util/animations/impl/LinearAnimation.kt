package noobestroutes.ui.util.animations.impl

import noobestroutes.ui.util.animations.Animation
import kotlin.math.max
import kotlin.math.min

class LinearAnimation<E>(duration: Long) : Animation<E>(duration) where E : Number, E : Comparable<E> {


    @Suppress("UNCHECKED_CAST")
    override fun get(start: E, end: E, reverse: Boolean): E {
        val startVal = if (reverse) end.toFloat() else start.toFloat()
        val endVal = if (reverse) start.toFloat() else end.toFloat()

        if (!isAnimating()) return if (reverse) start else end
        return (startVal + (endVal - startVal) * (getPercent() * 0.01f)).coerceIn(
            min(start.toFloat(), end.toFloat()),
            max(start.toFloat(), end.toFloat())
        ) as E
    }
}
