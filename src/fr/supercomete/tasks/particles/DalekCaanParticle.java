package fr.supercomete.tasks.particles;

import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;



public class DalekCaanParticle extends BukkitRunnable{
	private final UUID uuid;
	private final int duration;
	public DalekCaanParticle(UUID player,int duration) {
		this.uuid=player;
		this.duration=duration;
	}
	int amount=4;
	int ticktimer=0;
	final int rad=2;
	
	int counter=0;
	//particle attribute
	float xp;
	float zp;
	@Override
	public void run() {
		if(Bukkit.getPlayer(uuid)!=null) {
			Player player = Bukkit.getPlayer(uuid);
			for(int i =0;i<10;i++) {
				double angle = ((((double)ticktimer)/50.0 +(double)i/10.0)* 2*Math.PI)-Math.PI;
				
				xp = (float) (player.getLocation().getX()+rad * Math.cos(angle)); 
				zp = (float) (player.getLocation().getZ()+rad * Math.sin(angle));
				
				
				
				PacketPlayOutWorldParticles particle = new PacketPlayOutWorldParticles(EnumParticle.FLAME,true,xp,(float)player.getLocation().getY() + ((float)i/20F) +0.3F,zp,0,0,0,0,1);
				for(Player cast : Bukkit.getOnlinePlayers()) {
					((CraftPlayer)cast).getHandle().playerConnection.sendPacket(particle);
				}
			}
			if(counter>=duration) {
				cancel();
			}
			counter++;
			ticktimer=(ticktimer+1)% 50;
		}else Bukkit.getLogger().log(Level.FINE,"Player with UUID: "+uuid +" is offline during his particle animation");
	}
}
