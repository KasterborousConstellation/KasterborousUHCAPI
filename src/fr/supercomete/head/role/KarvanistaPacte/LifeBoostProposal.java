package fr.supercomete.head.role.KarvanistaPacte;

import java.util.UUID;

import fr.supercomete.head.role.Triggers.Trigger_onEpisodeTime;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;

public final class LifeBoostProposal extends Proposal implements Trigger_onEpisodeTime {

	

	public LifeBoostProposal(UUID karvanista, UUID ally) {
		super(karvanista, ally);
	}

	@Override
	protected int AskCost() {
		return 4;
	}

	@Override
	protected String AskName() {
		return "Vie augmentée";
	}

	@Override
	protected String AskDescription() {
		return "Ce composant donne à Karvanista et son allié 1♥ permanent supplémentaire chaque nouvel épisode après la persuasion.";
	}

	@Override
	public void tick(Player karvanista, Player ally) {
		
	}

	@Override
	public void start(Player karvanista, Player ally) {
		
	}

	@Override
	protected Material AskMaterial() {
		return Material.RED_ROSE;
	}


    @Override
    public void onEpisodeTime(Player player) {
        RoleHandler.getRoleOf(karvanista).addBonus(new Bonus(BonusType.Heart,2));
        RoleHandler.getRoleOf(ally).addBonus(new Bonus(BonusType.Heart,2));

    }
}