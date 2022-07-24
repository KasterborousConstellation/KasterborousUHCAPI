package fr.supercomete.head.role.Triggers;

import org.bukkit.entity.Player;

public interface Trigger_OnOwnerDeath {
	boolean onOwnerDeath(Player player, Player damager);
}
