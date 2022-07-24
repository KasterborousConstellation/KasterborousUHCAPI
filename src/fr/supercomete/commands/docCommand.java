package fr.supercomete.commands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.supercomete.head.core.Main;
public class docCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;
	public docCommand(Main main) {
		this.main=main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg3) {
		if(sender instanceof Player player){
            if(cmd.getName().equalsIgnoreCase("doc") ||cmd.getName().equalsIgnoreCase("docs") ||cmd.getName().equalsIgnoreCase("liens")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw "+player.getName()+" {\"text\":\"§bGoogle Document Cliquer ici <-----\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://echosia.gitbook.io/echosia/doctor-who-uhc/roles/camp-du-docteur/clara-oswald\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Google Doc\"}]}}");

				return true;
			}
		}
		return false;
	}
}