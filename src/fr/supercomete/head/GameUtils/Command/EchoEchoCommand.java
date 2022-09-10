package fr.supercomete.head.GameUtils.Command;

import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.content.EchoEchoUHC.Lois;
import org.bukkit.entity.Player;


public class EchoEchoCommand extends KasterborousCommand {
    private Main main;
    public EchoEchoCommand(String name) {
        super(name);
        this.addSubCommand(new SubCommand() {
            @Override
            public String subCommand() {
                return "nofall";
            }

            @Override
            public boolean execute(Player sender, String[] args) {
                sender.sendMessage(sender.getName());
                if(RoleHandler.isIsRoleGenerated()){
                    final Role role= RoleHandler.getRoleOf(sender);
                    if(role instanceof Lois){
                        final Lois lois = (Lois) role;
                        lois.setChute(!lois.IsChute());
                        if(lois.IsChute()){
                            sender.sendMessage(Main.UHCTypo+"Vous avez activé vos dégats de chute");
                        }else{
                            sender.sendMessage(Main.UHCTypo+"Vous avez desactivé vos dégats de chute");
                        }
                    }
                }
                return true;
            }

            @Override
            public String subCommandDescription() {
                return "Utilisable uniquement par Loïs";
            }
        });
    }
}
