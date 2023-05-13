package fr.supercomete.commands;

import fr.supercomete.head.Inventory.InventoryManager;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.nio.charset.MalformedInputException;

public class stuffconfirm implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;
	public stuffconfirm(Main main) {
		this.main=main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg3) {
		if(sender instanceof Player){
		    Player player =(Player)sender;
            if(cmd.getName().equalsIgnoreCase("stuffconfirm") && player.getGameMode().equals(GameMode.CREATIVE)) {
                try{
                    final int selected = Integer.parseInt(arg3[0]);
                    Inventory inventory = Bukkit.createInventory(null,54);
                    for(int i =0;i<player.getInventory().getSize();i++){
                        inventory.setItem(i,player.getInventory().getItem(i));
                    }
                    ItemStack helmet = player.getInventory().getHelmet();
                    if(helmet!=null) {
                        inventory.addItem(helmet);
                    }
                    ItemStack chest = player.getInventory().getChestplate();
                    if(chest!=null) {
                        inventory.addItem(chest);
                    }
                    ItemStack legs = player.getInventory().getLeggings();
                    if(legs!=null) {
                        inventory.addItem(legs);
                    }
                    ItemStack boots = player.getInventory().getBoots();
                    if(boots!=null) {
                        inventory.addItem(boots);
                    }
                    InventoryManager.set(inventory,selected);
                    player.setGameMode(GameMode.ADVENTURE);
                    player.getInventory().clear();
                    PlayerUtility.GiveHotBarStuff(player);
                }catch (NumberFormatException e){
                    player.sendMessage("§cAn error occured!");
                }
                return true;
			}
		}
		return false;
	}
}