package modid

import gg.essential.elementa.font.FontRenderer
import modid.Core.mc
import modid.commands.ModIDCommand
import modid.events.BossEventDispatcher
import modid.events.EventDispatcher
import modid.features.ModuleManager
import modid.ui.clickgui.ClickGui
import modid.utils.PacketUtils
import modid.utils.PlayerUtils
import modid.utils.Scheduler
import modid.utils.Utils
import modid.utils.clock.Executor
import modid.utils.render.RenderUtils
import modid.utils.render.Renderer
import modid.utils.render.initUIFramebufferStencil
import modid.utils.skyblock.LocationUtils
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import java.io.File


const val MODID = "@MOD_ID@"

@Mod(modid = MODID, useMetadata = true)
class NoobRoutes {
    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {

        listOf(
            ModIDCommand()
        ).forEach {
            ClientCommandHandler.instance.registerCommand(it)
        }

        listOf(
            Core,
            ModuleManager,
            Executor,
            Renderer,
            RenderUtils,
            ClickGui,
            Scheduler,
            PacketUtils,
            Utils,
            LocationUtils,
            EventDispatcher,
            BossEventDispatcher,
            PlayerUtils
        ).forEach {
            MinecraftForge.EVENT_BUS.register(it)
        }
        //this is probably done already by other mods, but it wasn't in the dev env, so I am doing it here
        FontRenderer.initShaders()
        //modid.font.FontRenderer.init()
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        Core.postInit()
        initUIFramebufferStencil()
    }

    @Mod.EventHandler
    fun loadComplete(event: FMLLoadCompleteEvent) {
        File(mc.mcDataDir, "config/@MOD_ID@").takeIf { !it.exists() }?.mkdirs()
        Core.loadComplete()
        ModuleManager.addModules()
    }

}
