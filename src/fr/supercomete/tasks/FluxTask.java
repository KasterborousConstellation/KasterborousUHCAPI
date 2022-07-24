package fr.supercomete.tasks;

import fr.supercomete.enums.Camps;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.world.worldBorderHandler;
import fr.supercomete.head.world.worldgenerator;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class FluxTask extends BukkitRunnable{
	private final Location location;
	public FluxTask(Location location) {
		this.location=location;
	}
	@Override
	public void run() {
	    for(final Player player:Bukkit.getOnlinePlayers()){
	        if(player.getWorld()==location.getWorld()){
	            if(RoleHandler.getRoleOf(player)!=null&&RoleHandler.getRoleOf(player).getCamp()!= Camps.Division){
	                if(player.getGameMode()!= GameMode.SPECTATOR){
                        if(player.getLocation().distance(location)<30){
                            if(player.getHealth()>2){
                                player.setHealth(player.getHealth()-2);
                                player.damage(0);
                            }else{
                                player.addPotionEffect(new PotionEffect(PotionEffectType.HARM,1,0,false,false));
                            }
                        }
                    }
                }

            }
        }
		if(Main.currentGame.isGameState(Gstate.Waiting)){
			cancel();
		}
	}
}