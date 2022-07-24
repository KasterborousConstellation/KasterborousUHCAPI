package fr.supercomete.head.world;

import java.lang.reflect.Field;
import java.util.UUID;

import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEventHandler;
import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEvents;
import fr.supercomete.head.GameUtils.Fights.Fight;
import fr.supercomete.head.GameUtils.Fights.FightHandler;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.role.Key.TardisHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.ColorScheme;
import fr.supercomete.head.GameUtils.Lag;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.ModeAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.TeamMode;
import fr.supercomete.head.GameUtils.GameMode.Modes.DWUHC;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.RoleModifier.BonusHeart;
import fr.supercomete.head.role.RoleModifier.InvisibleRoleWithArmor;
import fr.supercomete.head.role.content.DWUHC.Bill_Potts;
import fr.supercomete.head.role.content.DWUHC.GreatIntelligence;
import fr.supercomete.head.role.content.DWUHC.Jenny_Flint;
import fr.supercomete.head.role.content.DWUHC.Karvanista;
import fr.supercomete.head.role.content.DWUHC.Kate_Stewart;
import fr.supercomete.head.role.content.DWUHC.Strax;
import fr.supercomete.head.role.content.DWUHC.Vastra;
import fr.supercomete.head.world.ScoreBoard.ScoreBoardManager;
import fr.supercomete.head.world.ScoreBoard.SimpleScoreboard;
import fr.supercomete.nbthandler.NbtTagHandler;
import fr.supercomete.tasks.generatorcycle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class scoreboardmanager {
	private static Main main;
	private static byte timer=0;
	public scoreboardmanager(Main main) {
		scoreboardmanager.main = main;
	}

	public void ChangeScoreboard() {
		ColorScheme scheme = Main.currentGame.getColorScheme();
		ChatColor p = scheme.getPrimary();
		ChatColor s = scheme.getSecondary();
		ChatColor t = scheme.getTertiary();
		Bukkit.getServer().getScheduler().runTaskTimer(main, new Runnable() {
			@Override
			public void run() {
				timer++;
				timer=(byte)(timer%4);
				for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
					//Fix bug that kill people if they go under -64 y
					//Players cannot go under -5 if they are into uhc playworld 
					if(player.getWorld().equals(worldgenerator.currentPlayWorld)) {
						if(player.getLocation().getY()<-5) {
							player.teleport(new Location(player.getWorld(), player.getLocation().getX(), 150, player.getLocation().getZ()));
						}
					}
					if(player.getOpenInventory().getTitle().equals("§dTimers")){
					    main.updateTimerInventory(player);
                    }
					ScoreBoardManager.update(player);
					SetallScoreboard(player);
					if(Main.currentGame.getMode()instanceof CampMode) {
						if(RoleHandler.isIsRoleGenerated()) {
							Role role = RoleHandler.getRoleOf(player);
							if(role!=null) {
								int count =20;
								if(role instanceof BonusHeart) {
									BonusHeart bonus = (BonusHeart)role;
									count+=bonus.getHPBonus();
								}
								count += role.getPowerOfBonus(BonusType.Heart);
								player.setMaxHealth(count);
							}
						}
					}
					for(final Fight fight: FightHandler.currentFight){
					    fight.update(FightHandler.currentFight);
                    }
					double tps = Lag.getTPS() * 100;
					tps = Math.round(tps);
					tps = tps / 100;
					sendHeadAndFooter(player,
							s + "» " + p + ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode()).getName() + s
									+ " «" + "\n" + t + "Ping: §a" + getPing(player) + "ms " + t + "TPS: §a" + tps
									+ "\n",
							"\n" + t + "https://discord.gg/" + main.getDiscordLink() + "\n" + s + "»" + p
									+ main.getServerId() + s + "«\n" + "\n" + p + "Plugin par " + s + "Supercomete");
					if (Main.currentGame.getScenarios().contains(Scenarios.CatEyes)) {
						player.removePotionEffect(PotionEffectType.NIGHT_VISION);
						player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 251, 0));
					}
					if (Main.currentGame.getScenarios().contains(Scenarios.NoFood)) {
						player.setFoodLevel(20);
					}
					if (Main.currentGame.isGameState(Gstate.Waiting)) {
						Main.currentGame.setFirstBorder(Main.currentGame.getCurrentBorder());
					}
					if((main.getConfig().getString("serverapi.serverconfig.echosiakey")=="EchosiaBest"))
					Main.serverinfo.write();
					if (Main.currentGame.getScenarios().contains(Scenarios.FireEnchantLess)) {
						Inventory inv = player.getInventory();
						for (ItemStack it : inv) {
							if (it != null) {
								if (it.containsEnchantment(Enchantment.FIRE_ASPECT)) {
									it.removeEnchantment(Enchantment.FIRE_ASPECT);
								}
								if (it.containsEnchantment(Enchantment.ARROW_FIRE)) {
									it.removeEnchantment(Enchantment.ARROW_FIRE);
								}
							}
						}
					}
				}
			}
		}, 0L, 5L);
	}
	public static void SetallScoreboard(Player player) {
		if(timer==0) {
			SimpleScoreboard ss = ScoreBoardManager.boards.get(player.getUniqueId());
			Scoreboard sc = ScoreBoardManager.boards.get(player.getUniqueId()).getScoreboard();
			try {
				sc.getObjective("§a%").unregister();
			}catch (Exception e) {}
			try {
				sc.getObjective("%").unregister();
			}catch (Exception e) {}
			try {
				sc.getObjective("§b%").unregister();
			}catch (Exception e) {}
			try {
				sc.getObjective("§d§d%").unregister();
			}catch (Exception e) {}
			if (RoleHandler.isIsRoleGenerated()) {
				if(RoleHandler.getRoleOf(player)!=null) {
					Role role = RoleHandler.getRoleOf(player);
					float addpercent = role.getPowerOfBonus(BonusType.Speed);
					player.setWalkSpeed(0.2F * ((100.0F+addpercent)/100.0F));
				}
				if (RoleHandler.getRoleList().containsKey(player.getUniqueId())) {
					if (RoleHandler.getRoleOf(player) instanceof Jenny_Flint) {
						Jenny_Flint role = (Jenny_Flint) RoleHandler.getRoleOf(player);			
						Objective ob = sc.registerNewObjective("%", "score2");
						ob.setDisplaySlot(DisplaySlot.BELOW_NAME);
						Score score2;
						for (UUID uu:role.getPercentmap().keySet()) {
							Player p = Bukkit.getPlayer(uu);
							if (p == null || player.getUniqueId()==uu)
								continue;
							score2 = ob.getScore(p.getName());
							if (role.getPercentmap().get(uu) == 99 && p != player) {
								String value = (RoleHandler.getRoleOf(p) instanceof Vastra) ? "§aest Vasta":"§cn'est pas Vastra";
								player.sendMessage(Main.UHCTypo + "§3" + p.getName() + " " + value);
							}
							if (player.getLocation().distance(p.getLocation()) < 10 && role.getPercentmap().get(uu) < 100) {
								role.getPercentmap().put(uu, role.getPercentmap().get(uu) + 1);
							}
							int total_peopleat100 = 0;
							Jenny_Flint flint = (Jenny_Flint) RoleHandler.getRoleOf(player);
							for (UUID u : flint.getPercentmap().keySet()) {
								if (flint.getPercentmap().get(u) == 100 && u != flint.getOwner()) {
									total_peopleat100++;
								}
							}
							role.setAmount(total_peopleat100);
							score2.setScore(role.getPercentmap().get(uu));
						}
						ss.send(player);
					}else if(RoleHandler.getRoleOf(player)instanceof Kate_Stewart) {
						Kate_Stewart role = (Kate_Stewart)RoleHandler.getRoleOf(player);
						Objective ob = sc.registerNewObjective("§a%", "score");
						ob.setDisplaySlot(DisplaySlot.BELOW_NAME);
						Score score3;
						for (UUID uu : role.getIndices().keySet()) {
							Player p = Bukkit.getPlayer(uu);
							if (p == null || player.getUniqueId()==uu)
								continue;
							score3 = ob.getScore(p.getName());
							if(player.getLocation().distance(p.getLocation())<20) {
								if(Main.currentGame.getTime()%Main.currentGame.getDataFrom(Configurable.LIST.StewartUpdate)==0 && Main.getPlayerlist().contains(uu))role.updateProgression(uu);
							}
							score3.setScore(role.getProgression().get(uu));
						}
						ss.send(player);
					
					}else if(RoleHandler.getRoleOf(player)instanceof Strax) {
						Objective ob = sc.registerNewObjective("§b%", "score3");
						ob.setDisplaySlot(DisplaySlot.BELOW_NAME);
						Score score; 
						for (UUID uu : Main.getPlayerlist()) {
							Player p = Bukkit.getPlayer(uu);
							if (p == null || player.getUniqueId()==uu)
								continue;
							score = ob.getScore(p.getName());
							final double health =p.getHealth()/p.getMaxHealth()*100.0;
							final int heal = (int) Math.round(health);
							score.setScore(heal);
						}
						ss.send(player);
					}else if(RoleHandler.getRoleOf(player)instanceof GreatIntelligence) {
						final GreatIntelligence intell =(GreatIntelligence) RoleHandler.getRoleOf(player);
						final InvisibleRoleWithArmor armorinvisible=intell;
						if(intell.isShowing()==false) {
							if(intell.getTeleportedsnowman()!=null) {
								if(player.getWorld()==intell.getTeleportedsnowman().getWorld()) {
									if(player.getLocation().distance(intell.getTeleportedsnowman())>5) {
										player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0),false);
										intell.setTeleportedsnowman(null);
										intell.setShowing(true);
										armorinvisible.show(player);
									}else {
										if(player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
											player.removePotionEffect(PotionEffectType.INVISIBILITY);
										}
										armorinvisible.hide(player);
										player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,1200,1,false,false),false);
									}
								}
							}
						}
					}else if(RoleHandler.getRoleOf(player)instanceof Bill_Potts) {
						if(NbtTagHandler.hasUUIDTAG(player.getItemInHand())) {
							if(NbtTagHandler.getUUIDTAG(player.getItemInHand())==10) {
								CoolDown cap = ((Bill_Potts)RoleHandler.getRoleOf(player)).infinitePotionCoolDown;
								final String str= (cap.isAbleToUse())?"§aUtilisable":("§r["+generatorcycle.generateProgressBar(100-((double)cap.getRemainingTime() /((double)cap.getCooldown())*100.0), 40)+"§r]");
                                PlayerUtility.sendActionbar(player, str);
							}
						}
					}else if(RoleHandler.getRoleOf(player)instanceof Karvanista) {
						final Karvanista karva = (Karvanista) RoleHandler.getRoleOf(player);
						if(!karva.isFinished()){
							Objective ob = sc.registerNewObjective("§d§d%", "score4");
							ob.setDisplaySlot(DisplaySlot.BELOW_NAME);
							Score score;
							final Player p = Bukkit.getPlayer(karva.getTarget());
							if (!(p == null)) {
								score = ob.getScore(p.getName());
								score.setScore((int)((((double)karva.getProgress() )/(60.0))*100.0));	
							}
							ss.send(player);
						}
						
						
					}
					if(Main.currentGame.getMode()instanceof DWUHC) {
						if(player.getWorld()==worldgenerator.structureworld) {
							if(player.getLocation().distance(Main.currentGame.getMode().getStructure().get(0).getPositionRelativeToLocation(new int[] {18,20,12}))<1) {
								PlayerUtility.PlayerRandomTPMap(player);
                                TardisHandler.currentTardis.timespanlist.put(player.getUniqueId(),0);
                                PlayerEventHandler.Event(PlayerEvents.LeaveTardis,player,player.getLocation());
							}
						}
					}
				}
			}else {
				player.setWalkSpeed(0.2F);
			}
			if(Main.currentGame.IsTeamActivated()&& Main.currentGame.getMode()instanceof TeamMode) {
				for (fr.supercomete.head.GameUtils.Team t : Main.currentGame.getTeamList()) {
					ChatColor col = TeamManager.getColorOfShortColor(t.getColor());
					String prefix = col.toString() + t.getChar() + " ";
					if (sc.getTeam(t.getTeamName()) != null)
						sc.getTeam(t.getTeamName()).unregister();
					sc.registerNewTeam(t.getTeamName()).setPrefix(prefix);
					for (UUID uu : t.getMembers()) {
						Player p = Bukkit.getPlayer(uu);
						sc.getTeam(t.getTeamName()).addEntry(p.getName());
					}
				}
			}else {
				Main.currentGame.getTeamList().clear();
				if(Main.currentGame.getGamestate()== Gstate.Waiting) {
					if(sc.getTeam("1cohost")!=null) {
						sc.getTeam("1cohost").unregister();
					}
					if(sc.getTeam("0host")!=null) {
						sc.getTeam("0host").unregister();
					}
					if(sc.getTeam("2players")!=null) {
						sc.getTeam("2players").unregister();
					}
					final Team host = sc.registerNewTeam("0host");
					host.setPrefix("§cHost §r");
					final Team cohost = sc.registerNewTeam("1cohost");
					cohost.setPrefix("§3Cohost §r");
					final Team players = sc.registerNewTeam("2players");
					players.setPrefix("§7");
					for(final Player p : Bukkit.getOnlinePlayers()) {
						if(Main.host!=null&&Main.host.equals(p.getUniqueId())) {
							host.addEntry(p.getName());
							continue;
						}
						if(Main.cohost.contains(p.getUniqueId())) {
							cohost.addEntry(p.getName());
							continue;
						}
						players.addEntry(p.getName());
					}	
				}else {
					for(Player p : Bukkit.getOnlinePlayers()) {
						if (sc.getTeam(p.getName()) != null)
							sc.getTeam(p.getName()).unregister();
						sc.registerNewTeam(p.getName()).setPrefix("");
						sc.getTeam(p.getName()).addEntry(p.getName());
						if(p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
							sc.getTeam(p.getName()).setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
						}else {
							sc.getTeam(p.getName()).setNameTagVisibility(NameTagVisibility.ALWAYS);
						}
						sc.getTeam(p.getName()).setCanSeeFriendlyInvisibles(false);
					}
				}
			}
		}
	}

	public static void sendHeadAndFooter(Player player, String head, String foot) {
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		try {
			Field header = packet.getClass().getDeclaredField("a");
			Field footer = packet.getClass().getDeclaredField("b");
			header.setAccessible(true);footer.setAccessible(true);
			header.set(packet, ChatSerializer.a("\"" + head + "\""));
			footer.set(packet, ChatSerializer.a("\"" + foot + "\""));
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public static int getPing(Player player) {
		return ((CraftPlayer) player).getHandle().ping;
	}

	public static void titlemessage(String str) {
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\":\"" + str + "\"}");
			PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
			PacketPlayOutTitle length = new PacketPlayOutTitle(5, 50, 5);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
		}
	}

	public static void titlemessage(String str, Player player) {
		IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\":\"" + str + "\"}");
		PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
		PacketPlayOutTitle length = new PacketPlayOutTitle(5, 50, 5);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
	}
}