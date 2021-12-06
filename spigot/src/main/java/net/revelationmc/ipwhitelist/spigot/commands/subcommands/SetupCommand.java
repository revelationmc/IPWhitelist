package net.revelationmc.ipwhitelist.spigot.commands.subcommands;

import net.revelationmc.ipwhitelist.spigot.IpWhitelistPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SetupCommand implements SubCommand {
    private final IpWhitelistPlugin plugin;

    public SetupCommand(IpWhitelistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (!this.plugin.getConfiguration().isSetupModeEnabled() && !(this.plugin.getConfiguration().getIPs().isEmpty())) {
            sender.sendMessage(IpWhitelistPlugin.TAG + ChatColor.RED + "Cannot enable setup mode, some IPs are already whitelisted");
            return;
        }
        boolean setup = this.plugin.getConfiguration().toggleSetup();
        sender.sendMessage(IpWhitelistPlugin.TAG + ChatColor.AQUA + "Setup mode : " + ChatColor.RED + setup);
    }
}
