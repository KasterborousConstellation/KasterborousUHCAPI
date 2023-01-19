package fr.supercomete.head.PlayerUtils;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
public class EffectHandler {
    public static HashMap<UUID, CopyOnWriteArrayList<KTBSEffect>> effects = new HashMap<>();

    public static void apply(Player player, KTBSEffect effect) {
        if (effects.containsKey(player.getUniqueId())) {
            CopyOnWriteArrayList<KTBSEffect> l = effects.get(player.getUniqueId());
            boolean found = false;
            for(KTBSEffect iterate : l){
                if(iterate.type==effect.type){
                    if(iterate.level<=effect.level){
                        l.remove(iterate);
                        l.add(effect);
                        found=true;
                        break;
                    }
                }
            }
            if(!found){
                l.add(effect);
            }
            effects.put(
                    player.getUniqueId(),
                    l
            );
        } else {
            effects.put(
                    player.getUniqueId(),
                    new CopyOnWriteArrayList<>(Collections.singletonList(effect))
            );
        }
    }

    private static boolean hasNullifier(UUID uuid) {
        for (KTBSEffect effect : effects.get(uuid)) {
            if (effect.type == null) {
                return true;
            }
        }
        return false;
    }

    public static void init() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(Main.currentGame.getGamestate()== Gstate.Waiting){
                    if(effects.size()>0){
                        effects.clear();
                    }
                }
                for (Map.Entry<UUID, CopyOnWriteArrayList<KTBSEffect>> entry : effects.entrySet()) {
                    final UUID uuid = entry.getKey();
                    final Player player = Bukkit.getPlayer(uuid);
                    if (player == null) {
                        continue;
                    }
                    if (player.isOnline()) {
                        final boolean hasNullifier = hasNullifier(uuid);
                        if (hasNullifier) {
                            for (final KTBSEffect effect : effects.get(uuid)) {
                                if (effect.type != null) {
                                    player.removePotionEffect(effect.type);
                                }
                                effect.duration-=20;
                            }
                        } else {
                            for (final KTBSEffect effect : effects.get(uuid)) {
                                addProperlyEffect(player, new PotionEffect(effect.type, effect.duration-1, effect.level, false, false));
                                effect.duration-=20;
                            }
                        }
                    }
                    final CopyOnWriteArrayList<KTBSEffect> list = effects.get(uuid);
                    for (final KTBSEffect effect : list) {
                        if (effect.duration <= 0) {
                            list.remove(effect);
                        }
                    }
                    effects.put(uuid, list);
                }
            }
        }.runTaskTimer(
                Main.INSTANCE, 0, 20L
        );
    }

    private static void addProperlyEffect(@Nullable Player player, PotionEffect effect) {
        /*
         * Add a potion effect only if the player has the same potioneffect with a lower amplifier or if the player hasn't this potioneffect
         */
        if (player == null) return;
        if (player.hasPotionEffect(effect.getType())) {
            int amplifier = 0;
            for (final PotionEffect pot : player.getActivePotionEffects()) {
                if (pot.getType().equals(effect.getType())) amplifier = pot.getAmplifier();
            }
            if (amplifier <= effect.getAmplifier()) {
                player.removePotionEffect(effect.getType());
                player.addPotionEffect(effect);
            }
        } else {
            player.addPotionEffect(effect);
        }
    }
}