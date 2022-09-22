package fr.supercomete.head.role.content.EchoEchoUHC;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.EchoRole;
import fr.supercomete.head.role.Triggers.Trigger_OnInteractWithUUIDItem;
import fr.supercomete.head.role.Triggers.Trigger_WhileNight;
import fr.supercomete.nbthandler.NbtTagHandler;
import fr.supercomete.tasks.MangoJokeTask;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Flavio extends EchoRole implements Trigger_WhileNight, Trigger_OnInteractWithUUIDItem {
    private CoolDown yuriCooldown = new CoolDown(0,60);
    public Flavio(UUID owner) {
        super(owner);
    }

    @Override
    public BranlusqueLevel getBranlusqueLevel() {
        return BranlusqueLevel.Human;
    }

    @Override
    public float getMangaProbability() {
        return 0.8F;
    }

    @Override
    public String[] AskMoreInfo() {
        return new String[0];
    }

    @Override
    public String askName() {
        return "Flavio";
    }

    @Override
    public Camps getDefaultCamp() {
        return Camps.Solo;
    }

    @Override
    public List<String> askRoleInfo() {
        return Arrays.asList("Votre but est de gagner §6seul§7.","Votre humour sans failles vous octroye un objet nommé 'Blague de la mangue' qui lors de son activation provoque un rire unanime qui a pour effet de changer la direction dans laquelle ils regardent de tout les joueurs dans un rayon de 25 blocs du point d'activation de cet objet, ceci toute les 5s et pendant 2min. Utilisable une seule fois.",
                "Votre fanatisme pour les yuris vous donnes deux objets nommé 'Yuri' qui peuvent être consommés. Lorsque de sa consommation le 'Yuri' vous régénères votre vie totalement.",
                "A cause de votre cicle de sommeil atypique, vous avez l'effet §cforce§7 pendant la nuit"
                );
    }

    @Override
    public ItemStack[] askItemStackgiven() {
        ItemStack joke = InventoryUtils.getItem(Material.NETHER_STAR,"§bBlague de la mangue", Collections.singletonList("§aClique droit: §ractive la zone de la 'blague de la mangue'. La zone est statique."));
        joke = NbtTagHandler.createItemStackWithUUIDTag(joke,101);
        ItemStack mango = InventoryUtils.getItem(Material.NETHER_STAR,"§fYuri", Collections.singletonList("§rRestore toute votre vie lorsqu'elle est mangée."));
        mango = NbtTagHandler.createItemStackWithUUIDTag(mango,102);
        mango.setAmount(2);
        return new ItemStack[]{joke,mango};
    }

    @Override
    public boolean AskIfUnique() {
        return false;
    }

    @Override
    public String AskHeadTag() {
        return null;
    }

    @Override
    public void WhileNight(Player player) {
        PlayerUtility.addProperlyEffect(player,new PotionEffect(PotionEffectType.INCREASE_DAMAGE,3*20,0,false,false));
    }
    @Override
    public void OnInteractWithUUIDItem(Player player, int uuidtag, Action action) {
        if(uuidtag==101){
            player.setItemInHand(null);
            MangoJokeTask task = new MangoJokeTask(player.getLocation(),2*60, Main.INSTANCE,player);
        }else if(uuidtag==102&&(action==Action.RIGHT_CLICK_BLOCK||action==Action.RIGHT_CLICK_AIR)){
            if(yuriCooldown.isAbleToUse()){
                yuriCooldown.setUseNow();
                player.setHealth(player.getMaxHealth());
                if(player.getItemInHand().getAmount()>1){
                    player.getItemInHand().setAmount(player.getItemInHand().getAmount()-1);
                }else{
                    player.setItemInHand(null);
                }
                player.sendMessage(Main.UHCTypo+"Le Yuri vous a redonné toute votre vie");
            }else player.sendMessage(Main.UHCTypo+"Vous avez consommé un Yuri il y a peu de temps. Vous pourrez utilisé le Yuri a nouveau dans "+ TimeUtility.transform(yuriCooldown.getRemainingTime(),"§c"));
        }
    }
}