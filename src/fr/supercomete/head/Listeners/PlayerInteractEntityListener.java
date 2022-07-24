package fr.supercomete.head.Listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.GUI.RoleGUI;
import fr.supercomete.head.core.Main;

final class PlayerInteractEntityListener implements Listener {

	@EventHandler
	public void PlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		final Player player = event.getPlayer();
		final Entity entity = event.getRightClicked();
		if (event.getRightClicked() instanceof Horse && Main.currentGame.getScenarios().contains(Scenarios.NoHorse)) {
			event.setCancelled(true);
			player.sendMessage(Main.UHCTypo + "§cLe scénario " + Scenarios.NoHorse.getName() + " est activé");
			return;
		}
		
		if(entity instanceof Player) {
			final Player target = (Player)entity;
			if(player.getGameMode()==GameMode.SPECTATOR &&(Main.IsHost(player)||Main.IsCohost(player))) {
				new RoleGUI(player, target).open();
			}		
		}
	}
}