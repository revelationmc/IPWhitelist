package net.revelationmc.ipwhitelist.common;

import com.google.common.collect.ImmutableList;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigurateIpWhitelistConfiguration implements IpWhitelistConfiguration {
    private final ConfigurationLoader<CommentedConfigurationNode> loader;
    private CommentedConfigurationNode node;

    private final List<String> bungeeips;

    public ConfigurateIpWhitelistConfiguration(ConfigurationLoader<CommentedConfigurationNode> loader, List<String> ips) {
        this.bungeeips = Objects.requireNonNullElseGet(ips, ArrayList::new);
        this.loader = loader;
        this.reload();
    }

    public boolean isDebugEnabled() {
        return this.node.node("debug").getBoolean(false);
    }

    public boolean isSetupModeEnabled() {
        return this.node.node("setup").getBoolean(false);
    }

    public void reload() {
        try {
            this.node = loader.load();
            if (this.node.node("setup").getBoolean(false) && !bungeeips.isEmpty() && !Objects.requireNonNull(this.node.node("whitelist").getList(String.class)).isEmpty()) {
                this.node.node("setup").set(false);
                this.loader.save(this.node);
            }
        } catch (IOException e) {
            this.node = CommentedConfigurationNode.root();
            e.printStackTrace();
        }
    }

    public List<String> getIPs() {
        try {
            return ImmutableList.<String>builder()
                    .addAll(this.bungeeips)
                    .addAll(this.node.node("whitelist").getList(String.class, new ArrayList<>()))
                    .build();
        } // Impossible
        catch (SerializationException ignored) {
            return null;
        }
    }

    public String getKickMessage() {
        return this.node.node("playerKickMessage").getString("&cYou have to join through the proxy.");
    }

    public boolean allow(String ip) {
        try {
            return this.bungeeips.contains(ip) || this.node.node("whitelist").getList(String.class, new ArrayList<>()).contains(ip);
        } catch (SerializationException ignored) {
            return false;
        }
    }

    public boolean allow(InetSocketAddress addr) {
        return allow(addr.getAddress().getHostAddress());
    }

    public boolean disallow(InetAddress addr) {
        return !allow(addr.getHostAddress());
    }

    public boolean whitelist(InetSocketAddress ip) {
        return whitelist(ip.getAddress().getHostAddress());
    }

    public boolean whitelist(String ip) {
        List<String> whitelist = new ArrayList<>();
        try {
            whitelist = this.node.node("whitelist").getList(String.class, new ArrayList<>());
        } catch (SerializationException ignored) {
        }

        if (whitelist.contains(ip)) return false;
        whitelist.add(ip);
        try {
            this.node.node("whitelist").set(whitelist);
            this.node.node("setup").set(false);
        } catch (SerializationException ignored) {
        }
        try {
            this.loader.save(this.node);
        } catch (IOException e) {
            // TODO: log
        }
        return true;
    }

    public WhitelistRemoveResponse unwhitelist(String ip) {
        List<String> whitelist = new ArrayList<>();
        try {
            whitelist = new ArrayList<>(Objects.requireNonNull(this.node.node("whitelist").getList(String.class, new ArrayList<>())));
        } catch (SerializationException ignored) {
        }
        boolean removed = whitelist.remove(ip);
        try {
            this.node.node("whitelist").setList(String.class, whitelist);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        try {
            this.loader.save(this.node);
        } catch (IOException e) {
            // TODO: log
        }
        if (removed) return WhitelistRemoveResponse.SUCCESS;
        else if (bungeeips.contains(ip)) return WhitelistRemoveResponse.FAILED_IN_BUNGEE_PROXIES;
        else return WhitelistRemoveResponse.FAILED_UNKNOWN;
    }

    public boolean toggleDebug() {
        boolean newV = !this.node.node("debug").getBoolean(false);
        try {
            this.node.node("debug").set(newV);
        } catch (SerializationException ignored) {
        }
        try {
            this.loader.save(this.node);
        } catch (IOException e) {
            // TODO: log
        }
        return newV;
    }

    public boolean toggleSetup() {
        boolean newV = !this.node.node("setup").getBoolean(false);
        try {
            this.node.node("setup").set(newV);
        } catch (SerializationException ignored) {
        }
        try {
            this.loader.save(this.node);
        } catch (IOException e) {
            // TODO: log
        }
        return newV;
    }
}
