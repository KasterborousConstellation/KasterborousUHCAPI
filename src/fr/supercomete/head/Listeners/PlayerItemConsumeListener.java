package fr.supercomete.head.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

final class PlayerItemConsumeListener implements Listener {

	@EventHandler
	public void OnConsume(PlayerItemConsumeEvent e) {
		ItemStack item = e.getItem();
		Player player = e.getPlayer();
		if (item != null) {
			if (item.getType() == Material.GOLDEN_APPLE) {
				if (item.getItemMeta().hasLore()) {
					e.setCancelled(true);
					item.setAmount(item.getAmount() - 1);
					e.getPlayer().setItemInHand(item);
					player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2 * 60, 0));
					player.setFoodLevel(player.getFoodLevel() + 4);
				}
			}
		}
	}

}
