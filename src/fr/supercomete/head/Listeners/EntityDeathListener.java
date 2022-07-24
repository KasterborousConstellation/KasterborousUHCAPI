package fr.supercomete.head.Listeners;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;

final class EntityDeathListener implements Listener{
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		Entity entity = e.getEntity();
		if (entity.getType() == EntityType.ZOMBIE && Main.currentGame.getScenarios().contains(Scenarios.BetaZombie)) {
			ItemStack feather = InventoryUtils.getItem(Material.FEATHER, null, null);
			feather.setAmount(new Random().nextInt(3) + 1);
			entity.getWorld().dropItemNaturally(entity.getLocation(), feather);
		}
		if (entity.getType() == EntityType.COW && Main.currentGame.getScenarios().contains(Scenarios.CutClean)) {
			List<ItemStack> drops = e.getDrops();
			for (ItemStack drop : drops) {
				if (drop.getType() == Material.RAW_BEEF) {
					drop.setType(Material.COOKED_BEEF);
				}
			}
		}
		if (entity.getType() == EntityType.SHEEP && Main.currentGame.getScenarios().contains(Scenarios.CutClean)) {
			List<ItemStack> drops = e.getDrops();
			for (ItemStack drop : drops) {
				if (drop.getType() == Material.MUTTON) {
					drop.setType(Material.COOKED_MUTTON);
				}
			}
		}
		if (entity.getType() == EntityType.PIG && Main.currentGame.getScenarios().contains(Scenarios.CutClean)) {
			List<ItemStack> drops = e.getDrops();
			for (ItemStack drop : drops) {
				if (drop.getType() == Material.PORK) {
					drop.setType(Material.GRILLED_PORK);
				}
			}

		}
		if (entity.getType() == EntityType.RABBIT && Main.currentGame.getScenarios().contains(Scenarios.CutClean)) {
			List<ItemStack> drops = e.getDrops();
			for (ItemStack drop : drops) {
				if (drop.getType() == Material.RABBIT) {
					drop.setType(Material.COOKED_RABBIT);
				}
			}
		}
	}
}
