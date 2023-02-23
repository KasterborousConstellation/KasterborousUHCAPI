package fr.supercomete.head.Inventory.GUI;

import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.GameUtils.Time.TimerType;
import fr.supercomete.head.GameUtils.Time.WatchTime;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.core.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;

public class TimerGUI extends KTBSInventory {
    int selected=0;
    public TimerGUI(Player player) {
        super("§dTimers", 54, player);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getOpenInventory().getTitle().equals(getName())) {
                    refresh();
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.INSTANCE,1,20L);
    }

    @Override
    protected boolean denyDoubleClick() {
        return true;
    }

    @Override
    protected Inventory generateinventory(Inventory inv) {
        int i=18;
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        for(int e=0;e<9;e++){
            inv.setItem(e, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 11));
        }
        for(int e=0;e<9;e++){
            inv.setItem(53-e,InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 11));
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        for(Timer t: Main.INSTANCE.getCompatibleTimer()){
            if(t.getType()== TimerType.TimeDependent &&(Main.currentGame.getTimer(t).getData() - Main.currentGame.getTime()) >0){
                inv.setItem(i, InventoryUtils.getItem(Material.PAPER,Main.INSTANCE.generateNameTimer(t),null));
                i++;
            }
            if(t.getType()== TimerType.Literal){
                inv.setItem(i, InventoryUtils.getItem(Material.PAPER,Main.INSTANCE.generateNameTimer(t),null));
                i++;
            }
        }
        inv.setItem(9, InventoryUtils.getItem(Material.STONE_BUTTON, "§r-60m", null));
        inv.setItem(10, InventoryUtils.getItem(Material.STONE_BUTTON, "§r-10m", null));
        inv.setItem(11, InventoryUtils.getItem(Material.STONE_BUTTON, "§r-1m", null));
        inv.setItem(12, InventoryUtils.getItem(Material.STONE_BUTTON, "§r-10s", null));
        inv.setItem(13, InventoryUtils.getItem(Material.PAPER, "§r"+Main.INSTANCE.generateNameTimer(Main.INSTANCE.getCompatibleTimer().get(selected)), null));
        inv.setItem(14, InventoryUtils.getItem(Material.STONE_BUTTON, "§r+10s", null));
        inv.setItem(15, InventoryUtils.getItem(Material.STONE_BUTTON, "§r+1m", null));
        inv.setItem(16, InventoryUtils.getItem(Material.STONE_BUTTON, "§r+10m", null));
        inv.setItem(17, InventoryUtils.getItem(Material.STONE_BUTTON, "§r+60m", null));
        inv.getItem(18+selected).setType(Material.COMPASS);
        inv.setItem(49, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
        return inv;
    }

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        switch (slot) {
            case 49:
                new ModeGUI(Main.currentGame.getMode(), holder).open();
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
                int[] add = { -3600, -600, -60, -10, 0, 10, 60, 600, 3600 };
                final ArrayList<Timer> timerlist =Main.INSTANCE.getCompatibleTimer();
                int addNow = add[slot-9];
                ArrayList<Timer> drawnTimer = new ArrayList<>();
                for(Timer t: timerlist){
                    if(t.getType()== TimerType.TimeDependent && (Main.currentGame.getTimer(t).getData()-Main.currentGame.getTime())>0){
                        drawnTimer.add(t);
                    }
                    if(t.getType()== TimerType.Literal){
                        drawnTimer.add(t);
                    }
                }
                WatchTime selectedTimer = Main.currentGame.getTimer(drawnTimer.get(selected));
                if(selectedTimer.getId().getType() == TimerType.Literal||selectedTimer.getData()-Main.currentGame.getTime()+ addNow >0){
                    selectedTimer.add(addNow);
                    refresh();
                    holder.sendMessage(drawnTimer.get(selected).getName()+": "+ TimeUtility.transform(selectedTimer.getData(), "§d", "§d", "§d"));
                }

                break;
            default:
                ArrayList<Timer> timers = new ArrayList<>();
                for(Timer t: Main.INSTANCE.getCompatibleTimer()){
                    if(t.getType()== TimerType.TimeDependent && (Main.currentGame.getTimer(t).getData()-Main.currentGame.getTime())>0){
                        timers.add(t);
                    }
                    if(t.getType()== TimerType.Literal){
                        timers.add(t);
                    }
                }
                if (slot >= 18 && slot < (18 + timers.size())) {
                    selected = slot - 18;
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
