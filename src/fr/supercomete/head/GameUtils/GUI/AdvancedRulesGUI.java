package fr.supercomete.head.GameUtils.GUI;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.supercomete.head.GameUtils.Time.TimeUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.head.GameUtils.GameConfigurable.BindingType;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.Inventory.InventoryHandler;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
public class AdvancedRulesGUI extends GUI{
	private static final CopyOnWriteArrayList<AdvancedRulesGUI> allGui = new CopyOnWriteArrayList<AdvancedRulesGUI>();
	private Inventory inv;
	private Player player;
	public AdvancedRulesGUI(Main main){
		this.player=null;
	}
	public AdvancedRulesGUI(Player player) {
		this.player=player;
		if (player != null)
			allGui.add(this);
	}
	protected Inventory generateinv() {
		Inventory tmp = Bukkit.createInventory(null, 54,"Advanced Rules");
		for(int i = 0;i<9;i++) {
			tmp.setItem(i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)10));
		}
		for(int i = 0;i<9;i++) {
			tmp.setItem(53-i,InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)10));
		}
		tmp.setItem(49, InventoryUtils.getItem(Material.ARROW, "§bRetour", null));
		int e = 0;
		for(BindingType type: BindingType.values()) {
			ArrayList<String> arr = new ArrayList<String>();
			for(Configurable config : Main.currentGame.getConfigList()) {
				if(config.getId().getBind().getBinding()==type.getName()) {
					String str ="§r§a"+config.getId().getName();
					str+=": ";
					switch (config.getType()) {
					case Bool:
						str+= Main.TranslateBoolean((config.getData()%2==0));
						break;
					case Integer:
						str+="§b"+config.getData();
						break;
					case Percentage:
						str+="§b"+config.getData()+"%";
						break;
					case Timer:
						str+= TimeUtility.transform(config.getData(), "§b", "§b", "§b");
						break;
					default:
						break;
					}
					arr.add(str);
				}
				
			}
			ItemStack item = InventoryUtils.getItem(type.getMaterial(), "§b"+type.getName(), arr);
			tmp.setItem(e+9, item);	
			e++;
		}
		ArrayList<String> arr = new ArrayList<>();
		for(Configurable config : Main.currentGame.getConfigList()) {
			if(config.getId().getBind().getBinding()==Main.currentGame.getMode().getName()) {
				String str ="§r§a"+config.getId().getName();
				str+=": ";
				switch (config.getType()) {
				case Bool:
					str+= Main.TranslateBoolean((config.getData()%2==0));
					break;
				case Integer:
					str+="§b"+config.getData();
					break;
				case Percentage:
					str+="§b"+config.getData()+"%";
					break;
				case Timer:
					str+=TimeUtility.transform(config.getData(), "§b", "§b", "§b");
					break;
				default:
					break;
				}
				arr.add(str);
			}
		}
		tmp.setItem(40, InventoryUtils.getItem(Main.currentGame.getMode().getMaterial(),"§b"+Main.currentGame.getMode().getName(), arr));
		return tmp;
	}
	public void open() {
		this.inv = generateinv();
		player.openInventory(inv);
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		for (AdvancedRulesGUI gui:allGui) {
			if (e.getInventory().equals(gui.inv)) {
				e.setCancelled(true);
				int slot = e.getSlot();
				if(slot == 49) {
					InventoryHandler.openinventory(gui.player, 5);
				}
			}
		}
	}
	// Optimization --> Forget GUI that have been closed >|<
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		for (AdvancedRulesGUI gui : allGui) {
			if (e.getInventory().equals(gui.inv)) {
				allGui.remove(gui);
			}
		}
	}
}

