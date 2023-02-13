package fr.supercomete.head.GameUtils.GameMode.ModeHandler;

import fr.supercomete.head.core.Main;

import fr.supercomete.tasks.NoDamage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class MapHandler {
    public static void reset(){
        map=new Map();
    }
    public static void setMapToNull(){
        map=null;
    }
    private static Map map = null;
    public static @Nullable Map getMap(){
        return map;
    }
    public static class Map{
        Random random = new Random();
        private World currentWorld;
        private World structureWorld;
        public Map(){
            currentWorld=null;
            structureWorld=null;
        }
        public void setCurrentWorld(World world){
            currentWorld=world;
        }
        public void setStructureWorld(World world){
            structureWorld=world;
        }
        public World getPlayWorld(){
            return currentWorld;
        }
        public World getStructureWorld() {
            return structureWorld;
        }
        public int getRandomCoordinateInPlayWorld(){
            return random.nextInt((int)(Main.currentGame.getCurrentBorder()/2-(Main.currentGame.getCurrentBorder())/4));
        }
        public void PlayerRandomTPMap(Player player) {
            player.teleport(new Location(currentWorld,getRandomCoordinateInPlayWorld(),150,getRandomCoordinateInPlayWorld()));
        }
        public void PlayerRandomTPMap(Player player,int nodamageTime) {
            ArrayList<UUID> uu = new ArrayList<UUID>();
            uu.add(player.getUniqueId());
            NoDamage nodamage = new NoDamage(nodamageTime,uu);
            nodamage.runTaskTimer(Main.INSTANCE, 0, 20L);
            player.teleport(new Location(currentWorld,getRandomCoordinateInPlayWorld(),150,getRandomCoordinateInPlayWorld()));
        }

    }
}
