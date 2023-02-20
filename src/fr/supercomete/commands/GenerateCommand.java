package fr.supercomete.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.supercomete.head.core.Main;
import fr.supercomete.tasks.SeedFinderTask;
public class GenerateCommand implements CommandExecutor {
	private final Main main;
	public GenerateCommand(Main main) {
		this.main=main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg3) {
		if(sender instanceof Player){
			Player player=(Player)sender;
			if(cmd.getName().equalsIgnoreCase("admingenerate")) {
				if(player.getName().equals("Supercomete")&&Main.KTBSNetwork_Connected) {
					SeedFinderTask finder = new SeedFinderTask(main);
					finder.runTaskTimer(main, 0, 40L);
					return true;

				}else {
					player.sendMessage(Main.UHCTypo+"§cErreur vous n'avez pas le droit d'utiliser cette commande.");
					return false;
				}
			}
		}
		return false;
	}
}