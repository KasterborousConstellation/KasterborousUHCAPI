package fr.supercomete.head.Listeners;

import java.util.UUID;

import fr.supercomete.head.GameUtils.GUI.*;
import fr.supercomete.head.role.content.EchoEchoUHC.Neo;
import org.bukkit.plugin.PluginManager;

import fr.supercomete.head.GameUtils.GameMode.Modes.DWUHC;
import fr.supercomete.head.GameUtils.GameMode.Modes.Null_Mode;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.content.DWUHC.Kate_Stewart;
import fr.supercomete.head.role.content.DWUHC.Pting;

public class ListenersRegisterer {
	public static boolean Register(PluginManager pm,Main main) {
		try {
			//Gui Listeners
			pm.registerEvents(new RoleModeGUI(new DWUHC()), main);
			pm.registerEvents(new ModeGUI(new Null_Mode(), main), main);
			pm.registerEvents(new StewartGUI(new Kate_Stewart(UUID.randomUUID()),main), main);
			pm.registerEvents(new SeeInvGUI(main),main);
			pm.registerEvents(new PtingEat(new Pting(UUID.randomUUID()), main), main);
			pm.registerEvents(new TardisGUI(null, main), main);
			pm.registerEvents(new SnowmanGUI(null, main), main);
			pm.registerEvents(new ConfigurableGUI(main), main);
			pm.registerEvents(new AdvancedRulesGUI(main), main);
			pm.registerEvents(new RoleListGUI(main), main);
			pm.registerEvents(new KarvanistaGUI(main), main);
			pm.registerEvents(new RoleGUI(main), main);
			pm.registerEvents(new FullGUI(main), main);
			pm.registerEvents(new EnchantLimitGUI(main),main);
			pm.registerEvents(new EventGUI(main),main);
            pm.registerEvents(new MangaGui(main),main);
			//Listeners
			pm.registerEvents(new FurnaceBurnListener(main), main);
			pm.registerEvents(new EntityDeathListener(), main);
			pm.registerEvents(new ChatListeners(), main);
			pm.registerEvents(new DropListener(), main);
			pm.registerEvents(new InventoryClickListeners(main), main);
			pm.registerEvents(new LoginListener(), main);
			pm.registerEvents(new BlockPlaceListener(main), main);
			pm.registerEvents(new BlockBreakListener(main), main);
			pm.registerEvents(new InteractEventListener(main), main);
			pm.registerEvents(new EntityDamageListeners(main), main);
			pm.registerEvents(new JoinListener(main), main);
			pm.registerEvents(new PlayerAmorStandManipulateListeber(), main);
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