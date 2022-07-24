package fr.supercomete.tasks.particles;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.core.Main;
import fr.supercomete.tasks.FluxTask;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class FluxParticle extends BukkitRunnable {
    private final Location location;
    public FluxParticle(Location location){
        this.location=location;
    }
    @Override
    public void run() {
        for(int i=0; i<20;i++){
            float[] doubles=generate_xyz();
            float x=doubles[0]+ (float)location.getX();
            float y=doubles[1]+ (float)location.getY();
            float z=doubles[2]+(float)+location.getZ();
            PacketPlayOutWorldParticles particle = new PacketPlayOutWorldParticles(EnumParticle.FLAME,true,x,y,z,0,0,0,0,1);
            for(Player cast : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer)cast).getHandle().playerConnection.sendPacket(particle);
            }
        }
        if(Main.currentGame.getGamestate()== Gstate.Waiting){
            cancel();
        }
    }
    private float[] generate_xyz(){
        final int radius = 30;
        final Random random = new Random();
        float d;
        float x;
        float y;
        float z;
        do{
            x = (float) ((Math.random() * 2.0 - 1.0));
            y = (float) ((Math.random() * 2.0 - 1.0));
            z = (float) ((Math.random() * 2.0 - 1.0));
            d = x*x + y*y + z*z;
        }while(d>1.00);
        x=x*radius;
        y=y*radius;
        z=z*radius;
        return new float[]{x,y,z};
    }
}
