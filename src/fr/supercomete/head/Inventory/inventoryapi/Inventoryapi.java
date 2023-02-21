package fr.supercomete.head.Inventory.inventoryapi;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;
public final class Inventoryapi {
    public static JavaPlugin INSTANCE;
    public static boolean loaded=false;
    public static void init(JavaPlugin plugin) {
        Bukkit.getLogger().log(Level.INFO,"InventoryAPI SUCCESSFULLY LOADED by: "+plugin.getName());
        INSTANCE=plugin;
        Bukkit.getPluginManager().registerEvents(new KTBSInventoryListener(),plugin);
        loaded=true;
    }
}