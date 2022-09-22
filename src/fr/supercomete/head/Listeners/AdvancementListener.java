package fr.supercomete.head.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

public class AdvancementListener implements Listener {
    @EventHandler
    public void achievement(PlayerAchievementAwardedEvent e) {
        Player player = e.getPlayer();
        if (player.getWorld().getName().equalsIgnoreCase("yourworld")) {
            e.setCancelled(true);
        }
    }
}
