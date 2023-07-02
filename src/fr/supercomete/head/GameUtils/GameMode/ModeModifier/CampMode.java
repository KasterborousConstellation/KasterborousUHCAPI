package fr.supercomete.head.GameUtils.GameMode.ModeModifier;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.Inventory.GUI.RoleModeGUI;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.role.RoleGenerator;
import fr.supercomete.head.role.RoleGeneratorHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface CampMode extends NRGMode,SpecialTab{
    default RoleGenerator getRoleGenerator(){
        return RoleGeneratorHandler.StandardGenerator();
    }
    @Override
    default KTBSInventory getLinkedTab(Player player){
        return new RoleModeGUI(Bukkit.getServicesManager().load(KtbsAPI.class).getGameProvider().getCurrentGame().getMode(),player);
    }
    @Override
    default ItemStack getLinkedItem(){
        return InventoryUtils.createColorItem(Material.STAINED_CLAY, "§rRôles", 1, (short)6);
    }
    @Override
    default boolean showCompo() {
        return true;
    }
}