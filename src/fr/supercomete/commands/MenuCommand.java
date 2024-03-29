package fr.supercomete.commands;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.permissions.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.supercomete.head.Inventory.GUI.ModeGUI;
import fr.supercomete.head.core.Main;

public class MenuCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;
	public MenuCommand(Main main) {
		this.main=main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] arg3) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			if(cmd.getName().equalsIgnoreCase("menu")) {
                KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
                if(!api.getPermissionProvider().IsAllowed(player, Permissions.Allow_config)){
					player.sendMessage(Main.UHCTypo+"§7» §cVous n'avez pas la permission d'utiliser cette commande.");
					return false;
				}else{
					new ModeGUI(Main.currentGame.getMode(), player).open();
				}
			}
		}
		return false;
	}
}