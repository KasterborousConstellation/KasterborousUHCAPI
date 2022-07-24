package fr.supercomete.head.GameUtils.GUI;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.supercomete.head.GameUtils.GameConfigurable.BindingType;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.GameConfigurable.ModeBinding;
import fr.supercomete.head.GameUtils.GameConfigurable.TypeBinding;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
public class ConfigurableGUI extends GUI{
	private static final CopyOnWriteArrayList<ConfigurableGUI> allGui = new CopyOnWriteArrayList<ConfigurableGUI>();
	private Inventory inv;
	private Player player;
	private String openType;
	public ConfigurableGUI(Main main) {
		this.player=null;
	}
	public ConfigurableGUI(Player player,String openType) {
		this.player=null;
		this.player=player;
		this.openType=openType;
		if (player != null)
			allGui.add(this);
	}
	protected Inventory generateinv() {
		Inventory tmp = Bukkit.createInventory(null, 54,"Configurable "+openType);
		List<String> arr = Collections.singletonList("§aCliquez ici pour configurer");
		if(openType.equals("Principale")) {
			for(int i = 0;i<9;i++) {
				tmp.setItem(i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)10));
			}
			int e=0;
			for(BindingType type : BindingType.values()) {
				tmp.setItem(e+9, InventoryUtils.getItem(type.getMaterial(), "§b"+type.getName(), arr));
				e++;
			}
			for(int i = 0;i<9;i++) {
				tmp.setItem(53-i,InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)10));
			}
			ItemStack stack = InventoryUtils.getItem(Main.currentGame.getMode().getMaterial(), "§r"+Main.currentGame.getMode().getName(), arr);
			ItemMeta im = stack.getItemMeta();
			im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			stack.setItemMeta(im);
			tmp.setItem(40, stack);
			tmp.setItem(49, InventoryUtils.getItem(Material.ARROW, "Retour", null));
		}else {
			ArrayList<Configurable> configurables = new ArrayList<>();
			for(int i = 0;i<9;i++) {
				tmp.setItem(i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)10));
			}
			for(int i = 0;i<9;i++) {
				tmp.setItem(53-i,InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)10));
			}
			if(openType==Main.currentGame.getMode().getName()) {
				for(Configurable config : Main.currentGame.getConfigList()) {
					if(config.getId().getBind() instanceof ModeBinding) {
					    ModeBinding bind = (ModeBinding) config.getId().getBind();
                        if(bind.getBinding()==Main.currentGame.getMode().getName()) {
							configurables.add(config);
						}
					}
				}
				tmp.setItem(49, InventoryUtils.getItem(Material.ARROW, "Retour", null));
			}else {
				tmp.setItem(49, InventoryUtils.getItem(Material.ARROW, "Retour", null));
				for(Configurable config : Main.currentGame.getConfigList()) {
					if(config.getId().getBind() instanceof TypeBinding) {
                        TypeBinding bind = (TypeBinding) config.getId().getBind();
                        if(bind.getBinding()==openType) {
							configurables.add(config);
						}
					}
				}
				
			}
			for(int i =0;i<configurables.size();i++) {
				Configurable conf = configurables.get(i);
				tmp.setItem(9+i, conf.Representation());
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
		for (ConfigurableGUI gui:allGui) {
			if (e.getInventory().equals(gui.inv)) {
				e.setCancelled(true);
				int slot = e.getSlot();
				if(gui.openType=="Principale" && slot >=9 && slot<= 9+BindingType.values().length ){
					int index = (slot-9);
					gui.openType=BindingType.values()[index].getName();
					gui.open();
				}else {
					if(gui.openType=="Principale"){
						if(slot==49) {
							new ModeGUI(Main.currentGame.getMode(),gui.player).open();
							return;
						}
						if(slot==40) {
							gui.openType=Main.currentGame.getMode().getName();
							gui.open();
							return;
						}
					
					}else {
						if(slot==49) {
							gui.openType="Principale";
							gui.open();
							return;
						}
						ClickType currentClick = e.getClick();
						ArrayList<Configurable> configurables = new ArrayList<>();
						for(Configurable config : Main.currentGame.getConfigList()) {
							if(config.getId().getBind() instanceof TypeBinding) {
                                TypeBinding bind =(TypeBinding) config.getId().getBind();
                                if(bind.getBinding()==gui.openType) {
									configurables.add(config);
								}
							}else if(config.getId().getBind() instanceof ModeBinding) {
                                ModeBinding mode=(ModeBinding)config.getId().getBind();
                                if(mode.getBinding()==gui.openType) {
									configurables.add(config);
								}
							}
						}
						if(slot >=9 && slot< 9+ configurables.size()) {
							int index = slot - 9;
							Configurable config = configurables.get(index);
							config.update(currentClick);
							gui.open();
							return;
						}
					}
					
				}
			}
		}
	}
	// Optimization --> Forget GUI that have been closed >|<
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
        allGui.removeIf(gui -> e.getInventory().equals(gui.inv));
	}
}
