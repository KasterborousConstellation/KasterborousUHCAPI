package fr.supercomete.head.API;
import fr.supercomete.head.PlayerUtils.KTBSEffect;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

public interface PotionEffectProvider {
    boolean hasNullifer(Player player);
    void applyPotionEffect(Player player,KTBSEffect effect);
    ArrayList<PotionEffect>getPotionEffect(Player player);
}