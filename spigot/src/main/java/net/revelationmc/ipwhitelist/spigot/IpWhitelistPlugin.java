package net.revelationmc.ipwhitelist.spigot;

import com.google.common.collect.Lists;
import net.revelationmc.ipwhitelist.common.ConfigurateIpWhitelistConfiguration;
import net.revelationmc.ipwhitelist.common.IpWhitelistConfiguration;
import net.revelationmc.ipwhitelist.spigot.commands.IpWhitelistCommand;
import net.revelationmc.ipwhitelist.spigot.commands.subcommands.*;
import net.revelationmc.ipwhitelist.spigot.listeners.ConnectionListener;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

public class IpWhitelistPlugin extends JavaPlugin {
    public static final String TAG = ChatColor.ITALIC.toString() + ChatColor.GREEN + "[" + ChatColor.AQUA + "Revelation IP Whitelist" + ChatColor.GREEN + "] " + ChatColor.RESET;

    private IpWhitelistConfiguration configuration;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder().file(new File(this.getDataFolder(), "config.yml")).build();
        this.configuration = new ConfigurateIpWhitelistConfiguration(loader, this.getBukkitConfig());

        this.getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
        this.initCommands();
    }

    @Override
    public void onDisable() {
        configuration = null;
    }

    private List<String> getBukkitConfig() {
        // TODO: Get config
        File spigotyml = new File(this.getDataFolder().getParentFile().getParentFile(), "spigot.yml");
        File bukkityml = new File(this.getDataFolder().getParentFile().getParentFile(), "bukkit.yml");
        if (spigotyml.exists()) {
            Configuration spigotcfg = YamlConfiguration.loadConfiguration(spigotyml);
            if (spigotcfg.getBoolean("settings.bungeecord")) {
                return spigotcfg.getStringList("settings.bungeecord-addresses");
            }
        } else if (bukkityml.exists()) {
            Configuration bukkitcfg = YamlConfiguration.loadConfiguration(new File(this.getDataFolder().getParentFile().getParentFile(), "bukkit.yml"));
            return bukkitcfg.getStringList("settings.bungee-proxies");
        }
        return Lists.newArrayList();
    }

    private void initCommands() {
        final IpWhitelistCommand command = new IpWhitelistCommand(this);
        command.addSubCommand("addip", new AddIpCommand(this), "add");
        command.addSubCommand("debug", new DebugCommand(this));
        command.addSubCommand("list", new ListCommand(this));
        command.addSubCommand("reload", new ReloadCommand(this));
        command.addSubCommand("removeip", new RemoveIpCommand(this), "remip", "remove");
        command.addSubCommand("setup", new SetupCommand(this));

        final PluginCommand pluginCommand = this.getCommand("ipwhitelist");
        if (pluginCommand == null) { // Should never happen.
            throw new IllegalStateException("ipwhitelist command null! Is it in the plugin.yml?");
        }

        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);
    }

    public void debug(String s) {
        if (this.configuration.isDebugEnabled()) {
            this.getLogger().log(Level.INFO, "[DEBUG] " + s);
        }
    }

    public IpWhitelistConfiguration getConfiguration() {
        return this.configuration;
    }
}
