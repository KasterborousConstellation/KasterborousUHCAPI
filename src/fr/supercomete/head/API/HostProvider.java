package fr.supercomete.head.API;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public interface HostProvider {
    ArrayList<UUID> getCohosts();
    UUID getHost();
    boolean isHost(final Player player);
    boolean isHost(final UUID uuid);
    boolean isCohost(final UUID uuid);
    boolean isCohost(final Player player);
    boolean isBypassed(final Player player);
    boolean isBypassed(final UUID uuid);
}
