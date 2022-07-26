package fr.supercomete.commands;

import java.util.*;
import java.util.Map.Entry;

import fr.supercomete.head.GameUtils.Time.TimeUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.GUI.KarvanistaGUI;
import fr.supercomete.head.GameUtils.GUI.PtingEat;
import fr.supercomete.head.GameUtils.GUI.SeeInvGUI;
import fr.supercomete.head.GameUtils.GUI.SnowmanGUI;
import fr.supercomete.head.GameUtils.GUI.StewartGUI;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.ModeAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.RoleState.PurifiedRoleState;
import fr.supercomete.head.role.RoleState.RoleStateTypes;
import fr.supercomete.head.role.content.DWUHC.AmyPond;
import fr.supercomete.head.role.content.DWUHC.ClaraOswald;
import fr.supercomete.head.role.content.DWUHC.DannyPink;
import fr.supercomete.head.role.content.DWUHC.Davros;
import fr.supercomete.head.role.content.DWUHC.GreatIntelligence;
import fr.supercomete.head.role.content.DWUHC.Harriet_Jones;
import fr.supercomete.head.role.content.DWUHC.Karvanista;
import fr.supercomete.head.role.content.DWUHC.Kate_Stewart;
import fr.supercomete.head.role.content.DWUHC.Pting;
import fr.supercomete.head.role.content.DWUHC.RiverSong;
import fr.supercomete.head.role.content.DWUHC.RoryWilliams;
import fr.supercomete.head.role.content.DWUHC.SoldatUNIT;
import fr.supercomete.head.role.content.DWUHC.SoldatUNIT.SoldierType;
import fr.supercomete.head.role.content.DWUHC.Strax;
import fr.supercomete.head.role.content.DWUHC.TheDoctor;
import fr.supercomete.head.role.content.DWUHC.Vastra;
import fr.supercomete.head.role.content.DWUHC.Zygon;

public class DwCommand implements CommandExecutor {
	private final Main main;

