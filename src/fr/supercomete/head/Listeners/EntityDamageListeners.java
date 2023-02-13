package fr.supercomete.head.Listeners;

import java.util.UUID;
import java.util.logging.Level;

import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEventHandler;
import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEvents;
import fr.supercomete.head.GameUtils.Fights.Fight;
import fr.supercomete.head.GameUtils.Fights.FightHandler;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import fr.supercomete.head.PlayerUtils.BonusHandler;
import fr.supercomete.head.PlayerUtils.KTBSEffect;
import fr.supercomete.head.role.Triggers.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.DeathCause;
import fr.supercomete.head.GameUtils.HistoricData;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.DelayedDeath;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.RoleState.RoleStateTypes;
import fr.supercomete.nbthandler.NbtTagHandler;
import fr.supercomete.tasks.Cycle;
import fr.supercomete.tasks.DelayedModeDeath;

class EntityDamageListeners implements Listener {
    private Main main;
    EntityDamageListeners(Main main){
        this.main=main;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerDeathEvent(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player player = (Player) e.getEntity();
            if (Main.currentGame.getNodamagePlayerList().contains(player.getUniqueId()) || Main.currentGame.getGamestate() == Gstate.Waiting || Main.currentGame.getGamestate() == Gstate.Starting) {
                e.setCancelled(true);
                return;
            }
            if (RoleHandler.IsRoleGenerated()) {
                if (RoleHandler.getRoleOf(player) != null) {
                    if(RoleHandler.getRoleOf(player)instanceof Trigger_OnTakingHit){
                        Trigger_OnTakingHit hit=(Trigger_OnTakingHit)RoleHandler.getRoleOf(player);
                        hit.TakingDamage(player,e);
                    }
                }
            }
            if (Main.currentGame.getScenarios().contains(Scenarios.NoFire)) {
                if (!e.isCancelled()) {
                    if (e.getCause() == EntityDamageEvent.DamageCause.FIRE
                            || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
                            || e.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
            if (Main.currentGame.getScenarios().contains(Scenarios.NoFall)) {
                if (!e.isCancelled()) {
                    if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                        e.setCancelled(true);
                        return;
                    }
                }
            } else if (RoleHandler.IsRoleGenerated()) {
                final Role role = RoleHandler.getRoleOf(player);
                if (role.hasBonus(BonusType.NoFall) && EntityDamageEvent.DamageCause.FALL == e.getCause()) {
                    e.setCancelled(true);
                    return;
                }
            }
        }

        float strengthrate = Main.currentGame.getDataFrom(Configurable.LIST.ForcePercent);
        float resistancerate = Main.currentGame.getDataFrom(Configurable.LIST.ResistancePercent);
        if (e.getCause().equals(DamageCause.ENTITY_ATTACK)) {
            final EntityDamageByEntityEvent f = (EntityDamageByEntityEvent) e;
            if (f.getDamager() instanceof Player ) {
                final Player damager = (Player) f.getDamager();
                int sharp_level = damager.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL);
                // Thanks to package com.yahoo.brettbutcher98.PotionFix;
                // For the strengthfix
                int rolebonus = 0;
                int base_bonus = BonusHandler.getTotalOfBonus(damager,BonusType.Force);
                if (RoleHandler.IsRoleGenerated()) {
                    Role role = RoleHandler.getRoleOf(damager);
                    rolebonus= role.getPowerOfBonus(BonusType.Force);
                }

                if (damager.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                    float amplifier = 1;
                    for (final PotionEffect effect : damager.getActivePotionEffects()) {
                        if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                            amplifier = effect.getAmplifier() + 1;
                        }
                    }
                    double total = rolebonus+base_bonus;
                    e.setDamage(e.getDamage()-1.25*sharp_level);
                    e.setDamage((e.getDamage() / (1 + 1.299999952316284F * amplifier)));
                    e.setDamage(e.getDamage()+1.25*sharp_level);
                    e.setDamage(e.getDamage()* ((100.0f + total+(strengthrate * amplifier)) / 100.0f));
                }else{
                    e.setDamage(e.getDamage() * ((double)100+rolebonus+base_bonus)/100.0);
                }
                if (f.getEntity() instanceof Player) {
                    final Player player = (Player) e.getEntity();
                    rolebonus = 0;
                    base_bonus=BonusHandler.getTotalOfBonus(player,BonusType.Damage_Resistance);
                    if(RoleHandler.IsRoleGenerated()){
                        rolebonus = RoleHandler.getRoleOf(player).getPowerOfBonus(BonusType.Damage_Resistance);
                    }
                    if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                        float amplifier = 1;
                        for (final PotionEffect effect : player.getActivePotionEffects()) {
                            if (effect.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) {
                                amplifier = effect.getAmplifier() + 1;
                            }
                        }
                        if ((resistancerate * amplifier)+rolebonus+base_bonus>= 100) {
                            e.setCancelled(true);

                        }
                        e.setDamage((e.getDamage()/(1.0f-0.2f*amplifier)) * ((100.0f - (rolebonus+base_bonus+(resistancerate * amplifier)))/ 100.0f));
                    }else{
                        e.setDamage(e.getDamage()*(100.0f-(base_bonus+rolebonus))/100.0f);
                    }
                }
                if (f.getEntity() instanceof Player) {
                    boolean cancel = false;
                    Role role = RoleHandler.getRoleOf(damager);
                    if(RoleHandler.IsRoleGenerated()){
                        if (role instanceof Trigger_OnHitPlayer) {
                            boolean tmp = ((Trigger_OnHitPlayer) role).OnHitPlayer(damager,(Player) f.getEntity(), f.getDamage(), f.getCause());
                            if (tmp) {
                                cancel = true;
                            }
                        }
                    }

                    if (cancel) {
                        e.setCancelled(true);
                        return;
                    }
                    FightHandler.Fight(new Fight((f.getEntity()).getUniqueId(),damager.getUniqueId()));
                }
            }
            final Entity damagerit = f.getDamager();
            final Entity damaged = f.getEntity();

        } else if (e.getCause().equals(DamageCause.PROJECTILE)) {
            if (e.getEntity() instanceof Player) {
                Player player = (Player) e.getEntity();
                EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
                if (event.getDamager() instanceof Projectile) {
                    Projectile proj = (Projectile) event.getDamager();
                    if (proj.getShooter() instanceof Player) {
                        Player shooter = (Player) proj.getShooter();
                        if (RoleHandler.IsRoleGenerated()) {
                            Role role = RoleHandler.getRoleOf(shooter);
                            boolean cancel = false;
                            if (role instanceof Trigger_OnHitPlayer) {
                                boolean tmp = ((Trigger_OnHitPlayer) role).OnHitPlayer(shooter,player, e.getDamage(), e.getCause());
                                if (tmp) {
                                    cancel = true;
                                }
                            }
                            if (cancel) {
                                e.setCancelled(true);
                                return;
                            }
                            FightHandler.Fight(new Fight(player.getUniqueId(),shooter.getUniqueId()));
                        }
                    }
                }
            }
        }
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            Player damager = null;
            DeathCause.EnvironnementalCause advancedCause = DeathCause.EnvironnementalCause.PVE;
            if ((player.getHealth() - e.getFinalDamage()) <= 0
                    && !Main.currentGame.getNodamagePlayerList().contains(player.getUniqueId())
                    && Main.currentGame.getGamestate() != Gstate.Waiting
                    && Main.currentGame.getGamestate() != Gstate.Playing) {
                if (e.getCause().equals(DamageCause.ENTITY_ATTACK)) {
                    EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
                    if (event.getDamager() instanceof Player) {
                        damager = (Player) event.getDamager();
                        advancedCause = DeathCause.EnvironnementalCause.Sword;
                    }
                } else if (e.getCause().equals(DamageCause.PROJECTILE)) {
                    EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
                    if (event.getDamager() instanceof Projectile) {
                        Projectile proj =(Projectile) event.getDamager();
                        if (proj.getShooter() instanceof Player) {
                            damager = (Player) proj.getShooter();
                            advancedCause = DeathCause.EnvironnementalCause.Bow;
                        }
                    }
                } else if(e.getCause().equals(DamageCause.FIRE)||e.getCause().equals(DamageCause.FIRE_TICK)){
                    final Player lastDamagerOf = FightHandler.getLastDamagerof(player);
                    if(lastDamagerOf!=null&&Main.getPlayerlist().contains(lastDamagerOf.getUniqueId()))
                        damager=lastDamagerOf;
                    advancedCause = DeathCause.EnvironnementalCause.Fire;
                }else if(e.getCause().equals(DamageCause.FALL)){
                    final Player lastDamagerOf = FightHandler.getLastDamagerof(player);
                    if(lastDamagerOf!=null&&Main.getPlayerlist().contains(lastDamagerOf.getUniqueId()))
                        damager=lastDamagerOf;
                    advancedCause = DeathCause.EnvironnementalCause.Fall;
                }else if(e.getCause().equals(DamageCause.LAVA)){
                    final Player lastDamagerOf = FightHandler.getLastDamagerof(player);
                    if(lastDamagerOf!=null&&Main.getPlayerlist().contains(lastDamagerOf.getUniqueId()))
                        damager=lastDamagerOf;
                    advancedCause = DeathCause.EnvironnementalCause.Lava;
                }else if(e.getCause().equals(DamageCause.POISON)||e.getCause().equals(DamageCause.WITHER)){
                    final Player lastDamagerOf = FightHandler.getLastDamagerof(player);
                    if(lastDamagerOf!=null&&Main.getPlayerlist().contains(lastDamagerOf.getUniqueId()))
                        damager=lastDamagerOf;
                    advancedCause = DeathCause.EnvironnementalCause.Wither;
                }
                e.setCancelled(true);
                if (Configurable.ExtractBool(Configurable.LIST.ReviveBeforePvp)) {
                    if (Main.currentGame.getMode() instanceof CampMode) {
                        if (!RoleHandler.IsRoleGenerated()) {
                            Bukkit.broadcastMessage(Main.UHCTypo+"Le joueur +§4" + player.getName()+ "§7 a été revive.");
                            player.setHealth(player.getMaxHealth());
                            assert MapHandler.getMap() != null;
                            MapHandler.getMap().PlayerRandomTPMap(player,12);
                            return;
                        }
                    } else {
                        if (!Cycle.hasPvpForced || (Main.currentGame.getTimer(Timer.PvPTime).getData() > Main.currentGame.getTime() && !Cycle.hasPvpForced)) {
                            player.setHealth(player.getMaxHealth());
                            MapHandler.getMap().PlayerRandomTPMap(player,12);
                            return;
                        }
                    }
                }
                if (RoleHandler.IsRoleGenerated()) {
                    boolean revive = false;
                    Role role = RoleHandler.getRoleOf(player);
                    if (role instanceof Trigger_OnOwnerDeath) {
                        boolean bool = ((Trigger_OnOwnerDeath) role).onOwnerDeath(player, damager);
                        if (bool) revive = true;
                    }


                    for (UUID uu : Main.getPlayerlist()) {
                        if (Bukkit.getPlayer(uu) != null) {
                            Player current = Bukkit.getPlayer(uu);
                            Role r = RoleHandler.getRoleOf(current);
                            if (r instanceof Trigger_OnAnyKill) {
                                ((Trigger_OnAnyKill) r).onOtherKill(player, damager);
                            }

                        }
                    }

                    if (revive) {
                        player.setHealth(player.getMaxHealth());
                        assert MapHandler.getMap() != null;
                        MapHandler.getMap().PlayerRandomTPMap(player,12);
                        return;
                    }
                    if (damager != null) {
                        Role r2 = RoleHandler.getRoleOf(damager);
                        if (r2 instanceof Trigger_OnKill) {
                            ((Trigger_OnKill) r2).onKill(damager, player);
                        }
                    }
                }
                // To cancel death heal the player and add return statement
                // Don't forget to add a break statement at each end of a mode death declaration
                Location deathLocation = player.getLocation();
                if (deathLocation == null)
                    return;
                Mode mode = Main.currentGame.getMode();
                if (mode instanceof DelayedDeath && RoleHandler.IsRoleGenerated()) {
                    DelayedModeDeath delayed = new DelayedModeDeath(mode, deathLocation, damager, player, ((DelayedDeath)mode).getDeathDelay());
                    player.setGameMode(GameMode.SPECTATOR);
                    delayed.runTaskTimer(main, 0, 20L);
                } else {
                    player.getWorld().playSound(player.getLocation(), Sound.WITHER_SPAWN, 20, 0);
                    mode.OnKillMethod(deathLocation, player, damager);
                    mode.ModeDefaultOnDeath(player,damager,player.getLocation());
                }
                if(damager==null){
                    PlayerEventHandler.Event("Suicide",player,player.getLocation());
                }else {
                    PlayerEventHandler.Event(PlayerEvents.Kill, damager, player.getLocation());
                }
                if (damager != null) {
                    if (Main.currentGame.getKillList().containsKey(damager.getUniqueId())) {
                        Main.currentGame.getKillList().put(damager.getUniqueId(), Main.currentGame.getKillList().get(damager.getUniqueId()) + 1);
                    } else {
                        Main.currentGame.getKillList().put(damager.getUniqueId(), 1);
                    }
                }
                if (Main.currentGame.getMode() instanceof CampMode) {
                    HistoricData data = RoleHandler.getHistoric().getEntry(player.getUniqueId());
                    if (damager != null) {
                        data.setCause(new DeathCause (damager,advancedCause));
                    } else {
                        data.setCause(new DeathCause());
                    }
                    RoleHandler.getHistoric().setEntry(player.getUniqueId(), data);
                }

            }
        }
    }
}
