package fr.supercomete.head.role.Triggers;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public interface Trigger_OnTakingHit {
    void TakingDamage(Player player, EntityDamageEvent e);
}
