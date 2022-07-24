package fr.supercomete.head.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import fr.supercomete.head.core.Main;

final class LoginListener implements Listener{

	
	@EventHandler
	public boolean onLogin(PlayerLoginEvent e) {
		if (Main.currentGame.getMaxNumberOfplayer() != 0&& Main.countnumberofplayer() >= Main.currentGame.getMaxNumberOfplayer()) {
			e.disallow(Result.KICK_FULL, "Game Full");
			return false;
		}
		return true;
	}

}
