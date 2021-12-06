package net.revelationmc.ipwhitelist.spigot.commands.subcommands;

import net.revelationmc.ipwhitelist.spigot.IpWhitelistPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.ChatPaginator;

public class ListCommand implements SubCommand {
    private final IpWhitelistPlugin plugin;

    public ListCommand(IpWhitelistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        sender.sendMessage(IpWhitelistPlugin.TAG + ChatColor.AQUA + "Whitelisted IPs :");
        StringBuilder iplistbuff = new StringBuilder();
        for (String ip : this.plugin.getConfiguration().getIPs()) {
            iplistbuff.append(ChatColor.AQUA + ip + "\n");
        }
        // Delete last newline if there is one.
        if (iplistbuff.length() > 0) {
            iplistbuff.deleteCharAt(iplistbuff.length() - 1);
        }
        String iplist = iplistbuff.toString();
        ChatPaginator.ChatPage page;
        if (args.length > 1) {
            page = ChatPaginator.paginate(iplist, Integer.parseInt(args[1]), ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH, ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 2);
            sender.sendMessage(page.getLines());
        } else {
            page = ChatPaginator.paginate(iplist, 1, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH, ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 2);
            sender.sendMessage(page.getLines());
        }
        sender.sendMessage(ChatColor.AQUA + "Page " + page.getPageNumber() + "/" + page.getTotalPages() + ".");
    }
}
