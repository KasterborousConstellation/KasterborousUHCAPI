package fr.supercomete.head.Listeners;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurables;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.core.Main;
final class OnQuitListener implements Listener{

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (Main.currentGame.hasOfflinePlayer(player)) {
			Main.currentGame.getOfflinelist().remove(Main.currentGame.getOffline_Player(player));
		}
		Main.currentGame.getOfflinelist().add(new Offline_Player(player));
		if(!Main.currentGame.isGameState(Gstate.Waiting) && Main.playerlist.contains(player.getUniqueId()))
		    Bukkit.broadcastMessage("§7" + player.getName() + " s'est déconnecté, il a "+ TimeUtility.transform(Main.currentGame.getDataFrom(Configurable.LIST.DecTime), "§b", "§b", "§b")+ "§7 pour se réconnecter");
	}
}