package fr.supercomete.head.Inventory.GUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;

import fr.supercomete.head.Inventory.GUI.ModeGUI;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import org.bukkit.inventory.meta.ItemMeta;

public class ScenarioGUI extends KTBSInventory {
    private static KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
    private int page=0;
    public ScenarioGUI(Player player) {
        super("§aScénarios",54,player);
    }
    private ArrayList<KasterborousScenario> getScenarios(int page){
        final ArrayList<KasterborousScenario> list = new ArrayList<>();
        for(int i = 0; i < Math.max(0,api.getScenariosProvider().getRegisteredScenarios().size()-36*page); i++){
            list.add(api.getScenariosProvider().getRegisteredScenarios().get(36*page+i));
        }
        return list;
    }
    @Override
    protected Inventory generateinventory(Inventory tmp) {
        for(int i=0;i<9;i++) {
            tmp.setItem(i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 2));
        }
        for(int i=0;i<9;i++) {
            tmp.setItem(53-i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 2));
        }
        ItemStack stack = InventoryUtils.getItem(Material.PAPER,"§rPage: "+(page+1),null);
        stack.setAmount(page+1);
        tmp.setItem(4,stack);
        if(page>0){
            tmp.setItem(45,InventoryUtils.getItem(Material.ARROW,"§rPage précédente", Collections.singletonList("§7Retourne une page en arrière")));
        }
        tmp.setItem(53,InventoryUtils.getItem(Material.ARROW,"§rPage suivante",Collections.singletonList("§7Passe a la page suivante ")));
        ArrayList<KasterborousScenario> scenarios = getScenarios(page);
        for(int i = 0; scenarios.size()>i; i++) {
            KasterborousScenario sc = scenarios.get(i);
            String bool=(Main.currentGame.getScenarios().contains(sc))?"§aOn":"§cOff";
            ArrayList<String>Lines=new ArrayList<String>();
            String compatibility=(sc.getCompatiblity().IsCompatible(Main.currentGame.getMode()))?"§a✔":"§c✖";
            Lines.add(compatibility+"§r§7Compatible");
            ItemStack item = InventoryUtils.getItem(sc.getMat(),"§b"+sc.getName()+" "+bool,Lines);
            if(Main.currentGame.getScenarios().contains(sc)) {
                item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
                ItemMeta ime=item.getItemMeta();
                ime.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(ime);
            }
            tmp.setItem(i+9, item);
        }
        tmp.setItem(49,InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
        return tmp;
    }

    @Override
    protected boolean denyDoubleClick() {
        return true;
    }

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        switch (slot) {
            case 49:
                new ModeGUI(Main.currentGame.getMode(), holder).open();
                break;
            case 53:
                page++;
                refresh();
                break;
            case 45:
                if(page>0) {
                    page--;
                    refresh();
                }
                break;
            default:
                final int clicked_slot_index= slot -9;
                final int index = 36 * page + clicked_slot_index;
                if (index >= 0 && index < api.getScenariosProvider().getRegisteredScenarios().size()) {
                    KasterborousScenario scenarios= api.getScenariosProvider().getRegisteredScenarios().get(index);
                    if (Main.currentGame.getScenarios().contains(scenarios)) {
                        if(scenarios.onDisable()){
                            Main.INSTANCE.removeScenarios(scenarios);
                            Bukkit.broadcastMessage("§cDésactivation du scénario: §b"+scenarios.getName());
                        }
                    } else {
                        if (scenarios.getCompatiblity().IsCompatible(Main.currentGame.getMode()))
                            if(scenarios.onEnable()){
                                Main.INSTANCE.addScenarios(scenarios);
                                Bukkit.broadcastMessage("§aActivation du scénario: §b"+scenarios.getName());
                            }
                            else holder.sendMessage(Main.UHCTypo + "§CScénario imcompatible");
                    }
                    refresh();
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