package fr.supercomete.head.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

public class WorldVerifier {
	static Biome[] biomed = new Biome[] {Biome.OCEAN,Biome.DEEP_OCEAN,Biome.FROZEN_OCEAN};
	public static boolean verifie(World world , Biome biome) {
		double total = 0;
		double sub = 0;
		double forbidensub=0;
		for(int x=-1000;x<1000;x++) {
			for(int z=-1000;z<1000;z++) {
				total++;
				for(Biome b : biomed) {
					if(world.getBiome(x, z).equals(b))forbidensub++;
				}
				if(world.getBiome(x, z).equals(biome)) {
					sub++;
				}	
			}	
		}
		Bukkit.broadcastMessage("§b[§aWorldGenerator§b] §aStabilité de la carte: "+((sub/total>0.65)?"§a":"§c")+((sub/total)*100.0)+"%");
		Bukkit.broadcastMessage("§b[§aWorldGenerator§b] §aPourcentage de mauvais biome de la carte: "+((forbidensub/total<0.05)?"§a":"§c")+((forbidensub/total)*100.0)+"%");
		//Spawn Quality
        double s_bad=0;
        double s_total=0;
        for(int x =-150;x<150;x++){
            for(int z =-150;z<150;z++){
                double y = worldgenerator.getMaxY(x,z,world);
                Block block = world.getBlockAt(new Location(world,x,y,z));
                Block block1=world.getBlockAt(new Location(world,x,y-1,z));
                if(block.getType().equals(Material.WATER)&&block1.getType().equals(Material.WATER)){
                    s_bad++;
                }
                s_total++;
            }
        }
        Bukkit.broadcastMessage("§b[§aWorldGenerator§b] §aStabilité du spawn: "+((s_bad/s_total<0.05)?"§a":"§c")+(100-(s_bad/s_total)*100.0)+"%");
        return sub/total>0.65 && forbidensub/total<0.05 && s_bad/s_total<0.05;
	}
}