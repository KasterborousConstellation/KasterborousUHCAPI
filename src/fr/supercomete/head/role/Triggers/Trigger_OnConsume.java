package fr.supercomete.head.role.Triggers;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public interface Trigger_OnConsume {
    void onConsume(Player player, PlayerItemConsumeEvent e);
}