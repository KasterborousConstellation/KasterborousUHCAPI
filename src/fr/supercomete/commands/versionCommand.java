package fr.supercomete.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.supercomete.enums.Choice;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
public class versionCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;
	public versionCommand(Main main) {
		this.main=main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player player=(Player)sender;
			if(cmd.getName().equalsIgnoreCase("cversion")) {
				if(!(Main.currentGame.getMode()instanceof CampMode)){
					return false;
				}
				if(!RoleHandler.IsRoleGenerated()) {
					return false;
				}
				final Role role = RoleHandler.getRoleOf(player);
				if (role == null) {
					return false;
				}
				if(args.length==0) {
					player.sendMessage(Main.UHCTypo + "§cUsage: /dw version <N°Version>");
					return false;
				}
				if (role.getChoices().size() > 0 && role.getChoice() == Choice.None) {
					if (args[0] != null) {
						try {
							int v = Integer.parseInt(args[0]);
							if (role.getChoices().size() >= v) {
								role.ExecuteChoice(role.getChoices().get(v - 1));
							} else
								player.sendMessage(Main.UHCTypo + "§cUsage: /dw version <N°Version>");
						} catch (NumberFormatException e) {
							player.sendMessage(Main.UHCTypo + "§cUsage: /dw version <N°Version>");
						}
					} else
						player.sendMessage(Main.UHCTypo + "§cUsage: /dw version <N°Version>");
				} else
					player.sendMessage(Main.UHCTypo + "§cVous n'avez plus de choix à faire");
				return true;
			}
		}
		return false;
	}
}