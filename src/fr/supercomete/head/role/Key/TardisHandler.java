package fr.supercomete.head.role.Key;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.world.worldBorderHandler;
import fr.supercomete.head.world.worldgenerator;
import fr.supercomete.tasks.particles.TardisParticle;
public final class TardisHandler {
	private static Main main;
	public static Tardis currentTardis;
	public TardisHandler(Main main) {
		TardisHandler.main=main;
	}
	public static Location TardisLocation;
	public static boolean IsTardisGenerated=false;
	public static void generateTardisLocation() {
		int x =  (new Random().nextInt((int) Main.currentGame.getCurrentBorder())-((int)( Main.currentGame.getCurrentBorder()/2)));
		int z =  (new Random().nextInt((int) Main.currentGame.getCurrentBorder())-((int)( Main.currentGame.getCurrentBorder()/2)));
		TardisHandler.TardisLocation=new Location(worldgenerator.currentPlayWorld, x,worldBorderHandler.getMaxY(x, z, worldgenerator.currentPlayWorld)+10.0, z);
	}
	public static void placeTardis() {
		World w = worldgenerator.currentPlayWorld;
		//Destruction de l'ancien tardis
		if(TardisLocation!=null){
			Location a = TardisLocation;
			a.setY(a.getY()-1);
			Block bl =w.getBlockAt(a);
			bl.setType(Material.AIR);
			a.setY(a.getY()+1);
			bl = w.getBlockAt(a);
			bl.setType(Material.AIR);
		}
		generateTardisLocation();
		Location a = TardisLocation;
		a.setY(a.getY()-1);
		Block bl =w.getBlockAt(a);
		bl.setType(Material.LAPIS_BLOCK);
		a.setY(a.getY()+1);
		bl = w.getBlockAt(a);
		bl.setType(Material.LAPIS_BLOCK);
		Location tardisparticle = a.clone();
		tardisparticle.setY(tardisparticle.getY()-1);
		tardisparticle.setX(tardisparticle.getX()+0.5);
		tardisparticle.setZ(tardisparticle.getZ()+0.5);
		TardisParticle particle = new TardisParticle(tardisparticle, Main.currentGame.getTimer(Timer.TardisDelay).getData());
		particle.runTaskTimer(main, 0, 20L);
	}
}