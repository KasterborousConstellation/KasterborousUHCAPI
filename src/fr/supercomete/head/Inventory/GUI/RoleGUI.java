package fr.supercomete.head.Inventory.GUI;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.supercomete.head.Inventory.GUI.SeeInvGUI;
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

import fr.supercomete.head.GameUtils.Historic;
import fr.supercomete.head.GameUtils.HistoricData;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleHandler;
public class RoleGUI extends KTBSInventory {
	private final Player target;
	public RoleGUI(Player player,Player target) {
        super("Spec",27,player);
        this.target=target;
	}

    @Override
    protected boolean denyDoubleClick() {
        return true;
    }

    @Override
	protected Inventory generateinventory(Inventory tmp) {
		for(int i=0;i<9;i++) {
			tmp.setItem(i, InventoryUtils.getItem(Material.STAINED_GLASS_PANE, " ", null));
		}
		for(int i=0;i<9;i++) {
			tmp.setItem(26-i, InventoryUtils.getItem(Material.STAINED_GLASS_PANE, " ", null));
		}
		int diamond = (Main.diamondmap.get(target.getUniqueId())==null)?0:Main.diamondmap.get(target.getUniqueId());
		ItemStack it = InventoryUtils.getItem(Material.DIAMOND_ORE, "§bLimite de diamant: §r"+diamond, null);
		diamond = (diamond==0)?1:diamond;
		it.setAmount(diamond);
		tmp.setItem(11, it);
		tmp.setItem(12, InventoryUtils.getItem(Material.IRON_SWORD, "§eKills: "+Main.currentGame.getKillList().get(target.getUniqueId()), null));
		tmp.setItem(13, InventoryUtils.getItem(Material.BARRIER, "§cRafraichir", null));
		tmp.setItem(14, InventoryUtils.getItem(Material.CHEST, "§bVoir l'inventaire", null));
		if(Main.currentGame.getMode()instanceof CampMode) {
			final Historic historic = RoleHandler.getHistoric();
			final HistoricData data = historic.getEntry(target.getUniqueId());
			
			if(data.getCause()==null&&RoleHandler.IsRoleGenerated()) {
				tmp.setItem(22, InventoryUtils.getItem(Material.BEACON, "§r"+data.getPlayer().getUsername(),RoleHandler.getRoleDescription(data)));
			}else {
				tmp.setItem(22, InventoryUtils.getItem(Material.BEACON, "§cAucun", null));
			}
		}
		
		return tmp;
	}

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        if(!target.isOnline()) {
            holder.closeInventory();
            holder.sendMessage(Main.UHCTypo+"§cLe joueur n'est plus connecté");
            return true;
        }
        switch (slot) {
            case 13:
                refresh();
                break;
            case 14:
                new SeeInvGUI(holder, target).open();
                break;
            default:

                break;
        }
        return false;
    }

    @Override
    protected boolean onClose(Player holder) {
        return false;
    }

}