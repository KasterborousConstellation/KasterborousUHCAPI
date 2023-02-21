package fr.supercomete.head.GameUtils.GUI;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.supercomete.head.Inventory.GUI.SeeInvGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.head.GameUtils.Historic;
import fr.supercomete.head.GameUtils.HistoricData;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleHandler;
public class RoleGUI extends GUI{
	private static final CopyOnWriteArrayList<RoleGUI> allGui = new CopyOnWriteArrayList<RoleGUI>();
	private Inventory inv;
	private Player target;
	private Player player;
	public RoleGUI( Main main) {
		this.player=null;
	}
	public RoleGUI(Player player,Player target) {
		this.player=null;
		this.player=player;
		this.target=target;
		if (player != null)
			allGui.add(this);
	}
	protected Inventory generateinv() {
		Inventory tmp = Bukkit.createInventory(null, 27,"Spec");
		for(int i=0;i<9;i++) {
			tmp.setItem(i, InventoryUtils.getItem(Material.STAINED_GLASS_PANE, " ", null));
		}
		for(int i=0;i<9;i++) {
			tmp.setItem(26-i, InventoryUtils.getItem(Material.STAINED_GLASS_PANE, " ", null));
		}
		int diamond = (Main.diamondmap.get(target.getUniqueId())==null)?0:Main.diamondmap.get(target.getUniqueId());
		ItemStack it = InventoryUtils.getItem(Material.DIAMOND_ORE, "§bLimite de diamant: §r"+diamond, null);
		diamond = (diamond==0)?1:diamond;
		it.setAmount(diamond);
		tmp.setItem(11, it);
		tmp.setItem(12, InventoryUtils.getItem(Material.IRON_SWORD, "§eKills: "+Main.currentGame.getKillList().get(target.getUniqueId()), null));
		tmp.setItem(13, InventoryUtils.getItem(Material.BARRIER, "§cRafraichir", null));
		tmp.setItem(14, InventoryUtils.getItem(Material.CHEST, "§bVoir l'inventaire", null));
		if(Main.currentGame.getMode()instanceof CampMode) {
			final Historic historic = RoleHandler.getHistoric();
			final HistoricData data = historic.getEntry(target.getUniqueId());
			
			if(data.getCause()==null&&RoleHandler.IsRoleGenerated()) {
				tmp.setItem(22, InventoryUtils.getItem(Material.BEACON, "§r"+data.getPlayer().getUsername(),RoleHandler.getRoleDescription(data)));
			}else {
				tmp.setItem(22, InventoryUtils.getItem(Material.BEACON, "§cAucun", null));
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
		for (RoleGUI gui:allGui) {
			if (e.getInventory().equals(gui.inv)) {
				e.setCancelled(true);
				final int currentSlot = e.getSlot();
//				final ClickType action = e.getClick();
				if(!gui.target.isOnline()) {
					gui.player.closeInventory();
					gui.player.sendMessage(Main.UHCTypo+"§cLe joueur n'est plus connecté");
					return;
				}
				switch (currentSlot) {
				case 13:
					gui.open();
					break;
				case 14:
					new SeeInvGUI(gui.player, gui.target).open();
					break;
				default:
					
					break;
				}
			}
		}
	}
	// Optimization --> Forget GUI that have been closed >|<
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		for (RoleGUI gui : allGui) {
			if (e.getInventory().equals(gui.inv)) {
				allGui.remove(gui);
			}
		}
	}
}