package fr.supercomete.commands;
import java.util.*;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.NRGMode;
import fr.supercomete.head.role.Role;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleHandler;
public class RolesCommand implements CommandExecutor {
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

}