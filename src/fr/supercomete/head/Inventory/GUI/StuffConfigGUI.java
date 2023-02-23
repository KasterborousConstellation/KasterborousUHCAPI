package fr.supercomete.head.Inventory.GUI;

import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class StuffConfigGUI extends KTBSInventory {
    public StuffConfigGUI(Player player) {
        super("§dStuff", 27, player);
    }

    @Override
    protected boolean denyDoubleClick() {
        return true;
    }

    @Override
    protected Inventory generateinventory(Inventory inv) {
        inv.setItem(12, InventoryUtils.getItem(Material.CHEST, "§bSauvegarder le stuff", Main.SplitCorrectlyString("§rSauvegarde votre stuff actuel comme stuff de départ pour tout les joueurs", 25, "§r")));
        inv.setItem(13, InventoryUtils.getItem(Material.PAPER, "§bNote",  Main.SplitCorrectlyString("Les têtes sont des objets interdits. Les enchantements illégaux ne peuvent pas être sauvegardés. Les pièces d'armures finiront dans l'inventaire des joueurs quelque soit les slots dans lesquels les pièces d'armures ont été mise.", 32, "§7•")));
        inv.setItem(14, InventoryUtils.getItem(Material.CHEST, "§bCharger le stuff actuel", Main.SplitCorrectlyString("§rCliquer vous mettra en créatif pour pouvoir configurer le stuff", 25, "§r")));
        inv.setItem(22, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
        return inv;
    }

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        switch (slot) {
            case 22:
                new ModeGUI(Main.currentGame.getMode(), holder).open();
                break;
            case 14:
                ArrayList<UUID> it = new ArrayList<>();
                it.add(holder.getUniqueId());
                PlayerUtility.giveStuff(it);
                holder.setGameMode(GameMode.CREATIVE);
                break;
            case 12:
                PlayerUtility.saveStuff(holder);
                holder.getInventory().clear();
                PlayerUtility.GiveHotBarStuff(holder);
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
