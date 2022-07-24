package fr.supercomete.head.role.KarvanistaPacte;

import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Triggers.Trigger_onEpisodeTime;
import fr.supercomete.nbthandler.NbtTagHandler;

public final class ShieldProposal extends Proposal implements Trigger_onEpisodeTime{
	public boolean CanUse=true;
	
	public ShieldProposal(UUID karvanista, UUID ally) {
		super(karvanista, ally);
	}

	@Override
	protected int AskCost() {
		return 2;
	}

	@Override
	protected String AskName() {
		return "Cage";
	}

	@Override
	protected String AskDescription() {
		return "Karvanista obtient un objet nommé 'Cage'. En cliquant dessus son allié peu importe la distance les séparant sera téléporter au dessus de Karvanista dans une cage de verre. Le verre est destructible. Et ce pouvoir peut être utilisé tout les épisodes.";
	}

	@Override
	public void tick(Player karvanista, Player ally) {
		
	}

	@Override
	public void start(Player karvanista, Player ally) {
		ItemStack stack = InventoryUtils.getItem(Material.NETHER_STAR, "§bCage", List.of("§bCliquez ici pour créer votre Cage."));
		stack =NbtTagHandler.createItemStackWithUUIDTag(stack, 13);
		InventoryUtils.addsafelyitem(karvanista, stack);
	}	

	@Override
	protected Material AskMaterial() {
		return Material.GLASS;
	}

	@Override
	public void onEpisodeTime(Player player) {
		if(!CanUse) {
			CanUse=true;
			player.sendMessage(Main.UHCTypo+"§7Votre pouvoir est de nouveau utilisable");
		}
	}
}
