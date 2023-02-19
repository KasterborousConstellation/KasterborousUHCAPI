package fr.supercomete.head.Listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import fr.supercomete.head.GameUtils.Enchants.EnchantHandler;
import fr.supercomete.head.GameUtils.Enchants.EnchantLimit;
import fr.supercomete.head.GameUtils.Enchants.EnchantType;
import fr.supercomete.head.GameUtils.GUI.EnchantLimitGUI;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.TeamMode;
import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.GameUtils.Time.TimerType;
import fr.supercomete.head.GameUtils.Time.WatchTime;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.supercomete.enums.BiomeGeneration;
import fr.supercomete.enums.GenerationMode;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Team;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.GameUtils.WhiteListHandler;
import fr.supercomete.head.GameUtils.GUI.AdvancedRulesGUI;
import fr.supercomete.head.GameUtils.GUI.ModeGUI;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.Inventory.InventoryHandler;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.structure.Structure;
import fr.supercomete.head.world.WorldSeedGetter;
import fr.supercomete.head.world.worldgenerator;
import net.md_5.bungee.api.ChatColor;

final class InventoryClickListeners implements Listener{
	private final Main main;
	public InventoryClickListeners(Main main) {
		this.main=main;
	}
    private enum ObjectType{
        Null,Iron,Diams,Bow,Rod;
    }
    private boolean ContainEnchant(Enchantment enchant,Map<Enchantment,Integer>map){
        return map.containsKey(enchant);
    }
    private int getEnchantmentLevel(Enchantment enchantment,Map<Enchantment,Integer>map){
        return map.get(enchantment);
    }
	@SuppressWarnings("deprecation")
	@EventHandler
	public void PrepareAnvilEvent(InventoryClickEvent event) {
	    final Player player = (Player)event.getWhoClicked();
		if (event.getClickedInventory()!=null &&event.getClickedInventory().getType() == InventoryType.ANVIL && event.getWhoClicked() instanceof Player) {
			if (event.getClickedInventory() instanceof AnvilInventory) {
                AnvilInventory inv = (AnvilInventory) event.getClickedInventory();
                ItemStack result = inv.getItem(2);
                Map<Enchantment, Integer> map = result.getEnchantments();
                if (result != null && Main.currentGame.IsDisabledAnvil&&event.getSlot()==2) {
                    ObjectType type = ObjectType.Null;
                    if (result.getType().equals(Material.IRON_SWORD) || result.getType().equals(Material.IRON_HELMET)
                            || result.getType().equals(Material.IRON_CHESTPLATE)
                            || result.getType().equals(Material.IRON_LEGGINGS)
                            || result.getType().equals(Material.IRON_BOOTS)) {
                        type = ObjectType.Iron;
                    } else if (result.getType().equals(Material.DIAMOND_SWORD)
                            || result.getType().equals(Material.DIAMOND_HELMET)
                            || result.getType().equals(Material.DIAMOND_CHESTPLATE)
                            || result.getType().equals(Material.DIAMOND_LEGGINGS)
                            || result.getType().equals(Material.DIAMOND_BOOTS)) {
                        type = ObjectType.Diams;
                    } else {
                        if (result.getType() == Material.BOW) {
                            type = ObjectType.Bow;
                        } else if (result.getType() == Material.FISHING_ROD) {
                            type = ObjectType.Rod;
                        }
                    }
                    boolean hasIllegalEnchant = false;
                    if (type == ObjectType.Bow) {
                        for (final EnchantLimit limit : EnchantHandler.getLimite(EnchantType.Bow)) {
                            if (ContainEnchant(limit.getEnchant(), map)) {
                                int lvl = getEnchantmentLevel(limit.getEnchant(), map);

                                if (lvl > limit.getMax()) {
                                    hasIllegalEnchant = true;
                                }
                            }
                        }
                    } else if (type == ObjectType.Iron) {
                        for (final EnchantLimit limit : EnchantHandler.getLimite(EnchantType.Iron)) {
                            if (ContainEnchant(limit.getEnchant(), map)) {
                                int lvl = getEnchantmentLevel(limit.getEnchant(), map);
                                if (lvl > limit.getMax()) {
                                    hasIllegalEnchant = true;
                                }
                            }
                        }
                    } else if (type == ObjectType.Diams) {
                        for (final EnchantLimit limit : EnchantHandler.getLimite(EnchantType.Diamond)) {
                            if (ContainEnchant(limit.getEnchant(), map)) {
                                int lvl = getEnchantmentLevel(limit.getEnchant(), map);
                                if (lvl > limit.getMax()) {
                                    hasIllegalEnchant = true;
                                }
                            }
                        }
                    } else if (type == ObjectType.Rod) {
                        for (final EnchantLimit limit : EnchantHandler.getLimite(EnchantType.Rod)) {
                            if (ContainEnchant(limit.getEnchant(), map)) {
                                int lvl = getEnchantmentLevel(limit.getEnchant(), map);
                                if (lvl > limit.getMax()) {
                                    hasIllegalEnchant = true;
                                }
                            }
                        }
                    }
                    if (type == ObjectType.Iron || type == ObjectType.Diams) {
                        for (final EnchantLimit limit : EnchantHandler.getLimite(EnchantType.ALL)) {
                            if (ContainEnchant(limit.getEnchant(), map)) {
                                int lvl = getEnchantmentLevel(limit.getEnchant(), map);
                                if (lvl > limit.getMax()) {
                                    hasIllegalEnchant = true;
                                }
                            }
                        }
                    }
                    if (hasIllegalEnchant) {
                        event.setCancelled(true);
                        player.sendMessage(Main.UHCTypo + "§cImpossible: Cet object contient des enchantements illégaux. -> /rules");
                    }
                }
            }
		}
	}

