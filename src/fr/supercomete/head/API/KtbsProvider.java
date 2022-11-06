package fr.supercomete.head.API;

import fr.supercomete.head.core.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class KtbsProvider implements HostProvider{

    public HostProvider getHostProvider(){
        return (HostProvider) this;
    }
    @Override
    public ArrayList<UUID> getCohosts() {
        return Main.cohost;
    }

    @Override
    public UUID getHost() {
        return Main.host;
    }

    @Override
    public boolean isHost(Player player) {
        assert Main.host!=null;
        return Main.host.equals(player.getUniqueId());
    }

    @Override
    public boolean isHost(UUID uuid) {
        assert Main.host!=null;
        return Main.host.equals(uuid);
    }

    @Override
    public boolean isCohost(UUID uuid) {
        return Main.cohost.contains(uuid);
    }

    @Override
    public boolean isCohost(Player player) {
        return Main.cohost.contains(player.getUniqueId());
    }

    @Override
    public boolean isBypassed(Player player) {
        return (isCohost(player)||isHost(player))&&
                Main.bypass.contains(player.getUniqueId());
    }

    @Override
    public boolean isBypassed(UUID uuid) {
        return (isHost(uuid)||isCohost(uuid))&& Main.bypass.contains(uuid);
    }
}
