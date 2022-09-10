package fr.supercomete.tasks;

import fr.supercomete.head.core.Main;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Random;
import java.util.logging.Level;

public class MangoJokeTask extends BukkitRunnable {
    private int time;
    private Location location;
    private Player player;
    public MangoJokeTask(Location location,int time, Main main,Player player){
        this.time=time;
        this.location=location;
        this.runTaskTimer(main,0,20L);
        this.player=player;
    }
    private int tick;
    private Random random = new Random();
    @Override
    public void run() {
        if(tick%3==0){
            for(final Player player : Bukkit.getOnlinePlayers()){
                if(player.getUniqueId()!=this.player.getUniqueId()&&player.getWorld().equals(location.getWorld())){
                    if(player.getLocation().distance(location)<25){
                        Location location1 = player.getLocation().clone();
                        float sign= (random.nextBoolean())?-1:1;
                        location1.setPitch((random.nextFloat() *180)-90);
                        location1.setYaw((random.nextFloat() *360)-180);
                        player.teleport(location1);
                    }
                }
            }

        }
        for(int i =0;i<100;i++){
            double angle = (Math.PI*2)*((float)i)/100.0;
            double x = Math.sin(angle)*25;
            double z =Math.cos(angle)*25;
            PacketPlayOutWorldParticles particle = new PacketPlayOutWorldParticles(EnumParticle.FLAME,true,(float)(x+location.getX()),(float)location.getY(),(float)(z+location.getZ()),0,0,0,0,1);
            for(Player cast : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer)cast).getHandle().playerConnection.sendPacket(particle);
            }
        }
        Bukkit.getLogger().log(Level.INFO,""+(time-tick));
        if(time-tick<1){
            cancel();
        }
        tick++;
    }
}
