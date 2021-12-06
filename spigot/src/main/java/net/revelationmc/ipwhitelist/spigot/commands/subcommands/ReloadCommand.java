package net.revelationmc.ipwhitelist.spigot.commands.subcommands;

import net.revelationmc.ipwhitelist.spigot.IpWhitelistPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements SubCommand {
    private final IpWhitelistPlugin plugin;

    public ReloadCommand(IpWhitelistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        this.plugin.getConfiguration().reload();
        sender.sendMessage(IpWhitelistPlugin.TAG + ChatColor.AQUA + "Successfully reloaded config!");
    }
}
