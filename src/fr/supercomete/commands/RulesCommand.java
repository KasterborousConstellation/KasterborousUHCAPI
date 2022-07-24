package fr.supercomete.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.supercomete.head.GameUtils.GameMode.Modes.Null_Mode;
import fr.supercomete.head.Inventory.InventoryHandler;
import fr.supercomete.head.core.Main;
@SuppressWarnings("unused")
public class RulesCommand implements CommandExecutor{
	private final Main main;
	public RulesCommand(Main main) {
		this.main=main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			if(!(Main.currentGame.getMode()instanceof Null_Mode))
			InventoryHandler.openinventory(player,5);
		}
		return false;
	}
}
