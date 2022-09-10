package fr.supercomete.head.role.KarvanistaPacte;

import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.RoleHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LargeLifeIncrease extends  Proposal{
    public LargeLifeIncrease(UUID karvanista, UUID ally) {
        super(karvanista, ally);
    }

    @Override
    protected int AskCost() {
        return 5;
    }

    @Override
    protected String AskName() {
        return "(Max) Vie supplémentaire";
    }

    @Override
    protected String AskDescription() {
        return "Ce composant donne 3♥ permanent supplémentaire à Karvanista et à son allié";
    }

    @Override
    public void tick(Player karvanista, Player ally) {

    }

    @Override
    public void start(Player karvanista, Player ally) {
        RoleHandler.getRoleOf(karvanista).addBonus(new Bonus(BonusType.Heart,6));
        RoleHandler.getRoleOf(ally).addBonus(new Bonus(BonusType.Heart,6));
    }

    @Override
    protected Material AskMaterial() {
        return Material.BARRIER;
    }
}