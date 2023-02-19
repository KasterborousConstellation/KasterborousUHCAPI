package fr.supercomete.head.Listeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

public class AdvancementListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void achievement(PlayerAchievementAwardedEvent e) {
        e.setCancelled(true);
    }
}