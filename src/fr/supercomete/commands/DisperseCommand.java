package fr.supercomete.commands;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
public class DisperseCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;
	public DisperseCommand(Main main) {
		this.main=main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
		    Player player = (Player)sender;
            if(cmd.getName().equalsIgnoreCase("disperse") ) {
				if(!(Main.IsCohost(player) || Main.IsHost(player))) {
					player.sendMessage(Main.UHCTypo+"§cVous n'avez pas le droit d'utiliser cette commande");
					return false;
				}
				if(Main.currentGame.getGamestate()!=Gstate.Waiting &&Main.currentGame.getGamestate()!=Gstate.Finish) {
					if(args.length!=1) {
						player.sendMessage(Main.UHCTypo+"Usage: /disperse <Rayon>");
						return false;
					}
					int v;
					try {
						v = Integer.parseInt(args[0]);
					} catch (NumberFormatException e) {
						player.sendMessage(Main.UHCTypo + "§cUsage: /disperse <Rayon>");
						return false;
					}
					for(final Player target :Bukkit.getOnlinePlayers()) {
						if(target.getWorld()==player.getWorld()) {
							if(target.getGameMode()!=GameMode.SPECTATOR) {
								if(target.getLocation().distance(player.getLocation())<=v) {
									PlayerUtility.PlayerRandomTPMap(player);
								}
							}
						}
					}
					return true;
				}else {
					player.sendMessage(Main.UHCTypo+"§cVous ne pouvez pas utiliser cette commande quand la partie n'est pas en cours.");
					return false;
				}
			}
		}
		return false;
	}
}