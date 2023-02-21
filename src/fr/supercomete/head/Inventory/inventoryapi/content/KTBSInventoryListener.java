package fr.supercomete.head.Inventory.inventoryapi.content;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KTBSInventoryListener implements Listener {
    public static final String decode = "§§§r";
    public static HashMap<UUID,KTBSInventory> holdercontent= new HashMap<>();
    @EventHandler
    public void onClickEvent(InventoryClickEvent e){
        final Player clicker = (Player) e.getWhoClicked();
        final int slot = e.getSlot();
        final ClickType type = e.getClick();
        final InventoryAction action= e.getAction();
        boolean motion = false;
        for(Map.Entry<UUID,KTBSInventory> entry : KTBSInventoryListener.holdercontent.entrySet()){
            if(e.getInventory().getName().equals(entry.getValue().getName())&&entry.getKey().equals(clicker.getUniqueId())){
                if(entry.getValue().denyDoubleClick()){
                    if(ClickType.DOUBLE_CLICK==type){
                        continue;
                    }
                }
                motion = motion||entry.getValue().onClick(clicker,slot,new KTBSAction(type,action));
            }
        }
        if(motion){
            Bukkit.getScheduler().runTask(Main.INSTANCE, clicker::updateInventory);
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onCloseEvent(InventoryCloseEvent e){
        final Player closer = (Player) e.getPlayer();
        for(Map.Entry<UUID,KTBSInventory> entry : KTBSInventoryListener.holdercontent.entrySet()){
            if(e.getInventory().getName().equals(entry.getValue().getName())&&entry.getKey().equals(closer.getUniqueId())){
                if(entry.getValue().onClose(closer)){
                    entry.getValue().open();
                }else{
                    KTBSInventoryListener.holdercontent.remove(entry.getKey());
                }
                break;
            }
        }
    }
}
