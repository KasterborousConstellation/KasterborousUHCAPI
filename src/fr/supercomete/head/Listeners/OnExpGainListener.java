package fr.supercomete.head.Listeners;

import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import fr.supercomete.head.core.Main;

final class OnExpGainListener implements Listener {
	@EventHandler
	public void onExpGain(PlayerExpChangeEvent e) {
		e.setAmount(e.getAmount()*(Main.currentGame.getDataFrom(Configurable.LIST.ExpBoost)/100));
	}
}
