package fr.supercomete.head.world;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;

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
		return sub/total>0.65 && forbidensub/total<0.05;
	}
}