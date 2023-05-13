package fr.supercomete.head.Inventory.GUI;

import fr.supercomete.head.Inventory.InventoryManager;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Level;

public class StuffConfigGUI extends KTBSInventory {
    public StuffConfigGUI(Player player) {
        super("§dInventaires", 36, player);
    }

    @Override
    protected boolean denyDoubleClick() {
        return true;
    }

    @Override
    protected Inventory generateinventory(Inventory inv) {
        final ArrayList<Inventory>inventories=InventoryManager.list();
        inv.setItem(31,InventoryUtils.getItem(Material.ARROW,"§7Retour",null));
        for(int i =0;i<9;i++){
            final Inventory inventory = inventories.get(i);
            if(inventory==null){
                inv.setItem(i,InventoryManager.blank_inventory);
                inv.setItem(i+9,InventoryManager.create_inventory);
            }else{
                final boolean selected =i==InventoryManager.selected;
                inv.setItem(i,InventoryManager.getInventoryHead(i,selected));
                final ItemStack modify = InventoryUtils.getItem(Material.BARRIER,"§6Modifier", Arrays.asList("§7Cliquez ici pour modifier le contenu de cet inventaire."));
                if(selected){
                    final ItemMeta meta = modify.getItemMeta();
                    meta.setDisplayName(meta.getDisplayName() +" §a[§bSélectionné§a]");
                    meta.addEnchant(Enchantment.ARROW_INFINITE,1,true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    modify.setItemMeta(meta);
                }
                inv.setItem(i+9,modify);
                ItemStack see = InventoryUtils.getItem(Material.EYE_OF_ENDER,"§aVoir l'inventaire",Arrays.asList("§7Cliquez ici pour voir l'inventaire."));
                if(selected){
                    ItemMeta meta = see.getItemMeta();
                    meta.setDisplayName(meta.getDisplayName() +" §a[§bSélectionné§a]");
                    meta.addEnchant(Enchantment.ARROW_INFINITE,1,true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    see.setItemMeta(meta);
                }
                inv.setItem(i+18,see);
            }
        }
        return inv;
    }

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        if (slot == 31) {
            new ModeGUI(Main.currentGame.getMode(), holder).open();
        }else {
            final ArrayList<Inventory> inventories = InventoryManager.list();
            if (slot >= 0 && slot < 9) {
                if (action.ShiftedClicked() && action.IsRightClick()) {
                    InventoryManager.delete(slot);
                    refresh();
                } else if (!action.ShiftedClicked() && action.getClick().isLeftClick()) {
                    InventoryManager.selected = slot;
                    refresh();
                }
            } else if (slot > 8 && slot < 18) {
                final int id = slot - 9;
                holder.getOpenInventory().close();
                holder.getInventory().clear();
                if (inventories.get(id) != null) {
                    final Inventory selected = inventories.get(id);
                    for (int i = 0; i < Math.min(selected.getSize(), 40); i++) {
                        ItemStack selected_item = selected.getItem(i);
                        holder.getInventory().setItem(i, selected_item);
                    }
                }
                holder.setGameMode(GameMode.CREATIVE);
                final TextComponent confirm = new TextComponent("Cliquez ici pour sauvegarder l'inventaire.");
                confirm.setUnderlined(true);
                confirm.setColor(ChatColor.GRAY);
                confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/stuffconfirm " + id));
                confirm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Sauvegarder l'inventaire").create()));
                holder.spigot().sendMessage(confirm);
            } else if (slot > 17 && slot < 27) {
                final int id = slot - 18;
                final Inventory selected = inventories.get(id);
                new SeeInvGUI(holder, selected).open();
            }
        }
        return true;
    }

    @Override
    protected boolean onClose(Player holder) {
        return false;
    }
}
