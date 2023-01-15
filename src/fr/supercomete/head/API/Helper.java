package fr.supercomete.head.API;

import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

public class Helper implements PlayerHelper{

    @Override
    public Inventory getStartInventory() {
        return PlayerUtility.getInventory();
    }

    @Override
    public String getNameFromUUID(UUID uuid) {
        return PlayerUtility.getNameByUUID(uuid);
    }

    @Override
    public void sendActionBarMessage(Player player, String message) {
        PlayerUtility.sendActionbar(player,message);
    }

    @Nullable
    @Override
    public Player getTargetedPlayer(Player player, int range) {
        return PlayerUtility.getTarget(player,range);
    }

    @Override
    public Offline_Player getOfflinePlayer(Player player) {
        return Main.currentGame.getOffline_Player(player);
    }

    @Override
    public Offline_Player getOfflinePlayer(UUID uuid) {
        return (Main.currentGame.getOffline_Player(uuid)==null)?
                new Offline_Player(Bukkit.getPlayer(uuid))
                :
                Main.currentGame.getOffline_Player(uuid)
                ;
    }

    @Override
    public ArrayList<Offline_Player> getOfflinePlayers() {
        return Main.currentGame.getOfflinelist();
    }

    @Override
    public boolean IsPlayerInGame(UUID uuid){
        if(Bukkit.getPlayer(uuid)==null){
            return false;
        }
        Player player =Bukkit.getPlayer(uuid);
        if(!player.isOnline()){
            return false;
        }
        return Main.getPlayerlist().contains(uuid);
    }

    @Override
    public boolean IsPlayerAlive(UUID uuid) {
        return Main.getPlayerlist().contains(uuid);
    }
}