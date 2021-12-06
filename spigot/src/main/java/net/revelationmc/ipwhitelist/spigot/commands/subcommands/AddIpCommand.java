package net.revelationmc.ipwhitelist.spigot.commands.subcommands;

import net.revelationmc.ipwhitelist.spigot.IpWhitelistPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AddIpCommand implements SubCommand {
    private final IpWhitelistPlugin plugin;

    public AddIpCommand(IpWhitelistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(IpWhitelistPlugin.TAG + ChatColor.AQUA + "Command usage : ");
            sender.sendMessage(ChatColor.AQUA + "/ipwhitelist addip <ip>");
        } else if (this.plugin.getConfiguration().whitelist(args[1])) {
            sender.sendMessage(IpWhitelistPlugin.TAG + ChatColor.AQUA + "Successfully whitelisted IP " + args[1] + "!");
        } else {
            sender.sendMessage(IpWhitelistPlugin.TAG + ChatColor.AQUA + "IP " + args[1] + " was already whitelisted!");
        }
    }
}
