package fr.supercomete.head.Listeners;

import fr.supercomete.head.GameUtils.GUI.*;

import org.bukkit.plugin.PluginManager;

import fr.supercomete.head.GameUtils.GameMode.Modes.Null_Mode;
import fr.supercomete.head.core.Main;


public class ListenersRegisterer {
	public static boolean Register(PluginManager pm,Main main) {
		try {
			//Gui Listeners

			pm.registerEvents(new ModeGUI(new Null_Mode(), main), main);

			pm.registerEvents(new SeeInvGUI(main),main);

			pm.registerEvents(new ConfigurableGUI(main), main);
			pm.registerEvents(new AdvancedRulesGUI(main), main);
			pm.registerEvents(new RoleListGUI(main), main);

			pm.registerEvents(new RoleGUI(main), main);
			pm.registerEvents(new FullGUI(main), main);
			pm.registerEvents(new EnchantLimitGUI(main),main);
			pm.registerEvents(new EventGUI(main),main);
            pm.registerEvents(new ScenarioGUI(),main);
            pm.registerEvents(new GenerationGUI(),main);
			//Listeners
			pm.registerEvents(new FurnaceBurnListener(main), main);
			pm.registerEvents(new EntityDeathListener(), main);
			pm.registerEvents(new ChatListeners(), main);
			pm.registerEvents(new DropListener(), main);
			pm.registerEvents(new InventoryClickListeners(main), main);
			pm.registerEvents(new LoginListener(), main);
			pm.registerEvents(new BlockBreakListener(main), main);
			pm.registerEvents(new InteractEventListener(), main);
			pm.registerEvents(new EntityDamageListeners(main), main);
			pm.registerEvents(new JoinListener(main), main);
            pm.registerEvents(new RoleModeGUI(), main);
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