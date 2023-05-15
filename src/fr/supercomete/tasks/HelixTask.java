package fr.supercomete.tasks;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import fr.supercomete.head.GameUtils.TeamManager;

public class HelixTask extends BukkitRunnable{
	private double radius;
	private final Location location;
	private int presicion=100;
	private final boolean stop;
	float xp;
	float zp;
	float bonusy=0;
//	private int ticktimer=0;
	int i = 0;
	public HelixTask(Location location,int radius,boolean stop) {
		this.location=location;
		this.radius=radius;
		this.stop=stop;
	}
	public void setPresicion(int precision) {
		this.presicion=precision;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		for(int e =0;e<presicion;e++) {
			double angle = (((double)i/presicion)* 2*Math.PI)-Math.PI;
			
			xp = (float) (location.getX()+radius * Math.cos(angle)); 
			zp = (float) (location.getZ()+radius * Math.sin(angle));
			if(i%20==0)bonusy++;
			Block bloc =location.getWorld().getBlockAt(new Location(location.getWorld(), Math.round(xp), Math.round(location.getY()-(double)bonusy), Math.round(zp)));
			if(stop) {
				bloc.setType(Material.AIR);
			}else {
				bloc.setType(Material.STAINED_GLASS);
				
				short color = new Random().nextBoolean()?(new Random().nextBoolean()?TeamManager.getShortOfChatColor(ChatColor.YELLOW):TeamManager.getShortOfChatColor(ChatColor.RED)):new Random().nextBoolean()?TeamManager.getShortOfChatColor(ChatColor.DARK_PURPLE):TeamManager.getShortOfChatColor(ChatColor.GOLD);
				
				bloc.setData((byte)color);
			}
			i++;
		}
		radius=radius -1.5;
		if(radius<=0)cancel();
	}
}