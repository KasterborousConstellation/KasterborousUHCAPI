package fr.supercomete.head.Inventory.GUI;

import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.core.Main;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;

public class ScenarioRuleGUI extends KTBSInventory {
    public ScenarioRuleGUI(Player player) {
        super("§dScénarios Actif", 54, player);
    }

    @Override
    protected boolean denyDoubleClick() {
        return false;
    }

    @Override
    protected Inventory generateinventory(Inventory inv) {
        for(int i=0;i<9;i++){
            inv.setItem(i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 2));
        }
        for(int i=0;i<9;i++){
            inv.setItem(53-i,InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 2));
        }
        for(int i = 0; i< Main.currentGame.getScenarios().size(); i++){
            KasterborousScenario sc=Main.currentGame.getScenarios().get(i);
            ArrayList<String> Lines=new ArrayList<String>();
            String compatibility=(sc.getCompatiblity().IsCompatible(Main.currentGame.getMode()))?"§a✔":"§c✖";
            Lines.add(compatibility+"§r§7Compatible");
            ItemStack item = InventoryUtils.getItem(sc.getMat(),"§b"+sc.getName(),Lines);
            if(Main.currentGame.getScenarios().contains(sc)){
                item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
                ItemMeta ime=item.getItemMeta();
                ime.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(ime);
            }
            inv.setItem(i+9, item);
        }
        inv.setItem(49, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au règles")));
        return inv;
    }

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        if (slot == 49) {
            new RuleGUI(holder).open();
        }
        return true;
    }

    @Override
    protected boolean onClose(Player holder) {
        return false;
    }
}
