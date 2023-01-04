package fr.supercomete.head.GameUtils.Scenarios;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.KasterborousRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StarterTools implements KasterborousScenario{
    @Override
    public boolean onEnable() {
        Inventory inventory = PlayerUtility.getInventory();
        if(inventory==null){
            inventory= Bukkit.createInventory(null,54);
        }
        final ItemStack pickaxe = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta = pickaxe.getItemMeta();
        meta.addEnchant(Enchantment.DIG_SPEED,3,true);
        meta.addEnchant(Enchantment.DURABILITY,2,true);
        pickaxe.setItemMeta(meta);
        ItemStack axe = pickaxe.clone();
        axe.setType(Material.IRON_AXE);
        ItemStack shovel = axe.clone();
        shovel.setType(Material.IRON_SPADE);
        inventory.addItem(pickaxe,shovel,axe);
        PlayerUtility.saveStuff(inventory);
        return KasterborousScenario.super.onEnable();
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