	public DwCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (sender instanceof Player) {
		    Player player = (Player)sender;
            if (cmd.getName().equalsIgnoreCase("dw")) {
				if (args.length == 0) {
					player.sendMessage(Main.UHCTypo + "Commande inconnue /dw help pour plus d'information");
					return false;
				}
				Role role = RoleHandler.getRoleOf(player);
				switch (args[0].toLowerCase()) {
				case "gap":
					if(role != null) {
						if(role instanceof Vastra) {
						    Vastra vastra =(Vastra)role;
                            final CoolDown cap = vastra.gap;
							if(args.length!=2) {
								player.sendMessage(Main.UHCTypo+"§cUsage: /dw gap <Joueur>");
								return false;
							}
							if(!cap.isAbleToUse()) {
								player.sendMessage(Main.UHCTypo+"§cEncore "+ TimeUtility.transform(cap.getRemainingTime(),"§4", "§4", "§4")+" avant de pouvoir réutiliser cette commande.");
								return false;
							}
							String pselec = args[1];
							if (Bukkit.getPlayer(pselec)!=null&&Bukkit.getPlayer(pselec).isOnline() && RoleHandler.getRoleList().containsKey(Bukkit.getPlayer(pselec).getUniqueId())) {
								if (!Bukkit.getPlayer(pselec).getUniqueId().equals(player.getUniqueId())) {
									cap.setUseNow();
									final Player target =Bukkit.getPlayer(pselec);
									int count=0;
									for(final ItemStack item : target.getInventory()) {
										if(item!=null&&item.getType().equals(Material.GOLDEN_APPLE)) {
											count+=item.getAmount();
										}
									}
									player.sendMessage(Main.UHCTypo+"§7Le joueur §c"+target.getName()+"§7 a §6"+count+"§7 pomme d'or");
								}else player.sendMessage(Main.UHCTypo+ "§cVous ne pouvez pas utiliser cette capacité sur vous-même");
							}else player.sendMessage(Main.UHCTypo+ "§cLe joueur ciblé n'existe pas, n'est pas connecté, ou n'est plus en vie");
						}
						
					}
					break;
				case "pacte":
					if(role!=null) {
						if(role instanceof Karvanista) {
							new KarvanistaGUI(player).open();
						}else if(role.getCamp()==Camps.DuoKarvanista) {
							new KarvanistaGUI(player).open();		
						}
					}
					break;
				case "me":
					if (role != null) {
						RoleHandler.DisplayRole(player);
					}
					break;
				case "liste":
					player.performCommand("rolelist");
					break;
				case "status":
					if (ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode()) instanceof CampMode) {
						if (RoleHandler.isIsRoleGenerated()) {
							if(role instanceof ClaraOswald) {
							    final ClaraOswald oswald =(ClaraOswald)role;
                                final CoolDown cap = oswald.statusCoolDown;
								if(args.length!=2) {
									player.sendMessage(Main.UHCTypo+"§cUsage: /dw status <Joueur>");
									return false;
								}
								if(cap.getUtilisation()>0) {
									String pselec = args[1];
									if (Bukkit.getPlayer(pselec)!=null&&Bukkit.getPlayer(pselec).isOnline() && RoleHandler.getRoleList().containsKey(Bukkit.getPlayer(pselec).getUniqueId())) {
										if (!Bukkit.getPlayer(pselec).getUniqueId().equals(player.getUniqueId())) {
											final DWRole targetrole = (DWRole) RoleHandler.getRoleOf(Bukkit.getPlayer(pselec));
											cap.setUseNow();
											oswald.commandUse.addUse(Bukkit.getPlayer(pselec).getName());
											cap.addUtilisation(-1);
											final Status[]status = targetrole.getStatus();
											if(status.length==0) {
												player.sendMessage(Main.UHCTypo+"§cForme de vie inconnue§7");
											}else if(status.length==1){
												player.sendMessage(Main.UHCTypo+"Ce joueur a le status "+status[0].getName());
											}else {
												player.sendMessage(Main.UHCTypo+"Ce joueur a le status "+status[new Random().nextInt(status.length-1)].getName());
											}
											
										}else player.sendMessage(Main.UHCTypo+ "§cVous ne pouvez pas utiliser cette capacité sur vous-même");
									}else player.sendMessage(Main.UHCTypo+ "§cLe joueur ciblé n'existe pas, n'est pas connecté, ou n'est plus en vie");
								}
							}
						}
					}
					break;
				case "compo":
				case "roles": 
				case "role":
					if (ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode()) instanceof CampMode) {
						if (RoleHandler.isIsRoleGenerated()) {
							HashMap<Class<?>, Integer> map = new HashMap<>();
							for (Role r : RoleHandler.getRoleList().values()) {
								int amount = (map.containsKey(r.getClass())) ? map.get(r.getClass()) + 1 : 1;
								map.put(r.getClass(), amount);
							}
							if (!RoleHandler.IsHiddenRoleNCompo)
								RolesCommand.display(map, player);
							else
								player.sendMessage(Main.UHCTypo
										+ "§4Impossible la composition est brouillée jusqu'a l'épisode suivant");
							return false;
						} else {
							RolesCommand.display(Main.currentGame.getRoleCompoMap(), player);
							return true;
						}
					}
					break;
				case "cell":
					if (role != null) {
						if (role instanceof Davros) {
						    final Davros davros =(Davros) role;
                            if (args.length < 2) {
								player.sendMessage(Main.UHCTypo
										+ "§cErreur vous n'avez pas spécifié de joueur à désigner comme cible");
							} else {
								CoolDown cap = davros.cooldown;
								String pselec = args[1];
								if (Bukkit.getPlayer(pselec)!=null&&Bukkit.getPlayer(pselec).isOnline() && RoleHandler.getRoleList()
										.containsKey(Bukkit.getPlayer(pselec).getUniqueId())) {
									if (Bukkit.getPlayer(pselec).getUniqueId() != player.getUniqueId()) {
										if (cap.getUtilisation() > 0) {
											final Player target = Bukkit.getPlayer(pselec);
											if (target.getLocation().distance(player.getLocation()) < 30) {
												final DWRole target_role = (DWRole) RoleHandler.getRoleOf(target);
												cap.addUtilisation(-1);
												target_role.addBonus(new Bonus(BonusType.Heart, 2));
												target.sendMessage(
														Main.UHCTypo + "§cDavros vous a donné un ♥ supplémentaire.");
											} else
												player.sendMessage(Main.UHCTypo + "Le joueur est trop loin.");
										} else
											player.sendMessage(Main.UHCTypo
													+ "Vous avez atteint la limite d'utilisation de cette capacité");
									} else
										player.sendMessage(
												Main.UHCTypo + "Vous ne pouvez pas utiliser ce pouvoir sur vous même.");
								} else
									player.sendMessage(Main.UHCTypo
											+ "§cLe joueur ciblé n'existe pas, n'est pas connecté, ou n'est plus en vie");
							}
						}
					}
					break;
				case "tempete":
					if (role != null) {
						if (role instanceof GreatIntelligence) {
							new SnowmanGUI((GreatIntelligence) role, player).open();
						}
					}
					break;
				case "snowman":
					if (role != null) {
						if (role instanceof GreatIntelligence) {
							final GreatIntelligence great = (GreatIntelligence)role;
							CoolDown cap = great.snowmancooldown;
							if (cap.getUtilisation() > 0) {
								GreatIntelligence intelligence = (GreatIntelligence) RoleHandler.getRoleOf(player);
								double distancemin = 400;
								for (Entry<Location, Integer> entry : intelligence.getSnowman().entrySet()) {
									double distance = entry.getKey().distance(player.getLocation());
									if (distance < distancemin) {
										distancemin = distance;
									}
								}
								if (distancemin > 50) {
									intelligence.addNewSnowman(player.getLocation(), main);
									cap.addUtilisation(-1);
									intelligence.addBonus(new Bonus(BonusType.Heart, -1));
								} else
									player.sendMessage(Main.UHCTypo
											+ "§cImpossible cet emplacement est trop près d'un autre bonhomme de neige");
							} else
								player.sendMessage(
										Main.UHCTypo + "Vous avez atteint la limite d'utilisation de cette capacité");
						}
					}
					break;
				case "enquete":
					if (role != null) {
						if (args.length < 2) {
							player.sendMessage(Main.UHCTypo
									+ "§cErreur vous n'avez pas spécifié de joueur à désigner comme cible");
						} else {
							if (role instanceof SoldatUNIT) {
								final SoldatUNIT soldat =(SoldatUNIT)role;
								if (soldat.soldiertype==SoldierType.Enqueteur) {
									SoldatUNIT spy = (SoldatUNIT) role;
									CoolDown cap = soldat.generalCoolDown;
									if (cap.getUtilisation() > 0) {
										String pselec = args[1];
										if (Bukkit.getPlayer(pselec)!=null&&Bukkit.getPlayer(pselec).isOnline() && RoleHandler.getRoleList()
												.containsKey(Bukkit.getPlayer(pselec).getUniqueId())) {
											if (!Bukkit.getPlayer(pselec).getUniqueId().equals(player.getUniqueId())) {
												HashMap<UUID, Integer> tmp = spy.getEnquetes();
												cap.addUtilisation(-1);
												tmp.put(Bukkit.getPlayer(pselec).getUniqueId(), 0);
												player.sendMessage(Main.UHCTypo + "Vous avez lancé une enquête sur §e"
														+ Bukkit.getPlayer(pselec).getName());
											} else
												player.sendMessage(Main.UHCTypo
														+ "§cVous ne pouvez pas utiliser cette capacité sur vous-même");
										} else
											player.sendMessage(Main.UHCTypo
													+ "§cLe joueur ciblé n'existe pas, n'est pas connecté, ou n'est plus en vie");
									} else
										player.sendMessage(
												Main.UHCTypo + "Vous ne pouvez plus utiliser cette capacité.");
								}
							}
						}
					}
					break;
				case "heal":
					if (role != null) {
						if (args.length < 2) {
							player.sendMessage(Main.UHCTypo
									+ "§cErreur vous n'avez pas spécifié de joueur à désigner comme cible");
						} else {
							if (role instanceof SoldatUNIT) {
								SoldatUNIT soldat = (SoldatUNIT)role;
								if (soldat.soldiertype==SoldierType.Medic) {
									final CoolDown cap = soldat.generalCoolDown;
									final String pselec = args[1];
									if (Bukkit.getPlayer(pselec)!=null&&Bukkit.getPlayer(pselec).isOnline() && RoleHandler.getRoleList().containsKey(Bukkit.getPlayer(pselec).getUniqueId())) {
										if (cap.getUtilisation() > 0) {
                                            if (!Objects.equals(Bukkit.getPlayer(pselec).getName(), player.getName())) {
											    final Player target = Bukkit.getPlayer(pselec);
											    target.setHealth(target.getMaxHealth());
											    target.sendMessage(Main.UHCTypo + "§a Votre vie a été régénerée par un autre joueur.");
											    player.sendMessage(Main.UHCTypo + "§aVous avez soigné " + target.getName());
											    cap.addUtilisation(-1);
                                            } else
                                                player.sendMessage(Main.UHCTypo + "§cVous ne pouvez pas vous choisir comme cible");
										} else
											player.sendMessage(
													Main.UHCTypo + "Vous ne pouvez plus utiliser cette capacité.");
									} else
										player.sendMessage(Main.UHCTypo
												+ "§cLe joueur ciblé n'existe pas, n'est pas connecté, ou n'est plus en vie");

								} else
									player.sendMessage(Main.UHCTypo + "Vous n'avez pas le bon role.");
							} else
								player.sendMessage(Main.UHCTypo + "Vous n'avez pas le bon role.");
						}

					}
					break;
				case "poison":
					if (role != null) {
						if (role instanceof SoldatUNIT) {
							SoldatUNIT soldat = (SoldatUNIT)role;
							if (soldat.soldiertype==SoldierType.Garde) {
								SoldatUNIT soldier = (SoldatUNIT) role;
								soldier.setActivated(!soldier.isActivated());
								player.sendMessage(
										Main.UHCTypo + "Vous avez " + ((soldier.isActivated()) ? "activé" : "désactivé")
												+ " votre capacité de poison");
							} else
								player.sendMessage(Main.UHCTypo + "Vous n'avez pas le bon role.");
						} else
							player.sendMessage(Main.UHCTypo + "Vous n'avez pas le bon role.");
					}
					break;
				case "eat":
					if (role != null) {
						if (role instanceof Pting) {
							if (args.length < 2) {
								player.sendMessage(Main.UHCTypo
										+ "§cErreur vous n'avez pas spécifié de joueur à désigner comme cible");
							} else {
								String pselec = args[1];
								final Pting ptingrole = (Pting)role;
								CoolDown pting = ptingrole.peopleeat;
								if (Bukkit.getPlayer(pselec)!=null&&Bukkit.getPlayer(pselec).isOnline() && RoleHandler.getRoleList()
										.containsKey(Bukkit.getPlayer(pselec).getUniqueId())) {
									if (!Bukkit.getPlayer(pselec).getUniqueId().equals(player.getUniqueId())) {
										if (pting.isAbleToUse()) {
											pting.setLastuse(Main.currentGame.getTime());
											PtingEat gui = new PtingEat((Pting) role, player, Bukkit.getPlayer(pselec));
											gui.open();

										} else
											player.sendMessage(Main.UHCTypo
													+ "Vous ne pouvez pas utiliser cette capacité pour l'instant. Essayez de nouveau dans "
													+ TimeUtility.transform(pting.getRemainingTime(), "§7", "§7", "§4"));
									} else
										player.sendMessage(Main.UHCTypo
												+ "§cVous ne pouvez pas utiliser cette capacité sur vous-même");
								} else
									player.sendMessage(Main.UHCTypo
											+ "§cLe joueur ciblé n'existe pas, n'est pas connecté, ou n'est plus en vie");
							}
						} else
							player.sendMessage(Main.UHCTypo + "Vous n'avez pas le bon role.");
					}
					break;
				case "spectate":
					if (role != null) {
						if (role instanceof DannyPink ) {
                            DannyPink dannypink= (DannyPink)role;
                            CoolDown danny = dannypink.cooldown;
							if (args.length < 2) {
								player.sendMessage(Main.UHCTypo
										+ "§cErreur vous n'avez pas spécifié de joueur à désigner comme cible");
							} else {
								final String pselec = args[1];
								if (Bukkit.getPlayer(pselec)!=null&&Bukkit.getPlayer(pselec).isOnline() && RoleHandler.getRoleList()
										.containsKey(Bukkit.getPlayer(pselec).getUniqueId())) {
									Player target = Bukkit.getPlayer(args[1]);
									if (!Bukkit.getPlayer(pselec).getUniqueId().equals(player.getUniqueId())) {
										if (danny.isAbleToUse()) {
											if (danny.getUtilisation() > 0) {
												danny.setUtilisation(danny.getUtilisation() - 1);
												danny.setLastuse(Main.currentGame.getTime());
												dannypink.commandUse.addUse(Bukkit.getPlayer(pselec).getName());
												SeeInvGUI gui = new SeeInvGUI(player, target,true);
												gui.open();
											} else
												player.sendMessage(
														Main.UHCTypo + "Vous ne pouvez plus utiliser cette capacité.");
										} else
											player.sendMessage(Main.UHCTypo
													+ "Vous ne pouvez pas utiliser cette capacité pour l'instant. Réessayer dans "
													+ TimeUtility.transform(danny.getRemainingTime(), "§7", "§7", "§4"));
									} else
										player.sendMessage(Main.UHCTypo
												+ "§cVous ne pouvez pas utiliser cette capacité sur vous-même");
								} else
									player.sendMessage(Main.UHCTypo
											+ "§cLe joueur ciblé n'existe pas, n'est pas connecté, ou n'est plus en vie");
							}
						} else
							player.sendMessage(Main.UHCTypo + "Vous n'avez pas cette capacité.");
					}
					break;
				case "target":
					if (role != null) {
						if (role instanceof Zygon) {
                            Zygon castedrole=(Zygon) role;
                            if (args.length < 2) {
								player.sendMessage(Main.UHCTypo
										+ "§cErreur vous n'avez pas spécifié de joueur à désigner comme cible");
							} else {
								Player target = Bukkit.getPlayer(args[1]);
								if (target.getUniqueId() != castedrole.getOwner()) {
									if (!castedrole.isStole()) {
										if (RoleHandler.getRoleList().containsKey(target.getUniqueId())) {
											castedrole.setUuid(target.getUniqueId());
											castedrole.setStole(true);
											return true;
										} else
											player.sendMessage(
													Main.UHCTypo + "§cCe joueur ne peut pas être désigner comme cible");
									} else
										player.sendMessage(Main.UHCTypo + "§cVous avez déja choisi votre cible");
								} else
									player.sendMessage(Main.UHCTypo + "§cVous ne pouvez pas vous choisir comme cible");
							}
						} else
							player.sendMessage(Main.UHCTypo + "§cVous n'avez pas le rôle requis.");
					}
					break;
				case "indice":
					if (role != null) {
						if (role instanceof Kate_Stewart) {
							new StewartGUI((Kate_Stewart) role, player).open();
                        } else
							player.sendMessage(Main.UHCTypo + "§cVous n'avez pas le rôle requis.");
					}
					break;
				case "chat":
					if (role != null) {
						if (role instanceof AmyPond || role instanceof RoryWilliams) {
							ArrayList<String> str = new ArrayList<String>();
							str.add("§d§lAmour §r§d" + player.getName() + ":§r");
                            str.addAll(Arrays.asList(args).subList(1, args.length));
							RoleHandler.PondChat(str);
						}
					}
					break;
				case "source":
					if (role != null) {
						if (role instanceof Harriet_Jones) {
                            Harriet_Jones castedrole =(Harriet_Jones) role;
                            if (args.length < 2) {
								player.sendMessage(Main.UHCTypo
										+ "§cErreur vous n'avez pas spécifié de joueur à désigner comme source");
							} else {
								Player target = Bukkit.getPlayer(args[1]);
								if(target==null) {
									player.sendMessage(Main.UHCTypo+"§cLe joueur n'est pas correctement indiqué");
									return false;
								}
                                if (target.getUniqueId() != castedrole.getOwner()) {
                                    if (castedrole.getTargettedplayers().size() < 3 && castedrole.sources.getUtilisation() > 0) {
                                        if (RoleHandler.getRoleList().containsKey(target.getUniqueId())) {
                                            if (!castedrole.getTargettedplayers().contains(target.getUniqueId())) {
                                                castedrole.addTargetPlayer(target);
                                                castedrole.sources.removeUtilisation(1);
                                                RoleHandler.DisplayRole(player);

                                            } else
                                                player.sendMessage(
                                                        Main.UHCTypo + "§cCe joueur est déjà est sur votre de sources");
                                        } else
                                            player.sendMessage(
                                                    Main.UHCTypo + "§cCe joueur ne peut pas être désigner comme source");

                                    }
                                }else player.sendMessage(Main.UHCTypo + "§cVous ne pouvez pas vous choisir comme source");
							}
						}
					}
					break;
				case "purify":
					if (role != null) {
						if (role instanceof Strax) {
							Strax strax = (Strax)role;
							CoolDown cp = strax.purify;
							if (cp.getUtilisation() > 0) {
								if (args[1] != null) {
									if (!args[1].isEmpty() && args.length > 1) {
										String pselec = args[1];
										if (Bukkit.getPlayer(pselec).isOnline() && RoleHandler.getRoleList().containsKey(Bukkit.getPlayer(pselec).getUniqueId())) {
											final Player targetplayer =Bukkit.getPlayer(pselec);
											final Role Target = RoleHandler.getRoleOf(targetplayer);
											if (Target.hasRoleState(RoleStateTypes.Purified)) {
												player.sendMessage(Main.UHCTypo+"Vous avez déja utilisé la purification sur ce joueur");
												return false;
											}
											cp.removeUtilisation(1);
											strax.command.addUse(Bukkit.getPlayer(pselec).getName());
											player.sendMessage(Main.UHCTypo + "Vous avez utilisé la purification sur "+targetplayer.getName());
											Target.addBonus(new Bonus(BonusType.Heart, 2));
											if (!(Target.hasRoleState(RoleStateTypes.Infected))&& !(Target.hasRoleState(RoleStateTypes.Purified))) {
												Target.addRoleState(new PurifiedRoleState(RoleStateTypes.Purified));
												targetplayer.sendMessage(Main.UHCTypo+ "Vous avez été purifié, vous etes immunisé à l'infection");
												RoleHandler.DisplayRole(targetplayer);
												return true;
											} else if(Target.hasRoleState(RoleStateTypes.Infected)){
												Target.setCamp(Camps.Neutral);
												Target.removeRoleState(RoleStateTypes.Infected);
												targetplayer.sendMessage(Main.UHCTypo
														+ "§7Vous avez été purifié, vous étiez déjà infecté, vous avez perdu l'effet de §cforce§7 pendant la nuit"
														+"\n§7Vous devez maintenant gagner avec §6Seul");
												RoleHandler.DisplayRole(targetplayer);
											}
										} else
											player.sendMessage(Main.UHCTypo
													+ "§cLe joueur ciblé n'existe pas, n'est pas connecté, ou n'est plus en vie");
									} else
										player.sendMessage(Main.UHCTypo + "§cUsage: /dw purify <Joueur>");
								} else
									player.sendMessage(Main.UHCTypo + "§cUsage: /dw purify <Joueur>");
							} else
								player.sendMessage(Main.UHCTypo + "§cVous ne pouvez plus utiliser cette capacité");
						} else
							player.sendMessage(Main.UHCTypo + "§cVous n'avez pas le bon rôle");
					}
					break;
				case "echange":
					if (role != null) {
						if (role instanceof RiverSong) {
							RiverSong song = (RiverSong)role;
							if(!song.isExchange()) {
								role.addBonus(new Bonus(BonusType.Heart, -10));
								song.setExchange(true);
								RoleHandler.DisplayRole(player);
							}
							break;
						} else
							player.sendMessage(Main.UHCTypo + "§cVous n'avez pas le bon rôle");
					}
					break;
				case "energy":
					if (role != null) {
						if (role instanceof TheDoctor) {
							TheDoctor doctor = (TheDoctor)role;
							
							if (doctor.pts >= 2) {
								if (player.getHealth() + 6 <= player.getMaxHealth()) {
									doctor.pts=doctor.pts-2;
									player.setHealth(player.getHealth() + 6);
									return true;
								} else {
									player.sendMessage(
											Main.UHCTypo + "§cVous avez trop de vie pour utiliser cette commande");
									return false;
								}
							} else {
								player.sendMessage(
										Main.UHCTypo + "§cVous n'avez pas assez de point(s). Il ne vous reste que §a"
												+ doctor.pts + "pts§c alors qu'il vous en faut 2");
								return false;
							}
						} else {
							player.sendMessage(Main.UHCTypo + "§cVous n'avez pas le bon rôle.");
							return false;
						}
					} else {
						player.sendMessage(Main.UHCTypo + "§c La partie n'a pas commencé");
						return false;
					}
				case "help":
					player.sendMessage(Main.UHCTypo + "§6Menu d'aide");
					player.sendMessage("§7/dw help");
					Main.DisplayToPlayerInChat(player, "§8-Ouvre le menu d'aide.", 50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw liste");
					Main.DisplayToPlayerInChat(player,"§8-Affiche le role de toute les joueurs §4(Admin Only)",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw me");
					Main.DisplayToPlayerInChat(player, "§8-Affiche votre rôle.", 50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw compo/role/roles");
					Main.DisplayToPlayerInChat(player, "§8-Montre la liste des rôles.", 50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw purify");
					Main.DisplayToPlayerInChat(player, "§8-Permet au Strax de purifier un joueur §4(Uniquement §4"+ ModeAPI.getRoleByClass(Strax.class).getName() + ")", 50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw energy");
					Main.DisplayToPlayerInChat(player,"§8-Commande pour se heal contre des points de régénération. §4(Uniquement §4"+ ModeAPI.getRoleByClass(TheDoctor.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw echange");
					Main.DisplayToPlayerInChat(player,"§8-Commande pour faire l'échange d'information contre des coeurs permanents. §4(Uniquement §4"+ ModeAPI.getRoleByClass(RiverSong.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw source");
					Main.DisplayToPlayerInChat(player,"§8-Commande pour désigner une source §4(Uniquement §4"+ ModeAPI.getRoleByClass(Harriet_Jones.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw chat");
					Main.DisplayToPlayerInChat(player,"§8-Accès au chat entre Rory et Amy §4(Uniquement §4"+ ModeAPI.getRoleByClass(AmyPond.class).getName()+" / "+ModeAPI.getRoleByClass(RoryWilliams.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw indice");
					Main.DisplayToPlayerInChat(player,"§8-Commande pour voir vos indice §4(Uniquement §4"+ ModeAPI.getRoleByClass(Kate_Stewart.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw target");
					Main.DisplayToPlayerInChat(player,"§8-Désigne le modèle §4(Uniquement §4"+ ModeAPI.getRoleByClass(Zygon.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw spectate");
					Main.DisplayToPlayerInChat(player,"§8-Ouvre l'inventaire du joueur  §4(Uniquement §4"+ ModeAPI.getRoleByClass(DannyPink.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw eat");
					Main.DisplayToPlayerInChat(player,"§8-Permet de manger des blocs dans l'inventaire d'un joueur §4(Uniquement §4"+ ModeAPI.getRoleByClass(Pting.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw poison");
					Main.DisplayToPlayerInChat(player,"§8-Active ou désactive le poison sur les flêches §4(Uniquement §4"+ ModeAPI.getRoleByClass(SoldatUNIT.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw heal");
					Main.DisplayToPlayerInChat(player,"§8-Désigne une personne pour soigner une personne §4(Uniquement §4"+ ModeAPI.getRoleByClass(SoldatUNIT.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw enquete");
					Main.DisplayToPlayerInChat(player,"§8-Lance une enquête sur un joueur §4(Uniquement §4"+ ModeAPI.getRoleByClass(SoldatUNIT.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw gap");
					Main.DisplayToPlayerInChat(player,"§8-Indique le nombre de pomme en or d'un joueur §4(Uniquement §4"+ ModeAPI.getRoleByClass(Vastra.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw pacte");
					Main.DisplayToPlayerInChat(player,"§8-Permet à Karvanista de former son pacte §4(Uniquement §4"+ ModeAPI.getRoleByClass(Karvanista.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw status");
					Main.DisplayToPlayerInChat(player,"§8-Permet de montrer le status d'un joueur §4(Uniquement §4"+ ModeAPI.getRoleByClass(ClaraOswald.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw cell");
					Main.DisplayToPlayerInChat(player,"§8-Donne un ♥ supplémentaire à un joueur §4(Uniquement §4"+ ModeAPI.getRoleByClass(Davros.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw tempete");
					Main.DisplayToPlayerInChat(player,"§8-Permet de se téléporter à ses bonhommes de neige §4(Uniquement §4"+ ModeAPI.getRoleByClass(GreatIntelligence.class).getName() + ")",50, ChatColor.DARK_GRAY);
					player.sendMessage("§7/dw snowman");
					Main.DisplayToPlayerInChat(player,"§8-Permet de créer un bonhomme de neige §4(Uniquement §4"+ ModeAPI.getRoleByClass(GreatIntelligence.class).getName() + ")",50, ChatColor.DARK_GRAY);
					
					break;
				default:
					player.sendMessage(Main.UHCTypo + "Commande inconnue /dw help pour plus d'information");
					return true;
				}
			}
		}
		return false;
	}

}
