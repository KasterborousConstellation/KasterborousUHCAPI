package fr.supercomete.head.Inventory.GUI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
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
public class ConfigurableGUI extends KTBSInventory {
	private String openType;

    public ConfigurableGUI(Player player,String openType) {
        super("Configurables", 54, player);
        this.openType=openType;
    }

    @Override
	protected Inventory generateinventory(Inventory tmp) {
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
				tmp.setItem(49, InventoryUtils.getItem(Material.ARROW, "§rRetour", null));
			}else {
				tmp.setItem(49, InventoryUtils.getItem(Material.ARROW, "§rRetour", null));
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

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        if(openType=="Principale" && slot >=9 && slot<= 9+BindingType.values().length ){
            int index = (slot-9);
            openType=BindingType.values()[index].getName();
            refresh();
        }else {
            if(openType=="Principale"){
                if(slot==49) {
                    new ModeGUI(Main.currentGame.getMode(),holder).open();
                    return true;
                }
                if(slot==40) {
                    openType=Main.currentGame.getMode().getName();
                    refresh();
                }

            }else {
                if(slot==49) {
                    openType="Principale";
                    refresh();
                }
                ArrayList<Configurable> configurables = new ArrayList<>();
                for(Configurable config : Main.currentGame.getConfigList()) {
                    if(config.getId().getBind() instanceof TypeBinding) {
                        TypeBinding bind =(TypeBinding) config.getId().getBind();
                        if(bind.getBinding()==openType) {
                            configurables.add(config);
                        }
                    }else if(config.getId().getBind() instanceof ModeBinding) {
                        ModeBinding mode=(ModeBinding)config.getId().getBind();
                        if(mode.getBinding()==openType) {
                            configurables.add(config);
                        }
                    }
                }
                if(slot >=9 && slot< 9+ configurables.size()) {
                    int index = slot - 9;
                    Configurable config = configurables.get(index);
                    config.update(action.getClick());
                    refresh();
                    return true;
                }
            }

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
