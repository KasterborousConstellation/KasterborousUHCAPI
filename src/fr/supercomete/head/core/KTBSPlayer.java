package fr.supercomete.head.core;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nullable;
import java.util.UUID;
public final class KTBSPlayer {
    private final UUID player;
    private final String username;
    public KTBSPlayer(Player player){
        this.player=player.getUniqueId();
        this.username=player.getName();
    }
    public String getName() {
        return username;
    }
    public UUID getUniqueId(){
        return player;
    }
    public boolean isOnline(){
        return !Main.currentGame.hasOfflinePlayer(player);
    }
    public int getTimeSinceLastConnexion(){
        if(isOnline()){
            return 0;
        }else{
            return (int) Main.currentGame.getOffline_Player(player).getTimeElapsedSinceLastConnexion();
        }
    }
    public Location getLocation(){
        if(isOnline()){
            return getPlayer().getLocation();
        }else{
            return Main.currentGame.getOffline_Player(player).getLocation();
        }
    }
    public @Nullable Player getPlayer(){
        return Bukkit.getPlayer(player);
    }
    public void teleport(Location location){
        if(isOnline()){
            getPlayer().teleport(location);
        }else{
            Main.currentGame.getOffline_Player(player).setLocation(location);
        }

    }
    public double getHealth(){
        if(isOnline()){
            return getPlayer().getHealth();
        }else{
            return Main.currentGame.getOffline_Player(player).getLife();
        }
    }
    public double getMaxHealth(){
        if(isOnline()){
            return getPlayer().getMaxHealth();
        }else{
            return Main.currentGame.getOffline_Player(player).getMaxLife();
        }
    }
    public Inventory getInventory(){
        if(isOnline()){
            return getPlayer().getInventory();
        }else{
            return Main.currentGame.getOffline_Player(player).getInventory();
        }
    }
    public Role getRole(){
        return RoleHandler.getRoleOf(player);
    }
    public int getDiamondLimit(){
        final int limit = Bukkit.getServicesManager().load(KtbsAPI.class).getConfigurableProvider().getDataFrom(Configurable.LIST.DiamondLimit);
        try {
            return limit-Main.diamondmap.get(player);
        }catch (Exception e){
            return limit;
        }
    }
}