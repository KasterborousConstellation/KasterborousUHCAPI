package fr.supercomete.head.role.Triggers;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public interface Trigger_OnHitPlayer {
	boolean OnHitPlayer(Player player,Player hitted, double amount, EntityDamageEvent.DamageCause cause);
}
