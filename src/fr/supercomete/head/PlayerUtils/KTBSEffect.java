package fr.supercomete.head.PlayerUtils;

import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class KTBSEffect {

    /**
     * @param duration define how many ticks does the effect nullifier last
     * @param potionEffectTypeArrayList define the list of potioneffect that are affected
     * @param toremoveornot if WHITELIST then it remove only those effects else if BLACKLIST it remove all other effects
     * @return an object of type KTBSEFFECT that match the properties
     */
    public static @NotNull KTBSEffect getNullifer(int duration, ArrayList<PotionEffectType> potionEffectTypeArrayList, EffectNullifier.Type toremoveornot){
        return new EffectNullifier(duration,potionEffectTypeArrayList,toremoveornot);
    }
    public PotionEffectType type;
    public int level;
    public int duration;
    private KTBSEffect(int duration){
        level=0;
        this.duration=duration;
        type=null;
    }
    public  KTBSEffect(PotionEffectType type, int level, int duration) {
        this.duration = duration;
        this.type = type;
        this.level = level;
    }
}