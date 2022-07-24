package fr.supercomete.head.GameUtils.Events.PlayerEvents;

import fr.supercomete.head.GameUtils.Time.BoundedWatchTime;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerEventHandler{
    private static ArrayList<PlayerEvent>events = new ArrayList<>();
    public static void Event(PlayerEvents event, Player player, Location location){
        events.add(new PlayerEvent(event.getName(),new Offline_Player(player),location));
    }
    public static void Event(String string,Player player, Location location){
        events.add(new PlayerEvent(string,new Offline_Player(player),location));
    }
    public static void resetEvents(){
        events=new ArrayList<>();
    }


}
