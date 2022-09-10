package fr.supercomete.head.role.KarvanistaPacte;

import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.content.DWUHC.Karvanista;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LifeIncreaseProposal extends Proposal{
    public LifeIncreaseProposal(UUID karvanista, UUID ally) {
        super(karvanista, ally);
    }

    @Override
    protected int AskCost() {
        return 2;
    }

    @Override
    protected String AskName() {
        return "(Small) Vie supplémentaire";
    }

    @Override
    protected String AskDescription() {
        return "Ce composant donne 1♥ permanent supplémentaire à Karvanista et à son allié";
    }

    @Override
    public void tick(Player karvanista, Player ally) {

    }

    @Override
    public void start(Player karvanista, Player ally) {
        RoleHandler.getRoleOf(karvanista).addBonus(new Bonus(BonusType.Heart,2));
        RoleHandler.getRoleOf(ally).addBonus(new Bonus(BonusType.Heart,2));
    }

    @Override
    protected Material AskMaterial() {
        return Material.BARRIER;
    }
}
