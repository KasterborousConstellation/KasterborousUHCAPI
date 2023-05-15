package fr.supercomete.head.world;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

import fr.supercomete.datamanager.FileManager.Fileutils;
import fr.supercomete.enums.BiomeGeneration;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.supercomete.enums.GenerationMode;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.structure.Structure;
import fr.supercomete.tasks.generatorcycle;

public class worldgenerator {
	static UUID worlduuid;
    public static final File file = new File(Main.INSTANCE.getDataFolder(),"seeds");
    public static void init(){
        try {
            for(BiomeGeneration biomeGeneration:BiomeGeneration.values()){
                String name = ""+biomeGeneration;
                Bukkit.getLogger().log(Level.INFO,"Searching: "+name);
                String collected = new BufferedReader(new InputStreamReader(Main.INSTANCE.getResource(name), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
                File possibleFile = new File(file,biomeGeneration+"");
                if(!possibleFile.exists()){
                    Bukkit.getLogger().log(Level.INFO,"Creating file: Their a missing file seeds/"+biomeGeneration);
                    File created = new File(file,""+biomeGeneration);
                    Fileutils.createFile(created);
                    Bukkit.getLogger().log(Level.INFO,"SUCCESSFULLY CREATED FILE");
                    Fileutils.save(created,collected);
                    Bukkit.getLogger().log(Level.INFO,"SUCCESSFULLY FILLED FILE");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static long getRandomSeed(BiomeGeneration biome) {
        final File f = new File(file,""+biome);
        final String content = Fileutils.loadContent(f);
        final String[] splitted = content.split("L,");
        ArrayList<Long> seeds = new ArrayList<Long>();
        for(String string:splitted) {
            if(string.isEmpty())continue;
            seeds.add(Long.parseLong(string));
        }
        return seeds.get(new Random().nextInt(seeds.size()-1));
    }
    public static int getAmountOfSeed(BiomeGeneration biome) {
        final File f = new File(file,""+biome);
        final String content = Fileutils.loadContent(f);
        if(content.isEmpty())return 0;
        return content.split("L,").length;
    }
    public static double getMaxY(double x, double z,World w) {
        Block block = w.getBlockAt(new Location(w, x,255, z));
        double y =255;
        while(block.getType()== Material.AIR) {
            y--;
            block = w.getBlockAt(new Location(w, x,y, z));
        }
        return y;
    }
    public static void setWorldBorder(double bordersize,double x,double z,World world){
        WorldBorder border = world.getWorldBorder();
        border.setSize(bordersize);
        border.setCenter(x,z);
    }
	public static void generateworld() {
        MapHandler.reset();
		worlduuid = UUID.randomUUID();
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(Main.spawn);
			player.setGameMode(GameMode.ADVENTURE);
		}
        assert MapHandler.getMap()!=null;
        final BiomeGenerator biomegen = Main.generator;
        if(getAmountOfSeed(biomegen.getBiome())==0){
            Bukkit.broadcastMessage(Main.UHCTypo+"Il n'y a pas assez de graines pour générer un monde.");
            return;
        }
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Bukkit.broadcastMessage("[Creation du monde structure]");
				final WorldCreator wc = new WorldCreator("StructureWorld");
		        wc.type(WorldType.FLAT);
		        wc.generatorSettings("2;0;1;"); //This is what makes the world empty (void)
		        MapHandler.getMap().setStructureWorld(wc.createWorld());
                MapHandler.getMap().getStructureWorld().setGameRuleValue("doMobSpawning", "false");
		        if(Main.currentGame.getMode().getStructure().size()>0) {
		        	Bukkit.broadcastMessage("§b[Génération des structures]");
		        	int i =0;
		        	for(Structure structure : Main.currentGame.getMode().getStructure()) {
		        		final Location location = new Location(MapHandler.getMap().getStructureWorld(),i*1000 +10000, 10, i*1000 +10000);
		        		Bukkit.broadcastMessage("   §bGénération: "+structure.getStructurename());
		        		structure.generateStructure(location);
		        		Main.currentGame.getMode().setStructureLocation(location, structure.getStructurename());
		        		i++;
		        	}
		        }
		        for(Entity entity : MapHandler.getMap().getStructureWorld().getEntities()) {
		        	if(entity instanceof Item) {
		        		entity.remove();
		        	}
		        }
				Bukkit.broadcastMessage("[Création du monde]");
				Bukkit.broadcastMessage("  §bTaille: "+Main.currentGame.getFirstBorder());
				final WorldCreator creaWorld = new WorldCreator("/" + worlduuid + "/").seed(getRandomSeed(biomegen.getBiome()));
				creaWorld.type(WorldType.CUSTOMIZED);
				creaWorld.generatorSettings(biomegen.generateWorldSetting());
				
				MapHandler.getMap().setCurrentWorld(Bukkit.createWorld(creaWorld));
				worldgenerator.setWorldBorder(Main.currentGame.getFirstBorder(), 0, 0, MapHandler.getMap().getPlayWorld());
				Main.currentGame.setGenmode(GenerationMode.WorldCreatedOnly);
				Bukkit.broadcastMessage("§bGénération du monde terminée");
				
			}
		}.runTaskLater(Main.INSTANCE, 10L);
	}
	public static void pregen() {
        assert MapHandler.getMap()!=null;
		Bukkit.broadcastMessage("§dPré-génération du monde");
		final generatorcycle cycle = new generatorcycle((int) (Main.currentGame.getFirstBorder() / 2), MapHandler.getMap().getPlayWorld(),Main.INSTANCE);
		cycle.runTaskTimer(Main.INSTANCE, 0, 1L);
	}
}