package fr.supercomete.head.API;

import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.UUID;

import java.util.logging.Level;

public class Helper implements PlayerHelper,CoordinateHelper{

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

    @Override
    public double Distance(Player player1, Player player2) {
        return player1.getWorld().equals(player2.getWorld())?player1.getLocation().distance(player2.getLocation()):Double.MAX_VALUE;
    }

    @Override
    public String getDirectionArrow(Location locationn, Location Toward){
        if(Toward==null|| !locationn.getWorld().equals(Toward.getWorld())){
            return "§c???";
        }
        final double pi = Math.PI;
        final Vector vector = locationn.getDirection();
        vector.setY(0);
        final Vector direction = new Vector(Toward.getX()-locationn.getX(),0,Toward.getZ()-locationn.getZ());
        final Vector direction_90 = new Vector(direction.getZ(),0,-direction.getX());
        final double discriminant = vector.angle(direction_90);
        double angle = direction.angle(vector);
        if(discriminant>pi/2.0){
            angle = 2*pi -angle;
        }
        if(angle< pi/8.0) {
            return "§b⬆";
        }else if(angle<(3.0*pi)/8.0){
            return "§b⬈";
        }else if(angle<(5.0*pi)/8.0){
            return "§b➡";
        }else if(angle< (7.0*pi)/8.0) {
            return "§b⬊";
        }else if(angle<(9.0*pi)/8.0) {
            return "§b⬇";
        }else if(angle<(11.0*pi)/8.0){
            return "§b⬋";
        }else if(angle<(13.0*pi)/8.0){
            return "§b⬅";
        }else if(angle<(15.0*pi)/8.0){
            return "§b⬉";
        }else{
            return "§b⬆";
        }
    }
}