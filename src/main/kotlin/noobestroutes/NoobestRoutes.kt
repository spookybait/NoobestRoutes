package noobestroutes

import gg.essential.elementa.font.FontRenderer
import noobestroutes.Core.mc
import noobestroutes.commands.NoobestRoutesCommand
import noobestroutes.events.BossEventDispatcher
import noobestroutes.events.EventDispatcher
import noobestroutes.features.ModuleManager
import noobestroutes.ui.clickgui.ClickGui
import noobestroutes.utils.PacketUtils
import noobestroutes.utils.PlayerUtils
import noobestroutes.utils.Scheduler
import noobestroutes.utils.Utils
import noobestroutes.utils.clock.Executor
import noobestroutes.utils.render.RenderUtils
import noobestroutes.utils.render.Renderer
import noobestroutes.utils.render.initUIFramebufferStencil
import noobestroutes.utils.skyblock.LocationUtils
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
            NoobestRoutesCommand()
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
        //noobestroutes.font.FontRenderer.init()
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
