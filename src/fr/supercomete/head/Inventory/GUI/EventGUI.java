package fr.supercomete.head.Inventory.GUI;
import fr.supercomete.head.GameUtils.Events.GameEvents.Event;
import fr.supercomete.head.GameUtils.Events.GameEvents.EventsHandler;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.core.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collections;

public class EventGUI extends KTBSInventory {
    private EventGUIMode mode;
    private enum EventGUIMode{
        Event("Probabilités"),
        MinTime("Temps minimum"),
        MaxTime("Temps maximum")
        ;
        private final String name;
        EventGUIMode(String name){
            this.name=name;
        }
        public String getName(){
            return name;
        }
    }
	public EventGUI(Player player) {
        super("§6Event",54,player);
        mode = EventGUIMode.Event;
	}
    @Override
	protected Inventory generateinventory(Inventory tmp) {
		for(int i=0;i<9;i++){
            tmp.setItem(i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE," ",1,(short)4));
        }
        for(int i=0;i<9;i++){
            tmp.setItem(53-i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE," ",1,(short)4));
        }
        tmp.setItem(49,InventoryUtils.getItem(Material.ARROW,"§fMenu Principal", Collections.singletonList("§7Retour en arrière")));
        ArrayList<String> desc = new ArrayList<>();
        for(EventGUIMode mode : EventGUIMode.values()){
            desc.add(((mode == this.mode)?"§a":"§f")+mode.getName());
        }
        tmp.setItem(4,InventoryUtils.getItem(Material.ANVIL,"§fModification",desc));
        int x = 0;
        for(Event event: EventsHandler.registered){
            if(event.IsCompatible()){
                ArrayList<String> description = Main.SplitCorrectlyString(event.getDescription(),40,"§7");
                description.add("§f"+ TimeUtility.transform(event.getMin(),((this.mode==EventGUIMode.MinTime)?"§a":"§f")) +"-"+ TimeUtility.transform(event.getMax(),((this.mode==EventGUIMode.MaxTime)?"§a":"§f")));
                description.add(InventoryUtils.ClickTypoAdd+"1");
                description.add(InventoryUtils.ClickTypoMassAdd+"5");
                description.add(InventoryUtils.ClickTypoRemove+"1");
                description.add(InventoryUtils.ClickTypoMassRemove+"5");
                tmp.setItem(9+x,InventoryUtils.getItem(Material.REDSTONE,"§r"+event.getName()+" "+((this.mode==EventGUIMode.Event)?"§a":"§f")+event.getChance()+"%",description));
                x++;
            }
        }
		return tmp;
	}

    @Override
    protected boolean denyDoubleClick() {
        return true;
    }


    @Override
    protected boolean onClick(Player holder, int currentSlot, KTBSAction action) {
        final ClickType click= action.getClick();
        ArrayList<Event>events = new ArrayList<>();
        for(final Event event : EventsHandler.registered){
            if(event.IsCompatible()){
                events.add(event);
            }
        }
        switch(currentSlot) {
            case 49:
                new ModeGUI(Main.currentGame.getMode(), holder).open();
                break;
            case 4:
                int index = mode.ordinal();
                mode = EventGUIMode.values()[(index + 1) % EventGUIMode.values().length];
                refresh();
            default:
                if (currentSlot >= 9 && currentSlot < 9 + events.size()) {
                    final Event currentEvent = events.get(currentSlot - 9);
                    if (mode == EventGUIMode.Event) {
                        if (click.isShiftClick()) {
                            if (click.isRightClick()) {
                                if (currentEvent.getChance() <= 95) {
                                    currentEvent.addChance(5);
                                }
                            } else {
                                if (currentEvent.getChance() >= 5) {
                                    currentEvent.addChance(-5);
                                }
                            }
                        } else {
                            if (click.isRightClick()) {
                                if (currentEvent.getChance() <= 99) {
                                    currentEvent.addChance(1);
                                }
                            } else {
                                if (currentEvent.getChance() >= 1) {
                                    currentEvent.addChance(-1);
                                }
                            }
                        }
                        refresh();
                    } else if (mode == EventGUIMode.MaxTime) {
                        if (click.isShiftClick()) {
                            if (click.isRightClick()) {
                                currentEvent.addMax(5);
                            } else {
                                if (currentEvent.getMax() >= 5 && currentEvent.getMin() <= currentEvent.getMax() - 5) {
                                    currentEvent.addMax(-5);
                                }
                            }
                        } else {
                            if (click.isRightClick()) {
                                currentEvent.addMax(1);
                            } else {
                                if (currentEvent.getMax() >= 1 && currentEvent.getMin() <= currentEvent.getMax() - 1) {
                                    currentEvent.addMax(-1);
                                }
                            }
                        }
                        refresh();
                    } else {
                        if (click.isShiftClick()) {
                            if (click.isRightClick()) {
                                if (currentEvent.getMin() + 5 <= currentEvent.getMax())
                                    currentEvent.addMin(5);
                            } else {
                                if (currentEvent.getMin() >= 5) {
                                    currentEvent.addMin(-5);
                                }
                            }
                        } else {
                            if (click.isRightClick()) {
                                if (currentEvent.getMin() + 1 <= currentEvent.getMax()) {
                                    currentEvent.addMin(1);
                                }
                            } else {
                                if (currentEvent.getMin() >= 1) {
                                    currentEvent.addMin(-1);
                                }
                            }
                        }
                        refresh();
                    }
                }
        }
        return true;
    }
    @Override
    protected boolean onClose(Player holder) {
        return false;
    }
}