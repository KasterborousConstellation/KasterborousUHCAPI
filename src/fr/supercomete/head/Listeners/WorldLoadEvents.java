package fr.supercomete.head.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoadEvents implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorld(WorldLoadEvent e){

    }
}
