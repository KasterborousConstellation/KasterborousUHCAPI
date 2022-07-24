package fr.supercomete.tasks.particles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.supercomete.head.core.Main;
import fr.supercomete.head.structure.Structure;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class TardisParticle extends BukkitRunnable{
	public static TardisParticle PrinInstance;
	private final Location loc;
	private final int duration;
	public TardisParticle(Location loc,int duration) {
		this.loc=loc;
		this.duration=duration;
		PrinInstance=this;
	}
	int amount=4;
	int ticktimer=0;
	final int rad=1;
	int counter=0;
	
	//particle attribute
	float xp;
	float zp;
	final Structure struc = Main.currentGame.getMode().getStructure().get(0);
	@Override
	public void run() {
			for(int min=0;min<10;min++) {
				for(int i =0;i<10;i++) {
					double angle = ((((double)ticktimer)/50.0 +(double)i/10.0)* 2*Math.PI)-Math.PI;
					xp = (float) (loc.getX()+(rad+min) * Math.cos(angle)); 
					zp = (float) (loc.getZ()+(rad+min) * Math.sin(angle));
					final PacketPlayOutWorldParticles particle = new PacketPlayOutWorldParticles(EnumParticle.DRIP_WATER,true,xp,(float)loc.getY() + ((float)i/20F) +0.3F,zp,0,0,0,0,1);
					for(Player cast : Bukkit.getOnlinePlayers()) {
						((CraftPlayer)cast).getHandle().playerConnection.sendPacket(particle);
					}
				}
			}	
			createHelix(struc.getPositionRelativeToLocation(new int[] {18,20,12}));
			if(counter>=duration|| PrinInstance==null||!PrinInstance.equals(this)) {
				cancel();
			}
			counter++;
			ticktimer=(ticktimer+1)% 50;
	}
	public void createHelix(Location loc) {
        final int radius = 1;
        
        for(double y = 0; y <= 5; y+=0.05) {
        	double x = radius * Math.cos(y + ticktimer*0.15);
        	double z = radius * Math.sin(y + ticktimer*0.15);
        	PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SPELL,true, (float) (loc.getX() + x), (float) (loc.getY() + y), (float) (loc.getZ() + z), 0, 0, 0, 0, 1);
            for(Player online : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
            } 
        }
    }
}
