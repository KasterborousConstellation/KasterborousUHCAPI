package fr.supercomete.head.GameUtils.GUI;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.supercomete.ServerExchangeProtocol.File.PlayerAccountManager;
import fr.supercomete.ServerExchangeProtocol.Rank.Rank;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.Permission;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.Inventory.GUI.ConfigurableGUI;
import fr.supercomete.head.Inventory.GUI.EnchantLimitGUI;
import fr.supercomete.head.Inventory.GUI.SlotGUI;
import fr.supercomete.head.Inventory.GUI.TimerGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import fr.supercomete.head.core.Main;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Game;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.TeamMode;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.GameMode.Modes.Null_Mode;
import fr.supercomete.head.Inventory.InventoryHandler;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.PlayerUtility;


public class ModeGUI extends GUI {
    private static KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
	private static final CopyOnWriteArrayList<ModeGUI> allGui = new CopyOnWriteArrayList<ModeGUI>();
	private Inventory inv;
	private final Mode m;
	private final Player player;
	public ModeGUI(Mode mode, Main main) {
		this.m=mode;
		this.player=null;
	}
	public ModeGUI(Mode mode, Player player) {
		this.m = mode;
		this.player = player;
		this.inv = generateinv();
		
		if (player != null)
			allGui.add(this);
	}

