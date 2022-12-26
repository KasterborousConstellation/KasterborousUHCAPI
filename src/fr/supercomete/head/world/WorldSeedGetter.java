package fr.supercomete.head.world;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import fr.supercomete.datamanager.FileManager.Fileutils;
import fr.supercomete.enums.BiomeGeneration;
import fr.supercomete.head.Exception.KTBSOfflineFailure;
import fr.supercomete.head.core.Main;

public class WorldSeedGetter {
	public static long getRandomSeed(BiomeGeneration biome) {
        if(Main.KTBSNetwork_Connected){
            final File file = new File("/var/games/minecraft/Library/Seeds/",""+biome);
            final String content = Fileutils.loadContent(file);
            final String[] splitted = content.split("L,");
            ArrayList<Long> seeds = new ArrayList<Long>();
            for(String string:splitted) {
                if(string.isEmpty())continue;
                seeds.add(Long.parseLong(string));
            }
            return seeds.get(new Random().nextInt(seeds.size()-1));
        }else {
            final File MainFile = Main.INSTANCE.getDataFolder().getParentFile().getParentFile();
            final File seedFile = new File(MainFile,"seeds");
            if(!seedFile.exists() ||!seedFile.isDirectory()){
                try {
                    throw new KTBSOfflineFailure("THIS IS A KTBS OFFLINE FAULT. THE SEED DIRECTORY IS NECESSARY IN OFFLINE MODE. PLEASE CONSIDER ADDING IT.");
                }catch (KTBSOfflineFailure e){
                    e.printStackTrace();
                    return 0;
                }
            }else{
                for(BiomeGeneration b : BiomeGeneration.values()){
                    final String name = b+"";
                    final File biomeFile = new File(seedFile,name);
                    if(b==biome){
                        if(!biomeFile.exists()){
                            try{
                                throw new KTBSOfflineFailure("THIS IS A KTBS OFFLINE FAULT. THE SEED DIRECTORY IS MISSING THE "+name+" BIOME FILE.");
                            }catch (KTBSOfflineFailure e){
                                e.printStackTrace();
                                return 0;
                            }
                        }
                        final String content = Fileutils.loadContent(biomeFile);
                        final String[] splitted = content.split("L,");
                        ArrayList<Long> seeds = new ArrayList<Long>();
                        for(String string:splitted) {
                            if(string.isEmpty())continue;
                            seeds.add(Long.parseLong(string));
                        }
                        return seeds.get(new Random().nextInt(seeds.size()-1));
                    }
                }
                return 0;
            }
        }
	}
	public static int getAmountOfSeed(BiomeGeneration biome) {
		if(Main.KTBSNetwork_Connected){
            final File file = new File("/var/games/minecraft/Library/Seeds/",""+biome);
            final String content = Fileutils.loadContent(file);
            if(content.isEmpty())return 0;
            return content.split("L,").length;
        }else{
            final File MainFile = Main.INSTANCE.getDataFolder().getParentFile().getParentFile();
            final File seedFile = new File(MainFile,"Seeds");
            if(!seedFile.exists() ||!seedFile.isDirectory()){
                try {
                    throw new KTBSOfflineFailure("THIS IS A KTBS OFFLINE FAULT. THE SEED DIRECTORY IS NECESSARY IN OFFLINE MODE. PLEASE CONSIDER ADDING IT.");
                }catch (KTBSOfflineFailure e){
                    e.printStackTrace();
                    return 0;
                }
            }else{
                for(BiomeGeneration b : BiomeGeneration.values()) {
                    final String name = b+"";
                    final File biomeFile = new File(seedFile, name);
                    if (b == biome) {
                        if (!biomeFile.exists()) {
                            try {
                                throw new KTBSOfflineFailure("THIS IS A KTBS OFFLINE FAULT. THE SEED DIRECTORY IS MISSING THE " + name + " BIOME FILE.");
                            } catch (KTBSOfflineFailure e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                        final String content = Fileutils.loadContent(biomeFile);
                        final String[] splitted = content.split("L,");
                        return splitted.length;
                    }
                }
                return 0;
            }
        }
	}
}