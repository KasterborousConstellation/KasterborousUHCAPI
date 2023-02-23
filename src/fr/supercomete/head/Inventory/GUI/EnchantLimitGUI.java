package fr.supercomete.head.Inventory.GUI;

import fr.supercomete.head.GameUtils.Enchants.EnchantHandler;
import fr.supercomete.head.GameUtils.Enchants.EnchantLimit;
import fr.supercomete.head.GameUtils.Enchants.EnchantType;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.core.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EnchantLimitGUI extends KTBSInventory {
	private final boolean modifiable;
	private EnchantType type=EnchantType.Iron;
	public EnchantLimitGUI(Player player,boolean modifiable) {
		super("§6Limite d'enchantement",54,player);
        this.modifiable=modifiable;

	}

    @Override
    protected boolean denyDoubleClick() {
        return false;
    }

    protected Inventory generateinventory(Inventory tmp) {
		for(int i=0;i<9;i++) {
			tmp.setItem(i, InventoryUtils.getItem(Material.STAINED_GLASS_PANE, " ", null));
		}
		for(int i=0;i<9;i++) {
			tmp.setItem(53-i, InventoryUtils.getItem(Material.STAINED_GLASS_PANE, " ", null));
		}
		int e=0;
		for(EnchantType enchantType: EnchantType.values()){
            tmp.setItem(e,InventoryUtils.getItem(enchantType.getMaterial(),"§bLimite d'enchant: §f"+enchantType.getName(),null));
		    e++;
        }
		e=9;

		for(EnchantLimit enchantLimit: EnchantHandler.getLimite(type)){
            List<String> stringlist =(!modifiable)?null:Arrays.asList("§fMax: "+enchantLimit.getEnchant().getMaxLevel(),InventoryUtils.ClickTypoAdd+"1",InventoryUtils.ClickTypoRemove+"1");
            ItemStack stack = InventoryUtils.getItem(Material.ENCHANTED_BOOK,"§f"+enchantLimit.getEnchantname()+" "+enchantLimit.getMax(), stringlist);
            stack.setAmount(enchantLimit.getMax());
            tmp.setItem(e,stack);
		    e++;
        }
		tmp.setItem(48,InventoryUtils.getItem(Material.ANVIL,"§bEmpêcher la fusion: "+Main.getCheckMark(Main.currentGame.IsDisabledAnvil), Collections.singletonList(InventoryUtils.ClickBool)));
        tmp.setItem(49,InventoryUtils.getItem(Material.ARROW,"§7Retour",Collections.singletonList("§7Cliquer ici pour revenir en arrière")));
        tmp.setItem(50,InventoryUtils.getItem(Material.ENCHANTMENT_TABLE,"§bEmpêcher l'enchant: "+Main.getCheckMark(Main.currentGame.IsDisabledEnchant), Collections.singletonList(InventoryUtils.ClickBool)));
		return tmp;
	}

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        if(modifiable){
            switch (slot){
                case 49 :
                    new ModeGUI(Main.currentGame.getMode(),holder).open();
                    break;
                case 48 :
                    Main.currentGame.IsDisabledAnvil=!Main.currentGame.IsDisabledAnvil;
                    refresh();
                    break;
                case 50 :
                    Main.currentGame.IsDisabledEnchant=!Main.currentGame.IsDisabledEnchant;
                    refresh();
                    break;
                default :
                    if(slot>8 && slot<9+EnchantHandler.getLimite(type).size()){
                        final EnchantLimit enchantLimit = EnchantHandler.getLimite(type).get(slot-9);
                        int i = EnchantHandler.getIndexOf(enchantLimit);
                        if(action.IsLeftClick()){
                            enchantLimit.setMax(Math.max(0,enchantLimit.getMax()-1));
                        }else if(action.IsRightClick()){
                            enchantLimit.setMax(Math.min(enchantLimit.getEnchant().getMaxLevel(),enchantLimit.getMax()+1));
                        }
                        Main.currentGame.getLimites().set(i,enchantLimit);
                        refresh();
                    }
                    break;
            }

        }else{
            if(slot==49){
                new RuleGUI(holder).open();
            }
        }
        if(slot>=0&&slot<EnchantType.values().length){
            type=EnchantType.values()[slot];
            refresh();
        }
        return true;
    }

    @Override
    protected boolean onClose(Player holder) {
        return false;
    }

}