package fr.supercomete.head.role.Triggers;

import org.bukkit.entity.Player;

public interface Trigger_OnAnyKill {
	void onOtherKill(Player player, Player killer);
}
