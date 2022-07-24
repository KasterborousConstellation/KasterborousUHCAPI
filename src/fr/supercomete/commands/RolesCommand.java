package fr.supercomete.commands;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.ModeAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;

public class RolesCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;

	public RolesCommand(Main main) {
		this.main = main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("roles") || cmd.getName().equalsIgnoreCase("compo")) {
				if (ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode())instanceof CampMode) {
					if (RoleHandler.isIsRoleGenerated()) {
						HashMap<Class<?>, Integer> map = new HashMap<Class<?>, Integer>();
						for (Role r : RoleHandler.getRoleList().values()) {
							int amount = (map.containsKey(r.getClass())) ? map.get(r.getClass()) + 1 : 1;
							map.put(r.getClass(), amount);
						}
						if (!RoleHandler.IsHiddenRoleNCompo)
							display(map, player);
						else
							player.sendMessage(Main.UHCTypo
									+ "§4Impossible la composition est brouillée jusqu'a l'épisode suivant");
						return false;
					} else {
						display(Main.currentGame.getRoleCompoMap(), player);
						return true;
					}
				}
			}
		}
		return false;
	}
	public static void display(HashMap<Class<?>, Integer> map, Player player) {
		player.sendMessage(Main.UHCTypo + "§6Composition §r(§a"+Main.CountIntegerValue(map)+"§r)");
		for (Camps camp : Camps.values()) {
			HashMap<Class<?>, Integer> campsMap = new HashMap<Class<?>, Integer>();
			for (Entry<Class<?>, Integer> entry : map.entrySet()) {
				if (ModeAPI.getRoleByClass(entry.getKey()).getDefaultCamp() == camp)
					campsMap.put(entry.getKey(), entry.getValue());
			}
			if (campsMap.size() != 0) {
				player.sendMessage(camp.getColor() + " ➤" + camp.getName()+"§r ("+camp.getColor()+Main.CountIntegerValue(campsMap)+"§r)");
				for (Entry<Class<?>, Integer> e1 : campsMap.entrySet()) {
					player.sendMessage("  -" + camp.getColor() + ModeAPI.getRoleByClass(e1.getKey()).getName()+ " §rx" + e1.getValue());
				}
			}
		}
	}
	
}
