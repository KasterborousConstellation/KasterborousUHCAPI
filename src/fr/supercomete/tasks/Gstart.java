package fr.supercomete.tasks;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.world.worldgenerator;
public class Gstart extends BukkitRunnable{
	private int timer =10;
	private final Main main;
	private final ArrayList<Location> location;
	public Gstart(Main main, ArrayList<Location> loc) {
		this.main = main;
		this.location=loc;
	}
	@Override
	public void run() {
		if(timer <=0) {
			if(!Main.currentGame.isGameState(Gstate.Starting))cancel();
			Bukkit.broadcastMessage(Main.UHCTypo+"§rDébut de la partie");
			Main.currentGame.setGamestate(Gstate.Playing);
			Cycle cycle = new Cycle(this.main);
			cycle.runTaskTimer(this.main, 0, 20L);
			NoDamage nodamage= new NoDamage(main, Main.currentGame.getTimer(Timer.InvincibilityTime).getData());
			nodamage.runTaskTimer(main, 0, 20L);
			for(Location loc:location){
				double locx=loc.getX();
				double locz=loc.getZ();
				for(int xl=-2;xl<3;xl++){
					for(int yl=-2;yl<3;yl++){
						for(int zl=-2;zl<3;zl++){
							worldgenerator.currentPlayWorld.getBlockAt(new Location(worldgenerator.currentPlayWorld, xl+locx, 145+yl, zl+locz)).setType(Material.AIR);
						}
					}
				}
			}
			PlayerUtility.giveStuff(Main.getPlayerlist());
			for(UUID uuid:Main.getPlayerlist()) {
				Main.currentGame.getKillList().put(uuid, 0);
			}
			cancel();
		}
		if(timer==1 || timer ==2 || timer ==3|| timer ==4|| timer ==5|| timer ==10)Bukkit.broadcastMessage(Main.UHCTypo+"§rDébut de la partie dans §c"+timer);
		timer--;
	}
}