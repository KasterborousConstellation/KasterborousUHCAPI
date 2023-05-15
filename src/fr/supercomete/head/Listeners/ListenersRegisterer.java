package fr.supercomete.head.Listeners;
import org.bukkit.plugin.PluginManager;
import fr.supercomete.head.core.Main;
public class ListenersRegisterer {
	public static boolean Register(PluginManager pm,Main main) {
		try {
			//Listeners
            pm.registerEvents(new WorldLoadEvents(),main);
			pm.registerEvents(new FurnaceBurnListener(main), main);
			pm.registerEvents(new EntityDeathListener(), main);
			pm.registerEvents(new ChatListeners(), main);
			pm.registerEvents(new DropListener(), main);
			pm.registerEvents(new InventoryClickListeners(), main);
			pm.registerEvents(new LoginListener(), main);
			pm.registerEvents(new BlockBreakListener(main), main);
			pm.registerEvents(new InteractEventListener(), main);
			pm.registerEvents(new EntityDamageListeners(main), main);
			pm.registerEvents(new JoinListener(), main);
			pm.registerEvents(new OnQuitListener(), main);
			pm.registerEvents(new PlayerInteractEntityListener(), main);
			pm.registerEvents(new EnchantItemListener(), main);
            pm.registerEvents(new AdvancementListener(),main);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}