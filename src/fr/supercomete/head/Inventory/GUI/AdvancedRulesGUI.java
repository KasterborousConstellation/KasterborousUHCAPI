package fr.supercomete.head.Inventory.GUI;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.Inventory.GUI.RuleGUI;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
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
public class AdvancedRulesGUI extends KTBSInventory {

    public AdvancedRulesGUI(Player player) {
        super("Advanced Rules", 54, player);
    }

    @Override
	protected Inventory generateinventory(Inventory tmp) {
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

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        if(slot == 49) {
            new RuleGUI(holder).open();
        }
        return true;
    }

    @Override
    protected boolean onClose(Player holder) {
        return false;
    }

    @Override
    protected boolean denyDoubleClick() {
        return true;
    }
}

