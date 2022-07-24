package fr.supercomete.commands;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.supercomete.head.core.Main;
public class HelpopCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;
	public HelpopCommand(Main main) {
		this.main=main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player player=(Player)sender;
			if(cmd.getName().equalsIgnoreCase("helpop")) {
				if(args.length==0) {
					player.sendMessage(Main.UHCTypo+"Usage: /helpop <Message>");
					return false;
				}
				String mess ="";
				for(String string : args) {
					mess=mess+string+" ";
				}
				ArrayList<Player>reciever = new ArrayList<>();
				for(final Player it : Bukkit.getOnlinePlayers()) {
					if(Main.IsHost(it)||Main.IsCohost(it))reciever.add(it);
				}
				for(final Player it : reciever) {
					it.sendMessage(Main.UHCTypo+"Un joueur vient de demander de l'aide.");
					it.sendMessage("§6Helpop: "+ChatColor.AQUA+mess);
				}
				return true;
				
			}
		}
		return false;
	}
}