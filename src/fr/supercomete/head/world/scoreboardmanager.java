package fr.supercomete.head.world;
import java.util.UUID;
import fr.supercomete.head.GameUtils.Fights.Fight;
import fr.supercomete.head.GameUtils.Fights.FightHandler;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import fr.supercomete.head.GameUtils.KTBS_Team;
import fr.supercomete.head.PlayerUtils.BonusHandler;
import fr.supercomete.head.core.KasterborousRunnable;
import fr.supercomete.head.role.Triggers.Trigger_OnScoreBoardUpdate;
import fr.supercomete.head.schema.ScoreBoardSchemaHandler;
import fr.supercomete.head.world.Scoreboard.SimpleScoreboard;
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
import org.bukkit.scoreboard.*;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.TeamMode;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.RoleModifier.BonusHeart;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
public class scoreboardmanager {
	private static Main main;
	private static byte timer=0;
	public scoreboardmanager(Main main) {
		scoreboardmanager.main = main;
	}
    private static KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
	public void ChangeScoreboard() {
		Bukkit.getServer().getScheduler().runTaskTimer(main, ()->{
				timer++;
				timer=(byte)(timer%4);

                for(final KasterborousRunnable runnables : api.getKTBSRunnableProvider().getRunnables()){
                    runnables.onScoreBoardUpdate(api);
                }

				for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
					//Fix bug that kill people if they go under -64 y
					//Players cannot go under -5 if they are into uhc playworld
					if(MapHandler.getMap()!=null&&player.getWorld().equals(MapHandler.getMap().getPlayWorld())) {
						if(player.getLocation().getY()<-5) {
							player.teleport(new Location(player.getWorld(), player.getLocation().getX(), 150, player.getLocation().getZ()));
						}
					}
					SetallScoreboard(player);
                    int count =20;
					if(Main.currentGame.getMode()instanceof CampMode) {
						if(RoleHandler.IsRoleGenerated()) {
							Role role = RoleHandler.getRoleOf(player);
							if(role!=null) {
								if(role instanceof BonusHeart) {
									BonusHeart bonus = (BonusHeart)role;
									count+=bonus.getHPBonus();
								}
								count += role.getPowerOfBonus(BonusType.Heart);

							}
						}
					}
                    int bonusheart = BonusHandler.getTotalOfBonus(player,BonusType.Heart);
                    player.setMaxHealth(count+bonusheart);
					for(final Fight fight: FightHandler.currentFight){
					    FightHandler.update(fight,FightHandler.currentFight);
                    }
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

		}, 0L, 5L);
	}
	public static void SetallScoreboard(Player player) {
		if(timer==0) {
			final SimpleScoreboard ss = ScoreBoardSchemaHandler.get(player);
			Scoreboard sc = ss.getScoreboard();
            float addpercent=0;
			if (RoleHandler.IsRoleGenerated()) {
				if(RoleHandler.getRoleOf(player)!=null) {
					Role role = RoleHandler.getRoleOf(player);
                    addpercent = role.getPowerOfBonus(BonusType.Speed);

				}
				if (RoleHandler.getRoleList().containsKey(player.getUniqueId())) {
                    if(RoleHandler.getRoleOf(player)instanceof Trigger_OnScoreBoardUpdate){
                        ((Trigger_OnScoreBoardUpdate)RoleHandler.getRoleOf(player)).onScoreBoardUpdate(player,ss,sc);
                        ss.send();
                    }
				}
			}
            float addbonus = BonusHandler.getTotalOfBonus(player,BonusType.Speed);
            player.setWalkSpeed(0.2F * ((100.0F+addpercent+addbonus)/100.0F));

			if(Main.currentGame.getMode()instanceof TeamMode) {
                for (KTBS_Team t : api.getTeamProvider().getTeams()) {
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
				TeamManager.teamlist.clear();
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