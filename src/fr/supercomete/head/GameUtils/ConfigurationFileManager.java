package fr.supercomete.head.GameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.supercomete.datamanager.FileManager.Fileutils;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.ModeAPI;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.core.Main;
import net.md_5.bungee.api.ChatColor;

public class ConfigurationFileManager{
	@SuppressWarnings("unused")
	private static Main main;
	private static File folder;

    public ConfigurationFileManager(Main main) {
		ConfigurationFileManager.main = main;
		folder = new File(main.getDataFolder(), "/Config/");
	}

	public static String getModePath(Mode mode) {
		return new File(folder, "/" + mode.getName() + "/").getAbsolutePath();
	}

	public static void createConfigFile(Game game, Player player) {
		if (game.getGameName() == "§d<Nom de la partie>" || game.getGameName() == "" || game.getGameName().isEmpty()) {
			player.sendMessage(Main.UHCTypo + "§cNom de partie invalide");
			return;
		}
		if (!game.isGameState(Gstate.Waiting)) {
			player.sendMessage(Main.UHCTypo + "§cImpossible de sauvegarder une configuration pendant une partie");
			return;
		}

		final File folder = new File(getModePath(ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode())), "/" + player.getUniqueId() + "/");
		final File file = new File(folder, game.getGameName() + ".json");
		if (!file.exists()) {
			try {
				Fileutils.createFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		final String content = Main.manager.serialize(game);
		Fileutils.save(file, content);
		player.sendMessage(Main.UHCTypo + "§aVous avez sauvegardé votre configuration");
	}

	public static File getPlayerPath(Player player) {
		return new File(getModePath(ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode())), "/" + player.getUniqueId() + "/");
	}

	public static ArrayList<File> getallJsonInsideFolder(File folder) {
		if (!folder.exists())
			return new ArrayList<File>();
		ArrayList<File> filelist = new ArrayList<File>();
		File[] file = folder.listFiles();
		for (File f : file) {
			filelist.add(f);
		}
		return filelist;
	}
	public static void RemoveFile(Player player, int index) {
		final File folder = new File(getModePath(ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode())), "/" + player.getUniqueId() + "/");
		final ArrayList<File> configs = getallJsonInsideFolder(folder);
		final File file = configs.get(index);
		file.delete();
		player.sendMessage(Main.UHCTypo + "§aConfiguration détruite");
	}

	public static ArrayList<ItemStack> convertFileIntoItemStack(ArrayList<File> files) {
		ArrayList<ItemStack> arr = new ArrayList<ItemStack>();
		for (File file : files) {
			String json = Fileutils.loadContent(file);
			Game game = Main.manager.deserialize(json);
			ArrayList<String> strl = new ArrayList<String>();
			int nbrscenarios = game.getScenarios().size();
			strl.add("§7 │" + ChatColor.WHITE + "Mode: §1" + ModeAPI.getModeByIntRepresentation(game.getEmode()).getName());
			if (ModeAPI.getModeByIntRepresentation(game.getEmode()) instanceof CampMode) {
				strl.add("§7 │" + ChatColor.WHITE + "Roles: §4" + Main.CountIntegerValue(game.getRoleCompoMap()));
			}
			strl.add("§7 │" + ChatColor.WHITE + "Bordure: " + ChatColor.GOLD
					+ ((int) (Main.currentGame.getFirstBorder())) + "/" + ((int) (Main.currentGame.getFirstBorder())));
			strl.add("");
			strl.add("§7 │" + ChatColor.WHITE + "PvP: " + ChatColor.RED
					+ TimeUtility.transform(game.getTimer(Timer.PvPTime).getData(), "§7", "§7", "§7"));
			strl.add("§7 │" + ChatColor.WHITE + "Bordure: " + ChatColor.RED
					+ TimeUtility.transform(game.getTimer(Timer.BorderTime).getData(), "§7", "§7", "§7"));
			if (ModeAPI.getModeByIntRepresentation(game.getEmode()) instanceof CampMode) {
				strl.add("§7 │" + ChatColor.WHITE + "Role: " + ChatColor.RED
						+ TimeUtility.transform(game.getTimer(Timer.RoleTime).getData(), "§7", "§7", "§7"));
			}

			strl.add("");
			strl.add("§7 │" + ChatColor.WHITE + "Scénarios:");

			for (int i = 0; (Math.min(nbrscenarios, 5)) > i; i++) {
				strl.add("      §7" + game.getScenarios().get(i).getName());
			}
			strl.add("      §7+ " + ((nbrscenarios > 5) ? nbrscenarios - 5 : 0) + " Scénarios");
			strl.add("");
			strl.add("");
			strl.add("§aClique Droit pour charger");
			strl.add("§cShift + Clique Gauche pour effacer");
			ItemStack item = new ItemStack(Material.BOOK);
			ItemMeta im = item.getItemMeta();
			im.setDisplayName("§b" + game.getGameName());
			im.setLore(strl);

			item.setItemMeta(im);
			arr.add(item);
		}
		return arr;
	}

	public static void loadConfigFile(Player player, int index) {
		final File folder = new File(getModePath(ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode())), "/" + player.getUniqueId() + "/");
		final ArrayList<File> configs = getallJsonInsideFolder(folder);
		final File file = configs.get(index);
		final String json = Fileutils.loadContent(file);
		final Game game = Main.manager.deserialize(json);
		Main.currentGame = game;
		player.sendMessage(Main.UHCTypo + "Configuration chargée");
	}
}
