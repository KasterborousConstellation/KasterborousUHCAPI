package fr.supercomete.head.PlayerUtils;
import java.util.ArrayList;
import java.util.UUID;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.world.worldgenerator;
import fr.supercomete.tasks.PlayerTPTask;
public class playerStartHandler{
	public static void start() {
		int rad = (int)((Main.currentGame.getFirstBorder()/2)-(0.2*(Main.currentGame.getFirstBorder()/2)));
        assert MapHandler.getMap() != null;
        ArrayList<Location> llist= generatePlayerStartingLoc.generateLocation(0,0, Bukkit.getOnlinePlayers().size(), rad, MapHandler.getMap().getPlayWorld());
		Main.getPlayerlist().clear();
		for(Player pl:Bukkit.getOnlinePlayers()){
			if(pl.getGameMode()!=GameMode.SPECTATOR)Main.getPlayerlist().add(pl.getUniqueId());
		}
		for(UUID uu:Main.getPlayerlist())TeamManager.CompletingTeam(uu);
        final Main main = Main.INSTANCE;
		PlayerTPTask task = new PlayerTPTask(main,llist);
		task.runTaskTimer(main, 0, 10L);
	}
}