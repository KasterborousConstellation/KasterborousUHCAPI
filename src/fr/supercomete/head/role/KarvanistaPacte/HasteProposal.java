package fr.supercomete.head.role.KarvanistaPacte;

import fr.supercomete.head.PlayerUtils.PlayerUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public final class HasteProposal extends Proposal{

    public HasteProposal(UUID karvanista, UUID ally) {
        super(karvanista, ally);
    }
    @Override
    protected int AskCost() {
        return 1;
    }

    @Override
    protected String AskName() {
        return "Hâte";
    }

    @Override
    protected String AskDescription() {
        return "Ce composant donne l'effet §6Haste§7 a Karvanista et son allié.";
    }

    @Override
    public void tick(Player karvanista, Player ally) {
        PlayerUtility.addProperlyEffect(karvanista,new PotionEffect(PotionEffectType.FAST_DIGGING,20*3,0,false,false));
        PlayerUtility.addProperlyEffect(ally,new PotionEffect(PotionEffectType.FAST_DIGGING,20*3,0,false,false));
    }

    @Override
    public void start(Player karvanista, Player ally) {

    }

    @Override
    protected Material AskMaterial() {
        return Material.GOLD_PICKAXE;
    }
}
