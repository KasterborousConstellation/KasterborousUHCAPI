package fr.supercomete.head.Inventory.GUI;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.structure.Structure;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collections;

public class WorldsGUI extends KTBSInventory {
    public WorldsGUI(Player player) {
        super("§dMondes", 9, player);
    }

    @Override
    protected boolean denyDoubleClick() {
        return false;
    }

    @Override
    protected Inventory generateinventory(Inventory inv) {
        inv.setItem(0, InventoryUtils.getItem(Material.REDSTONE_BLOCK, "§aLobby", Collections.singletonList("§7Cliquez ici pour vous téléporter au Lobby")));
        inv.setItem(1, InventoryUtils.getItem(Material.GRASS, "§bArena", Collections.singletonList("§7Cliquez ici pour vous téléporter au monde Arena")));
        int h =0;
        for(Structure structure: Main.currentGame.getMode().getStructure()) {
            inv.setItem(2+h, InventoryUtils.getItem(Main.currentGame.getMode().getMaterial(), "§b"+structure.getStructurename(), null));
            h++;
        }
        return inv;
    }

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        switch (slot) {
            case 0:
                holder.teleport(new Location(Bukkit.getWorld("world"), Main.INSTANCE.getConfig().getInt("serverapi.spawn.x"),
                        Main.INSTANCE.getConfig().getInt("serverapi.spawn.y"),
                        Main.INSTANCE.getConfig().getInt("serverapi.spawn.z")));
                break;
            case 1:
                holder.teleport(new Location(MapHandler.getMap().getPlayWorld(), 0,128,0));
                break;
            default:
                if(slot>1 && slot < 2+Main.currentGame.getMode().getStructure().size()) {
                    final Structure structure = Main.currentGame.getMode().getStructure().get(slot-2);
                    structure.teleport(holder);
                }
                break;
        }
        return true;
    }

    @Override
    protected boolean onClose(Player holder) {
        return false;
    }
}
