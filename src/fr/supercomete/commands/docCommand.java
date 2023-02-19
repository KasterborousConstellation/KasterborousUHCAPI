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
		if(sender instanceof Player){
		    Player player =(Player)sender;
            if(cmd.getName().equalsIgnoreCase("doc") ||cmd.getName().equalsIgnoreCase("docs") ||cmd.getName().equalsIgnoreCase("liens")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw "+player.getName()+" {\"text\":\"§1>§aDocument§1<\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\""+Main.INSTANCE.getConfig().getString("serverapi.serverconfig.doc_url")+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Google Doc\"}]}}");

				return true;
			}
		}
		return false;
	}
}