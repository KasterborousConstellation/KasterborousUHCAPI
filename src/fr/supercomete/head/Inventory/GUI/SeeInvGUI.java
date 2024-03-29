package fr.supercomete.head.Inventory.GUI;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import fr.supercomete.head.Inventory.InventoryUtils;
public class SeeInvGUI extends KTBSInventory {
	private final Inventory inventory;
	public SeeInvGUI(Player player,Player openplayer) {
        super("Spec-Inventaire",54,player);
		this.inventory=openplayer.getInventory();
	}
    public SeeInvGUI(Player player,Inventory inventory) {
        super("Inventaire",54,player);
        this.inventory=inventory;
    }

    @Override
    protected boolean denyDoubleClick() {
        return false;
    }

    protected Inventory generateinventory(Inventory tmp) {
		for(int e=0;e<9;e++) {
			tmp.setItem(e, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)3));
		}
		for(int r=45;r<54;r++) {
			tmp.setItem(r, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)3));
		}
		for(int i=0;i<Math.min(inventory.getSize(),36);i++){
			ItemStack it = inventory.getItem(i);
			if(inventory.getItem(i)!=null && inventory.getItem(i).getType()!=Material.AIR)
			{
				if(i<9)
					tmp.setItem(36+i, it);else tmp.setItem(i, it);
			}
		}
		return tmp;
	}

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        return true;
    }

    @Override
    protected boolean onClose(Player holder) {
        return false;
    }

}