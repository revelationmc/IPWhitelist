package net.revelationmc.ipwhitelist.spigot.commands;

import net.revelationmc.ipwhitelist.spigot.IpWhitelistPlugin;
import net.revelationmc.ipwhitelist.spigot.commands.subcommands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class IpWhitelistCommand implements CommandExecutor, TabCompleter {
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    private final IpWhitelistPlugin plugin;

    public IpWhitelistCommand(IpWhitelistPlugin plugin) {
        this.plugin = plugin;
    }

    public void addSubCommand(String name, SubCommand command, String... aliases) {
        this.subCommands.put(name.toLowerCase(Locale.ROOT), command);
        for (String alias : aliases) {
            this.subCommands.put(alias.toLowerCase(Locale.ROOT), command);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            this.sendHelp(sender);
            return true;
        }

        final String subCommandName = args[0].toLowerCase(Locale.ROOT);

        if (!this.subCommands.containsKey(subCommandName)) {
            this.sendHelp(sender);
            return true;
        }

        this.subCommands.get(subCommandName).execute(sender, label, args);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(IpWhitelistPlugin.TAG + ChatColor.AQUA + "Commands : ");
        sender.sendMessage(ChatColor.AQUA + "/ipwhitelist list [page] - List whitelisted IPs");
        sender.sendMessage(ChatColor.AQUA + "/ipwhitelist addip <ip> - Add IP to whitelist");
        sender.sendMessage(ChatColor.AQUA + "/ipwhitelist remip <ip> - Removes IP to whitelist");
        sender.sendMessage(ChatColor.AQUA + "/ipwhitelist reload - Reload whitelist");
        sender.sendMessage(ChatColor.AQUA + "/ipwhtelist debug - Toggles debug state");
        sender.sendMessage(ChatColor.AQUA + "/ipwhitelist setup - Turn setup mode on");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0) {
            return new ArrayList<>(this.subCommands.keySet());
        }

        final String typed = args[0].toLowerCase(Locale.ROOT);
        final List<String> completions = new ArrayList<>();

        for (String subCommandName : this.subCommands.keySet()) {
            if (typed.startsWith(subCommandName)) {
                completions.add(subCommandName);
            }
        }
        return completions;
    }
}
