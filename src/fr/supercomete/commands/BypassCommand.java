package fr.supercomete.commands;
import fr.supercomete.head.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class BypassCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;
	public BypassCommand(Main main) {
		this.main=main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg3) {
		if(sender instanceof Player ){
		    Player player=(Player)sender;
            if(cmd.getName().equalsIgnoreCase("bypass")) {
				Main.updateBypass();
                if(Main.IsHost(player)||Main.IsCohost(player)) {
                    if(Main.bypass.contains(player.getUniqueId())){
                        Main.bypass.remove(player.getUniqueId());
                        player.sendMessage(Main.UHCTypo+"§fBypass: "+Main.TranslateBoolean(false));
                    }else{
                        Main.bypass.add(player.getUniqueId());
                        player.sendMessage(Main.UHCTypo+"§fBypass: "+Main.TranslateBoolean(true));
                    }

                    return true;
                }
			}
		}
		return false;
	}
}