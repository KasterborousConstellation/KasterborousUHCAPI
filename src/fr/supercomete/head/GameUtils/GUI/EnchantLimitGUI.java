package fr.supercomete.head.GameUtils.GUI;

import fr.supercomete.head.GameUtils.Enchants.EnchantHandler;
import fr.supercomete.head.GameUtils.Enchants.EnchantLimit;
import fr.supercomete.head.GameUtils.Enchants.EnchantType;
import fr.supercomete.head.Inventory.GUI.RuleGUI;
import fr.supercomete.head.Inventory.InventoryHandler;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class EnchantLimitGUI extends GUI{
	private static final CopyOnWriteArrayList<EnchantLimitGUI> allGui = new CopyOnWriteArrayList<>();
	private Inventory inv;
	private final Player player;
	private boolean modifiable=false;
	private EnchantType type=EnchantType.Iron;
	public EnchantLimitGUI(Main main) {
		this.player=null;
	}
	public EnchantLimitGUI(Player player,boolean modifiable) {
		this.modifiable=modifiable;
		this.player=player;
		if (player != null)
			allGui.add(this);
	}
	protected Inventory generateinv() {
		Inventory tmp = Bukkit.createInventory(null, 54,"§6Limite d'enchant: "+type.getName());
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
            List<String> stringlist =(!modifiable)?null:Arrays.asList("§fMax: "+enchantLimit.getEnchant().getMaxLevel(),InventoryHandler.ClickTypoAdd+"1",InventoryHandler.ClickTypoRemove+"1");
            ItemStack stack = InventoryUtils.getItem(Material.ENCHANTED_BOOK,"§f"+enchantLimit.getEnchantname()+" "+enchantLimit.getMax(), stringlist);
            stack.setAmount(enchantLimit.getMax());
            tmp.setItem(e,stack);
		    e++;
        }
		tmp.setItem(48,InventoryUtils.getItem(Material.ANVIL,"§bEmpêcher la fusion: "+Main.getCheckMark(Main.currentGame.IsDisabledAnvil), Collections.singletonList(InventoryHandler.ClickBool)));
        tmp.setItem(49,InventoryUtils.getItem(Material.ARROW,"§7Retour",Collections.singletonList("§7Cliquer ici pour revenir en arrière")));
        tmp.setItem(50,InventoryUtils.getItem(Material.ENCHANTMENT_TABLE,"§bEmpêcher l'enchant: "+Main.getCheckMark(Main.currentGame.IsDisabledEnchant), Collections.singletonList(InventoryHandler.ClickBool)));
		return tmp;
	}
	public void open() {
		this.inv = generateinv();
		player.openInventory(inv);
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		for (EnchantLimitGUI gui:allGui) {
			if (e.getInventory().equals(gui.inv)) {
				e.setCancelled(true);
				final int currentSlot = e.getSlot();
				final ClickType action = e.getClick();
				if(gui.modifiable){
                    switch (currentSlot){
                        case 49 :
                            new ModeGUI(Main.currentGame.getMode(),gui.player).open();
                            break;
                        case 48 :
                            Main.currentGame.IsDisabledAnvil=!Main.currentGame.IsDisabledAnvil;
                            gui.open();
                            break;
                        case 50 :
                            Main.currentGame.IsDisabledEnchant=!Main.currentGame.IsDisabledEnchant;
                            gui.open();
                            break;
                        default :
                            if(currentSlot>8 && currentSlot<9+EnchantHandler.getLimite(gui.type).size()){
                                final EnchantLimit enchantLimit = EnchantHandler.getLimite(gui.type).get(currentSlot-9);
                                int i = EnchantHandler.getIndexOf(enchantLimit);
                                if(action.isLeftClick()){
                                    enchantLimit.setMax(Math.max(0,enchantLimit.getMax()-1));
                                }else if(action.isRightClick()){
                                    enchantLimit.setMax(Math.min(enchantLimit.getEnchant().getMaxLevel(),enchantLimit.getMax()+1));
                                }
                                Main.currentGame.getLimites().set(i,enchantLimit);
                                gui.open();
                            }
                            break;
                        }

                }else{
				    if(currentSlot==49){
				        new RuleGUI(gui.player).open();
                    }
                }
                if(currentSlot>=0&&currentSlot<EnchantType.values().length){
                    gui.type=EnchantType.values()[currentSlot];
                    gui.open();
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