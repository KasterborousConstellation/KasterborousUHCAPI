package fr.supercomete.head.Inventory.GUI;

import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TeleportGUI extends KTBSInventory {
    public TeleportGUI(Player player) {
        super("§dTéléportations",9 , player);
    }

    @Override
    protected boolean denyDoubleClick() {
        return false;
    }

    @Override
    protected Inventory generateinventory(Inventory inv) {
        inv.setItem(1, InventoryUtils.getItem(Material.BOOK, "§dTéléportation à la salle des règles", null));
        inv.setItem(0,InventoryUtils.getItem(Material.NETHER_STAR, "§dTéléportation au spawn", null));
        inv.setItem(2,InventoryUtils.getItem(Material.BRICK, "§dTéléportation dans la boite à host", null));
        return inv;
    }

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        switch (slot) {
            case 1:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.teleport(new Location(p.getWorld(), Main.INSTANCE.getConfig().getInt("serverapi.ruleroom.x"),
                            Main.INSTANCE.getConfig().getInt("serverapi.ruleroom.y"),
                            Main.INSTANCE.getConfig().getInt("serverapi.ruleroom.z")));
                }
                break;
            case 0:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.teleport(new Location(p.getWorld(), Main.INSTANCE.getConfig().getInt("serverapi.spawn.x"),
                            Main.INSTANCE.getConfig().getInt("serverapi.spawn.y"),
                            Main.INSTANCE.getConfig().getInt("serverapi.spawn.z")));
                }
                break;
            case 2:
                holder.teleport(new Location(holder.getWorld(), Main.INSTANCE.getConfig().getInt("serverapi.hostbox.x"),
                        Main.INSTANCE.getConfig().getInt("serverapi.hostbox.y"),
                        Main.INSTANCE.getConfig().getInt("serverapi.hostbox.z")));
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
