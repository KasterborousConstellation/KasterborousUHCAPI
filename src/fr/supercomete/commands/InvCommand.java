package fr.supercomete.commands;
import fr.supercomete.head.Inventory.GUI.SeeInvGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
public class InvCommand implements CommandExecutor {
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] arg3) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			if(cmd.getName().equalsIgnoreCase("inv")){
				SeeInvGUI gui =new SeeInvGUI(player,PlayerUtility.getInventory());
                gui.open();
			}
		}
		return false;
	}

}
