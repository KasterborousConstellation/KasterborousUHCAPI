package fr.supercomete.head.Inventory.GUI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
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
public class FullGUI extends KTBSInventory {
	public FullGUI(Player player) {
        super("FullInv",27,player);
	}
	protected Inventory generateinventory(Inventory tmp) {
		for(int i=0;i<9;i++) {
			tmp.setItem(i, InventoryUtils.getItem(Material.STAINED_GLASS_PANE, " ", null));
		}
		for(int i=0;i<9;i++) {
			tmp.setItem(26-i, InventoryUtils.getItem(Material.STAINED_GLASS_PANE, " ", null));
		}
		ArrayList<ItemStack> stack = Main.currentGame.getFullinv().get(getHolder().getUniqueId());
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

    @Override
    protected boolean onClick(Player player, int currentSlot, KTBSAction action) {
        final ArrayList<ItemStack> stack = Main.currentGame.getFullinv().get(player.getUniqueId());
        if (currentSlot > 8 && currentSlot < 9 + stack.size()) {
            InventoryUtils.addOrDrop(player, stack.get(currentSlot - 9));
            HashMap<UUID, ArrayList<ItemStack>> hash = Main.currentGame.getFullinv();
            stack.remove(currentSlot - 9);
            hash.put(player.getUniqueId(), stack);
            player.closeInventory();
        }
        return true;
    }

    @Override
    protected boolean onClose(Player holder) {
        return false;
    }

    @Override
    protected boolean denyDoubleClick() {
        return true;
    }
}