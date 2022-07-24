package fr.supercomete.head.world;
import java.io.File;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import fr.supercomete.head.core.Main;
public class WorldGarbageCollector {
	public static void init(Main main) {
		ArrayList<String> allowed=new ArrayList<String>();
		allowed.add("world");
		allowed.add("world_the_end");
		allowed.add("world_nether");
		new BukkitRunnable() {
			@Override
			public void run() {
				for(World world : Bukkit.getWorlds()) {
                    if(!(allowed.contains(world.getName())|| world == worldgenerator.currentPlayWorld|| world == worldgenerator.structureworld || world.getPlayers().size()!=0)) {
                        unloadWorld(world);
                        deleteWorld(world.getWorldFolder());
                    }
                }
			}			
		}.runTaskTimer(main, 0L, 200L);
	}
	//Taken from https://bukkit.org/threads/unload-delete-copy-worlds.182814/
	public static void unloadWorld(World world) {
	    if(!world.equals(null)) {
	        Bukkit.getServer().unloadWorld(world, true);
	    }
	}
	private static boolean deleteWorld(File path) {
	      if(path.exists()) {
	          File[] files = path.listFiles();
	          for(int i=0; i<files.length; i++) {
	              if(files[i].isDirectory()) {
	                  deleteWorld(files[i]);
	              } else {
	                  files[i].delete();
	              }
	          }
	      }
	      return(path.delete());
	}
}