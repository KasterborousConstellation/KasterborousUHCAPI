package fr.supercomete.head.GameUtils.GUI;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.nbthandler.NbtTagHandler;
public class SeeInvGUI extends GUI{
	private static final CopyOnWriteArrayList<SeeInvGUI> allGui = new CopyOnWriteArrayList<SeeInvGUI>();
	private Inventory inv;
	private Player player;
	private Player openPlayer;
	private boolean hide=false;
	public SeeInvGUI(Main main) {
		this.player=null;
	}
	public SeeInvGUI(Player player,Player openplayer,boolean hide) {
		this.player=null;
		this.hide=hide;
		this.player=player;
		this.openPlayer=openplayer;
		if (player != null)
			allGui.add(this);
	}
	protected Inventory generateinv() {
		Inventory tmp = Bukkit.createInventory(null, 54,"Spec-Inventaire");
		for(int e=0;e<9;e++) {
			tmp.setItem(e, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)3));
		}
		for(int r=45;r<54;r++) {
			tmp.setItem(r, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)3));
		}
		for(int i=0;i<openPlayer.getInventory().getSize();i++) {
			ItemStack it = openPlayer.getInventory().getItem(i);
			if(openPlayer.getInventory().getItem(i)!=null && openPlayer.getInventory().getItem(i).getType()!=Material.AIR)
			{	
				if(hide&&NbtTagHandler.hasUUIDTAG(openPlayer.getInventory().getItem(i)))
				{
					if(NbtTagHandler.getUUIDTAG(openPlayer.getInventory().getItem(i))==8)
					{
						it = InventoryUtils.getItem(Material.COBBLESTONE, null, null);
						it.setAmount((new Random().nextInt(63))+1);
						if(i<9)
							tmp.setItem(36+i, it);else tmp.setItem(i, it);
					}
				}
				if(i<9)
					tmp.setItem(36+i, it);else tmp.setItem(i, it);
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
		for (SeeInvGUI gui:allGui) {
			if (e.getInventory().equals(gui.inv)) {
				e.setCancelled(true);
			}
		}
	}
	// Optimization --> Forget GUI that have been closed >|<
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		for (SeeInvGUI gui : allGui) {
			if (e.getInventory().equals(gui.inv)) {
				allGui.remove(gui);
			}
		}
	}
}