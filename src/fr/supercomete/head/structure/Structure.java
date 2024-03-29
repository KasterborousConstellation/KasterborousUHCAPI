package fr.supercomete.head.structure;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public final class Structure {
	private final String structurename;
	private int x,y,z = 0;
	private final CustomBlock[][][] tridimentionalarray;
	private int XoffSet,YoffSet,ZoffSet=0;
    private String worldname;
	public Structure(String name, CustomBlock[][][] tridimentionalarray) {
		this.structurename=name;
		this.tridimentionalarray=tridimentionalarray;
	}
    public Structure(String name, String WorldName, CustomBlock[][][] tridimentionalarray) {
        this.structurename=name;
        this.tridimentionalarray=tridimentionalarray;
        this.worldname=WorldName;
    }
	@SuppressWarnings("deprecation")
	public void generateStructure(Location location) {
		final World world = location.getWorld();
		for(int x=0;x<tridimentionalarray.length;x++) {
			for(int y=0;y<tridimentionalarray[0].length;y++) {
				for(int z=0;z<tridimentionalarray[0][0].length;z++) {
					final Block block = world.getBlockAt(new Location(world,location.getX()+ x,location.getY()+ y,location.getZ()+ z));
					if(tridimentionalarray[x][y][z]!=null) {
						block.setType(tridimentionalarray[x][y][z].getMaterial());
						block.setData(tridimentionalarray[x][y][z].getData());
					}else {
						block.setType(Material.AIR);
					}
				}
			}
		}
	}
    public CustomBlock[][][] getData(){
        return tridimentionalarray;
    }
	public Location getPositionRelativeToLocation(int[] place) {
		final Location loc = this.getLocation();
		loc.add(((double)place[0])+0.5,(place[1]),((double) place[2])+0.5);
		return loc;
	}
	public String getStructurename() {
		return structurename;
	}
	public Location getLocation() {
        if(MapHandler.getMap()==null){
            return new Location(Bukkit.getWorlds().get(0),0,0,0);
        }
        World world;
        if(worldname==null|| worldname.equals("StructureWorld")){
            world = MapHandler.getMap().getStructureWorld();
        }else if(worldname.equals("PlayWorld")) {
            world =MapHandler.getMap().getPlayWorld();
        }else{
            world = Bukkit.getWorld(worldname);
        }
		return new Location(world, x, y, z);
	}
	public void setLocation(Location location) {
		x=(int)location.getX();
		y=(int)location.getY();
		z=(int)location.getZ();
	}
	public void setSpawnLocation(int x, int y,int z) {
		XoffSet=x;
		YoffSet=y;
		ZoffSet=z;
	}
    public String getWorldName(){
        return worldname;
    }
    public void teleport(Player player) {
		Location location = getLocation();
		location.setX(location.getX()+XoffSet);
		location.setY(location.getY()+YoffSet);
		location.setZ(location.getZ()+ZoffSet);
		player.teleport(location);
	}
}