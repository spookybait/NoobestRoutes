package noobestroutes.utils.skyblock.dungeon

import noobestroutes.events.impl.PacketEvent
import noobestroutes.utils.PlayerUtils
import noobestroutes.utils.noControlCodes
import noobestroutes.utils.romanToInt
import net.minecraft.network.play.server.S3EPacketTeams
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter


class DungeonInstance(val floor: Floor) {

    val inBoss: Boolean get() = getBoss()

    private fun getBoss(): Boolean {
        return when (floor.floorNumber) {
            1 -> PlayerUtils.posX > -71 && PlayerUtils.posZ > -39
            in 2..4 -> PlayerUtils.posX > -39 && PlayerUtils.posZ > -39
            in 5..6 -> PlayerUtils.posX > -39 && PlayerUtils.posZ > -7
            7 -> PlayerUtils.posX > -7 && PlayerUtils.posZ > -7
            else -> false
        }
    }


    fun onPacket(event: PacketEvent.Receive) {
        when (event.packet) {
            is S3EPacketTeams -> handleScoreboardPacket(event.packet)
            is S47PacketPlayerListHeaderFooter -> handleHeaderFooterPacket(event.packet)
        }
    }

    fun onWorldLoad() {
        Blessing.entries.forEach { it.current = 0 }
    }

    private fun handleHeaderFooterPacket(packet: S47PacketPlayerListHeaderFooter) {
        Blessing.entries.forEach { blessing ->
            blessing.regex.find(packet.footer.unformattedText.noControlCodes)?.let { match -> blessing.current =
                romanToInt(match.groupValues[1])
            }
        }
    }

    private fun handleScoreboardPacket(packet: S3EPacketTeams) {
        if (packet.action != 2) return
    }

}