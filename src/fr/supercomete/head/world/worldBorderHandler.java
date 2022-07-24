package fr.supercomete.head.world;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
public class worldBorderHandler {
	public worldBorderHandler(double bordersize,double x,double z,World world){
		WorldBorder border = world.getWorldBorder();
		border.setSize(bordersize);
		border.setCenter(x,z);
	}
	public static double getMaxY(double x, double z,World w) {
		Block block = w.getBlockAt(new Location(w, x,255, z));
		double y =255;
		while(block.getType()==Material.AIR) {
			y--;
			block = w.getBlockAt(new Location(w, x,y, z));
		}
		return y;
	}
}