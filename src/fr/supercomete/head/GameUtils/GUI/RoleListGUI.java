package fr.supercomete.head.GameUtils.GUI;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.HistoricData;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleHandler;
public class RoleListGUI extends GUI{
	private static final CopyOnWriteArrayList<RoleListGUI> allGui = new CopyOnWriteArrayList<RoleListGUI>();
	private Inventory inv;
	private Player player;
	private String openType;
	public RoleListGUI(Main main) {
		this.player=null;
	}
	public RoleListGUI(Player player,String openType) {
		this.player=null;
		this.player=player;
		this.openType=openType;
		if (player != null)
			allGui.add(this);
	}
	
	protected Inventory generateinv() {
		Inventory tmp = Bukkit.createInventory(null, 54,"Roles "+openType);
		for(int i = 0 ; i < 9 ; i++) {
			tmp.setItem(i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)0));
		}
		for(int i = 0 ; i < 9 ; i++) {
			tmp.setItem(53-i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)0));
		}
		CampMode campmode = (CampMode) Main.currentGame.getMode();
		int i=0;
		for (Camps camp : campmode.getPrimitiveCamps()) {
			tmp.setItem(i, InventoryUtils.createColorItem(Material.WOOL, "§r" + camp.getColor() + camp.getName(), 1,TeamManager.getShortOfChatColor(camp.getColor())));
			i++;
		}
		int e = 0;
		for(Entry<UUID,HistoricData> entry : RoleHandler.getHistoric().getRoleList().entrySet()) {
			if(entry.getValue().getRole().getDefaultCamp().getName()==openType) {
				final ItemStack stack = new ItemStack(Material.SKULL_ITEM);
				stack.setDurability((short)3);
				final SkullMeta meta = (SkullMeta) stack.getItemMeta();
				meta.setDisplayName(ChatColor.WHITE+entry.getValue().getPlayer().getUsername());
				meta.setOwner(entry.getValue().getPlayer().getUsername());
				
				meta.setLore(RoleHandler.getRoleDescription(entry.getValue()));
				
				
				stack.setItemMeta(meta);
				tmp.setItem(9+e, stack);
				e++;
			}
		}
		return tmp;
	}
	public void open() {
		this.inv = generateinv();
		player.openInventory(inv);
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		for (RoleListGUI gui:allGui) {
			if (e.getInventory().equals(gui.inv)) {
				e.setCancelled(true);
				int slot = e.getSlot();
				CampMode mode =(CampMode) Main.currentGame.getMode();
				if(slot<mode.getPrimitiveCamps().length) {
					gui.openType= mode.getPrimitiveCamps()[slot].getName();
					gui.open();
				}
			}
		}
	}
	// Optimization --> Forget GUI that have been closed >|<
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		for (RoleListGUI gui : allGui) {
			if (e.getInventory().equals(gui.inv)) {
				allGui.remove(gui);
			}
		}
	}
}
