package modid.commands

import modid.Core.display
import modid.ui.clickgui.ClickGui
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class ModIDCommand : CommandBase() {
    override fun getCommandName(): String {
        return "@MOD_ID@"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "Opens @MOD_ID@ GUI"
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if (args == null || args.isEmpty()) {
            display = ClickGui
            return
        }
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }

    override fun getCommandAliases(): List<String> {
        return listOf()
    }



}