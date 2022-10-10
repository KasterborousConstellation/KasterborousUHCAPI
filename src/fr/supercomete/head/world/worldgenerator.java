package fr.supercomete.head.world;

import java.util.UUID;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
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
	private static Main main;
	public worldgenerator(Main main) {
		worldgenerator.main = main;
	}
	static UUID worlduuid;
	public static void generateworld() {
        MapHandler.reset();
		worlduuid = UUID.randomUUID();
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(main.spawn);
			player.setGameMode(GameMode.ADVENTURE);
		}
        assert MapHandler.getMap()!=null;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Bukkit.broadcastMessage("[Creation du monde structure]");
				final WorldCreator wc = new WorldCreator("StructureWorld");
		        wc.type(WorldType.FLAT);
		        wc.generatorSettings("2;0;1;"); //This is what makes the world empty (void)
		        MapHandler.getMap().setStructureWorld(wc.createWorld());
                MapHandler.getMap().getStructureWorld().setGameRuleValue("doMobSpawning", "false");
		        if(KtbsAPI.getCurrentGame().getMode().getStructure().size()>0) {
		        	Bukkit.broadcastMessage("§b[Génération des structures]");
		        	int i =0;
		        	for(Structure structure : KtbsAPI.getCurrentGame().getMode().getStructure()) {
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
				final BiomeGenerator biomegen = Main.generator;
				Bukkit.broadcastMessage("[Création du monde]");
				Bukkit.broadcastMessage("  §bTaille: "+Main.currentGame.getFirstBorder());
				final WorldCreator creaWorld = new WorldCreator("/" + worlduuid + "/").seed(WorldSeedGetter.getRandomSeed(biomegen.getBiome()));
				creaWorld.type(WorldType.CUSTOMIZED);
				creaWorld.generatorSettings(biomegen.generateWorldSetting());
				
				MapHandler.getMap().setCurrentWorld(Bukkit.createWorld(creaWorld));
				new worldBorderHandler(Main.currentGame.getFirstBorder(), 0, 0, MapHandler.getMap().getPlayWorld());
				Main.currentGame.setGenmode(GenerationMode.WorldCreatedOnly);
				Bukkit.broadcastMessage("§bGénération du monde terminée");
				
			}
		}.runTaskLater(main, 50L);
	}
	public static void pregen() {
        assert MapHandler.getMap()!=null;
		Bukkit.broadcastMessage("§dPré-génération du monde");
		final generatorcycle cycle = new generatorcycle((int) (Main.currentGame.getFirstBorder() / 2), MapHandler.getMap().getPlayWorld(),main);
		cycle.runTaskTimer(main, 0, 1L);
	}
}