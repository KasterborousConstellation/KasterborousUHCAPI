package fr.supercomete.head.world.ScoreBoard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import fr.supercomete.head.GameUtils.GameMode.ModeModifier.Groupable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.supercomete.head.GameUtils.ColorScheme;
import fr.supercomete.head.core.Main;
public class ScoreBoardManager {
	private static Main main;
	public ScoreBoardManager(Main main) {
		ScoreBoardManager.main=main;
	}
	public static HashMap<UUID, SimpleScoreboard> boards = new HashMap<>();
	public static void init(Player player) {
		SimpleScoreboard ss = new SimpleScoreboard("§1»§a"+Main.INSTANCE.getConfig().getString("serverapi.serverconfig.servername")+"§1«");
		int max = generateScoreboard(player).size();
		for(int i =0;i < max;i++)
		    ss.add(generateScoreboard(player).get(i), max-i);
		ss.update();
		ss.send(player);
		
		boards.put(player.getUniqueId(), ss);
	}
    public static void reset(){
        boards.clear();
        for(Player player : Bukkit.getOnlinePlayers()){
            init(player);
        }
    }
	public static void update(Player player) {
		if(boards.get(player.getUniqueId())==null)init(player);
		SimpleScoreboard ss = boards.get(player.getUniqueId());
		int max = generateScoreboard(player).size();
		for(int i =0;i < max;i++)
			ss.add(generateScoreboard(player).get(i), max-i);
		ss.update();
		ss.send(player);
	}
	public static ArrayList<String> generateScoreboard(Player player){
		ArrayList<String> list = new ArrayList<>();
		ColorScheme scheme = Main.currentGame.getColorScheme();
		ChatColor p = scheme.getPrimary();
		ChatColor s = scheme.getSecondary();
		ChatColor t = scheme.getTertiary();
		list.add(ChatColor.BOLD+"§7§7------------------");
		final String number = (Main.currentGame.getMaxNumberOfplayer()==0)?"":p+"("+s+Main.countnumberofplayer()+t+"/"+s+Main.currentGame.getMaxNumberOfplayer()+p+")";
		list.add(p + Main.currentGame.getGameName());
		list.add(number);
		list.add("§r");
		list.add("§c» " + p + "Host " + t + "» " + s + ((Main.host!=null&& Bukkit.getPlayer(Main.host)!=null)?Bukkit.getPlayer(Main.host).getName():"§cUnknown"));
		list.add("§c» " + p + "Mode " + t + "» " + s+ Main.currentGame.getMode().getName());
		list.add("§c» " + p + "Kills " + t + "» " + s + ((Main.currentGame.getKillList().get(player.getUniqueId())==null)?"Aucun":Main.currentGame.getKillList().get(player.getUniqueId())));
		if(Main.currentGame.getMode()instanceof Groupable){
            list.add("§c» " + p + "Groupe " + t + "» " + s + Main.currentGame.getGroupe());
        }else{
            list.add("§C§C§C§C§C§C");
        }
		list.add("§r§r");
		list.add("§c» " + p + "Temps " + t + "» " + s+ Main.transformScoreBoardType(Main.currentGame.getTime(), s.toString(), s.toString()));
		list.add("§c» " + p + "Bordure " + t + "» " + s + "±"+((int) (Main.currentGame.getFirstBorder()/2)));
		list.add("§c");
		list.add("§c» " + p + main.getServerId());
		list.add(ChatColor.BOLD+"§7------------------");
		return list;
	}
}