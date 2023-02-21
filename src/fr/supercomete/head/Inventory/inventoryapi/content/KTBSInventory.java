package fr.supercomete.head.Inventory.inventoryapi.content;
import fr.supercomete.head.Inventory.inventoryapi.Inventoryapi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.logging.Level;
public abstract class KTBSInventory{
    private String name;
    protected abstract boolean denyDoubleClick();
    private final Player holder;
    private final int size;
    public void setName(String name){
        this.name=name;
    }
    public Player getHolder(){
        return holder;
    }
    public String getName(){
        return KTBSInventoryListener.decode+name;
    }
    public void refresh(){
        if(holder.isOnline()){
            Inventory e = generateinventory(getTMP());
            for(int i = 0;i<e.getSize();i++){
                holder.getOpenInventory().setItem(i,e.getItem(i));
            }
        }else{
            KTBSInventoryListener.holdercontent.remove(holder.getUniqueId());
        }
    }

    public KTBSInventory(String name,int size,Player player){
        if(!Inventoryapi.loaded){
            throw new RuntimeException("InventoryAPI not loaded. Please consider loading it before using any KTBSInventory.");
        }
        this.name=name;
        this.size =size;
        KTBSInventoryListener.holdercontent.put(player.getUniqueId(),this);
        holder = player;
    }
    private Inventory getTMP(){
        return Bukkit.createInventory(null,((size)- (size%9)), getName());
    }
    public void open(){
        Inventory tmp = getTMP();
        new BukkitRunnable(){
            @Override
            public void run() {
                try{
                    holder.openInventory(generateinventory(tmp));
                    cancel();
                }catch (Exception e){
                    Bukkit.getLogger().log(Level.INFO,"Holder: "+holder.getName()+" is unable to open the inventory. Delayed to 3Tick ahead");
                }
            }
        }.runTaskTimer(Inventoryapi.INSTANCE,0,3L);
    }
    protected abstract Inventory generateinventory(Inventory tmp);
    protected abstract boolean onClick(Player holder, int slot, KTBSAction action);
    protected abstract boolean onClose(Player holder);
}