	protected Inventory generateinv(){
		Inventory tmp = Bukkit.createInventory(null, 54,m.getName());
		if(m instanceof Null_Mode) {
			tmp=Bukkit.createInventory(null, 54,"§dChoix du mode de jeux");
			for(int i=1;i<api.getModeProvider().getRegisteredModes().size();i++){
				tmp.setItem(i-1, InventoryUtils.getItem(api.getModeProvider().getRegisteredModes().get(i).getMaterial(),"§r§b"+api.getModeProvider().getRegisteredModes().get(i).getName(),api.getModeProvider().getRegisteredModes().get(i).getDescription()));
			}
		}else {
			tmp.setItem(0,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)3));
			tmp.setItem(8,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)3));
			tmp.setItem(53, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)3));
			tmp.setItem(45, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)3));
			tmp.setItem(9,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(1,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(7,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(17, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(46, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(36, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(52, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(44, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(47, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(2,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(6,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(51, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(48, InventoryUtils.createColorItem(Material.STAINED_CLAY, "§rStart", 1, (short)5));
			ItemStack it2=new ItemStack(Material.SKULL_ITEM);
			SkullMeta im2=(SkullMeta)it2.getItemMeta();
			it2.setDurability((short)3);
			im2.setOwner(this.player.getName());
			if(Main.currentGame.getMode()instanceof TeamMode)im2.setLore(Collections.singletonList("§cImcompatible si les équipes sont activées"));
			im2.setDisplayName("§rSlots");
			it2.setItemMeta(im2);
			tmp.setItem(26, it2);
			tmp.setItem(4,InventoryUtils.getItem(Material.PAPER,"§rEvents",null));
			tmp.setItem(5, InventoryUtils.getItem(Material.ANVIL, "§rConfigurable",null));
			tmp.setItem(10, InventoryUtils.getItem(Material.BOOK, "§rWhitelist/Ouverture",null));
			tmp.setItem(16, InventoryUtils.getItem(Material.DIAMOND_CHESTPLATE, "§rEnchantement", null));
			tmp.setItem(37, InventoryUtils.getItem(Material.BOOK_AND_QUILL, "§rScenario", null));
			tmp.setItem(43, InventoryUtils.getItem(Material.CHEST, "§rStuff", null));
			tmp.setItem(18, InventoryUtils.getItem(Material.WATCH, "§rTimers", null));
			tmp.setItem(27, InventoryUtils.getItem(Material.BARRIER, "§rBordure", null));
			ItemStack i = InventoryUtils.createColorItem(Material.GRASS,"§rGénération", 1, (short)3);
			ItemMeta im = i.getItemMeta();
			im.setLore(Collections.singletonList("§cLa génération n'est pas sauvegardée dans les configurations"));
			tmp.setItem(3,  i);
			tmp.setItem(50, InventoryUtils.createColorItem(Material.STAINED_CLAY, "§rStop", 1, (short)14));
			if(m instanceof CampMode) {
				tmp.setItem(35, InventoryUtils.createColorItem(Material.STAINED_CLAY, "§rRôles", 1, (short)6));
			}else if(m instanceof TeamMode) {
				ItemStack titem =InventoryUtils.createColorItem(Material.BANNER, "§rTeams", 1, (short)0);
				if(!Main.currentGame.isGameState(Gstate.Waiting)) {
					ItemMeta itmTeam=titem.getItemMeta();
					itmTeam.setLore(Collections.singletonList("§cImpossible quand la partie a déjà commencer"));
				}
				tmp.setItem(35, titem);
			}
			tmp.setItem(45, InventoryUtils.getItem(Material.BARRIER, "§7Retour", Collections.singletonList("§rRetour au choix du mode de jeu")));
		}
		return tmp;
	}

	public void open() {
		this.inv = generateinv();
		player.openInventory(inv);
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		for (ModeGUI mode : allGui) {
			if (e.getInventory().equals(mode.inv)) {
				e.setCancelled(true);
				Player player = mode.player;
				int currentSlot= e.getSlot();
				if(mode.m instanceof Null_Mode) {
					e.setCancelled(true);

					if (!Main.currentGame.isGameState(Gstate.Waiting)) {
						player.sendMessage(Main.UHCTypo + "§cVous ne pouvez pas changer de mode de jeux pendant une partie");
						return;
					}
                    if (currentSlot < api.getModeProvider().getRegisteredModes().size() - 1) {
                        Mode chosenMode = api.getModeProvider().getRegisteredModes().get(currentSlot + 1);
                        if (chosenMode instanceof Permission) {
                            final Permission perm = (Permission) chosenMode;
                            Rank rank = perm.getPermission();
                            if (!PlayerAccountManager.getPlayerAccount(mode.player).hasRank(rank)) {
                                return;
                            }
                        }
                        Main.currentGame = new Game(chosenMode.getName(), Main.INSTANCE);
                        TeamManager.setupTeams();
                        new ModeGUI(api.getModeProvider().getRegisteredModes().get(currentSlot + 1), mode.player).open();
                        for(Player p : Bukkit.getOnlinePlayers()) {
                            PlayerUtility.GiveHotBarStuff(p);
                        }
                    }
                    break;
				}else{
					e.setCancelled(true);
					switch (currentSlot) {
                        case 4:
                            new EventGUI(player).open();
                            break;
					case 3:
						if(Main.currentGame.getGamestate().equals(Gstate.Waiting)) {
							new GenerationGUI(player).open();
						}else player.sendMessage(Main.UHCTypo+"§cImpossible d'acceder à la génération pendant la partie");
						break;
					case 48:
                        Main.INSTANCE.StartGame(player);
						break;
					case 26:
						new SlotGUI(player).open();
						break;
					case 50:
						Main.StopGame(player);
						break;
					case 10:
						InventoryHandler.openinventory(player, 12);
						break;
					case 18:
						new TimerGUI(player).open();
						break;
					case 45:
						if (Main.currentGame.isGameState(Gstate.Waiting)) {
							Main.currentGame = new Game((new Null_Mode()).getName(),Main.INSTANCE);
							new ModeGUI(new Null_Mode(), mode.player).open();
                        } else
							player.sendMessage(Main.UHCTypo
									+ "§cErreur avant de changer de mode de jeux, veuillez mettre fin à la partie");
						break;
					case 37:
						new ScenarioGUI(player).open();
						break;
					case 27:
						InventoryHandler.openinventory(player, 8);
						break;
					case 43:
						InventoryHandler.openinventory(player, 11);
						break;
					case 16:
						new EnchantLimitGUI(player,true).open();
						break;
					case 5:
						new ConfigurableGUI(player,"Principale").open();
						break;
					case 35:
						if(mode.m instanceof CampMode) {
							new RoleModeGUI(api.getGameProvider().getCurrentGame().getMode(),player).open(0);
						}else if(mode.m instanceof TeamMode) {
                            if(Main.currentGame.getGamestate()==Gstate.Waiting){
                                InventoryHandler.openinventory(mode.player, 10);
                            }else player.sendMessage(Main.UHCTypo+"§cLes équipes ne peuvent pas être modifiés pendant la partie.");
						}
						break;
					default:
						break;
					}
					break;
				}
			}
		}
	}
	// Optimization --> Forget GUI that have been closed >|<
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		for (ModeGUI gui : allGui) {
			if (e.getInventory().equals(gui.inv)) {
				allGui.remove(gui);
			}
		}
	}
}