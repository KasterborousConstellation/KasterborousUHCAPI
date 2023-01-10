package fr.supercomete.commands;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.structure.CustomBlock;
import fr.supercomete.head.structure.Structure;
public class createStructureCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;
	public createStructureCommand(Main main) {
		this.main=main;
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
            Player player=(Player)sender;
            if(cmd.getName().equalsIgnoreCase("createStructure")) {
				if(args.length == 10) {
					double x1,x2,y1,y2,z1,z2,xo,yo,zo=0;
					x1=Double.parseDouble(args[0]);
					x2=Double.parseDouble(args[3]);
					y1=Double.parseDouble(args[1]);
					y2=Double.parseDouble(args[4]);
					z1=Double.parseDouble(args[2]);
					z2=Double.parseDouble(args[5]);
					xo=Double.parseDouble(args[7]);
					yo=Double.parseDouble(args[8]);
					zo=Double.parseDouble(args[9]);
					if(x1>x2) {
						double tmp= x1;
						x1=x2;
						x2=tmp;
					}
					if(y1>y2) {
						double tmp= y1;
						y1=y2;
						y2=tmp;
					}
					if(z1>z2) {
						double tmp= z1; 
						z1=z2;
						z2=tmp;
					}
					CustomBlock[][][] array= new CustomBlock[(int) (x2-x1)][(int) (y2-y1)][(int) (z2-z1)];
					int size=0;
					for(double x = 0;x<(x2-x1);x++) {
						for(double y = 0;y<(y2-y1);y++) {
							for(double z = 0;z<(z2-z1);z++) {
								Block custom = player.getWorld().getBlockAt(new Location(player.getWorld(), x1+x, y1+y, z1+z));
								array[(int)(x)][(int)(y)][(int)(z)] = (custom.getType()==Material.AIR)?null:new CustomBlock(custom.getType(), custom.getData());
								size++;
							}
						}
					}
					System.out.println(size);
					final Structure structure = new Structure(args[6], array);
					structure.setSpawnLocation((int)xo, (int)yo, (int)zo);
					Main.structurehandler.write(structure);
				}
				return true;
			}
		}
		return false;
	}
}