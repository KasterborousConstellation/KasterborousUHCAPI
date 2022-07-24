package fr.supercomete.tasks;
import java.util.ArrayList;
import java.util.UUID;

import fr.supercomete.head.PlayerUtils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
public class NoDamage extends BukkitRunnable{
	private final Main main;
	int timer = 20;
	public NoDamage(Main main,int time) {
		main.setNodamage(true);
		this.main =main;
		timer=time;
	}
	@Override
	public void run(){
		timer--;
		for(UUID player : Main.currentGame.getNodamagePlayerList()) {
			if(Bukkit.getPlayer(player)!=null)
                PlayerUtility.sendActionbar(Bukkit.getPlayer(player), "§aInvincibilité: §r"+ timer+"s");
		}
		if(Main.currentGame.isGameState(Gstate.Waiting)||Main.currentGame.isGameState(Gstate.Finish)) {main.setNodamage(true); cancel();}
		if(timer<=5) {
			for(UUID player : Main.currentGame.getNodamagePlayerList()) {
				if(Bukkit.getPlayer(player)!=null)
					Bukkit.getPlayer(player).sendMessage(Main.UHCTypo+"Fin de l'invincibilité dans §4"+timer);
			}
		}
		if(timer<=0){
			main.setNodamage(false);
			for(UUID player : Main.currentGame.getNodamagePlayerList()) {
				if(Bukkit.getPlayer(player)!=null)
				Bukkit.getPlayer(player).sendMessage(Main.UHCTypo+"Fin de l'invincibilité");
			}
			Main.currentGame.setNodamagePlayerList(new ArrayList<UUID>());
			cancel();
		}
	}
}