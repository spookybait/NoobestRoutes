package noobestroutes.events.impl

import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
open class MoveEntityWithHeadingEvent() : Event() {
    class Pre() : MoveEntityWithHeadingEvent()

    class Post() : MoveEntityWithHeadingEvent()
}