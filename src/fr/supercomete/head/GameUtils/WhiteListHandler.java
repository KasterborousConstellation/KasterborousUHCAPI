package fr.supercomete.head.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.supercomete.head.core.Main;
public final class WhiteListHandler{
	public static boolean isWhiteList(){return Bukkit.getServer().hasWhitelist();}
	public static void setWhitelist(boolean bool) {Bukkit.setWhitelist(bool);}
	public static void clearWhitelist() {
		for(OfflinePlayer player:Bukkit.getWhitelistedPlayers()) {
			player.setWhitelisted(false);
		}
		Bukkit.reloadWhitelist(); //reload whitelist
	}
	public static void addAllOnlinePlayerToWhiteList(Player output,boolean t){
		for(Player player:Bukkit.getServer().getOnlinePlayers()){
			if(!Bukkit.getWhitelistedPlayers().contains(player)){
				player.setWhitelisted(true);
				if(t){
                    output.sendMessage(Main.UHCTypo+"Le joueur §e"+player.getName()+"§7 a été ajouté à la whitelist");
                }
			}
		}
		Bukkit.reloadWhitelist(); //reload whitelist
	}
}