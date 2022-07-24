package fr.supercomete.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.supercomete.head.GameUtils.GUI.RoleGUI;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleHandler;

public class RoleCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;

	public RoleCommand(Main main) {
		this.main = main;
	}
//
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("role")) {
				if(args.length>1) {
					player.sendMessage(Main.UHCTypo+"§cUsage: /role");
					return false;
				}
				if(args.length==1) {
					if(Main.IsHost(player)||Main.IsCohost(player)) {
						if(!RoleHandler.isIsRoleGenerated()) {
							player.sendMessage(Main.UHCTypo+"§cLes rôles ne sont pas encore générés");
							return false;
						}
						final Player target = Bukkit.getPlayer(args[0]);
						if(target==null) {
							player.sendMessage(Main.UHCTypo+"§cImpossible de trouver le joueur spécifié");
							return false;
						}
						new RoleGUI(player,target).open();
					}else {
						player.sendMessage(Main.UHCTypo+"§cVous ne pouvez pas utiliser cette commande");
					}
						
					return true;
				}
				if (!RoleHandler.isIsRoleGenerated()) {
					player.sendMessage(Main.UHCTypo + "§cVous n'avez pas encore de rôle");
					return false;
				} else
					RoleHandler.DisplayRole(player);
			}
		}
		return false;
	}
	//
}
