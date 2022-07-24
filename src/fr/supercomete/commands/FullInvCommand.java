package fr.supercomete.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.supercomete.head.GameUtils.GUI.FullGUI;
import fr.supercomete.head.core.Main;
public class FullInvCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;
	public FullInvCommand(Main main) {
		this.main=main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg3) {
		if(sender instanceof Player){
			Player player=(Player)sender;
			if(cmd.getName().equalsIgnoreCase("fullinv")) {
				new FullGUI(player).open();
				return true;
				
			}
		}
		return false;
	}
}