	@EventHandler
	public void OnCraft(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;
		if (event.getClickedInventory().getType() == InventoryType.WORKBENCH && event.getWhoClicked() instanceof Player) {
			if (event.getClickedInventory() instanceof CraftingInventory) {
//				final Player player = (Player) event.getWhoClicked();
				CraftingInventory inv = (CraftingInventory) event.getClickedInventory();
				ItemStack result = inv.getItem(0);
				if(result!=null&&result.getType().equals(Material.DIAMOND_LEGGINGS) && Configurable.ExtractBool(Configurable.LIST.DiamondLeggings)){
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage(Main.UHCTypo+"§cCet item est désactivé.");
                }

			}
		}
	}
	@EventHandler @SuppressWarnings("unused")
	public void onClick(InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();
		final Inventory currentInv = event.getInventory();
		
		final ItemStack currentItem = event.getCurrentItem();
		final ClickType currentClick = event.getClick();
		final int currentSlot = event.getSlot();
		switch (currentInv.getName()) {
		case "§dMultiplicateur":
			event.setCancelled(true);
			switch (currentSlot) {
			default:
				break;
			}
			break;
		case "§dSlots":
			event.setCancelled(true);
			switch (currentSlot) {
			case 11:
				if (Main.currentGame.getMaxNumberOfplayer() >= 10) {
					Main.currentGame.setMaxNumberOfplayer(Main.currentGame.getMaxNumberOfplayer() - 10);
					main.updateSlotsInventory(player);
				}
				break;
			case 12:
				if (Main.currentGame.getMaxNumberOfplayer() >= 1) {
					Main.currentGame.setMaxNumberOfplayer(Main.currentGame.getMaxNumberOfplayer() - 1);
					main.updateSlotsInventory(player);
				}
				break;
			case 14:
				Main.currentGame.setMaxNumberOfplayer(Main.currentGame.getMaxNumberOfplayer() + 1);
				main.updateSlotsInventory(player);
				break;
			case 15:
				Main.currentGame.setMaxNumberOfplayer(Main.currentGame.getMaxNumberOfplayer() + 10);
				main.updateSlotsInventory(player);
				break;
			case 22:
				new ModeGUI(Main.currentGame.getMode(), player).open();
				break;
			default:
				break;
			}
			break;

		case "§dRules":
			event.setCancelled(true);
			switch (currentSlot) {
			case 22:
				InventoryHandler.openinventory(player, 6);
				break;
			case 24:
				new EnchantLimitGUI(player,false).open();
				break;
			case 20:
				new AdvancedRulesGUI(player).open();
                default:
				break;
			}
			break;
		case "§dScénarios Actif":
            case "§dEnchants":
                event.setCancelled(true);
			switch (currentSlot) {
			case 49:
				InventoryHandler.openinventory(player, 5);
				break;
			default:
				break;
			}
			break;
		case "§dTimers":
			event.setCancelled(true);
			switch (currentSlot) {
			case 49:
				new ModeGUI(Main.currentGame.getMode(), player).open();
				break;
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
				int[] add = { -3600, -600, -60, -10, 0, 10, 60, 600, 3600 };
				final ArrayList<Timer> timerlist =main.getCompatibleTimer();
				int addNow = add[currentSlot-9];
				ArrayList<Timer> drawnTimer = new ArrayList<>();
				for(Timer t: main.getCompatibleTimer()){
				    if(t.getType()== TimerType.TimeDependent && (Main.currentGame.getTimer(t).getData()-Main.currentGame.getTime())>0){
				        drawnTimer.add(t);
                    }
				    if(t.getType()== TimerType.Literal){
                        drawnTimer.add(t);
                    }
                }
				WatchTime selectedTimer = Main.currentGame.getTimer(drawnTimer.get(main.Selected));
				if(selectedTimer.getId().getType() == TimerType.Literal||selectedTimer.getData()-Main.currentGame.getTime()+ addNow >0){
                    selectedTimer.add(addNow);
                    main.updateTimerInventory(player);
                    player.sendMessage(drawnTimer.get(main.Selected).getName()+": "+ TimeUtility.transform(selectedTimer.getData(), "§d", "§d", "§d"));
                }

				break;
			default:
                ArrayList<Timer> timers = new ArrayList<>();
                for(Timer t: main.getCompatibleTimer()){
                    if(t.getType()== TimerType.TimeDependent && (Main.currentGame.getTimer(t).getData()-Main.currentGame.getTime())>0){
                        timers.add(t);
                    }
                    if(t.getType()== TimerType.Literal){
                        timers.add(t);
                    }
                }
				if (currentSlot >= 18 && currentSlot < (18 + timers.size())) {
					main.Selected = currentSlot - 18;
					main.updateTimerInventory(player);
				}
				break;
			}
			break;
		case "§dBordure":
			event.setCancelled(true);
			switch (currentSlot) {
			case 22:
				new ModeGUI(Main.currentGame.getMode(), player).open();
				break;
			case 12:
				if (currentClick.isShiftClick() && currentClick.isRightClick())
					Main.currentGame.setCurrentBorder(Main.currentGame.getCurrentBorder() + 100);
				if (currentClick.isRightClick() && !currentClick.isShiftClick())
					Main.currentGame.setCurrentBorder(Main.currentGame.getCurrentBorder() + 10);
				if (currentClick.isLeftClick() && currentClick.isShiftClick()
						&& Main.currentGame.getCurrentBorder() - 100 >= 600
						&& Main.currentGame.getCurrentBorder() - 100 >= Main.currentGame.getFinalBorder())
					Main.currentGame.setCurrentBorder(Main.currentGame.getCurrentBorder() - 100);
				if (currentClick.isLeftClick() && (!currentClick.isShiftClick())
						&& Main.currentGame.getCurrentBorder() - 10 >= 600
						&& Main.currentGame.getCurrentBorder() - 10 >= Main.currentGame.getFinalBorder())
					Main.currentGame.setCurrentBorder(Main.currentGame.getCurrentBorder() - 10);
				Math.round(Main.currentGame.getBorderSpeed());
				player.getOpenInventory().setItem(12, InventoryUtils.getItem(Material.BARRIER,
						ChatColor.BOLD + "§bBordure Initale§r: §a" + Main.currentGame.getCurrentBorder(),
						Arrays.asList("", InventoryHandler.ClickTypoAdd + "10",
								InventoryHandler.ClickTypoMassAdd + "100", InventoryHandler.ClickTypoRemove + "10",
								InventoryHandler.ClickTypoMassRemove + "100")));
				break;
			case 13:
				if (currentClick.isShiftClick() && currentClick.isRightClick())
					Main.currentGame.setBorderSpeed(Main.currentGame.getBorderSpeed() + 0.5);
				if (currentClick.isRightClick() && !currentClick.isShiftClick())
					Main.currentGame.setBorderSpeed(Main.currentGame.getBorderSpeed() + 0.1);
				if (currentClick.isLeftClick() && currentClick.isShiftClick()
						&& Main.currentGame.getBorderSpeed() - 0.5 > 0)
					Main.currentGame.setBorderSpeed(Main.currentGame.getBorderSpeed() - 0.5);
				if (currentClick.isLeftClick() && (!currentClick.isShiftClick())
						&& Main.currentGame.getBorderSpeed() - 0.1 > 0)
					Main.currentGame.setBorderSpeed(Main.currentGame.getBorderSpeed() - 0.1);
				double i = Main.currentGame.getBorderSpeed();
				i *= 10;
				i = Math.round(i);
				i /= 10;
				player.getOpenInventory().setItem(13, InventoryUtils.getItem(Material.FEATHER,
						ChatColor.BOLD + "§bVitesse de la bordure§r: §a" + i + "bps",
						Arrays.asList("", InventoryHandler.ClickTypoAdd + "0.1",
								InventoryHandler.ClickTypoMassAdd + "0.5", InventoryHandler.ClickTypoRemove + "0.1",
								InventoryHandler.ClickTypoMassRemove + "0.5")));
				break;
			case 14:
				if (currentClick.isShiftClick() && currentClick.isRightClick()
						&& Main.currentGame.getCurrentBorder() >= (Main.currentGame.getFinalBorder() + 100))
					Main.currentGame.setFinalBorder(Main.currentGame.getFinalBorder() + 100);
				if (currentClick.isRightClick() && !currentClick.isShiftClick()
						&& Main.currentGame.getCurrentBorder() >= (Main.currentGame.getFinalBorder() + 10))
					Main.currentGame.setFinalBorder(Main.currentGame.getFinalBorder() + 10);
				if (currentClick.isLeftClick() && currentClick.isShiftClick()
						&& Main.currentGame.getFinalBorder() - 100 >= 100)
					Main.currentGame.setFinalBorder(Main.currentGame.getFinalBorder() - 100);
				if (currentClick.isLeftClick() && (!currentClick.isShiftClick())
						&& Main.currentGame.getFinalBorder() - 10 >= 100)
					Main.currentGame.setFinalBorder(Main.currentGame.getFinalBorder() - 10);
				player.getOpenInventory().setItem(14, InventoryUtils.getItem(Material.IRON_FENCE,
						ChatColor.BOLD + "§bBordure Finale§r: §a" + Main.currentGame.getFinalBorder(),
						Arrays.asList("", InventoryHandler.ClickTypoAdd + "10",
								InventoryHandler.ClickTypoMassAdd + "100", InventoryHandler.ClickTypoRemove + "10",
								InventoryHandler.ClickTypoMassRemove + "100")));
				break;
			default:
				break;
			}
			break;
		case "§dMondes":
			event.setCancelled(true);
			switch (currentSlot) {
			case 0:
				player.teleport(new Location(Bukkit.getWorld("world"), main.getConfig().getInt("serverapi.spawn.x"),
						main.getConfig().getInt("serverapi.spawn.y"),
						main.getConfig().getInt("serverapi.spawn.z")));
				break;
			case 1:
				player.teleport(new Location(MapHandler.getMap().getPlayWorld(), 0,100,0));
				break;
			default:
				if(currentSlot>1 && currentSlot < 2+Main.currentGame.getMode().getStructure().size()) {
					final Structure structure = Main.currentGame.getMode().getStructure().get(currentSlot-2);
					structure.teleport(player);
				}
				break;
			}
			break;
		case "§dTeam":
			event.setCancelled(true);
			switch (currentSlot) {
			default:
				if (currentSlot > 8 && currentSlot < 9 + TeamManager.teamlist.size()) {
					Team team = TeamManager.teamlist.get(currentSlot - 9);

					Bukkit.broadcastMessage(Main.UHCTypo + player.getName() + " a rejoint l'équipe "
							+ TeamManager.getColorOfShortColor(team.getColor()) + team.getTeamName());
					for (Team t : TeamManager.teamlist)
						if (t.isMemberInTeam(player.getUniqueId()))
							t.removeMember(player.getUniqueId());// Remove The player if the player have join another
																	// team
                    TeamManager.teamlist.get(currentSlot - 9).addMembers(player.getUniqueId());
					player.closeInventory();
				}
				break;
			}
			break;
		case "§dTeam Configuration":
			event.setCancelled(true);
			switch (currentSlot) {
			case 11:
                TeamMode teammode =(TeamMode) Main.currentGame.getMode();
				if (currentClick.isRightClick()) {
					if (TeamManager.NumberOfPlayerPerTeam < teammode.TeamSizeBound().getMax()) {
                        TeamManager.NumberOfPlayerPerTeam=(TeamManager.NumberOfPlayerPerTeam + 1);
					}
				} else {
					if (TeamManager.NumberOfPlayerPerTeam > teammode.TeamSizeBound().getMin()) {
                        TeamManager.NumberOfPlayerPerTeam=(TeamManager.NumberOfPlayerPerTeam - 1);
					}
				}
				main.updateTeamsInventory(player);
				TeamManager.setupTeams();
				break;
			case 15:
				if (currentClick.isRightClick() && TeamManager.TeamNumber + 1 < 37)
                    TeamManager.TeamNumber=(TeamManager.TeamNumber + 1);
				if (currentClick.isLeftClick() && TeamManager.TeamNumber - 1 >= 2)
                    TeamManager.TeamNumber=(TeamManager.TeamNumber - 1);
				main.updateTeamsInventory(player);
				TeamManager.setupTeams();
				break;
			case 22:
				new ModeGUI(Main.currentGame.getMode(), player).open();
				break;
			default:
				break;
			}
			break;
		case "§dTéléportations":
			switch (currentSlot) {
			case 1:
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.teleport(new Location(p.getWorld(), main.getConfig().getInt("serverapi.ruleroom.x"),
							main.getConfig().getInt("serverapi.ruleroom.y"),
							main.getConfig().getInt("serverapi.ruleroom.z")));
				}
				break;
			case 0:
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.teleport(new Location(p.getWorld(), main.getConfig().getInt("serverapi.spawn.x"),
							main.getConfig().getInt("serverapi.spawn.y"),
							main.getConfig().getInt("serverapi.spawn.z")));
				}
				break;
			case 2:
				player.teleport(new Location(player.getWorld(), main.getConfig().getInt("serverapi.hostbox.x"),
						main.getConfig().getInt("serverapi.hostbox.y"),
						main.getConfig().getInt("serverapi.hostbox.z")));
				break;
			default:
				break;
			}
			break;
		case "§dStuff":
			event.setCancelled(true);
			switch (currentSlot) {
			case 22:
				new ModeGUI(Main.currentGame.getMode(), player).open();
				break;
			case 14:
				ArrayList<UUID> it = new ArrayList<UUID>();
				it.add(player.getUniqueId());
				PlayerUtility.giveStuff(it);
				player.setGameMode(GameMode.CREATIVE);
				break;
			case 12:
				PlayerUtility.saveStuff(player);
				player.getInventory().clear();
				break;
			default:
				break;
			}
			break;
		case "§dInventaire":
			event.setCancelled(true);
			break;
		case "§dWhitelist":
			event.setCancelled(true);
			switch (currentSlot) {
			case 22:
				new ModeGUI(Main.currentGame.getMode(), player).open();
				break;
			case 11:
				player.sendMessage("§aWhitelist "+Main.TranslateBoolean(Bukkit.hasWhitelist()));
				for(final OfflinePlayer offp : Bukkit.getWhitelistedPlayers()) {
					player.sendMessage("  §b"+offp.getName()+" "+ Main.getCheckMark(offp.isOnline()));
				}
				break; 
			case 13:
				Bukkit.setWhitelist(!Bukkit.hasWhitelist());
				main.updateWhiteListInventory(player);
				break;
			case 14:
				WhiteListHandler.addAllOnlinePlayerToWhiteList(player, true);
				break;
			case 12:
				WhiteListHandler.clearWhitelist();
				WhiteListHandler.addAllOnlinePlayerToWhiteList(null, false);
				break;

			default:
				break;
			}
			break;
            case "§dConfiguration Avancée":
			event.setCancelled(true);
			switch (currentSlot) {
			case 49:
				new ModeGUI(Main.currentGame.getMode(), player).open();
				break;
			default:
			}
			break;
		}
	}

}
