package fr.supercomete.tasks;

import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleHandler;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class Freeze extends BukkitRunnable {
	private final UUID player;
	private final int time;
	private int timer = 0;

	public Freeze(UUID player, int time) {
		this.player = player;
		this.time = time;
	}

	@Override
	public void run() {
		if (Bukkit.getPlayer(player) != null) {
			Player player = Bukkit.getPlayer(this.player);
			if (timer >= time) {
				player.sendMessage(Main.UHCTypo + "§bLe froid vient de se dissiper.");
				cancel();
			}
			for (UUID uu : Main.getPlayerlist()) {
				if (Bukkit.getPlayer(uu) != null) {
					Player other = Bukkit.getPlayer(uu);
					if (other.getWorld().equals(player.getWorld())) {
						if (other.getLocation().distance(player.getLocation()) < 10) {
							if(RoleHandler.getRoleOf(other).getCamp().equals(Camps.DoctorCamp)) {
								if (other.hasPotionEffect(PotionEffectType.SLOW)) {
									other.removePotionEffect(PotionEffectType.SLOW);
								}
								other.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1, false, false));
							}
						}
					}
				}
			}
			for (int i = 0; i < 20; i++)
				GenerateParticle(player, 10);
			timer++;
		} else {
			Bukkit.getLogger().log(Level.INFO, "Player with uuid " + player + " is offline while freezing is active.");
		}
	}
	private void GenerateParticle(Player player, int radius) {
		int x = new Random().nextInt(radius * 2) - radius;
		int z = new Random().nextInt(radius * 2) - radius;

		Location loc = new Location(player.getWorld(), player.getLocation().getX()+x, player.getLocation().getY(), player.getLocation().getZ()+z);
		if (loc.distance(player.getLocation()) > radius) {
			GenerateParticle(player, radius);
		} else {
			PacketPlayOutWorldParticles particle = new PacketPlayOutWorldParticles(EnumParticle.SNOW_SHOVEL, true, (float)loc.getX(),(float) player.getLocation().getY() + (new Random().nextFloat() - 0.5F), (float)loc.getZ(), 0, 0, 0, 0, 1);
			for (Player cast : Bukkit.getOnlinePlayers()) {
				((CraftPlayer) cast).getHandle().playerConnection.sendPacket(particle);
			}
		}

	}
}
