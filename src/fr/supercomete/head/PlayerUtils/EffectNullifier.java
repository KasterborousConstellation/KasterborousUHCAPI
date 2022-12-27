package fr.supercomete.head.PlayerUtils;

import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;

public final class EffectNullifier extends KTBSEffect{
    private ArrayList<PotionEffectType> to_remove = new ArrayList<>();
    public EffectNullifier(int duration, ArrayList<PotionEffectType>potionEffectTypes,Type type) {
        super(null, 0, duration);
        if(type.equals(Type.BLACKLIST)){
            ArrayList<PotionEffectType> types = new ArrayList<>(Arrays.asList(PotionEffectType.values()));
            for(PotionEffectType i : potionEffectTypes){
                types.remove(i);
            }
            to_remove=types;
        }else{
            to_remove=potionEffectTypes;
        }
    }
    public static enum Type{
        WHITELIST,
        BLACKLIST
    }
}
