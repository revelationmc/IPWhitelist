package net.revelationmc.ipwhitelist.spigot.commands.subcommands;

import net.revelationmc.ipwhitelist.spigot.IpWhitelistPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DebugCommand implements SubCommand {
    private final IpWhitelistPlugin plugin;

    public DebugCommand(IpWhitelistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        boolean debug = this.plugin.getConfiguration().toggleDebug();
        sender.sendMessage(IpWhitelistPlugin.TAG + ChatColor.AQUA + "Debug mode : " + ChatColor.RED + debug);
    }
}
