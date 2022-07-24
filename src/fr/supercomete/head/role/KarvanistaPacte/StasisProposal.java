package fr.supercomete.head.role.KarvanistaPacte;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.nbthandler.NbtTagHandler;
public class StasisProposal extends Proposal {
	public CoolDown cooldown=new CoolDown(0,10*60);
	public StasisProposal(UUID karvanista, UUID ally) {
		super(karvanista, ally);
	}

	@Override
	protected int AskCost() {
		return 3;
	}

	@Override
	protected String AskName() {
		return "Stase";
	}

	@Override
	protected String AskDescription() {
		return "Karvanista obtiendra une nether star appelée 'Stasis', elle peut être utilisée toute les 10min pour enfermer un joueur dans une cage de verre indestrutible pendant 20s. Le joueur ciblé doit être dans un rayon de 20bloc";
	}

	@Override
	public void tick(Player karvanista, Player ally) {

	}

	@Override
	public void start(Player karvanista, Player ally) {
		ItemStack power = InventoryUtils.getItem(Material.NETHER_STAR, "§bStasis" , Arrays.asList("§bCliquez vers un joueur pour l'enfermer dans une cage de verre"));
		power = NbtTagHandler.createItemStackWithUUIDTag(power, 12);
		InventoryUtils.addsafelyitem(karvanista, power);
	}

	@Override
	protected Material AskMaterial() {
		return Material.DIAMOND;
	}

}
