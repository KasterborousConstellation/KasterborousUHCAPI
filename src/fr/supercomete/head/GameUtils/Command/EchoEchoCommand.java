package fr.supercomete.head.GameUtils.Command;

import fr.supercomete.head.GameUtils.GameMode.Modes.EchoEchoUHC;
import fr.supercomete.head.GameUtils.Scenarios.Compatibility;
import fr.supercomete.head.GameUtils.Scenarios.CompatibilityType;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.EchoRole;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.content.EchoEchoUHC.Lois;
import fr.supercomete.head.role.content.EchoEchoUHC.Neo;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.swing.*;
import java.util.Random;


public class EchoEchoCommand extends ModeCommand {
    private Main main;
    final Random random= new Random();
    public EchoEchoCommand(String name) {
        super(name,new Compatibility(CompatibilityType.WhiteList, new Class[]{EchoEchoUHC.class}));
        this.addSubCommand(new SubCommand() {
            @Override
            public String subCommand() {
                return "nofall";
            }

            @Override
            public boolean execute(Player sender, String[] args) {
                if (RoleHandler.IsRoleGenerated()) {
                    final Role role = RoleHandler.getRoleOf(sender);
                    if (role instanceof Lois) {
                        final Lois lois = (Lois) role;
                        lois.setChute(!lois.IsChute());
                        if (lois.IsChute()) {
                            sender.sendMessage(Main.UHCTypo + "Vous avez activé vos dégats de chute");
                        } else {
                            sender.sendMessage(Main.UHCTypo + "Vous avez desactivé vos dégats de chute");
                        }
                    }
                }
                return true;
            }

            @Override
            public String subCommandDescription() {
                return "Utilisable uniquement par Loïs";
            }
        }, new SubCommand() {
            @Override
            public String subCommand() {
                return "manga";
            }

            @Override
            public boolean execute(Player sender, String[] args) {
                if (RoleHandler.IsRoleGenerated()) {
                    Role role = RoleHandler.getRoleOf(sender);
                    if (role instanceof Neo) {
                        final Neo neo = (Neo) role;
                        neo.gui.open();
                    }
                }
                return true;
            }

            @Override
            public String subCommandDescription() {
                return "Utilisable uniquement par Néo";
            }
        }, new SubCommand() {
            @Override
            public String subCommand() {
                return "more";
            }

            @Override
            public boolean execute(Player sender, String[] args) {
                if (RoleHandler.IsRoleGenerated()) {
                    final Role role = RoleHandler.getRoleOf(sender);
                    if (role instanceof Neo) {
                        sender.sendMessage("§6Annexe:");
                        for (Role them : RoleHandler.getRoleList().values()) {
                            if (them instanceof EchoRole) {
                                final EchoRole them_echo = (EchoRole) them;
                                sender.sendMessage(them_echo.getCamp().getColor() + them_echo.getName() + ": " + (them_echo.getMangaProbability() * 100) + "%");
                            }
                        }
                    }
                }
                return true;
            }

            @Override
            public String subCommandDescription() {
                return "Utilisable uniquement par Néo";
            }
        }, new SubCommand() {
            @Override
            public String subCommand() {
                return "fouille";
            }

            @Override
            public boolean execute(Player sender, String[] args) {
                if(args.length!=1){
                    sender.sendMessage("§cUsage: /echo fouille <Joueur>");
                    return false;
                }
                if(!(RoleHandler.getRoleOf(sender)instanceof Neo)){
                    return false;
                }
                final Neo neo =(Neo)RoleHandler.getRoleOf(sender);
                Neo.Manga manga = neo.ChoseManga();
                if(manga==null){
                    sender.sendMessage(Main.UHCTypo+"§cVous avez déjà tout les mangas");
                    return false;
                }
                if(!neo.coolDown.isAbleToUse()){
                    sender.sendMessage(Main.UHCTypo+"§cVous devez encore attendre "+ TimeUtility.transform(neo.coolDown.getRemainingTime(),"§c")+"§c avant de pouvoir réutiliser cette commande.");
                    return false;
                }
                final String arg = args[0];
                final Player player = Bukkit.getPlayer(arg);
                if(player!=null&&RoleHandler.isOnlineAndHaveRole(player.getUniqueId())){
                    if(sender.getWorld()!=player.getWorld()||player.getLocation().distance(sender.getLocation())>10){
                        sender.sendMessage(Main.UHCTypo+"§cVous êtes trop loin du joueur ciblé.");
                        return false;
                    }
                    if(player.getUniqueId()==sender.getUniqueId()){
                        sender.sendMessage(Main.UHCTypo+"§cVous ne pouvez pas utilisé cette commande sur vous-même.");
                        return false;
                    }
                    final float fl = random.nextFloat();
                    final EchoRole role = (EchoRole) RoleHandler.getRoleOf(player);
                    neo.coolDown.setUseNow();
                    if(fl<= role.getMangaProbability()){
                        neo.addManga(manga);
                        sender.sendMessage(Main.UHCTypo+"§6Vous avez obtenu le manga: "+manga.getName());
                    }else{
                        sender.sendMessage(Main.UHCTypo+"§6La fouille a échoué");
                    }
                }
                return true;
            }

            @Override
            public String subCommandDescription() {
                return "Utilisable uniquement par Néo";
            }
        });
    }
}
