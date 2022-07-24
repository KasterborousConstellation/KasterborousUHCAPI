package fr.supercomete.head.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import fr.supercomete.nbthandler.NbtTagHandler;
final class DropListener implements Listener{

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(NbtTagHandler.hasUUIDTAG(e.getItemDrop().getItemStack())) {
			e.setCancelled(true);
		}
	}

}
