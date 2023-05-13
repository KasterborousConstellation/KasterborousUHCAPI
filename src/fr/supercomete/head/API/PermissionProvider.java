package fr.supercomete.head.API;

import fr.supercomete.head.permissions.Permissions;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public interface PermissionProvider {
    ArrayList<UUID> getAllowedPlayers(Permissions permission);
    void retrievePermission(Player player, Permissions permission);
    boolean IsAllowed(Player player, Permissions permission);
    void givePermission(Player player, Permissions permission);
    void retrievePermission(UUID uuid, Permissions permission);
    boolean IsAllowed(UUID uuid, Permissions permission);
    void givePermission(UUID uuid, Permissions permission);
    void sendDenyPermissionMessage(Player player);
    ArrayList<Permissions> getHostPermissions();
    ArrayList<Permissions> getCoHostPermissions();
}
