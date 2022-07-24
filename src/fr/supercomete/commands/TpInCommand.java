package fr.supercomete.commands;

import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpInCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;
	public TpInCommand(Main main) {
		this.main=main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
		    Player player =(Player)sender;
            if(cmd.getName().equalsIgnoreCase("tpin")) {
				if(args.length!=5 && args.length!=4){
				    player.sendMessage(Main.UHCTypo+"Usage: /tpin <DimensionName> <Player> <X> <Y> <Z>");
				    return false;
                }
                final String dimension = args[0];
                if(args.length==4){
                    final double x = Double.parseDouble(args[1]);
                    final double y = Double.parseDouble(args[2]);
                    final double z = Double.parseDouble(args[3]);
                    final World world = Bukkit.getWorld(dimension);
                    final Location location = new Location(world,x,y,z);
                    player.teleport(location);
                }else{
                    final String playername = args[1];
                    if(Bukkit.getPlayer(playername)==null){
                        player.sendMessage(Main.UHCTypo+"§cLe joueur "+playername+" n'est pas connecté.");
                        return false;
                    }
                    final double x = Double.parseDouble(args[2]);
                    final double y = Double.parseDouble(args[3]);
                    final double z = Double.parseDouble(args[4]);
                    final World world = Bukkit.getWorld(dimension);
                    final Location location = new Location(world,x,y,z);
                    Bukkit.getPlayer(playername).teleport(location);
                }
				return true;
			}
		}
		return false;
	}
}