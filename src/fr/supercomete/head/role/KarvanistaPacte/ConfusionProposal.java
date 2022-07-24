package fr.supercomete.head.role.KarvanistaPacte;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.nbthandler.NbtTagHandler;

public class ConfusionProposal extends Proposal {

	public ConfusionProposal(UUID karvanista, UUID ally) {
		super(karvanista, ally);
	}

	@Override
	protected int AskCost() {
		return 2;
	}

	@Override
	protected String AskName() {
		return "§3Confusion";
	}

	@Override
	protected String AskDescription() {
		return "§7Donne a Karvanista une épée qui donne nausée pendant 30s lorsqu'elle frappe un joueur.";
	}

	@Override
	public void tick(Player karvanista, Player ally) {

	}

	@Override
	public void start(Player karvanista, Player ally) {
		ItemStack stack = InventoryUtils.getItem(Material.STONE_SWORD, "§3Confusion", null);
		ItemMeta im = stack.getItemMeta();
		im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		im.spigot().setUnbreakable(true);
		stack.setItemMeta(im);
		InventoryUtils.addsafelyitem(karvanista, NbtTagHandler.createItemStackWithUUIDTag(stack, 11));
	}

	@Override
	protected Material AskMaterial() {
		return Material.SLIME_BALL;
	}

}
