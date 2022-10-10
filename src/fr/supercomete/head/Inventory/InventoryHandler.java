package fr.supercomete.head.Inventory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fr.supercomete.head.GameUtils.Enchants.EnchantHandler;
import fr.supercomete.head.GameUtils.Enchants.EnchantType;
import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;
import fr.supercomete.head.GameUtils.Time.TimerType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.Team;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.structure.Structure;
import fr.supercomete.head.world.WorldSeedGetter;
import net.md_5.bungee.api.ChatColor;
public class InventoryHandler {
	private static Main main;
	public InventoryHandler(Main main) {
		InventoryHandler.main=main;
	}
	public final static String ClickTypoAdd="§aClique droit pour ajouter ";
	public final static String ClickTypoRemove="§cClique gauche pour retirer ";
	public final static String ClickTypoMassAdd="§aShift Clique droit pour ajouter ";
	public final static String ClickTypoMassRemove="§cShift Clique gauche pour retirer ";
	public final static String ClickBool="§aClique droit pour Activer/Désactiver";
	public static void openinventory(Player player,int id){
		Inventory inv;
		switch(id) {
		case 2:
			inv=Bukkit.createInventory(null, 54,"§dGénération");
			for(int i=0;i<9;i++) {
				inv.setItem(i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 11));
			}
			ItemStack it = Main.generator.getBiome().getItem();
			ItemMeta meta = it.getItemMeta();
			meta.setDisplayName("§bBiome: §a"+Main.generator.getBiome().getName());
			ArrayList<String> strl =Main.SplitCorrectlyString("Défini le biome de la carte. Cliquer pour changer.", 45, "§7");
			strl.add("§cNombre de Seeds: §4"+WorldSeedGetter.getAmountOfSeed(Main.generator.getBiome()));
			meta.setLore(strl);
			it.setItemMeta(meta);
			inv.setItem(22, it);

			for(int i=0;i<9;i++) {
				inv.setItem(53-i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 11));
			}
			inv.setItem(49, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
			player.openInventory(inv);
			Main.updateGeneration(player);
			return;
		case 3://Slots GUI
			inv=Bukkit.createInventory(null, 27,"§dSlots");
			if(Main.currentGame.IsTeamActivated())return;
			inv.setItem(0,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
			inv.setItem(8,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
			inv.setItem(26, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
			inv.setItem(18, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
			inv.setItem(11, InventoryUtils.getItem(Material.STONE_BUTTON, "§r-10 Slots", null));
			inv.setItem(12, InventoryUtils.getItem(Material.STONE_BUTTON, "§r-1 Slots", null));
			String name=(Main.currentGame.getMaxNumberOfplayer()==0)?"§rSlots:§a Illimité":"§rSlots:§a "+ Main.currentGame.getMaxNumberOfplayer();
			inv.setItem(13, InventoryUtils.getItem(Material.PAPER,name, null));
			inv.setItem(14, InventoryUtils.getItem(Material.STONE_BUTTON, "§r+1 Slots", null));
			inv.setItem(15, InventoryUtils.getItem(Material.STONE_BUTTON, "§r+10 Slots", null));
			inv.setItem(22, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
			break;
		case 4://Scenarios GUI
			inv=Bukkit.createInventory(null, 54,"§dScenarios");
			for(int i=0;i<9;i++) {
				inv.setItem(i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 2));
			}
			for(int i=0;i<9;i++) {
				inv.setItem(53-i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 2));
			}
			for(int i=0;KtbsAPI.getScenarios().size()>i;i++) {
				KasterborousScenario sc = KtbsAPI.getScenarios().get(i);
				String bool=(Main.currentGame.getScenarios().contains(sc))?"§aOn":"§cOff";
				ArrayList<String>Lines=new ArrayList<String>();
				if(sc==Scenarios.DiamondLimit){
                    Lines.add("§3Limite de diamant: §b"+Main.currentGame.getDiamondlimit());
                }
				String compatibility=(sc.getCompatiblity().IsCompatible(Main.currentGame.getMode()))?"§a✔":"§c✖";
				Lines.add(compatibility+"§r§7Compatibilité");
				ItemStack item = InventoryUtils.getItem(sc.getMat(),"§b"+sc.getName()+" "+bool,Lines);
				if(Main.currentGame.getScenarios().contains(sc)) {
					item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
					ItemMeta ime=item.getItemMeta();
					ime.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					item.setItemMeta(ime);
				}
				inv.setItem(i+9, item);
			}
			inv.setItem(49,InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
			break;
		case 5://Rules GUI
			inv=Bukkit.createInventory(null, 54,"§dRules");
			inv.setItem(0, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
			inv.setItem(8, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
			inv.setItem(53, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
			inv.setItem(45, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
			inv.setItem(9, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
			inv.setItem(1, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
			inv.setItem(7, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
			inv.setItem(17, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
			inv.setItem(46, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
			inv.setItem(36, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
			inv.setItem(52, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
			inv.setItem(44, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
			inv.setItem(47, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
			inv.setItem(2, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
			inv.setItem(6, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
			inv.setItem(51, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
			inv.setItem(22, InventoryUtils.getItem(Material.BOOKSHELF, "§bScénarios", null));
			inv.setItem(20, InventoryUtils.getItem(Material.PAPER, "§bRegles avancées", null));
			inv.setItem(24, InventoryUtils.getItem(Material.DIAMOND_CHESTPLATE, "§bEnchants", Arrays.asList(
                    EnchantHandler.generateLine(Enchantment.DAMAGE_ALL, EnchantType.Iron),
                    EnchantHandler.generateLine(Enchantment.DAMAGE_ALL, EnchantType.Diamond),
			        EnchantHandler.generateLine(Enchantment.PROTECTION_ENVIRONMENTAL, EnchantType.Iron),
                    EnchantHandler.generateLine(Enchantment.PROTECTION_ENVIRONMENTAL, EnchantType.Diamond),
                    EnchantHandler.generateLine(Enchantment.KNOCKBACK, EnchantType.ALL),
                    EnchantHandler.generateLine(Enchantment.ARROW_KNOCKBACK, EnchantType.Bow)
            )));
			break;
		case 6://Active Scénarios GUI
			inv=Bukkit.createInventory(null, 54,"§dScénarios Actif");
			for(int i=0;i<9;i++){
				inv.setItem(i,InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 2));
			}
			for(int i=0;i<9;i++){
				inv.setItem(53-i,InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 2));
			}
			for(int i=0 ;i<Main.currentGame.getScenarios().size();i++){
				KasterborousScenario sc=Main.currentGame.getScenarios().get(i);
				ArrayList<String>Lines=new ArrayList<String>();
				String compatibility=(sc.getCompatiblity().IsCompatible(Main.currentGame.getMode()))?"§a✔":"§c✖";
				if(sc==Scenarios.DiamondLimit)Lines.add("§3Limite de diamant: §b"+Main.currentGame.getDiamondlimit());
				Lines.add(compatibility+"§r§7Compatibilité:");
				ItemStack item = InventoryUtils.getItem(sc.getMat(),"§b"+sc.getName(),Lines);
				if(Main.currentGame.getScenarios().contains(sc)){
					item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
					ItemMeta ime=item.getItemMeta();
					ime.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					item.setItemMeta(ime);
				}
				inv.setItem(i+9, item);
			}
			inv.setItem(49, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au règles")));
			break;
		case 7://Timers
			inv=Bukkit.createInventory(null, 54,"§dTimers");
			int i=18;
			/////////////////////////////////////////////////////////////////////////////////////////////////////
			for(int e=0;e<9;e++){
				inv.setItem(e,InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 11));
			}
			for(int e=0;e<9;e++){
				inv.setItem(53-e,InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 11));
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////
			for(Timer t:main.getCompatibleTimer()){
			    if(t.getType()== TimerType.TimeDependent &&(Main.currentGame.getTimer(t).getData() - Main.currentGame.getTime()) >0){
                    inv.setItem(i, InventoryUtils.getItem(Material.PAPER,main.generateNameTimer(t),null));
                    i++;
                }
                if(t.getType()== TimerType.Literal){
                    inv.setItem(i, InventoryUtils.getItem(Material.PAPER,main.generateNameTimer(t),null));
                    i++;
                }
			}
			inv.setItem(9, InventoryUtils.getItem(Material.STONE_BUTTON, "§r-60m", null));
			inv.setItem(10, InventoryUtils.getItem(Material.STONE_BUTTON, "§r-10m", null));
			inv.setItem(11, InventoryUtils.getItem(Material.STONE_BUTTON, "§r-1m", null));
			inv.setItem(12, InventoryUtils.getItem(Material.STONE_BUTTON, "§r-10s", null));
			inv.setItem(13, InventoryUtils.getItem(Material.PAPER, "§r"+main.generateNameTimer(main.getCompatibleTimer().get(main.Selected)), null));
			inv.setItem(14, InventoryUtils.getItem(Material.STONE_BUTTON, "§r+10s", null));
			inv.setItem(15, InventoryUtils.getItem(Material.STONE_BUTTON, "§r+1m", null));
			inv.setItem(16, InventoryUtils.getItem(Material.STONE_BUTTON, "§r+10m", null));
			inv.setItem(17, InventoryUtils.getItem(Material.STONE_BUTTON, "§r+60m", null));
			inv.getItem(18+main.Selected).setType(Material.COMPASS);
			inv.setItem(49, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
			break;
		case 8://Border GUI;
			inv=Bukkit.createInventory(null, 27,"§dBordure");
			inv.setItem(0,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
			inv.setItem(8,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
			inv.setItem(26, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
			inv.setItem(18, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)6));
			inv.setItem(12, InventoryUtils.getItem(Material.BARRIER, ChatColor.BOLD+"§bBordure Initale§r: §a"+Main.currentGame.getFirstBorder(), Arrays.asList("",ClickTypoAdd+"10",ClickTypoMassAdd+"100",ClickTypoRemove+"10",ClickTypoMassRemove+"100")));
			inv.setItem(13, InventoryUtils.getItem(Material.FEATHER, ChatColor.BOLD+"§bVitesse de la bordure§r: §a"+Math.round(Main.currentGame.getBorderSpeed())+"bps", Arrays.asList("",ClickTypoAdd+"0.1",ClickTypoMassAdd+"0.5",ClickTypoRemove+"0.1",ClickTypoMassRemove+"0.5")));
			inv.setItem(14, InventoryUtils.getItem(Material.IRON_FENCE, ChatColor.BOLD+"§bBordure Finale§r: §a"+Main.currentGame.getFinalBorder(), Arrays.asList("",ClickTypoAdd+"10",ClickTypoMassAdd+"100",ClickTypoRemove+"10",ClickTypoMassRemove+"100")));
			inv.setItem(22, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
			break;
		case 9://Team
			inv=Bukkit.createInventory(null, 54,"§dTeam");
			if(!Main.currentGame.isGameState(Gstate.Waiting))return;
			for(int h=0;h<9;h++)inv.setItem(h, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)0));
			for(int h=45;h<54;h++)inv.setItem(h, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)0));
			for(int r=9;r<9+Main.currentGame.getTeamList().size();r++){
				int index =r-9;
				Team t=Main.currentGame.getTeamList().get(index);
				ItemStack TeamStack=InventoryUtils.createColorItem(Material.BANNER,TeamManager.getColorOfShortColor(t.getColor()).toString()+t.getChar()+" "+TeamManager.getNameOfShortColor(t.getColor()),1, t.getColor());
				ItemMeta itm=TeamStack.getItemMeta();
				itm.setLore(t.getTeamItemLore());
				TeamStack.setItemMeta(itm);
				inv.setItem(r,TeamStack);
			}
			break;
		case 10://Team Config
			inv=Bukkit.createInventory(null, 27, "§dTeam Configuration");
			short col=(short) ((Main.currentGame.IsTeamActivated())?5:14);
			String bool=(Main.currentGame.IsTeamActivated())?"§aOn":"§cOff";
			inv.setItem(11, InventoryUtils.getItem(Material.WOOL, "§bNombre de joueur par équipe: §4"+Main.currentGame.getNumberOfPlayerPerTeam(),Arrays.asList(InventoryHandler.ClickTypoAdd+"1",InventoryHandler.ClickTypoRemove+"1")));
			inv.setItem(13, InventoryUtils.createColorItem(Material.WOOL, "§bTeam: "+bool, 1, col));
			inv.setItem(15, InventoryUtils.getItem(Material.PAPER, "§bNombre d'équipes: §a"+Main.currentGame.getTeamNumber(), Arrays.asList(InventoryHandler.ClickTypoAdd+"1",InventoryHandler.ClickTypoRemove+"1")));
			inv.setItem(22, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
			break;
		case 11://StuffConfig
			inv=Bukkit.createInventory(null, 27, "§dStuff");
			inv.setItem(12, InventoryUtils.getItem(Material.CHEST, "§bSauvegarder le stuff", Main.SplitCorrectlyString("§rSauvegarde votre stuff actuel comme stuff de départ pour tout les joueurs", 25, "§r")));
			inv.setItem(13, InventoryUtils.getItem(Material.PAPER, "§bNote",  Main.SplitCorrectlyString("Les têtes sont des objets interdits. Les enchantements illégaux ne peuvent pas être sauvegardés. Les pièces d'armures finiront dans l'inventaire des joueurs quelque soit les slots dans lesquels les pièces d'armures ont été mise.", 32, "§7•")));
			inv.setItem(14, InventoryUtils.getItem(Material.CHEST, "§bCharger le stuff actuel", Main.SplitCorrectlyString("§rCliquer vous mettra en créatif pour pouvoir configurer le stuff", 25, "§r")));
			inv.setItem(22, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
			break;
		case 12://whiteList
			inv=Bukkit.createInventory(null, 27,"§dWhitelist");
			for(int h=0;h<9;h++)inv.setItem(h,InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE," ",1,(short)3));
			for(int h=18;h<27;h++)inv.setItem(h,InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE," ",1,(short)3));
			short color=(short)((Bukkit.hasWhitelist())?5:14);
			final String booleans=(Bukkit.hasWhitelist())?"§aOn":"§cOff";
			inv.setItem(12, InventoryUtils.getItem(Material.BARRIER, "§cNettoyer la whitelist", Main.SplitCorrectlyString("Tout les joueurs sauf les joueurs connectés seront enlevés de la whitelist", 35, "§7")));
			inv.setItem(13, InventoryUtils.createColorItem(Material.WOOL, "§bWhiteList: "+booleans, 1, color));
			inv.setItem(14, InventoryUtils.getItem(Material.PAINTING, "§bRemplir la whitelist",Main.SplitCorrectlyString("Ajoute tous les joueurs connectés à la whitelist", 35, "§7")));
			inv.setItem(22, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
			inv.setItem(11, InventoryUtils.getItem(Material.SIGN,"§rAfficher les joueurs whitelist", Main.SplitCorrectlyString("Affiche tout les joueurs whitelist, ainsi que si il sont connecté.", 40, "§7")));
			break;
		case 15://Configuration Avancée
			inv=Bukkit.createInventory(null, 54,"§dConfiguration Avancée");
			player.openInventory(inv);
			return;
		case 19://Fichier de sauvegarde
			inv= Bukkit.createInventory(null, 54, "§dFichiers de sauvegarde");
			player.openInventory(inv);
			Main.updateConfigFile(player);
			return;
		case 20: //Téléportations
			inv= Bukkit.createInventory(null,9,"§dTéléportations");
			inv.setItem(1, InventoryUtils.getItem(Material.BOOK, "§dTéléportation à la salle des règles", null));
			inv.setItem(0,InventoryUtils.getItem(Material.NETHER_STAR, "§dTéléportation au spawn", null));
			inv.setItem(2,InventoryUtils.getItem(Material.BRICK, "§dTéléportation dans la boite à host", null));
			break;
		case 22://world generation menu teleportation
			inv= Bukkit.createInventory(null, 9, "§dMondes");
			inv.setItem(0, InventoryUtils.getItem(Material.REDSTONE_BLOCK, "§aLobby", Collections.singletonList("§7Cliquez ici pour vous téléporter au Lobby")));
			inv.setItem(1, InventoryUtils.getItem(Material.GRASS, "§bArena", Collections.singletonList("§7Cliquez ici pour vous téléporter au monde Arena")));
			int h =0;
			for(Structure structure:Main.currentGame.getMode().getStructure()) {
				inv.setItem(2+h, InventoryUtils.getItem(Main.currentGame.getMode().getMaterial(), "§b"+structure.getStructurename(), null));
				h++;
			}
			break;
		default:
			inv=Bukkit.createInventory(null, 54,"§0Null");
			break;
		}
		player.openInventory(inv);
		return;
	} 
}