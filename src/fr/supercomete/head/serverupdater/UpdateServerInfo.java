package fr.supercomete.head.serverupdater;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.ModeAPI;
import fr.supercomete.head.core.Main;
import net.md_5.bungee.api.ChatColor;
public class UpdateServerInfo {
	@SuppressWarnings("unused")
	private final Main main;
	private final File configFile;
	private final YamlConfiguration Ymlconfiguration;
	public UpdateServerInfo(Main main) {
		this.main=main;
		Bukkit.broadcastMessage(Main.UHCTypo+"ServerID: "+getServerid());
		configFile=new File("/var/games/minecraft/SEP/",getServerid()+".yml" );
        Ymlconfiguration= YamlConfiguration.loadConfiguration(configFile);
	}
	public void write() {
		final ConfigurationSection config = Ymlconfiguration.createSection("serverinfo");
		config.set("servername", getServerid());
		final String playercount =""+Main.currentGame.getMaxNumberOfplayer();
		final int nbrscenarios = Main.currentGame.getScenarios().size();
		ArrayList<String> strl = new ArrayList<String>();
		strl.add(" "+ ChatColor.GOLD+Main.currentGame.getGameName());
		strl.add("§7 │"+ChatColor.DARK_AQUA+"Mode: " +ChatColor.AQUA+ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode()).getName());
		strl.add("§7 │" + ChatColor.WHITE + "Scénarios:");
		for (int i = 0; i < ((nbrscenarios > 5) ? 5 : nbrscenarios); i++) {
			strl.add("      §7" + Main.currentGame.getScenarios().get(i).getName());
		}
		strl.add("      §7+ " + ((nbrscenarios > 5) ? nbrscenarios - 5 : 0) + " Scénarios");
		config.set("playercount", playercount);
		config.set("description", strl);
		config.set("status", Main.currentGame.getStatus());
		save();
	}
	public void save() {
		try {
			Ymlconfiguration.save(configFile);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getServerid() {
		String built ="";
		File file = new File("");
		String path = file.getAbsolutePath();
		char choose =' ';
		int i =0;
		while(choose !='/' && choose !=005) {
			choose=path.charAt(path.length()-1-i);
			built=choose+built;
			i++;
		}
		built =built.subSequence(1, built.length()).toString();
		return built;
	}
}
