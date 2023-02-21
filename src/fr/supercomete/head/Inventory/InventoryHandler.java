package fr.supercomete.head.Inventory;
import java.util.Arrays;
import java.util.Collections;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Team;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.structure.Structure;
import net.md_5.bungee.api.ChatColor;
public class InventoryHandler {

	public final static String ClickTypoAdd="§aClique droit pour ajouter ";
	public final static String ClickTypoRemove="§cClique gauche pour retirer ";
	public final static String ClickTypoMassAdd="§aShift Clique droit pour ajouter ";
	public final static String ClickTypoMassRemove="§cShift Clique gauche pour retirer ";
	public final static String ClickBool="§aClique droit pour Activer/Désactiver";
	public static void openinventory(Player player,int id){
		Inventory inv;
        final KtbsAPI api= Bukkit.getServicesManager().load(KtbsAPI.class);
		switch(id) {
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

            for (int r = 9; r < 9 + api.getTeamProvider().getTeams().size(); r++) {
                int index = r - 9;
                Team t = api.getTeamProvider().getTeams().get(index);
                ItemStack TeamStack = InventoryUtils.createColorItem(Material.BANNER, TeamManager.getColorOfShortColor(t.getColor()).toString() + t.getChar() + " " + TeamManager.getNameOfShortColor(t.getColor()), 1, t.getColor());
                ItemMeta itm = TeamStack.getItemMeta();
                itm.setLore(t.getTeamItemLore());
                TeamStack.setItemMeta(itm);
                inv.setItem(r, TeamStack);
            }

			break;
		case 10://Team Config
			inv=Bukkit.createInventory(null, 27, "§dTeam Configuration");
            inv.setItem(11, InventoryUtils.getItem(Material.WOOL, "§bNombre de joueur par équipe: §4" + api.getTeamProvider().getNumberOfMemberPerTeam(), Arrays.asList(InventoryHandler.ClickTypoAdd + "1", InventoryHandler.ClickTypoRemove + "1")));
            inv.setItem(15, InventoryUtils.getItem(Material.PAPER, "§bNombre d'équipes: §a" + api.getTeamProvider().getTeamNumber(), Arrays.asList(InventoryHandler.ClickTypoAdd + "1", InventoryHandler.ClickTypoRemove + "1")));
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