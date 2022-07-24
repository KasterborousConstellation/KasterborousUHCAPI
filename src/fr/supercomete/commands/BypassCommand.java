package fr.supercomete.commands;

import fr.supercomete.ServerExchangeProtocol.File.PlayerAccountManager;
import fr.supercomete.ServerExchangeProtocol.Rank.Rank;
import fr.supercomete.datamanager.Fileutils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.world.worldgenerator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class BypassCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;
	public BypassCommand(Main main) {
		this.main=main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg3) {
		if(sender instanceof Player player){
            if(cmd.getName().equalsIgnoreCase("bypass")) {
				Main.updateBypass();
                if(Main.IsHost(player)||Main.IsCohost(player)) {
                    if(Main.bypass.contains(player.getUniqueId())){
                        Main.bypass.remove(player.getUniqueId());
                        player.sendMessage(Main.UHCTypo+"§fBypass: "+Main.TranslateBoolean(false));
                    }else{
                        Main.bypass.add(player.getUniqueId());
                        player.sendMessage(Main.UHCTypo+"§fBypass: "+Main.TranslateBoolean(true));
                    }

                    return true;
                }
			}
		}
		return false;
	}
}