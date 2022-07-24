package fr.supercomete.head.GameUtils.GUI;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.supercomete.head.GameUtils.Time.TimeUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.content.DWUHC.GreatIntelligence;

public class SnowmanGUI extends GUI {
	private static final CopyOnWriteArrayList<SnowmanGUI> allGui = new CopyOnWriteArrayList<SnowmanGUI>();
	private Inventory inv;
	private final GreatIntelligence role;
	private final Player player;
	
	public SnowmanGUI(GreatIntelligence role, Main main) {
		this.role=role;
		this.player=null;
	}
	public SnowmanGUI(GreatIntelligence role, Player player) {
		this.role=role;
		this.player=player;
		if (player != null) {
			allGui.add(this);
		}
			
	}
	@Override
	protected Inventory generateinv() {
		Inventory tmp = Bukkit.createInventory(null, 9,"§bTempete");
		int i =1;
		for(Entry<Location, Integer> entry  :role.getSnowman().entrySet()) {
			tmp.setItem(i-1, InventoryUtils.getItem(Material.SNOW_BALL, "§bBonhomme de neige n°"+i, List.of(((Main.currentGame.getTime() - entry.getValue() > 15 * 60)) ? "§aUtilisable" : "§bCooldown: " + TimeUtility.transform(((entry.getValue() + 15 * 60) - Main.currentGame.getTime()), "§c", "§c", "§c"))));
			i++;
		}
		return tmp;
	}

	@Override
	public void open() {
		this.inv = generateinv();
		player.openInventory(inv);
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		for (SnowmanGUI gui : allGui) {
			if (e.getInventory().equals(gui.inv)) {
				allGui.remove(gui);
			}
		}

	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		for (SnowmanGUI gui : allGui) {
			if (e.getInventory().equals(gui.inv)) {
				e.setCancelled(true);
				int slot = e.getSlot();
				if(slot>-1 && slot<gui.role.getSnowman().size()) {
                    gui.role.teleporttoSnowman(slot);
                }
			}
		}
	}
}