package fr.supercomete.head.PlayerUtils;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.*;
public class EffectHandler {
    public static HashMap<UUID, List<KTBSEffect>> effects = new HashMap<>();

    public static void apply(Player player, KTBSEffect effect) {
        if (effects.containsKey(player.getUniqueId())) {
            List<KTBSEffect> l = effects.get(player.getUniqueId());
            l.add(effect);
            effects.put(
                    player.getUniqueId(),
                    l
            );
        } else {
            effects.put(
                    player.getUniqueId(),
                    Collections.singletonList(effect)
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
                for (Map.Entry<UUID, List<KTBSEffect>> entry : effects.entrySet()) {
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
                                effect.duration--;
                            }
                        } else {
                            for (final KTBSEffect effect : effects.get(uuid)) {
                                addProperlyEffect(player, new PotionEffect(effect.type, effect.duration, effect.level, false, false));
                                effect.duration--;
                            }
                        }
                    }
                    final List<KTBSEffect> list = effects.get(uuid);
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
            if (!(amplifier > effect.getAmplifier())) {
                player.removePotionEffect(effect.getType());
                player.addPotionEffect(effect);
            }
        } else {
            player.addPotionEffect(effect);
        }
    }
}