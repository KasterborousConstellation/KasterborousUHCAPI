package fr.supercomete.head.API;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;
public interface PlayerHelper {
    Inventory getStartInventory();
    String getNameFromUUID(UUID uuid);
    void sendActionBarMessage(Player player,String message);
    @Nullable Player getTargetedPlayer(Player player,int range);
    Offline_Player getOfflinePlayer(Player player);
    Offline_Player getOfflinePlayer(String name);
    Offline_Player getOfflinePlayer(UUID uuid);
    ArrayList<Offline_Player> getOfflinePlayers();
}