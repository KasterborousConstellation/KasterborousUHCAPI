package fr.supercomete.head.GameUtils.GameMode.ModeModifier;

import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface SpecialTab extends GameModeModifier {
    KTBSInventory getLinkedTab(Player player);
    ItemStack getLinkedItem();
}
