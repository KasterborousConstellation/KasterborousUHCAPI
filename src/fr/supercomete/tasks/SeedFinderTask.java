package fr.supercomete.tasks;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.supercomete.datamanager.FileManager.Fileutils;
import fr.supercomete.enums.BiomeGeneration;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.world.BiomeGenerator;
import fr.supercomete.head.world.WorldVerifier;

public class SeedFinderTask extends BukkitRunnable {
	@SuppressWarnings("unused")
	private final Main main;
	File file;
	public SeedFinderTask(Main main) {
		this.main=main;
		file = new File("/var/games/minecraft/Library/Seeds/",search+"");
	}
	World currentPlayWorld;
	
	BiomeGeneration search = Main.generator.getBiome();
	@Override
	public void run() {
		Bukkit.broadcastMessage("§b[§aWorldGenerator§b]§a Début d'une nouvelle recherche pour le biome: §b"+search);
		
		BiomeGenerator biomegen = new BiomeGenerator();
		biomegen.setBiome(search);
		//.seed(seedlist[new Random().nextInt((seedlist.length-1))]);
		WorldCreator creaWorld =new WorldCreator("/"+new Random().nextInt(1000)+"/");
		creaWorld.type(WorldType.CUSTOMIZED);
		creaWorld.generatorSettings(biomegen.generateWorldSetting());
		currentPlayWorld=Bukkit.createWorld(creaWorld);
		if(WorldVerifier.verifie(currentPlayWorld, search.getTargetBiome())) {
			if(!file.exists()) {
				try {
					Fileutils.createFile(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String content = Fileutils.loadContent(file);
				content+="\n" + currentPlayWorld.getSeed();
				Fileutils.save(file, content);
			}else{
				String content = Fileutils.loadContent(file);
				content+=(""+(currentPlayWorld.getSeed()))+"L,";
				Fileutils.save(file, content);
			}
		}else {
			Bukkit.broadcastMessage("§b[§aWorldGenerator§b]§c Cette Seed n'est pas conforme");
		}
	}
}