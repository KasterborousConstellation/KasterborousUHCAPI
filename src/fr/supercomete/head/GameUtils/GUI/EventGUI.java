package fr.supercomete.head.GameUtils.GUI;
import fr.supercomete.head.GameUtils.Events.GameEvents.Event;
import fr.supercomete.head.GameUtils.Events.GameEvents.EventsHandler;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.Inventory.InventoryHandler;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
public class EventGUI extends GUI{
	private static final CopyOnWriteArrayList<EventGUI> allGui = new CopyOnWriteArrayList<EventGUI>();
	private Inventory inv;
	private Player player;
	private Event current;
    private EventGUIMode mode;
    private static enum EventGUIMode{
        Event("Probabilités"),
        MinTime("Temps minimum"),
        MaxTime("Temps maximum")
        ;
        private String name;
        EventGUIMode(String name){
            this.name=name;
        }
        public String getName(){
            return name;
        }
    }
	public EventGUI(Main main) {
	    this.player=null;
	}
	public EventGUI(Player player) {
        mode = EventGUIMode.Event;
		this.player=player;
		if (player != null)
			allGui.add(this);
	}
	protected Inventory generateinv() {
		Inventory tmp = Bukkit.createInventory(null, 54,"§6Event "+((current==null)?"":current.getName()));
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
        if(current==null){
            int x = 0;
            for(Event event: EventsHandler.registered){
                if(event.IsCompatible()){
                    ArrayList<String> description = Main.SplitCorrectlyString(event.getDescription(),40,"§7");
                    description.add("§f"+ TimeUtility.transform(event.getMin(),((this.mode==EventGUIMode.MinTime)?"§a":"§f")) +"-"+ TimeUtility.transform(event.getMax(),((this.mode==EventGUIMode.MaxTime)?"§a":"§f")));
                    description.add(InventoryHandler.ClickTypoAdd+"1");
                    description.add(InventoryHandler.ClickTypoMassAdd+"5");
                    description.add(InventoryHandler.ClickTypoRemove+"1");
                    description.add(InventoryHandler.ClickTypoMassRemove+"5");
                    tmp.setItem(9+x,InventoryUtils.getItem(Material.REDSTONE,"§r"+event.getName()+" "+((this.mode==EventGUIMode.Event)?"§a":"§f")+event.getChance()+"%",description));
                    x++;
                }
            }
        }
		return tmp;
	}
	public void open() {
		this.inv = generateinv();
		player.openInventory(inv);
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		for (EventGUI gui:allGui){
			if (e.getInventory().equals(gui.inv)){
				e.setCancelled(true);
				final int currentSlot = e.getSlot();
				final Player player =gui.player;
				final ClickType click= e.getClick();
				ArrayList<Event>events = new ArrayList<>();
				for(final Event event : EventsHandler.registered){
				    if(event.IsCompatible()){
				        events.add(event);
                    }
                }
                switch(currentSlot){
                    case 49:
                        new ModeGUI(Main.currentGame.getMode(),player).open();
                        break;
                    case 4:
                        int index = gui.mode.ordinal();
                        gui.mode = EventGUIMode.values()[(index+1)%EventGUIMode.values().length];
                        gui.open();
                        break;
                    default:
                        if(currentSlot>=9 && currentSlot<9+events.size()){
                            final Event currentEvent = events.get(currentSlot-9);
                            if(gui.mode == EventGUIMode.Event){
                                if(click.isShiftClick()){
                                    if(click.isRightClick()){
                                        if(currentEvent.getChance()<=95){
                                            currentEvent.addChance(5);
                                        }
                                    }else{
                                        if(currentEvent.getChance()>=5){
                                            currentEvent.addChance(-5);
                                        }
                                    }
                                }else{
                                    if(click.isRightClick()){
                                        if(currentEvent.getChance()<=99){
                                            currentEvent.addChance(1);
                                        }
                                    }else{
                                        if(currentEvent.getChance()>=1){
                                            currentEvent.addChance(-1);
                                        }
                                    }
                                }
                                gui.open();
                            }else if(gui.mode == EventGUIMode.MaxTime){
                                if(click.isShiftClick()){
                                    if(click.isRightClick()){
                                        currentEvent.addMax(5);
                                    }else{
                                        if(currentEvent.getMax()>=5&& currentEvent.getMin() <= currentEvent.getMax() - 5){
                                            currentEvent.addMax(-5);
                                        }
                                    }
                                }else{
                                    if(click.isRightClick()){
                                        currentEvent.addMax(1);
                                    }else{
                                        if(currentEvent.getMax()>=1&& currentEvent.getMin() <= currentEvent.getMax() - 1){
                                            currentEvent.addMax(-1);
                                        }
                                    }
                                }
                                gui.open();
                            }else {
                                if(click.isShiftClick()){
                                    if(click.isRightClick()){
                                        if(currentEvent.getMin()+ 5 <= currentEvent.getMax())
                                            currentEvent.addMin(5);
                                    }else{
                                        if(currentEvent.getMin()>=5){
                                            currentEvent.addMin(-5);
                                        }
                                    }
                                }else{
                                    if(click.isRightClick()){
                                        if(currentEvent.getMin()+ 1 <= currentEvent.getMax()){
                                            currentEvent.addMin(1);
                                        }
                                    }else{
                                        if(currentEvent.getMin()>=1){
                                            currentEvent.addMin(-1);
                                        }
                                    }
                                }
                                gui.open();
                            }
                        }
                        break;
                }
			}
		}
	}
	// Optimization --> Forget GUI that have been closed >|<
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
        allGui.removeIf(gui -> e.getInventory().equals(gui.inv));
	}
}