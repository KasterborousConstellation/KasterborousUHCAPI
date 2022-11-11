package fr.supercomete.head.GameUtils.GUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;

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

public class ScenarioGUI extends GUI{
    private static KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
    private static final CopyOnWriteArrayList<ScenarioGUI> allGui = new CopyOnWriteArrayList<ScenarioGUI>();
    private Inventory inv;
    private Player player;
    public ScenarioGUI() {

    }
    public ScenarioGUI(Player player) {
        this.player=null;
        this.player=player;
        if (player != null)
            allGui.add(this);
    }
    protected Inventory generateinv() {
        Inventory tmp = Bukkit.createInventory(null, 54,"§aScénarios");
        tmp=Bukkit.createInventory(null, 54,"§dScenarios");
        for(int i=0;i<9;i++) {
            tmp.setItem(i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 2));
        }
        for(int i=0;i<9;i++) {
            tmp.setItem(53-i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 2));
        }
        for(int i = 0; api.getScenariosProvider().getScenarios().size()>i; i++) {
            KasterborousScenario sc = api.getScenariosProvider().getScenarios().get(i);
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
    public void open() {
        this.inv = generateinv();
        player.openInventory(inv);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        for (ScenarioGUI gui:allGui) {
            if (e.getInventory().equals(gui.inv)) {
                e.setCancelled(true);
                int slot = e.getSlot();
                switch (slot) {
                    case 49:
                        new ModeGUI(Main.currentGame.getMode(), gui.player).open();
                        break;
                    default:
                        if (slot - 9 >= 0 && slot - 9 < api.getScenariosProvider().getScenarios().size()) {
                            if (Main.currentGame.getScenarios().contains(api.getScenariosProvider().getScenarios().get(slot - 9))) {
                                Main.INSTANCE.removeScenarios(api.getScenariosProvider().getScenarios().get(slot-9));
                            } else {
                                KasterborousScenario scenarios= api.getScenariosProvider().getScenarios().get(slot - 9);
                                if (scenarios.getCompatiblity().IsCompatible(Main.currentGame.getMode()))
                                    Main.INSTANCE.addScenarios(api.getScenariosProvider().getScenarios().get(slot - 9));
                                else
                                    gui.player.sendMessage(Main.UHCTypo + "§CScénario imcompatible");
                            }
                            gui.open();
                        }
                        break;
                }
            }
        }
    }
    // Optimization --> Forget GUI that have been closed >|<
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        for (ScenarioGUI gui : allGui) {
            if (e.getInventory().equals(gui.inv)) {
                allGui.remove(gui);
            }
        }
    }
}