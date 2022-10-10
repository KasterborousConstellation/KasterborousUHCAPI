package fr.supercomete.tasks;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import org.bukkit.scheduler.BukkitRunnable;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.world.worldBorderHandler;
import fr.supercomete.head.world.worldgenerator;
public class BorderTask extends BukkitRunnable{
	private final Main main;
	public BorderTask(Main main) {
		this.main =main;
	}
	@Override
	public void run() {
		if((Main.currentGame.getTime()>Main.currentGame.getTimer(Timer.BorderTime).getData()|| main.isForcebordure()) && Main.currentGame.getFirstBorder()>Main.currentGame.getFinalBorder()) {
			Main.currentGame.setFirstBorder(Main.currentGame.getFirstBorder()-((Main.currentGame.getBorderSpeed()*0.2)/10));
			new worldBorderHandler(Main.currentGame.getFirstBorder(),0,0, MapHandler.getMap().getPlayWorld());
		}
		if(Main.currentGame.getFirstBorder()<Main.currentGame.getFinalBorder())Main.currentGame.setFirstBorder(Main.currentGame.getFinalBorder());
		if(Main.currentGame.isGameState(Gstate.Waiting)){
			cancel();
		}
	}
}