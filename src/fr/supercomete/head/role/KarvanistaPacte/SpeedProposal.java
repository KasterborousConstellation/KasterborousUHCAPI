package fr.supercomete.head.role.KarvanistaPacte;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.role.RoleHandler;

public class SpeedProposal extends Proposal {

	
	public SpeedProposal(UUID karvanista, UUID ally) {
		super(karvanista, ally);
	}

	@Override
	protected int AskCost() {
		return 3;
	}

	@Override
	protected String AskName() {
		return "Vitesse";
	}

	@Override
	protected String AskDescription() {
		return "Karvanista et son allié obtiennent l'effet vitesse quand ils sont à moins de 20blocs l'un de l'autre";
	}

	@Override
	public void tick(Player karvanista, Player ally) {
		if(karvanista!=null&&ally!=null) {
			if(RoleHandler.getRoleOf(ally)!=null&&RoleHandler.getRoleOf(karvanista)!=null) {
				if(ally.getWorld().equals(karvanista.getWorld())) {
					if(ally.getLocation().distance(karvanista.getLocation())<20) {
						PlayerUtility.addProperlyEffect(ally, new PotionEffect(PotionEffectType.SPEED, 20*3, 0, false,false));
						PlayerUtility.addProperlyEffect(karvanista, new PotionEffect(PotionEffectType.SPEED, 20*3, 0, false,false));
					}
				}
			}
		}
	}

	@Override
	public void start(Player karvanista, Player ally) {

	}

	@Override
	protected Material AskMaterial() {
		return Material.SUGAR;
	}

}
