package noobestroutes.utils.skyblock.dungeon

import noobestroutes.events.impl.PacketEvent
import noobestroutes.utils.skyblock.Island
import noobestroutes.utils.skyblock.LocationUtils
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object DungeonUtils {

    inline val inDungeons: Boolean
        get() = LocationUtils.currentArea.isArea(Island.Dungeon)

    inline val floorNumber: Int
        get() = LocationUtils.currentDungeon?.floor?.floorNumber ?: 0

    inline val floor: Floor
        get() = LocationUtils.currentDungeon?.floor ?: Floor.E

    /**
     * Checks if the current dungeon floor number matches any of the specified options.
     *
     * @param options The floor number options to compare with the current dungeon floor.
     * @return `true` if the current dungeon floor matches any of the specified options, otherwise `false`.
     */
    fun isFloor(vararg options: Int): Boolean {
        return floorNumber in options
    }

    @SubscribeEvent
    fun onPacket(event: PacketEvent.Receive) {
        if (inDungeons) LocationUtils.currentDungeon?.onPacket(event)
    }


    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        LocationUtils.currentDungeon?.onWorldLoad()
    }
}