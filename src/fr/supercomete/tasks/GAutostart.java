package fr.supercomete.tasks;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.PlayerUtils.playerStartHandler;
import fr.supercomete.head.core.Main;
public class GAutostart extends BukkitRunnable{
	private int timer =10;
	private final Main main;
	public GAutostart(Main main) {
		this.main = main;
	}
	@Override
	public void run() {
		if(timer <=0) {
			if(!Main.currentGame.isGameState(Gstate.Starting))cancel();
			Bukkit.broadcastMessage(Main.UHCTypo+"§rLancement du jeu");
			playerStartHandler.start(main);
			cancel();
		}
		if(timer==1 || timer ==2 || timer ==3|| timer ==4|| timer ==5|| timer ==10)Bukkit.broadcastMessage(Main.UHCTypo+"§rLancement du jeu dans §c"+timer);
		timer--;
	}
}