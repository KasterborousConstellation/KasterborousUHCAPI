package fr.supercomete.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.supercomete.head.GameUtils.GUI.RoleListGUI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleHandler;
public class RolelistCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;
	public RolelistCommand(Main main) {
		this.main=main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg3) {
		if(sender instanceof Player){
			Player player=(Player)sender;
			if(cmd.getName().equalsIgnoreCase("rolelist")) {
				if(Main.currentGame.getMode()instanceof CampMode) {
					if(RoleHandler.IsRoleGenerated()&& (Main.IsHost(player)||!RoleHandler.getRoleList().containsKey(player.getUniqueId()))) {
						new RoleListGUI(player,"open").open();
					}
					
				}else player.sendMessage("§cCe mode de jeu ne possède pas de systeme de rôle");
				return false;
			}
		}
		return false;
	}
}