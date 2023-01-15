package fr.supercomete.head.GameUtils.Command;

import fr.supercomete.head.GameUtils.Scenarios.Compatibility;
import fr.supercomete.head.PlayerUtils.BonusHandler;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
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
        }, new SubCommand() {
            @Override
            public String subCommand() {
                return "effect";
            }

            @Override
            public boolean execute(Player sender, String[] args) {
                if(Main.getPlayerlist().contains(sender.getUniqueId())){
                    sender.sendMessage("§aVoici vos effets en pourcentage en plus de vos effets minecraft:");
                    for(BonusType type : BonusType.values()){
                        if(type==BonusType.Heart||type==BonusType.NoFall){
                            continue;
                        }
                        int bonus= BonusHandler.getTotalOfBonus(sender,type);
                        int role_bonus = 0;
                        if(RoleHandler.IsRoleGenerated()){
                            Role role = RoleHandler.getRoleOf(sender);
                            role_bonus=role.getPowerOfBonus(type);
                        }
                        sender.sendMessage("    "+type.getColor()+type.getName()+"§7: §6"+(bonus+role_bonus)+"%");
                    }
                }
                return true;
            }

            @Override
            public String subCommandDescription() {
                return "Vous obtenez les informations sur vos effets en pourcentage.";
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
