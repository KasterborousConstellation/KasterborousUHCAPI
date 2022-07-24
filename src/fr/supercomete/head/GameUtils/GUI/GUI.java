package fr.supercomete.head.GameUtils.GUI;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public abstract class GUI implements Listener{
	protected abstract Inventory generateinv();
	public abstract void open();
	@EventHandler
	public abstract void onInventoryClose(InventoryCloseEvent e);
	@EventHandler
	public abstract void onInventoryClick(InventoryClickEvent e);
}
