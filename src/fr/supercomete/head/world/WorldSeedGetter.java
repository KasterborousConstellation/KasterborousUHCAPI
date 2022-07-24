package fr.supercomete.head.world;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import fr.supercomete.datamanager.FileManager.Fileutils;
import fr.supercomete.enums.BiomeGeneration;

public class WorldSeedGetter {

	public static long getRandomSeed(BiomeGeneration biome) {
		final File file = new File("/var/games/minecraft/Library/Seeds/",""+biome);
		final String content = Fileutils.loadContent(file);
		final String[] splitted = content.split("L,");
		
		ArrayList<Long> seeds = new ArrayList<Long>();
		
		for(String string:splitted) {
			if(string.isEmpty())continue;
			seeds.add(Long.parseLong(string));
			
		}
		
		return seeds.get(new Random().nextInt(seeds.size()-1));
	}
	public static int getAmountOfSeed(BiomeGeneration biome) {
		final File file = new File("/var/games/minecraft/Library/Seeds/",""+biome);
		final String content = Fileutils.loadContent(file);
		if(content.isEmpty())return 0;
		return content.split("L,").length;
	}
}
