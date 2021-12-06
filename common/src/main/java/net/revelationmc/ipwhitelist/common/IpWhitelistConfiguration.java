package net.revelationmc.ipwhitelist.common;

import java.net.InetAddress;
import java.util.List;

public interface IpWhitelistConfiguration {
    void reload();

    boolean toggleDebug();

    boolean toggleSetup();

    boolean isSetupModeEnabled();

    boolean isDebugEnabled();

    boolean whitelist(String address);

    boolean disallow(InetAddress address);

    boolean allow(String address);

    WhitelistRemoveResponse unwhitelist(String address);

    String getKickMessage();

    List<String> getIPs();
}
