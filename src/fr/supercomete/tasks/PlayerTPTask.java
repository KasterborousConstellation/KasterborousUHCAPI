package fr.supercomete.tasks;
import java.util.ArrayList;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
        playerlist.addAll(Bukkit.getOnlinePlayers());
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
            for (Player pl : playerlist)
                PlayerUtility.sendActionbar(pl,"§b[§r" + (iteration + 1) + "/" + playerlist.size() + "§b] §a" + player.getName());
            MapHandler.getMap().getPlayWorld().loadChunk(loc.getBlockX(), loc.getBlockZ());
			player.teleport(loc);
            PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport((Entity)player);
            for(final Player player1: Bukkit.getOnlinePlayers()){
                ((CraftPlayer)player1).getHandle().playerConnection.sendPacket(packet);
            }
			iteration++;
		}
	}
}