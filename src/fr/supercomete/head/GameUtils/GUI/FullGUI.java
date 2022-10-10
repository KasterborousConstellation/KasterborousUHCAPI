package fr.supercomete.head.GameUtils.GUI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
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
public class FullGUI extends GUI{
	private static final CopyOnWriteArrayList<FullGUI> allGui = new CopyOnWriteArrayList<FullGUI>();
	private Inventory inv;
	private Player player;
	public FullGUI( Main main) {
		this.player=null;
	}
	public FullGUI(Player player) {
		this.player=null;
		this.player=player;
		if (player != null)
			allGui.add(this);
	}
	protected Inventory generateinv() {
		Inventory tmp = Bukkit.createInventory(null, 27,"FullInv");
		for(int i=0;i<9;i++) {
			tmp.setItem(i, InventoryUtils.getItem(Material.STAINED_GLASS_PANE, " ", null));
		}
		for(int i=0;i<9;i++) {
			tmp.setItem(26-i, InventoryUtils.getItem(Material.STAINED_GLASS_PANE, " ", null));
		}
		ArrayList<ItemStack> stack = Main.currentGame.getFullinv().get(player.getUniqueId());
		if(stack==null) {
			stack=new ArrayList<>();
		}
		int r=0;
		for(final ItemStack item : stack) {
			tmp.setItem(9+r, item);
			r++;
		}
		return tmp;
	}
	public void open() {
		this.inv = generateinv();
		player.openInventory(inv);
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		for (FullGUI gui:allGui) {
			if (e.getInventory().equals(gui.inv)) {
				e.setCancelled(true);
				final int currentSlot = e.getSlot();
				switch (currentSlot) {
				default:
					final ArrayList<ItemStack> stack = Main.currentGame.getFullinv().get(gui.player.getUniqueId());
					if(currentSlot>8 && currentSlot<9 +stack.size()) {
						InventoryUtils.addOrDrop(gui.player, stack.get(currentSlot-9));
						HashMap<UUID,ArrayList<ItemStack>>hash = Main.currentGame.getFullinv();
						stack.remove(currentSlot-9);
						hash.put(gui.player.getUniqueId(), stack);
						gui.player.closeInventory();
					}
					break;
				}
			}
		}
	}
	// Optimization --> Forget GUI that have been closed >|<
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		for (FullGUI gui : allGui) {
			if (e.getInventory().equals(gui.inv)) {
				allGui.remove(gui);
			}
		}
	}
}