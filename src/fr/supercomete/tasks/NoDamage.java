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
    private ArrayList<UUID>nodamage;
	int timer = 20;
	public NoDamage(int time,ArrayList<UUID>nodamage) {
        Main.currentGame.getNodamagePlayerList().addAll(nodamage);
        this.nodamage=nodamage;
		timer=time;
	}
	@Override
	public void run(){
		timer--;
		for(UUID player : nodamage) {
			if(Bukkit.getPlayer(player)!=null)
                PlayerUtility.sendActionbar(Bukkit.getPlayer(player), "§aInvincibilité: §r"+ timer+"s");
		}
		if(Main.currentGame.isGameState(Gstate.Waiting)||Main.currentGame.isGameState(Gstate.Finish)) {
            cancel();
        }
		if(timer<=5) {
			for(UUID player : nodamage) {
				if(Bukkit.getPlayer(player)!=null)
					Bukkit.getPlayer(player).sendMessage(Main.UHCTypo+"Fin de l'invincibilité dans §4"+timer);
			}
		}
		if(timer<=0){
			for(UUID player : nodamage) {
				if(Bukkit.getPlayer(player)!=null) {
                    Bukkit.getPlayer(player).sendMessage(Main.UHCTypo + "Fin de l'invincibilité");
                }
			}
            ArrayList<UUID> uuids = Main.currentGame.getNodamagePlayerList();
            for(UUID uuid:nodamage){
                uuids.remove(uuid);
            }
            Main.currentGame.setNodamagePlayerList(uuids);
			cancel();
		}
	}
}