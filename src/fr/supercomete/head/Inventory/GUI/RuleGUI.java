package fr.supercomete.head.Inventory.GUI;

import fr.supercomete.head.GameUtils.Enchants.EnchantLimit;
import fr.supercomete.head.GameUtils.Enchants.EnchantType;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.core.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class RuleGUI extends KTBSInventory {
    public RuleGUI(Player player) {
        super("§dRules", 54, player);
    }

    @Override
    protected boolean denyDoubleClick() {
        return false;
    }
    private EnchantLimit getEnchanLimit(Enchantment enchantment,EnchantType type){
        for(EnchantLimit enchantLimit: Main.currentGame.getLimites()){
            if(enchantLimit.getEnchant().equals(enchantment)&&enchantLimit.getType().equals(type)){
                return enchantLimit;
            }
        }
        return null;
    }
    private String generateLine(Enchantment ench,EnchantType type){
        final EnchantLimit limit = getEnchanLimit(ench,type);
        if(limit==null)return "";
        return ChatColor.GRAY+limit.getEnchantname()+ChatColor.DARK_GRAY+" ("+limit.getType().getName()+"): §f"+limit.getMax();
    }
    @Override
    protected Inventory generateinventory(Inventory inv) {
        inv.setItem(0, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
        inv.setItem(8, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
        inv.setItem(53, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
        inv.setItem(45, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
        inv.setItem(9, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
        inv.setItem(1, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
        inv.setItem(7, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
        inv.setItem(17, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
        inv.setItem(46, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
        inv.setItem(36, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
        inv.setItem(52, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
        inv.setItem(44, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
        inv.setItem(47, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
        inv.setItem(2, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
        inv.setItem(6, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
        inv.setItem(51, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
        inv.setItem(22, InventoryUtils.getItem(Material.BOOKSHELF, "§bScénarios", null));
        inv.setItem(20, InventoryUtils.getItem(Material.PAPER, "§bRegles avancées", null));
        inv.setItem(24, InventoryUtils.getItem(Material.DIAMOND_CHESTPLATE, "§bEnchants", Arrays.asList(
                generateLine(Enchantment.DAMAGE_ALL, EnchantType.Iron),
                generateLine(Enchantment.DAMAGE_ALL, EnchantType.Diamond),
                generateLine(Enchantment.PROTECTION_ENVIRONMENTAL, EnchantType.Iron),
                generateLine(Enchantment.PROTECTION_ENVIRONMENTAL, EnchantType.Diamond),
                generateLine(Enchantment.KNOCKBACK, EnchantType.ALL),
                generateLine(Enchantment.ARROW_KNOCKBACK, EnchantType.Bow)
        )));
        return inv;
    }

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        switch (slot) {
            case 22:
                new ScenarioRuleGUI(holder).open();
                break;
            case 24:
                new EnchantLimitGUI(holder,false).open();
                break;
            case 20:
                new AdvancedRulesGUI(holder).open();
            default:
                break;
        }
        return true;
    }

    @Override
    protected boolean onClose(Player holder) {
        return false;
    }
}
