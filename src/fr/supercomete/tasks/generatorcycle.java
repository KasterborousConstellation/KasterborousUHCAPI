package fr.supercomete.tasks;
import java.util.ArrayList;

import fr.supercomete.head.PlayerUtils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.supercomete.enums.GenerationMode;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
public class generatorcycle extends BukkitRunnable{
	World w;
	int rad;
	double x=-rad;
	double z=-rad;
	int iteration = 0;
	private final Main main;
	public generatorcycle(int rad,World w,Main main) {
		this.rad=rad;
		this.w=w;
		this.main=main;
	}
	double totaliteration;
	long start;
	@Override
	public void run(){
		if(iteration==0){
			start= System.nanoTime();
			totaliteration=4*(rad/16*rad/16)+(4*rad/16)+27;
			x=-rad;
			z=-rad;
			main.setGenmode(GenerationMode.Generating);
		}
		for(int i=0;i<14;i++){
		if(x>rad){
			x=-rad;
			z=z+16;
			if(z>rad){
				long end = System.nanoTime()-start;
				Bukkit.broadcastMessage("§3Génération terminée §0[§rTemps: "+end/1000000+"ms"+"§0]");
				for(Player player:Bukkit.getOnlinePlayers()) {
					PlayerUtility.sendActionbar(player, "§wProgression: §a"+100 +"% §r["+generateProgressBar(100,40)+"§r]");
				}
				main.setGenmode(GenerationMode.Done);
				cancel();
			}
		}
		iteration++;
		x=x+16;
		final Location loc=new Location(w,x+0,254.0,z+0);
		w.loadChunk(w.getChunkAt(loc));
		}
		if(iteration%10==0){ 
			double percent=Math.round(100*iteration/totaliteration);
			if(percent<100)for(Player player:Bukkit.getOnlinePlayers()) {
                PlayerUtility.sendActionbar(player, "§wProgression: §a"+percent +"% §r["+generateProgressBar(percent,40)+"§r]");
			}
		}
	}
	public static String generateProgressBar(double percent,int precision){
		final ArrayList<String> array= new ArrayList<String>();
		array.add("§a");
		for(int i =0;i<precision+1;i++)array.add("|");
		if(percent>100)percent=100;
		double placingterm=100/((double)(precision));
		int place = (int) (percent/placingterm)+1;
		array.set(place,"§c");
		String str="";
		for(String sl:array){
			str=str+sl;
		}
		return str;
	}
}