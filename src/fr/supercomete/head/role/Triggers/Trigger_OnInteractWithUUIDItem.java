package fr.supercomete.head.role.Triggers;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public interface Trigger_OnInteractWithUUIDItem {
    void OnInteractWithUUIDItem(final Player player, int uuidtag, PlayerInteractEvent e);
}
