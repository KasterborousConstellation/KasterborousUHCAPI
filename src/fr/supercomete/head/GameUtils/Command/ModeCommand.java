package fr.supercomete.head.GameUtils.Command;

import fr.supercomete.head.GameUtils.Scenarios.Compatibility;
import fr.supercomete.head.core.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ModeCommand extends KasterborousCommand{
    private final Compatibility compatibility;
    public ModeCommand(String name,Compatibility compatibility) {
        super(name);
        this.compatibility = compatibility;
        this.addSubCommand(new SubCommand() {
            @Override
            public String subCommand() {
                return "liste";
            }

            @Override
            public boolean execute(Player sender, String[] args) {
                sender.performCommand("rolelist");
                return false;
            }

            @Override
            public String subCommandDescription() {
                return "Menu d'administrateur qui permet de voir les roles de tout les joueurs";
            }
        });
    }
    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(commandSender instanceof Player){
            final Player player =(Player)commandSender;
            if(strings.length==0){
                return false;
            }
            if(!compatibility.IsCompatible(Main.currentGame.getMode())){
                player.sendMessage("§cCette commande n'est pas faite pour ce mode de jeu");
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
}
