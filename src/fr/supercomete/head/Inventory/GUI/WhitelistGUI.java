package fr.supercomete.head.Inventory.GUI;

import fr.supercomete.head.GameUtils.GUI.ModeGUI;
import fr.supercomete.head.GameUtils.WhiteListHandler;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collections;

public class WhitelistGUI extends KTBSInventory {
    public WhitelistGUI( Player player) {
        super("§dWhitelist", 27, player);
    }

    @Override
    protected boolean denyDoubleClick() {
        return true;
    }

    @Override
    protected Inventory generateinventory(Inventory inv) {
        for(int h=0;h<9;h++)inv.setItem(h, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE," ",1,(short)3));
        for(int h=18;h<27;h++)inv.setItem(h,InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE," ",1,(short)3));
        short color=(short)((Bukkit.hasWhitelist())?5:14);
        final String booleans=(Bukkit.hasWhitelist())?"§aOn":"§cOff";
        inv.setItem(12, InventoryUtils.getItem(Material.BARRIER, "§cNettoyer la whitelist", Main.SplitCorrectlyString("Tout les joueurs sauf les joueurs connectés seront enlevés de la whitelist", 35, "§7")));
        inv.setItem(13, InventoryUtils.createColorItem(Material.WOOL, "§bWhiteList: "+booleans, 1, color));
        inv.setItem(14, InventoryUtils.getItem(Material.PAINTING, "§bRemplir la whitelist",Main.SplitCorrectlyString("Ajoute tous les joueurs connectés à la whitelist", 35, "§7")));
        inv.setItem(22, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
        inv.setItem(11, InventoryUtils.getItem(Material.SIGN,"§rAfficher les joueurs whitelist", Main.SplitCorrectlyString("Affiche tout les joueurs whitelist, ainsi que si il sont connecté.", 40, "§7")));
        return inv;
    }

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        switch (slot) {
            case 22:
                new ModeGUI(Main.currentGame.getMode(), holder).open();
                break;
            case 11:
                holder.sendMessage("§aWhitelist "+Main.TranslateBoolean(Bukkit.hasWhitelist()));
                for(final OfflinePlayer offp : Bukkit.getWhitelistedPlayers()) {
                    holder.sendMessage("  §b"+offp.getName()+" "+ Main.getCheckMark(offp.isOnline()));
                }
                break;
            case 13:
                Bukkit.setWhitelist(!Bukkit.hasWhitelist());
                refresh();
                break;
            case 14:
                WhiteListHandler.addAllOnlinePlayerToWhiteList(holder, true);
                break;
            case 12:
                WhiteListHandler.clearWhitelist();
                WhiteListHandler.addAllOnlinePlayerToWhiteList(null, false);
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
