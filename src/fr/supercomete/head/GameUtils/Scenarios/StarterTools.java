package fr.supercomete.head.GameUtils.Scenarios;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.KasterborousRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.List;

public class StarterTools implements KasterborousScenario{
    ItemStack[] stacks = new ItemStack[3];
    @Override
    public boolean onEnable() {
        Inventory inventory = PlayerUtility.getInventory();
        if(inventory==null){
            inventory= Bukkit.createInventory(null,54);
        }
        if(stacks.length==0){
            final ItemStack pickaxe = new ItemStack(Material.IRON_PICKAXE);
            ItemMeta meta = pickaxe.getItemMeta();
            meta.addEnchant(Enchantment.DIG_SPEED,3,true);
            meta.addEnchant(Enchantment.DURABILITY,2,true);
            pickaxe.setItemMeta(meta);
            ItemStack axe = pickaxe.clone();
            axe.setType(Material.IRON_AXE);
            ItemStack shovel = axe.clone();
            shovel.setType(Material.IRON_SPADE);
            stacks[0]=axe.clone();
            stacks[1]=pickaxe.clone();
            stacks[2]=shovel.clone();
        }
        inventory.addItem(stacks[0].clone(),stacks[1].clone(),stacks[2].clone());
        PlayerUtility.saveStuff(inventory);
        return KasterborousScenario.super.onEnable();
    }

    @Override
    public boolean onDisable() {
        Inventory inventory = PlayerUtility.getInventory();
        inventory.remove(stacks[0]);
        inventory.remove(stacks[1]);
        inventory.remove(stacks[2]);
        return KasterborousScenario.super.onDisable();
    }

    @Override
    public String getName() {
        return "Starter-Tools";
    }

    @Override
    public Compatibility getCompatiblity() {
        return Compatibility.allModes;
    }

    @Override
    public Material getMat() {
        return Material.IRON_PICKAXE;
    }

    @Nullable
    @Override
    public List<KasterborousRunnable> getAttachedRunnable() {
        return null;
    }
}
