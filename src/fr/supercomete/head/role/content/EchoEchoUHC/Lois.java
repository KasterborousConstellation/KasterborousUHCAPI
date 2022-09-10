package fr.supercomete.head.role.content.EchoEchoUHC;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.EchoRole;
import fr.supercomete.head.role.Triggers.Trigger_OnTakingHit;
import fr.supercomete.head.role.Triggers.Trigger_WhileAnyTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Lois extends EchoRole implements Trigger_WhileAnyTime, Trigger_OnTakingHit {
    boolean chute = true;
    public Lois(UUID owner) {
        super(owner);
    }

    @Override
    public BranlusqueLevel getBranlusqueLevel() {
        return BranlusqueLevel.MageTeacher;
    }

    @Override
    public String[] AskMoreInfo() {
        return new String[0];
    }

    @Override
    public String askName() {
        return "Lois";
    }

    @Override
    public Camps getDefaultCamp() {
        return Camps.EchoEcho;
    }

    @Override
    public List<String> askRoleInfo() {
        return Arrays.asList(
                "Votre but est de gagner avec les autres membres de "+Camps.EchoEcho.getColoredName(),
                "A cause de vos connaisances monstrueuses en vexillologie, les 5min vous obtenez les coordonnés d'un drapeau. Quand celui-ci est obtenu vous obtenez soit : L'information de si deux joueurs sont dans le même camp, soit un activable qui vous donne un effet aléatoire entre §cforce§7 "+ ChatColor.DARK_GRAY+"résistance§7 et §bvitesse§7 pendant une minute."
                ,"Votre nature discrete vous permet de glisser une voie supplémentaire pour vous lors du vote pour le chef d'Echo Echo."
                ,"Votre timidité extême fait que si il y a plus de 3 joueurs dans un rayon de 15blocs vous obtenez l'effet "+ChatColor.DARK_GRAY+"lenteur"
                ,"Votre petite taille vous permet activer/désactiver avec la commande '/echo nofall' vos dégats de chute."
        );
    }

    @Override
    public ItemStack[] askItemStackgiven() {
        return new ItemStack[0];
    }

    @Override
    public boolean AskIfUnique() {
        return true;
    }

    @Override
    public String AskHeadTag() {
        return null;
    }

    @Override
    public void WhileAnyTime(Player player) {
        int amount = 0;
        for(final Player other :Bukkit.getOnlinePlayers()){
            if(other.getUniqueId()!=player.getUniqueId()){
                if(Main.getPlayerlist().contains(other.getUniqueId())){
                    if(player.getLocation().distance(other.getLocation())<15){
                        amount++;
                    }
                }
            }
        }
        PlayerUtility.sendActionbar(player,"§fIl y a §c"+amount+" joueurs autour de vous.");
        if(amount>3){
            PlayerUtility.addProperlyEffect(player,new PotionEffect(PotionEffectType.SLOW,20*3,0,false,false));
        }
    }
    public void setChute(boolean b ){
        chute=b;
    }
    public boolean IsChute(){
        return chute;
    }

    @Override
    public void TakingDamage(Player player, EntityDamageEvent e) {
        if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
            if(!chute)e.setCancelled(true);
        }
    }
}