package fr.supercomete.head.GameUtils.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class GenericCommandTabCompleter implements TabCompleter {
    private final List<String> subcommand;
    private final String command;
    public GenericCommandTabCompleter(String cmd, List<String> subcommand){
        this.subcommand=subcommand;
        command =cmd;
    }
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(s.equalsIgnoreCase(this.command)){
            return subcommand;
        }
        return null;
    }
}
