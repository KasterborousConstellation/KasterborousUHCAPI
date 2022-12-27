package fr.supercomete.commands;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

import fr.supercomete.head.role.KasterBorousCamp;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;

public class RolesCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;
    private static KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
	public RolesCommand(Main main) {
		this.main = main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("roles") || cmd.getName().equalsIgnoreCase("compo")) {
				RoleHandler.showcompo(player);
			}
		}
		return false;
	}

	public static void display(HashMap<Class<?>, Integer> map, Player player) {
		player.sendMessage(Main.UHCTypo + "§6Composition §r(§a"+Main.CountIntegerValue(map)+"§r)");
        CampMode mode = (CampMode) Main.currentGame.getMode();
		for (KasterBorousCamp camp : mode.getPrimitiveCamps()) {
			HashMap<Class<?>, Integer> campsMap = new HashMap<Class<?>, Integer>();
			for (Entry<Class<?>, Integer> entry : map.entrySet()) {
				if (Objects.requireNonNull(api.getRoleProvider().getRoleByClass(entry.getKey())).getDefaultCamp() == camp)
					campsMap.put(entry.getKey(), entry.getValue());
			}
			if (campsMap.size() != 0) {
				player.sendMessage(camp.getColor() + " ➤" + camp.getName()+"§r ("+camp.getColor()+Main.CountIntegerValue(campsMap)+"§r)");
				for (Entry<Class<?>, Integer> e1 : campsMap.entrySet()) {
					player.sendMessage("  -" + camp.getColor() + Objects.requireNonNull(api.getRoleProvider().getRoleByClass(e1.getKey())).getName()+ " §rx" + e1.getValue());
				}
			}
		}
	}
	
}
