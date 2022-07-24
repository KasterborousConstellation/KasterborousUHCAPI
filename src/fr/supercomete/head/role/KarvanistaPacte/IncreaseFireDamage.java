package fr.supercomete.head.role.KarvanistaPacte;

import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Triggers.Trigger_OnTakingHit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public class IncreaseFireDamage extends Proposal implements Trigger_OnTakingHit {

    public IncreaseFireDamage(UUID karvanista, UUID ally) {
        super(karvanista, ally);
    }

    @Override
    protected int AskCost() {
        return -2;
    }

    @Override
    protected String AskName() {
        return "Multiplication des dégats de feu";
    }

    @Override
    protected String AskDescription() {
        return "Ce composant fait que Karvanista et son allié prennent 3 fois plus de dégat de feu. (Dégat de lave non compris)";
    }

    @Override
    public void tick(Player karvanista, Player ally) {

    }

    @Override
    public void start(Player karvanista, Player ally) {

    }

    @Override
    protected Material AskMaterial() {
        return Material.FLINT_AND_STEEL;
    }

    @Override
    public void TakingDamage(Player player, EntityDamageEvent e) {
        if(e.getCause()==EntityDamageEvent.DamageCause.FIRE||e.getCause()==EntityDamageEvent.DamageCause.FIRE_TICK){
            e.setDamage(e.getDamage()*3);
            player.sendMessage(Main.UHCTypo+"§cLes dégats de feu vous inflige plus de dégats");
        }
    }
}
