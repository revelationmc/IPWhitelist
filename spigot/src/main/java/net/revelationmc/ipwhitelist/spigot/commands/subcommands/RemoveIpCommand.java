package net.revelationmc.ipwhitelist.spigot.commands.subcommands;

import net.revelationmc.ipwhitelist.common.WhitelistRemoveResponse;
import net.revelationmc.ipwhitelist.spigot.IpWhitelistPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RemoveIpCommand implements SubCommand {
    private final IpWhitelistPlugin plugin;

    public RemoveIpCommand(IpWhitelistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(IpWhitelistPlugin.TAG + ChatColor.AQUA + "Command usage : ");
            sender.sendMessage(ChatColor.AQUA + "/ipwhitelist remip <ip>");
            return;
        }

        final WhitelistRemoveResponse response = this.plugin.getConfiguration().unwhitelist(args[1]);

        if (response == WhitelistRemoveResponse.SUCCESS) {
            sender.sendMessage(IpWhitelistPlugin.TAG + ChatColor.AQUA + "Successfully unwhitelisted IP " + args[1] + "!");
        } else if (response == WhitelistRemoveResponse.FAILED_IN_BUNGEE_PROXIES) {
            sender.sendMessage(IpWhitelistPlugin.TAG + ChatColor.AQUA + "IP " + args[1] + " is in your bukkit.yml or spigot.yml bungee-proxies. Remove it there!");
        } else {
            sender.sendMessage(IpWhitelistPlugin.TAG + ChatColor.AQUA + "IP " + args[1] + " was not whitelisted!");
        }
    }
}
