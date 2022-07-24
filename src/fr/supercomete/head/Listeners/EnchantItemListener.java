package fr.supercomete.head.Listeners;

import java.util.Map;

import fr.supercomete.head.GameUtils.Enchants.EnchantHandler;
import fr.supercomete.head.GameUtils.Enchants.EnchantLimit;
import fr.supercomete.head.GameUtils.Enchants.EnchantType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.head.core.Main;

final class EnchantItemListener implements Listener {

    private enum ObjectType{
        Null,Iron,Diams,Bow,Rod;
    }
    private boolean ContainEnchant(Enchantment enchant,Map<Enchantment,Integer>map){
        return map.containsKey(enchant);
    }
    private int getEnchantmentLevel(Enchantment enchantment,Map<Enchantment,Integer>map){
        return map.get(enchantment);
    }
	@EventHandler
	public void OnEnchant(EnchantItemEvent event) {
		Player player = event.getEnchanter();
		EnchantingInventory inv = (EnchantingInventory) event.getInventory();
		ItemStack result = inv.getItem();
        Map<Enchantment,Integer> map= event.getEnchantsToAdd();
		if (result != null&&Main.currentGame.IsDisabledEnchant) {
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
				if(result.getType()==Material.BOW){
				    type=ObjectType.Bow;
                }else if(result.getType()==Material.FISHING_ROD){
				    type=ObjectType.Rod;
                }
			}
            boolean hasIllegalEnchant =false;
            if(type==ObjectType.Bow){
                for(final EnchantLimit limit: EnchantHandler.getLimite(EnchantType.Bow)){
                    if(ContainEnchant(limit.getEnchant(),map)){
                        int lvl = getEnchantmentLevel(limit.getEnchant(),map);

                        if(lvl>limit.getMax()){
                            hasIllegalEnchant=true;
                        }
                    }
                }
            }else if(type==ObjectType.Iron){
                for(final EnchantLimit limit: EnchantHandler.getLimite(EnchantType.Iron)){
                    if(ContainEnchant(limit.getEnchant(),map)){
                        int lvl = getEnchantmentLevel(limit.getEnchant(),map);
                        if(lvl>limit.getMax()){
                            hasIllegalEnchant=true;
                        }
                    }
                }
            }else if(type==ObjectType.Diams){
                for(final EnchantLimit limit: EnchantHandler.getLimite(EnchantType.Diamond)){
                    if(ContainEnchant(limit.getEnchant(),map)){
                        int lvl = getEnchantmentLevel(limit.getEnchant(),map);
                        if(lvl>limit.getMax()){
                            hasIllegalEnchant=true;
                        }
                    }
                }
            }else if(type==ObjectType.Rod){
                for(final EnchantLimit limit: EnchantHandler.getLimite(EnchantType.Rod)){
                    if(ContainEnchant(limit.getEnchant(),map)){
                        int lvl = getEnchantmentLevel(limit.getEnchant(),map);
                        if(lvl>limit.getMax()){
                            hasIllegalEnchant=true;
                        }
                    }
                }
            }
            if(type==ObjectType.Iron||type==ObjectType.Diams){
                for(final EnchantLimit limit: EnchantHandler.getLimite(EnchantType.ALL)){
                    if(ContainEnchant(limit.getEnchant(),map)){
                        int lvl = getEnchantmentLevel(limit.getEnchant(),map);
                        if(lvl>limit.getMax()){
                            hasIllegalEnchant=true;
                        }
                    }
                }
            }
            if(hasIllegalEnchant){
                event.setCancelled(true);
                player.sendMessage(Main.UHCTypo+"§cImpossible: Cet object contient des enchantements illégaux. -> /rules");
            }
		}
	}

}
