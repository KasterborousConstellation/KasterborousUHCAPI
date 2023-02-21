package fr.supercomete.head.Inventory.GUI;

import fr.supercomete.head.GameUtils.GUI.ModeGUI;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.core.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Collections;

public class BorderGUI extends KTBSInventory {
    public BorderGUI(Player player) {
        super("§dBordure",27 , player);
    }

    @Override
    protected boolean denyDoubleClick() {
        return true;
    }

    @Override
    protected Inventory generateinventory(Inventory inv) {
        inv.setItem(0,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
        inv.setItem(8,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
        inv.setItem(26, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
        inv.setItem(18, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
        inv.setItem(12, InventoryUtils.getItem(Material.BARRIER, ChatColor.BOLD+"§bBordure Initale§r: §a"+ Main.currentGame.getFirstBorder(), Arrays.asList("",InventoryUtils.ClickTypoAdd+"10",InventoryUtils.ClickTypoMassAdd+"100",InventoryUtils.ClickTypoRemove+"10",InventoryUtils.ClickTypoMassRemove+"100")));
        inv.setItem(13, InventoryUtils.getItem(Material.FEATHER, ChatColor.BOLD+"§bVitesse de la bordure§r: §a"+Math.round(Main.currentGame.getBorderSpeed())+"bps", Arrays.asList("",InventoryUtils.ClickTypoAdd+"0.1",InventoryUtils.ClickTypoMassAdd+"0.5",InventoryUtils.ClickTypoRemove+"0.1",InventoryUtils.ClickTypoMassRemove+"0.5")));
        inv.setItem(14, InventoryUtils.getItem(Material.IRON_FENCE, ChatColor.BOLD+"§bBordure Finale§r: §a"+Main.currentGame.getFinalBorder(), Arrays.asList("",InventoryUtils.ClickTypoAdd+"10",InventoryUtils.ClickTypoMassAdd+"100",InventoryUtils.ClickTypoRemove+"10",InventoryUtils.ClickTypoMassRemove+"100")));
        inv.setItem(22, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
        return inv;
    }

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        switch (slot) {
            case 22:
                new ModeGUI(Main.currentGame.getMode(), holder).open();
                break;
            case 12:
                if (action.ShiftedClicked() && action.IsRightClick())
                    Main.currentGame.setCurrentBorder(Main.currentGame.getCurrentBorder() + 100);
                if (action.IsRightClick() && !action.ShiftedClicked())
                    Main.currentGame.setCurrentBorder(Main.currentGame.getCurrentBorder() + 10);
                if (action.IsLeftClick() && action.ShiftedClicked()
                        && Main.currentGame.getCurrentBorder() - 100 >= 600
                        && Main.currentGame.getCurrentBorder() - 100 >= Main.currentGame.getFinalBorder())
                    Main.currentGame.setCurrentBorder(Main.currentGame.getCurrentBorder() - 100);
                if (action.IsLeftClick() && (!action.ShiftedClicked())
                        && Main.currentGame.getCurrentBorder() - 10 >= 600
                        && Main.currentGame.getCurrentBorder() - 10 >= Main.currentGame.getFinalBorder())
                    Main.currentGame.setCurrentBorder(Main.currentGame.getCurrentBorder() - 10);
                holder.getOpenInventory().setItem(12, InventoryUtils.getItem(Material.BARRIER,
                        ChatColor.BOLD + "§bBordure Initale§r: §a" + Main.currentGame.getCurrentBorder(),
                        Arrays.asList("", InventoryUtils.ClickTypoAdd + "10",
                                InventoryUtils.ClickTypoMassAdd + "100", InventoryUtils.ClickTypoRemove + "10",
                                InventoryUtils.ClickTypoMassRemove + "100")));
                break;
            case 13:
                if (action.ShiftedClicked() && action.IsRightClick())
                    Main.currentGame.setBorderSpeed(Main.currentGame.getBorderSpeed() + 0.5);
                if (action.IsRightClick() && !action.ShiftedClicked())
                    Main.currentGame.setBorderSpeed(Main.currentGame.getBorderSpeed() + 0.1);
                if (action.IsLeftClick() && action.ShiftedClicked()
                        && Main.currentGame.getBorderSpeed() - 0.5 > 0)
                    Main.currentGame.setBorderSpeed(Main.currentGame.getBorderSpeed() - 0.5);
                if (action.IsLeftClick() && (!action.ShiftedClicked())
                        && Main.currentGame.getBorderSpeed() - 0.1 > 0)
                    Main.currentGame.setBorderSpeed(Main.currentGame.getBorderSpeed() - 0.1);
                double i = Main.currentGame.getBorderSpeed();
                i *= 10;
                i = Math.round(i);
                i /= 10;
                holder.getOpenInventory().setItem(13, InventoryUtils.getItem(Material.FEATHER,
                        ChatColor.BOLD + "§bVitesse de la bordure§r: §a" + i + "bps",
                        Arrays.asList("", InventoryUtils.ClickTypoAdd + "0.1",
                                InventoryUtils.ClickTypoMassAdd + "0.5", InventoryUtils.ClickTypoRemove + "0.1",
                                InventoryUtils.ClickTypoMassRemove + "0.5")));
                break;
            case 14:
                if (action.ShiftedClicked() && action.IsRightClick()
                        && Main.currentGame.getCurrentBorder() >= (Main.currentGame.getFinalBorder() + 100))
                    Main.currentGame.setFinalBorder(Main.currentGame.getFinalBorder() + 100);
                if (action.IsRightClick() && !action.ShiftedClicked()
                        && Main.currentGame.getCurrentBorder() >= (Main.currentGame.getFinalBorder() + 10))
                    Main.currentGame.setFinalBorder(Main.currentGame.getFinalBorder() + 10);
                if (action.IsLeftClick() && action.ShiftedClicked()
                        && Main.currentGame.getFinalBorder() - 100 >= 100)
                    Main.currentGame.setFinalBorder(Main.currentGame.getFinalBorder() - 100);
                if (action.IsLeftClick() && (!action.ShiftedClicked())
                        && Main.currentGame.getFinalBorder() - 10 >= 100)
                    Main.currentGame.setFinalBorder(Main.currentGame.getFinalBorder() - 10);
                holder.getOpenInventory().setItem(14, InventoryUtils.getItem(Material.IRON_FENCE,
                        ChatColor.BOLD + "§bBordure Finale§r: §a" + Main.currentGame.getFinalBorder(),
                        Arrays.asList("", InventoryUtils.ClickTypoAdd + "10",
                                InventoryUtils.ClickTypoMassAdd + "100", InventoryUtils.ClickTypoRemove + "10",
                                InventoryUtils.ClickTypoMassRemove + "100")));
                break;
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
