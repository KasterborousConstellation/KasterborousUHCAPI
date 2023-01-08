package fr.supercomete.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.Inventory.InventoryHandler;
import fr.supercomete.head.core.Main;

public class TeamCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;

	public TeamCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg3) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("team")) {
				if (!Main.currentGame.isGameState(Gstate.Waiting)) {
					player.sendMessage(Main.UHCTypo + "Les équipes ne peuvent pas être changés pendant la partie");
					return false;
				}
				InventoryHandler.openinventory(player, 9);
				return false;
			}
		}
		return false;
	}

}