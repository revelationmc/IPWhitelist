package net.revelationmc.ipwhitelist.spigot.listeners;

import net.revelationmc.ipwhitelist.spigot.IpWhitelistPlugin;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConnectionListener implements Listener {
    private final Map<UUID, InetAddress> addresses = new HashMap<>();

    private final IpWhitelistPlugin plugin;

    public ConnectionListener(IpWhitelistPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChannelRegistered(PlayerRegisterChannelEvent ev) {
        if (this.plugin.getConfiguration().isSetupModeEnabled() && ev.getChannel().equals("BungeeCord")) {
            this.plugin.getConfiguration().whitelist(this.addresses.get(ev.getPlayer().getUniqueId()).getHostAddress());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerLogin(final PlayerLoginEvent ev) {
        final InetAddress addr = ev.getRealAddress();
        this.plugin.debug("Player " + ev.getPlayer().getName() + " is connecting with IP : " + addr);
        if (this.plugin.getConfiguration().isSetupModeEnabled()) {
            this.addresses.put(ev.getPlayer().getUniqueId(), addr);
            this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                if (this.plugin.getConfiguration().isSetupModeEnabled())
                    ev.getPlayer().kickPlayer("Server is in setup mode");
                else if (this.plugin.getConfiguration().disallow(addr))
                    ev.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfiguration().getKickMessage()));
            }, 20L);
        } else if (this.plugin.getConfiguration().disallow(addr)) {
            ev.setKickMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfiguration().getKickMessage()));
            ev.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
        }
    }
}
