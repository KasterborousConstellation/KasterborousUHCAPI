package fr.supercomete.head.role.content.EchoEchoUHC;
import fr.supercomete.enums.Camps;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.role.EchoRole;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.Triggers.Trigger_OnHitPlayer;
import fr.supercomete.nbthandler.NbtTagHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Lucien extends EchoRole implements Trigger_OnHitPlayer {
    ArrayList<UUID>hittedplayer = new ArrayList<>();
    public Lucien(UUID owner) {
        super(owner);
    }

    @Override
    public BranlusqueLevel getBranlusqueLevel() {
        return BranlusqueLevel.MageTeacher;
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
        return "Lucien";
    }

    @Override
    public Camps getDefaultCamp() {
        return Camps.EchoEcho;
    }

    @Override
    public List<String> askRoleInfo() {
        return Arrays.asList("Votre but est de gagner avec les autres membres de "+Camps.EchoEcho.getColoredName(),"Votre toxicité vous octroye une potion de "+ ChatColor.DARK_GREEN +"poison","De plus a cause de vos engagements politiques vous obtenez une épée nommé 'Mein Kampf' avec tranchant 3 et aura de feu.","Votre masse imposante fait que le premier coup de la partie que vous infligez à un joueur lui donne l'effet "+ ChatColor.DARK_GRAY +"blindness. (Coup avec l'épée 'Mein Kampf' uniquement)");
    }

    @Override
    public ItemStack[] askItemStackgiven() {
        Potion potion = new Potion(PotionType.POISON);
        potion.setSplash(true);
        ItemStack stack=potion.toItemStack(1);
        ItemStack epee = InventoryUtils.getItem(Material.DIAMOND_SWORD,"§cMein Kampf",null);
        ItemMeta meta = epee.getItemMeta();
        meta.addEnchant(Enchantment.DAMAGE_ALL,3,true);
        meta.addEnchant(Enchantment.FIRE_ASPECT,1,true);
        epee.setItemMeta(meta);
        epee = NbtTagHandler.createItemStackWithUUIDTag(epee,103);
        return new ItemStack[]{epee,stack};
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
    public boolean OnHitPlayer(Player player,Player hitted, double amount, EntityDamageEvent.DamageCause cause) {
        player.sendMessage("TEST");
        if(NbtTagHandler.hasUUIDTAG(player.getItemInHand())){
            player.sendMessage("TEST");
            if(NbtTagHandler.getUUIDTAG(player.getItemInHand())==103){
                player.sendMessage("TEST");
                if(!hittedplayer.contains(hitted.getUniqueId())){
                    player.sendMessage("TEST");
                    hittedplayer.add(hitted.getUniqueId());
                    PlayerUtility.addProperlyEffect(hitted,new PotionEffect(PotionEffectType.BLINDNESS,20*8,0,false,false));
                }
            }
        }
        return false;
    }
}
