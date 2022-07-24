package fr.supercomete.head.GameUtils.GUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.content.DWUHC.Kate_Stewart;
import fr.supercomete.head.role.content.DWUHC.Kate_Stewart.Indice;
import fr.supercomete.tasks.generatorcycle;

public class StewartGUI extends GUI {
	private static final CopyOnWriteArrayList<StewartGUI> allGui = new CopyOnWriteArrayList<StewartGUI>();
	private Inventory inv;
	private final Kate_Stewart role;
	private Player player;
	private UUID openPlayer=null;
	public StewartGUI(Kate_Stewart role, Main main) {
		this.role=role;
		this.player=null;
	}
	public StewartGUI(Kate_Stewart role, Player player) {
		this.role=role;
		this.player=null;
		this.player=player;
		if (player != null)
			allGui.add(this);
	}
	protected Inventory generateinv() {
		Inventory tmp = Bukkit.createInventory(null, 54,role.getName()+" Indices");
		if(openPlayer==null) {
			for(int i =0;i<9;i++) {
				tmp.setItem(i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, TeamManager.getShortOfChatColor(ChatColor.AQUA)));
			}
			ItemStack it = InventoryUtils.getItem(Material.BOOK, "§aRépartition des Indices", Arrays.asList(Camps.DoctorCamp.getColor()+Camps.DoctorCamp.getName()+":","  §f70% §1Bleu","  §f20% §4Rouge","  §f10% §eJaune",Camps.EnnemiDoctorCamp.getColor()+Camps.EnnemiDoctorCamp.getName()+":","  §f70% §4Rouge","  §f20% §1Bleu","  §f10% §eJaune",Camps.Neutral.getColor()+Camps.Neutral.getName()+":","  §f40% §eJaune","  §f30% §1Bleu","  §f30% §4Rouge"));
			tmp.setItem(4, it);
			
			ItemStack counter = InventoryUtils.getItem(Material.PAPER, "§dInfos indices", Arrays.asList(
					"§eVous avez actuellement "+ChatColor.LIGHT_PURPLE+this.role.getNumberOfIndices()+"§e indices.",
					Main.getCheckMark(role.getNumberOfIndices()>=50)+" "+"Vitesse le jour: 50 Indices",
					Main.getCheckMark(role.getNumberOfIndices()>=100)+" "+"Vitesse de fouille x2: 100 Indices",
					Main.getCheckMark(role.getNumberOfIndices()>=200)+" "+"Vitesse la nuit et résistance: 200 Indices"
					));
			tmp.setItem(8, counter);
			int n = role.getIndices().size();
			if(n>45)n=45;
			for(int i=0;i<n;i++) {
				UUID targeted=(UUID)role.getIndices().keySet().toArray()[i];
				String name = "";
				if(Bukkit.getPlayer(targeted)==null) {
					name = Main.currentGame.getOffline_Player(targeted).getUsername();
				}else name=Bukkit.getPlayer(targeted).getName();
				ItemStack it2 = InventoryUtils.getItem(Material.SKULL_ITEM, name, Arrays.asList("§a"+role.getProgression().get(targeted)+"% "+generatorcycle.generateProgressBar(role.getProgression().get(targeted), 20)));
				it2.setDurability((short)3);
				SkullMeta meta = (SkullMeta) it2.getItemMeta();
				meta.setOwner(name);
				it2.setItemMeta(meta);
				tmp.setItem(9+i, it2);
			}
		}else {
			for(int i =0;i<9;i++) {
				tmp.setItem(i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, TeamManager.getShortOfChatColor(ChatColor.AQUA)));
			}
			for(int i =0;i<9;i++) {
				tmp.setItem(i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, TeamManager.getShortOfChatColor(ChatColor.AQUA)));
			}
			tmp.setItem(0, InventoryUtils.getItem(Material.ARROW, "§7Retour", null));
			int n = role.getIndices().get(openPlayer).size();
			if(n>45)n=45;
			ArrayList<Indice> indices = role.getIndicesOfPlayer(openPlayer);
			UUID targeted=openPlayer;
			String name = "";
			if(Bukkit.getPlayer(targeted)==null) {
				name = Main.currentGame.getOffline_Player(targeted).getUsername();
			}else name=Bukkit.getPlayer(targeted).getName();
			ItemStack it2 = InventoryUtils.getItem(Material.SKULL_ITEM, name, Arrays.asList("§a"+role.getProgression().get(targeted)+"% "+generatorcycle.generateProgressBar(role.getProgression().get(targeted), 20)));
			it2.setDurability((short)3);
			SkullMeta meta = (SkullMeta) it2.getItemMeta();
			meta.setOwner(name);
			it2.setItemMeta(meta);
			tmp.setItem(4, it2);
			for(int i=0;i<n;i++) {
				tmp.setItem(9+i, InventoryUtils.createSkullItem(indices.get(i).getHeadValue(), indices.get(i).getName(), null));
			}
		}
		return tmp;
	}
	public void open() {
		this.inv = generateinv();
		player.openInventory(inv);
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		for (StewartGUI gui : allGui) {
			if (e.getInventory().equals(gui.inv)) {
				e.setCancelled(true);
				int currentSlot= e.getSlot();
				if(gui.openPlayer==null && currentSlot>8 && currentSlot<=10+role.getIndices().size()) {
					gui.openPlayer=(UUID) gui.role.getIndices().keySet().toArray()[currentSlot-9];
					gui.open();
					return;
				}else if(gui.openPlayer!=null && currentSlot==0) {
					gui.openPlayer=null;
					gui.open();
					return;
				}
				
			}
		}
	}
	// Optimization --> Forget GUI that have been closed >|<
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		for (StewartGUI gui : allGui) {
			if (e.getInventory().equals(gui.inv)) {
				allGui.remove(gui);
			}
		}
	}
}
