package fr.supercomete.tasks;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.DelayedDeath;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.world.scoreboardmanager;
public class DelayedModeDeath extends BukkitRunnable{
	int timer;
	private final Player player;
	private final Mode mode;
	private final Location deathlocation;
	private final Player damager;
	public DelayedModeDeath(Mode mode,Location deathlocation,Player damager,Player player,int timer) {
		this.timer=timer;
		this.player=player;
		this.mode=mode;
		this.damager=damager;
		this.deathlocation=deathlocation;
		player.sendMessage("§4Un joueur vous a tué.\n§aUn joueur peut vous réanimer.\n§eSi vous décider de quitter vous serez tué instantanément.");
	}
	@Override
	public void run() {
		DelayedDeath delayed = (DelayedDeath)mode;
		scoreboardmanager.titlemessage("§a"+timer,player);
		if(player.getLocation().distance(new Location(deathlocation.getWorld(), 0, 200, 0))>10) {
			player.teleport(new Location(deathlocation.getWorld(), 0, 200, 0));
		}
		if(player.getGameMode()!=GameMode.SPECTATOR){
			PlayerUtility.PlayerRandomTPMap(player);
			stop(delayed);
		}
		if(Main.currentGame.getGamestate()==Gstate.Waiting){
			player.setGameMode(GameMode.ADVENTURE);
			stop(delayed);
		}
		if(!player.isOnline()){
			mode.DecoKillMethod(new Offline_Player(player));
            mode.ModeDefaultOnDeath(new Offline_Player(player),player.getLocation());
			player.getWorld().playSound(player.getLocation(), Sound.WITHER_SPAWN,20,0);
			stop(delayed);
		}
		if(timer<=0){
			if(player.getGameMode()==GameMode.SPECTATOR){
				player.getWorld().playSound(player.getLocation(), Sound.WITHER_SPAWN,20,0);
				mode.OnKillMethod(deathlocation,player,damager);
                mode.ModeDefaultOnDeath(player,damager,player.getLocation());
			}
			stop(delayed);
		}else{
		    delayed.onSecondtick(timer);
        }
		timer--;
	}
	private void stop(DelayedDeath delayed){
		cancel();
	}
}