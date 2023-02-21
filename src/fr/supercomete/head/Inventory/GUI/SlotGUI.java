package fr.supercomete.head.Inventory.GUI;

import fr.supercomete.head.GameUtils.GUI.ModeGUI;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.core.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collections;

public class SlotGUI extends KTBSInventory {
    public SlotGUI(Player player) {
        super("§dSlots", 27, player);
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
        inv.setItem(11, InventoryUtils.getItem(Material.STONE_BUTTON, "§r-10 Slots", null));
        inv.setItem(12, InventoryUtils.getItem(Material.STONE_BUTTON, "§r-1 Slots", null));
        String name=(Main.currentGame.getMaxNumberOfplayer()==0)?"§rSlots:§a Illimité":"§rSlots:§a "+ Main.currentGame.getMaxNumberOfplayer();
        inv.setItem(13, InventoryUtils.getItem(Material.PAPER,name, null));
        inv.setItem(14, InventoryUtils.getItem(Material.STONE_BUTTON, "§r+1 Slots", null));
        inv.setItem(15, InventoryUtils.getItem(Material.STONE_BUTTON, "§r+10 Slots", null));
        inv.setItem(22, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
        return inv;
    }

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {

        switch (slot) {
            case 11:
                if (Main.currentGame.getMaxNumberOfplayer() >= 10) {
                    Main.currentGame.setMaxNumberOfplayer(Main.currentGame.getMaxNumberOfplayer() - 10);
                    refresh();
                }
                break;
            case 12:
                if (Main.currentGame.getMaxNumberOfplayer() >= 1) {
                    Main.currentGame.setMaxNumberOfplayer(Main.currentGame.getMaxNumberOfplayer() - 1);
                    refresh();
                }
                break;
            case 14:
                Main.currentGame.setMaxNumberOfplayer(Main.currentGame.getMaxNumberOfplayer() + 1);
                refresh();
                break;
            case 15:
                Main.currentGame.setMaxNumberOfplayer(Main.currentGame.getMaxNumberOfplayer() + 10);
                refresh();
                break;
            case 22:
                new ModeGUI(Main.currentGame.getMode(), holder).open();
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
