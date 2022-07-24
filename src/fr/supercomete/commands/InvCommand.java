package fr.supercomete.commands;
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
				Inventory inv = Bukkit.createInventory(null, 54,"§dInventaire");
				
				for(int e=0;e<9;e++) {
					inv.setItem(e, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
				}
				for(int r=45;r<54;r++) {
					inv.setItem(r, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
				}
				for(int i=0;i<PlayerUtility.getInventory().getSize();i++){
					if(PlayerUtility.getInventory().getItem(i)!=null) {
						if(i<9)
							inv.setItem(36+i, PlayerUtility.getInventory().getItem(i));else inv.setItem(i, PlayerUtility.getInventory().getItem(i));
					}else continue;
				}
				player.openInventory(inv);
			}
		}
		return false;
	}

}
