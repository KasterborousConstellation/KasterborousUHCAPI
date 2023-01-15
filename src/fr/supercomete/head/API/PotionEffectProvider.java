package fr.supercomete.head.API;
import fr.supercomete.head.PlayerUtils.KTBSEffect;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.UUID;

public interface PotionEffectProvider {
    boolean hasNullifer(Player player);
    void applyPotionEffect(Player player,KTBSEffect effect);
    ArrayList<PotionEffect>getPotionEffect(Player player);
    void addBonus(Player player, Bonus bonus);
    int getBonus(Player player, BonusType type);
    void addBonus(UUID uuid,Bonus bonus);
    int getBonus(UUID uuid,BonusType type);
}