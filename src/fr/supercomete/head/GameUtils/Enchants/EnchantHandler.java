package fr.supercomete.head.GameUtils.Enchants;

import fr.supercomete.head.core.Main;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;

public class EnchantHandler {

    public static void init(){
        ArrayList<EnchantLimit>limites= new ArrayList<>();
        limites.add(new EnchantLimit("Sharpness",Enchantment.DAMAGE_ALL,EnchantType.Iron,4));
        limites.add(new EnchantLimit("Sharpness",Enchantment.DAMAGE_ALL,EnchantType.Diamond,3));
        limites.add(new EnchantLimit("Protection",Enchantment.PROTECTION_ENVIRONMENTAL,EnchantType.Diamond,2));
        limites.add(new EnchantLimit("Protection",Enchantment.PROTECTION_ENVIRONMENTAL,EnchantType.Iron,3));
        limites.add(new EnchantLimit("Thorns",Enchantment.THORNS,EnchantType.Iron,0));
        limites.add(new EnchantLimit("Thorns",Enchantment.THORNS,EnchantType.Diamond,0));
        limites.add(new EnchantLimit("Smite",Enchantment.DAMAGE_UNDEAD,EnchantType.Iron,4));
        limites.add(new EnchantLimit("Smite",Enchantment.DAMAGE_UNDEAD,EnchantType.Diamond,4));
        limites.add(new EnchantLimit("Bane of Arthropods",Enchantment.DAMAGE_ARTHROPODS,EnchantType.Iron,4));
        limites.add(new EnchantLimit("Bane of Arthropods",Enchantment.DAMAGE_ARTHROPODS,EnchantType.Diamond,4));
        limites.add(new EnchantLimit("Fire Protection",Enchantment.PROTECTION_FIRE,EnchantType.Diamond,4));
        limites.add(new EnchantLimit("Fire Protection",Enchantment.PROTECTION_FIRE,EnchantType.Iron,4));
        limites.add(new EnchantLimit("Blast Protection",Enchantment.PROTECTION_EXPLOSIONS,EnchantType.Diamond,4));
        limites.add(new EnchantLimit("Blast Protection",Enchantment.PROTECTION_EXPLOSIONS,EnchantType.Iron,4));
        limites.add(new EnchantLimit("Projectile Protection",Enchantment.PROTECTION_PROJECTILE,EnchantType.Iron,4));
        limites.add(new EnchantLimit("Projectile Protection",Enchantment.PROTECTION_PROJECTILE,EnchantType.Diamond,4));


        limites.add(new EnchantLimit("Power",Enchantment.ARROW_DAMAGE,EnchantType.Bow,3));
        limites.add(new EnchantLimit("Punch",Enchantment.ARROW_KNOCKBACK,EnchantType.Bow,0));
        limites.add(new EnchantLimit("Flame",Enchantment.ARROW_FIRE,EnchantType.Bow,0));
        limites.add(new EnchantLimit("Infinity",Enchantment.ARROW_INFINITE,EnchantType.Bow,0));

        limites.add(new EnchantLimit("Luck of the sea",Enchantment.LUCK,EnchantType.Rod,3));
        limites.add(new EnchantLimit("Lure",Enchantment.LURE,EnchantType.Rod,3));

        limites.add(new EnchantLimit("Efficiency",Enchantment.DIG_SPEED,EnchantType.ALL,5));
        limites.add(new EnchantLimit("Unbreaking",Enchantment.DURABILITY,EnchantType.ALL,3));
        limites.add(new EnchantLimit("Fortune",Enchantment.LOOT_BONUS_BLOCKS,EnchantType.ALL,2));
        limites.add(new EnchantLimit("Looting",Enchantment.LOOT_BONUS_MOBS,EnchantType.ALL,2));
        limites.add(new EnchantLimit("Knockback",Enchantment.KNOCKBACK,EnchantType.ALL,1));
        limites.add(new EnchantLimit("Fire Aspect",Enchantment.FIRE_ASPECT,EnchantType.ALL,0));
        limites.add(new EnchantLimit("Feather Falling",Enchantment.PROTECTION_FALL,EnchantType.ALL,4));
        limites.add(new EnchantLimit("Depth Strider",Enchantment.DEPTH_STRIDER,EnchantType.ALL,0));
        limites.add(new EnchantLimit("Respiration",Enchantment.OXYGEN,EnchantType.ALL,3));
        limites.add(new EnchantLimit("Aqua Affinity",Enchantment.WATER_WORKER,EnchantType.ALL,1));
        Main.currentGame.setLimites(limites);
    }
    public static int getIndexOf(EnchantLimit enchantLimit){
        int i =0;
        for(EnchantLimit enchantLimit1:Main.currentGame.getLimites()){
            if(enchantLimit.equals(enchantLimit1)){
                return i;
            }
            i++;
        }
        return -1;
    }
    private static EnchantLimit getEnchanLimit(Enchantment enchantment,EnchantType type){
        for(EnchantLimit enchantLimit:Main.currentGame.getLimites()){
            if(enchantLimit.getEnchant().equals(enchantment)&&enchantLimit.getType().equals(type)){
                return enchantLimit;
            }
        }
        return null;
    }
    public static String generateLine(Enchantment ench,EnchantType type){
        final EnchantLimit limit = getEnchanLimit(ench,type);
        if(limit==null)return "";
        return ChatColor.GRAY+limit.getEnchantname()+ChatColor.DARK_GRAY+" ("+limit.getType().getName()+"): §f"+limit.getMax();
    }
    public static ArrayList<EnchantLimit> getLimite(EnchantType enchantType) {
        ArrayList<EnchantLimit>types = new ArrayList<>();
        for(EnchantLimit enchantLimit:Main.currentGame.getLimites()){
            if(enchantLimit.getType().equals(enchantType)){
                types.add(enchantLimit);
            }
        }
        return types;
    }
}
