package fr.supercomete.head.Listeners;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;

final class CraftItemListener implements Listener {

	@EventHandler
	public void craft(CraftItemEvent e) {
		if (!Main.currentGame.getScenarios().contains(Scenarios.HasteyBoys))
			return;
		ItemStack item = e.getCurrentItem();
		if (InventoryUtils.AllHastyItem().contains(item.getType())) {
			ItemStack it = new ItemStack(item.getType(), 1);
			it.addEnchantment(Enchantment.DIG_SPEED, 3);
			it.addEnchantment(Enchantment.DURABILITY, 3);
			e.getInventory().setResult(it);
        }
	}

}
