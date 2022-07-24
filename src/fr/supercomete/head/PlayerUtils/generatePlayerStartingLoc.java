package fr.supercomete.head.PlayerUtils;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import fr.supercomete.head.core.Main;
public class generatePlayerStartingLoc {
	@SuppressWarnings("unused")
	private final Main main;
	public generatePlayerStartingLoc(Main main) {
		this.main =main;
	}
	public static ArrayList<Location> generateLocation(double x, double z,int nplayer, int rad,World w){
		double xp;
		double zp;
		ArrayList<Location> locationlist = new ArrayList<Location>();
		for(int i =0;i<nplayer;i++){
			xp = Math.round(x + rad * Math.cos(2 * Math.PI * i / nplayer)); 
			zp = Math.round(z + rad * Math.sin(2 * Math.PI * i / nplayer));
			Location l = new Location(w, xp, 150, zp);
			locationlist.add(l);
		}
		for(Location loc:locationlist){
			double locx=loc.getX();
			double locz=loc.getZ();
			for(int xl=-2;xl<3;xl++){
				for(int yl=-2;yl<3;yl++){
					for(int zl=-2;zl<3;zl++){
						w.getBlockAt(new Location(w, xl+locx, 145+yl, zl+locz)).setType(Material.BARRIER);
					}
				}
			}
			for(int xl=-1;xl<2;xl++){
				for(int yl=-1;yl<3;yl++){
					for(int zl=-1;zl<2;zl++){
						w.getBlockAt(new Location(w, xl+locx, 145+yl, zl+locz)).setType(Material.AIR);
					}
				}
			}
		}
		return locationlist;
	}
	
}
