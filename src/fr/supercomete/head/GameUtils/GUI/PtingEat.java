package fr.supercomete.head.GameUtils.GUI;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.content.DWUHC.Pting;
public class PtingEat extends GUI{
	private static final CopyOnWriteArrayList<PtingEat> allGui = new CopyOnWriteArrayList<PtingEat>();
	private Inventory inv;
	private final Pting role;
	private Player player;
	private Player openPlayer;
	public PtingEat(Pting role, Main main) {
		this.role=role;
		this.player=null;
	}
	public PtingEat(Pting role, Player player,Player openplayer) {
		this.role=role;
		this.player=null;
		this.player=player;
		this.openPlayer=openplayer;
		if (player != null)
			allGui.add(this);
	}
	protected Inventory generateinv() {
		Inventory tmp = Bukkit.createInventory(null, 54,role.getName()+" Pting-Manger");
		for(int e=0;e<9;e++) {
			tmp.setItem(e, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
		}
		for(int r=45;r<54;r++) {
			tmp.setItem(r, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
		}
		for(int i=0;i<openPlayer.getInventory().getSize();i++) {
			if(openPlayer.getInventory().getItem(i)!=null && openPlayer.getInventory().getItem(i).getType()!=Material.AIR)
			{	
				if(i<9)
					tmp.setItem(36+i, openPlayer.getInventory().getItem(i));else tmp.setItem(i, openPlayer.getInventory().getItem(i));
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
		for (PtingEat gui:allGui) {
			if (e.getInventory().equals(gui.inv)) {
				e.setCancelled(true);
				int currentSlot = e.getSlot();
				switch (currentSlot) {
				default:
					if(currentSlot>9 && currentSlot<45){
						int realinventorySlot = (currentSlot>=36)? currentSlot-36:currentSlot;
						if(gui.openPlayer.getInventory().getItem(realinventorySlot)!=null && gui.openPlayer.getInventory().getItem(realinventorySlot).getType()!=Material.AIR){
							if(gui.openPlayer.getInventory().getItem(realinventorySlot).getType().isBlock()) {
								gui.openPlayer.getInventory().setItem(realinventorySlot, null);
								gui.player.sendMessage(Main.UHCTypo+"§aVous avez gagner en mangeant 650pts.");
								gui.role.setNutrition(gui.role.getNutrition()+650);
								gui.player.getOpenInventory().close();
								gui.openPlayer.updateInventory();
							}else gui.player.sendMessage(Main.UHCTypo+"§cVous ne pouvez pas manger cet item");
						}
					}
					break;
				}
			}
		}
	}
	// Optimization --> Forget GUI that have been closed >|<
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		for (PtingEat gui : allGui) {
			if (e.getInventory().equals(gui.inv)) {
				allGui.remove(gui);
			}
		}
	}
}