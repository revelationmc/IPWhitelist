package net.revelationmc.ipwhitelist.spigot.commands.subcommands;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    void execute(CommandSender sender, String label, String[] args);
}
