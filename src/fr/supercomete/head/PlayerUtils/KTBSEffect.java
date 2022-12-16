package fr.supercomete.head.PlayerUtils;

import org.bukkit.potion.PotionEffectType;

public class KTBSEffect {
    public static KTBSEffect getNullifer(int duration){
        return new KTBSEffect(duration);
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