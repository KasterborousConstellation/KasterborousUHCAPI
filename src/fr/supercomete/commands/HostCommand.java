package fr.supercomete.commands;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.permissions.PermissionManager;
import fr.supercomete.head.permissions.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.Groupable;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.world.scoreboardmanager;

import java.util.UUID;

public class HostCommand implements CommandExecutor {
	private final Main main;
	public HostCommand(Main main) {
		this.main=main;
	}
    final KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(sender instanceof Player) {
			final Player player = (Player)sender;
			if(cmd.getName().equalsIgnoreCase("h")){
                if(args.length==0) {
					player.sendMessage(Main.UHCTypo+"Commande inconnue /h help pour plus d'information");
					return false;
				}
				if(!Main.IsHost(player) && !Main.IsCohost(player)) {
                    PermissionManager.sendDenyPermission(player);
					return false;
				}
				switch(args[0]) {
				    case "help":
					    player.sendMessage(Main.UHCTypo+"§6Menu d'aide");
					    player.sendMessage("§7/h fh \n§8 -Force un Final Heal sur tout les joueurs.");
					    player.sendMessage("§7/h help \n§8 -Ouvre le menu d'aide.");
					    player.sendMessage("§7/h forcepvp \n§8 -Force l'arrivée du PVP pendant une partie.");
					    player.sendMessage("§7/h forcebordure \n§8 -Force le début du mouvement de la bordure pendant une partie.");
					    player.sendMessage("§7/h forcerole \n§8 -Force l'annonce des rôles d'une partie.");
					    player.sendMessage("§7/h setgamename \n§8 -Défini le nom de la partie.");
					    player.sendMessage("§7/h whadd \n§8 -Ajoute un joueur a la whitelist.");
					    player.sendMessage("§7/h setgroupe \n§8 -Défini la taille des groupes.");
					    player.sendMessage("§7/h groupe \n§8 -Fait un rappel la taille des groupes.");
					    player.sendMessage("§7/h koff <Joueur> \n§8 -Permet le kill d'un joueur hors-ligne.");
					    player.sendMessage("§7/h addcohost <Joueur>\n§8 -Ajoute un cohost.");
					    player.sendMessage("§7/h removecohost <Joueur>\n§8 -Enlève un cohost.");
					    player.sendMessage("§7/h sethost <Joueur> \n§8 -Permet de changer l'host de la partie");
					    player.sendMessage("§7/h say <Args> \n§8 -Permet de faire une annonce");
                        player.sendMessage("§7/h showbypass \n§8 -Permet de voir tout les joueurs bypass");
				    	break;
                    case "showbypass":
                        player.sendMessage(Main.UHCTypo+"§aListe des bypass");
                        for(UUID uu : Main.bypass){
                            if(Bukkit.getPlayer(uu)!=null){
                                player.sendMessage("    "+Bukkit.getPlayer(uu).getName()+" "+Main.TranslateBoolean(true));
                            }
                        }
                        break;
				case "say":
                    if(!api.getPermissionProvider().IsAllowed(player, Permissions.Allow_hsay)){
                        PermissionManager.sendDenyPermission(player);
                        return true;
                    }
					if(args.length>=2) {
						String str = "";
						for(int i =1;i<args.length;i++) {
							str+=args[i]+" ";
						}
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§c"+player.getName()+"§7 » §f"+str);
						Bukkit.broadcastMessage("");
					}else player.sendMessage(Main.UHCTypo+"§cUsage: /h say <Args>");
					
					break;
				case "removecohost":
					if(Main.IsHost(player)) {
						if(args.length == 2) {
							final Player target= Bukkit.getPlayer(args[1]);
							if(target==null) {
								player.sendMessage(Main.UHCTypo+"§c Ce joueur n'est pas connecté");
								return false;
							}
							if(Main.cohost.contains(target.getUniqueId())) {
								Main.cohost.remove(target.getUniqueId());
                                Main.updateBypass();
							}else {
								player.sendMessage(Main.UHCTypo+"§cCe joueur n'est pas cohost.");
							}
						}else {
							player.sendMessage(Main.UHCTypo+"§cUsage: /h addcoshost <Joueur>");
						}
					}
					break;
				case "addcohost":
					if(Main.IsHost(player)) {
						if(args.length == 2) {
							final Player target= Bukkit.getPlayer(args[1]);
							if(target==null) {
								player.sendMessage(Main.UHCTypo+"§c Ce joueur n'est pas connecté");
								return false;
							}
							if(target.getUniqueId()==Main.host) {
								player.sendMessage(Main.UHCTypo+"§cVous ne pouvez pas mettre cohost l'host de la partie");
								return false;
							}
							if(Main.cohost.size()<4) {
								Main.cohost.add(target.getUniqueId());
                                PermissionManager.getPerms().put(target.getUniqueId(),PermissionManager.cohost_perms);
                                Main.updateBypass();
							}else {
								player.sendMessage(Main.UHCTypo+"§cIl y a trop de cohost dans la partie.");
							}
						}else {
							player.sendMessage(Main.UHCTypo+"§cUsage: /h addcoshost <Joueur>");
						}
					}else {
						player.sendMessage(Main.UHCTypo+"§cVous n'avez pas le droit d'utiliser cette commande.");
					}
					break;
				case "sethost":
					if(Main.IsHost(player)||(player.isOp()&&Main.INSTANCE.getConfig().getBoolean("serverapi.serverconfig.op_host_bypass"))) {
						if(args.length == 2) {
							//Deop Old host and remove perms
							if(Main.host!=null) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/deop "+ PlayerUtility.getNameByUUID(Main.host));
							    PermissionManager.getPerms().remove(Main.host);
                            }
							final Player target= Bukkit.getPlayer(args[1]);
							if(target==null) {
								player.sendMessage(Main.UHCTypo+"§c Ce joueur n'est pas connecté");
								return false;
							}
							target.setOp(true);
							Main.cohost.remove(target.getUniqueId());
                            PermissionManager.getPerms().remove(target.getUniqueId());
							Main.setHost(target);
						    Main.updateBypass();
						}else {
							player.sendMessage(Main.UHCTypo+"§cUsage: /h sethost <Joueur>");
						}
					}else {
						player.sendMessage(Main.UHCTypo+"§cVous n'avez pas le droit d'utiliser cette commande.");
					}
					break;
				case "koff":
                    if(!api.getPermissionProvider().IsAllowed(player, Permissions.Allow_koff)){
                        PermissionManager.sendDenyPermission(player);
                        return true;
                    }
					if(args.length!=2) {
						player.sendMessage(Main.UHCTypo+"Usage: /h koff <Joueur>");
						return false;
					}
					final String pselec = args[1];
					for(Offline_Player selected : Main.currentGame.getOfflinelist()) {
						if(selected.getUsername().equalsIgnoreCase(pselec)) {
							Main.currentGame.getMode().DecoKillMethod(selected);
                            Main.currentGame.getMode().ModeDefaultOnDeath(selected,player.getLocation());
							return true;
						}
					}
					player.sendMessage(Main.UHCTypo+"§cCe joueur ne fait pas partie des joueurs hors-ligne");
					return false;
				case "groupe":
                    if(!api.getPermissionProvider().IsAllowed(player, Permissions.Allow_groups_message)){
                        PermissionManager.sendDenyPermission(player);
                        return true;
                    }
					if(Main.currentGame.getMode()instanceof Groupable) {
						scoreboardmanager.titlemessage("§f►Groupe de §c"+Main.currentGame.getGroupe()+"§f◄");
						Bukkit.broadcastMessage("§f►Groupe de §c"+Main.currentGame.getGroupe()+"§f◄");
					}else player.sendMessage(Main.UHCTypo+"§cCe mode ne permet pas de modifier les groupes.");
					break;
				case "setgroupe":
                    if(!api.getPermissionProvider().IsAllowed(player, Permissions.Allow_setgroup)){
                        PermissionManager.sendDenyPermission(player);
                        return true;
                    }
					if(Main.currentGame.getMode()instanceof Groupable) {
						if(args.length!=2){
							player.sendMessage(Main.UHCTypo+"Usage: /h setgroupe <Taille>");
							return false;
						}
						if(args[1].isEmpty()){
							player.sendMessage(Main.UHCTypo+"Usage: /h setgroupe <Taille>");
							return false;
						}
						Main.currentGame.setGroupe(Integer.parseInt(args[1]));
						scoreboardmanager.titlemessage("§f►Groupe de §c"+Main.currentGame.getGroupe()+"§f◄");
						Bukkit.broadcastMessage("§f►Groupe de §c"+Main.currentGame.getGroupe()+"§f◄");
					}else player.sendMessage(Main.UHCTypo+"§cCe mode ne permet pas de modifier les groupes.");
					break;
				case "setgamename":
                    if(!api.getPermissionProvider().IsAllowed(player, Permissions.Allow_change_game_name)){
                        PermissionManager.sendDenyPermission(player);
                        return true;
                    }
					if(args.length<2){
						player.sendMessage(Main.UHCTypo+"Usage: /h setgamename <Nom de de la partie> \n");
						return false;
					}
					if(args[1].isEmpty()){
						player.sendMessage(Main.UHCTypo+ "Usage: /h setgamename <Nom de de la partie> \n");
						return false;
					}
                    final StringBuilder a = new StringBuilder(args[1]);
                    final int e = args.length-2;
                    for(int i =0;i<e;i++){
                        a.append(" ").append(args[i+2]);
                    }
                    if(a.length()>20){
                        player.sendMessage(Main.UHCTypo+"§cLe nom de de la partie est trop long (Taille Maximale: 20 caractères)");
                        return false;
                    }
					Main.currentGame.setGameName(a.toString());
					player.sendMessage(Main.UHCTypo+ "Le nom de la partie a bien été modifié");
					break;
				case "forcerole":
                    if(!api.getPermissionProvider().IsAllowed(player, Permissions.Allow_force)){
                        PermissionManager.sendDenyPermission(player);
                        return true;
                    }
					if(Main.currentGame.isGameState(Gstate.Waiting)||Main.currentGame.isGameState(Gstate.Starting)||Main.currentGame.isGameState(Gstate.Playing)) {
						player.sendMessage(Main.UHCTypo+"§cLa partie n'a pas commencé");
						return false;
					}else {
						main.setForcerole(true);
					}
					break;
				case "forcepvp":
                    if(!api.getPermissionProvider().IsAllowed(player, Permissions.Allow_force)){
                        PermissionManager.sendDenyPermission(player);
                        return true;
                    }
					if(Main.currentGame.isGameState(Gstate.Waiting)||Main.currentGame.isGameState(Gstate.Starting)||Main.currentGame.isGameState(Gstate.Playing)) {
						player.sendMessage(Main.UHCTypo+"§cLa partie n'a pas commencé");
						return false;
					}else {
						main.setForcedpvp(true);
					}
					break;
				case "forcebordure":
                    if(!api.getPermissionProvider().IsAllowed(player, Permissions.Allow_force)){
                        PermissionManager.sendDenyPermission(player);
                        return true;
                    }
					if(Main.currentGame.isGameState(Gstate.Waiting)||Main.currentGame.isGameState(Gstate.Starting)||Main.currentGame.isGameState(Gstate.Playing)) {
						player.sendMessage(Main.UHCTypo+"§cLa partie n'a pas commencé");
						return false;
					}else{
						main.setForcebordure(true);
					}
					break;
				case "fh":
                    if(!api.getPermissionProvider().IsAllowed(player, Permissions.Allow_finalheal)){
                        PermissionManager.sendDenyPermission(player);
                        return true;
                    }
					Main.finalheal();
					break;
				case "whadd":
                    if(!api.getPermissionProvider().IsAllowed(player, Permissions.Allow_add_whitelist)){
                        PermissionManager.sendDenyPermission(player);
                        return true;
                    }
					if(args.length==1){
						player.sendMessage(Main.UHCTypo+"Usage: /h whadd <Joueur>");
						return false;
					}
					if(args[1].isEmpty()){
						player.sendMessage(Main.UHCTypo+"Usage: /h whadd <Joueur>");
						return false;
					}
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"whitelist add "+args[1]);
					player.sendMessage(Main.UHCTypo+"Le joueur §e"+args[1]+"§7 a été ajouté à la whitelist");
					break;
				default:
					player.sendMessage(Main.UHCTypo+"Commande inconnue /h help pour plus d'information");
					break;
				}
				
			}
		}
		return false;
	}

}
