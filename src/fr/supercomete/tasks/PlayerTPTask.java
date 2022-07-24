package fr.supercomete.tasks;
import java.util.ArrayList;

import fr.supercomete.head.PlayerUtils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.world.worldgenerator;
public class PlayerTPTask extends BukkitRunnable {
	private final ArrayList<Location> arrll;
	private int iteration = 0;
	private final Main main;
	private final ArrayList<Player> playerlist = new ArrayList<Player>();
	public PlayerTPTask(Main main, ArrayList<Location> loclist) {
		this.arrll = loclist;
		this.main = main;
		for (Player pl : Bukkit.getOnlinePlayers()) {
			playerlist.add(pl);
		}
	}
	@Override
	public void run() {
		if (Bukkit.getOnlinePlayers().size() <= iteration) {
			Gstart start = new Gstart(this.main, arrll);
			start.runTaskTimer(this.main, 0, 20L);
			BorderTask border = new BorderTask(main);
			border.runTaskTimer(main, 0, 1L);  
			int id = this.getTaskId();
			Bukkit.getScheduler().cancelTask(id);
		} else {
			final Player player = playerlist.get(iteration);
			final Location loc = arrll.get(iteration);
			worldgenerator.currentPlayWorld.loadChunk(loc.getBlockX(), loc.getBlockZ());
			player.teleport(loc);
			player.setMaxHealth(20);
			player.setHealth(20);
			player.setFoodLevel(40);
			player.setExp(0);
			player.setLevel(0);
			player.getInventory().clear();
			player.getInventory().setHelmet(new ItemStack(Material.AIR));
			player.getInventory().setChestplate(new ItemStack(Material.AIR));
			player.getInventory().setLeggings(new ItemStack(Material.AIR));
			player.getInventory().setBoots(new ItemStack(Material.AIR));
			if (player.getGameMode() != GameMode.SPECTATOR)
				player.setGameMode(GameMode.SURVIVAL);
			player.setBedSpawnLocation(loc);
			for (Player pl : playerlist)
                PlayerUtility.sendActionbar(pl,"§b[§r" + (iteration + 1) + "/" + playerlist.size() + "§b] §a" + player.getName());
			iteration++;
		}
	}
}