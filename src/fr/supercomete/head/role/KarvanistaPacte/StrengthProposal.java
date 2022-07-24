package fr.supercomete.head.role.KarvanistaPacte;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.head.PlayerUtils.PlayerUtility;

public class StrengthProposal extends Proposal {

	public StrengthProposal(UUID karvanista, UUID ally) {
		super(karvanista, ally);
	}

	@Override
	protected int AskCost() {
		return 2;
	}

	@Override
	protected String AskName() {
		return "Force pour l'allié";
	}

	@Override
	protected String AskDescription() {
		return "Donne à votre allié force pendant toute la partie.";
	}

	@Override
	public void tick(Player karvanista, Player ally) {
		PlayerUtility.addProperlyEffect(ally, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*3, 0, false, false));
	}

	@Override
	public void start(Player karvanista, Player ally) {
		
	}

	@Override
	protected Material AskMaterial() {
		return Material.IRON_SWORD;
	}

	

}
