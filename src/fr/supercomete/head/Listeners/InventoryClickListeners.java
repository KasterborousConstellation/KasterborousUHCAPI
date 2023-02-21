package fr.supercomete.head.Listeners;

import java.util.Map;

import fr.supercomete.head.GameUtils.Enchants.EnchantHandler;
import fr.supercomete.head.GameUtils.Enchants.EnchantLimit;
import fr.supercomete.head.GameUtils.Enchants.EnchantType;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import fr.supercomete.head.core.Main;

final class InventoryClickListeners implements Listener{
	private final Main main;
	public InventoryClickListeners(Main main) {
		this.main=main;
	}
    private enum ObjectType{
        Null,Iron,Diams,Bow,Rod;
    }
    private boolean ContainEnchant(Enchantment enchant,Map<Enchantment,Integer>map){
        return map.containsKey(enchant);
    }
    private int getEnchantmentLevel(Enchantment enchantment,Map<Enchantment,Integer>map){
        return map.get(enchantment);
    }
	@SuppressWarnings("deprecation")
	@EventHandler
	public void PrepareAnvilEvent(InventoryClickEvent event) {
	    final Player player = (Player)event.getWhoClicked();
		if (event.getClickedInventory()!=null &&event.getClickedInventory().getType() == InventoryType.ANVIL && event.getWhoClicked() instanceof Player) {
			if (event.getClickedInventory() instanceof AnvilInventory) {
                AnvilInventory inv = (AnvilInventory) event.getClickedInventory();
                ItemStack result = inv.getItem(2);
                Map<Enchantment, Integer> map = result.getEnchantments();
                if (result != null && Main.currentGame.IsDisabledAnvil&&event.getSlot()==2) {
                    ObjectType type = ObjectType.Null;
                    if (result.getType().equals(Material.IRON_SWORD) || result.getType().equals(Material.IRON_HELMET)
                            || result.getType().equals(Material.IRON_CHESTPLATE)
                            || result.getType().equals(Material.IRON_LEGGINGS)
                            || result.getType().equals(Material.IRON_BOOTS)) {
                        type = ObjectType.Iron;
                    } else if (result.getType().equals(Material.DIAMOND_SWORD)
                            || result.getType().equals(Material.DIAMOND_HELMET)
                            || result.getType().equals(Material.DIAMOND_CHESTPLATE)
                            || result.getType().equals(Material.DIAMOND_LEGGINGS)
                            || result.getType().equals(Material.DIAMOND_BOOTS)) {
                        type = ObjectType.Diams;
                    } else {
                        if (result.getType() == Material.BOW) {
                            type = ObjectType.Bow;
                        } else if (result.getType() == Material.FISHING_ROD) {
                            type = ObjectType.Rod;
                        }
                    }
                    boolean hasIllegalEnchant = false;
                    if (type == ObjectType.Bow) {
                        for (final EnchantLimit limit : EnchantHandler.getLimite(EnchantType.Bow)) {
                            if (ContainEnchant(limit.getEnchant(), map)) {
                                int lvl = getEnchantmentLevel(limit.getEnchant(), map);

                                if (lvl > limit.getMax()) {
                                    hasIllegalEnchant = true;
                                }
                            }
                        }
                    } else if (type == ObjectType.Iron) {
                        for (final EnchantLimit limit : EnchantHandler.getLimite(EnchantType.Iron)) {
                            if (ContainEnchant(limit.getEnchant(), map)) {
                                int lvl = getEnchantmentLevel(limit.getEnchant(), map);
                                if (lvl > limit.getMax()) {
                                    hasIllegalEnchant = true;
                                }
                            }
                        }
                    } else if (type == ObjectType.Diams) {
                        for (final EnchantLimit limit : EnchantHandler.getLimite(EnchantType.Diamond)) {
                            if (ContainEnchant(limit.getEnchant(), map)) {
                                int lvl = getEnchantmentLevel(limit.getEnchant(), map);
                                if (lvl > limit.getMax()) {
                                    hasIllegalEnchant = true;
                                }
                            }
                        }
                    } else if (type == ObjectType.Rod) {
                        for (final EnchantLimit limit : EnchantHandler.getLimite(EnchantType.Rod)) {
                            if (ContainEnchant(limit.getEnchant(), map)) {
                                int lvl = getEnchantmentLevel(limit.getEnchant(), map);
                                if (lvl > limit.getMax()) {
                                    hasIllegalEnchant = true;
                                }
                            }
                        }
                    }
                    if (type == ObjectType.Iron || type == ObjectType.Diams) {
                        for (final EnchantLimit limit : EnchantHandler.getLimite(EnchantType.ALL)) {
                            if (ContainEnchant(limit.getEnchant(), map)) {
                                int lvl = getEnchantmentLevel(limit.getEnchant(), map);
                                if (lvl > limit.getMax()) {
                                    hasIllegalEnchant = true;
                                }
                            }
                        }
                    }
                    if (hasIllegalEnchant) {
                        event.setCancelled(true);
                        player.sendMessage(Main.UHCTypo + "§cImpossible: Cet object contient des enchantements illégaux. -> /rules");
                    }
                }
            }
		}
	}

	@EventHandler
	public void OnCraft(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;
		if (event.getClickedInventory().getType() == InventoryType.WORKBENCH && event.getWhoClicked() instanceof Player) {
			if (event.getClickedInventory() instanceof CraftingInventory) {
//				final Player player = (Player) event.getWhoClicked();
				CraftingInventory inv = (CraftingInventory) event.getClickedInventory();
				ItemStack result = inv.getItem(0);
				if(result!=null&&result.getType().equals(Material.DIAMOND_LEGGINGS) && Configurable.ExtractBool(Configurable.LIST.DiamondLeggings)){
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage(Main.UHCTypo+"§cCet item est désactivé.");
                }

			}
		}
	}
}
