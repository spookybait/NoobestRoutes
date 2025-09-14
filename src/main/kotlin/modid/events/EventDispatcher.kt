package modid.events

import modid.Core.mc
import modid.events.impl.ChatPacketEvent
import modid.events.impl.PacketEvent
import modid.events.impl.ServerTickEvent
import modid.events.impl.WorldChangeEvent
import modid.utils.noControlCodes
import modid.utils.postAndCatch
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.network.play.server.S32PacketConfirmTransaction
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object EventDispatcher {


    /**
     * Dispatches [ChatPacketEvent], [ServerTickEvent]
     */
    @SubscribeEvent
    fun onPacket(event: PacketEvent.Receive) {

        if (event.packet is S32PacketConfirmTransaction) ServerTickEvent().postAndCatch()

        if (event.packet !is S02PacketChat || !ChatPacketEvent(event.packet.chatComponent.unformattedText.noControlCodes).postAndCatch()) return
        event.isCanceled = true
    }

    private var lastEntityClick = System.currentTimeMillis()

    @SubscribeEvent
    fun onPacketSent(event: PacketEvent.Send) {
        if (event.packet !is C02PacketUseEntity) return
        val entity = event.packet.getEntityFromWorld(mc.theWorld)
        if (entity !is EntityArmorStand) return
        val armorStand: EntityArmorStand = entity
        if (armorStand.name.noControlCodes.contains("Inactive Terminal", true)) lastEntityClick = System.currentTimeMillis()
    }

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload){
        WorldChangeEvent().postAndCatch()
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        WorldChangeEvent().postAndCatch()
    }
}
