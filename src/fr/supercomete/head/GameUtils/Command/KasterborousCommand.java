package fr.supercomete.head.GameUtils.Command;

import fr.supercomete.head.core.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class KasterborousCommand extends BukkitCommand implements PluginIdentifiableCommand {
    final ArrayList<SubCommand> subCommands= new ArrayList<>();
    public KasterborousCommand(String name) {
        super(name);
        this.addSubCommand(new SubCommand() {
            @Override
            public String subCommand() {
                return "help";
            }

            @Override
            public boolean execute(Player sender, String[] args) {
                sender.sendMessage(Main.UHCTypo+"§6Menu d'aide pour la commande §b/"+getName());
                for (SubCommand subCommand : subCommands) {
                    sender.sendMessage(  "§b/" + getName() + " " + subCommand.subCommand() + " §f" + subCommand.subCommandDescription());
                }
                return true;
            }

            @Override
            public String subCommandDescription() {
                return "Fait apparaitre le menu d'aide.";
            }
        });
    }

    public void addSubCommand(SubCommand...subCommands){
        this.subCommands.addAll(Arrays.asList(subCommands));
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(commandSender instanceof Player){
            final Player player =(Player)commandSender;
            if(strings.length==0){
                return false;
            }
            for(SubCommand command:subCommands){
                if(Objects.equals(strings[0], command.subCommand())){
                    String[] sub= new String[strings.length-1];
                    int e =0;
                    for(String string: strings){
                        if(e!=0){
                            sub[e-1]=string;
                        }
                        e++;
                    }
                    return command.execute(player,sub);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Plugin getPlugin() {
        return Main.INSTANCE;
    }
}
