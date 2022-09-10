package fr.supercomete.head.role.KarvanistaPacte;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;

public class DayBothSlownessProposal extends Proposal {

	public DayBothSlownessProposal(UUID karvanista, UUID ally) {
		super(karvanista, ally);
	}

	@Override
	public int AskCost() {
		return -2;
	}

	@Override
	public String AskName() {
		return "Ralentissement pendant la journée";
	}

	@Override
	public String AskDescription() {
		return "Ce composant vous donne a tout les deux l'effet slowness pendant le jour, quand le pacte est conclut.";
	}

	@Override
	public void tick(Player karvanista, Player ally) {
		if(Main.currentGame.getGamestate().equals(Gstate.Day)) {
			PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 20*3, 0, false, false);
			if(ally!=null)PlayerUtility.addProperlyEffect(ally, effect);
			if(karvanista!=null)PlayerUtility.addProperlyEffect(karvanista, effect);
		}
	}

	@Override
	public void start(Player karvanista, Player ally) {
		
	}

	@Override
	public Material AskMaterial() {
		return Material.RABBIT_FOOT;
	}

}
