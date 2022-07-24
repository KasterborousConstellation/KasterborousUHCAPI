package fr.supercomete.head.Listeners;

import java.util.UUID;

import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEventHandler;
import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEvents;
import fr.supercomete.head.GameUtils.Fights.Fight;
import fr.supercomete.head.GameUtils.Fights.FightHandler;
import fr.supercomete.head.role.RoleState.KarvanistaRoleState;
import fr.supercomete.head.role.Triggers.*;
import fr.supercomete.head.role.content.DWUHC.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Camps;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.DeathCause;
import fr.supercomete.head.GameUtils.HistoricData;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.ModeAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.DelayedDeath;
import fr.supercomete.head.GameUtils.GameMode.Modes.DWUHC;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.KarvanistaPacte.Proposal;
import fr.supercomete.head.role.RoleState.InfectedRoleState;
import fr.supercomete.head.role.RoleState.RoleStateTypes;
import fr.supercomete.head.role.content.DWUHC.SoldatUNIT.SoldierType;
import fr.supercomete.nbthandler.NbtTagHandler;
import fr.supercomete.tasks.Cycle;
import fr.supercomete.tasks.DelayedModeDeath;

record EntityDamageListeners(Main main) implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerDeathEvent(EntityDamageEvent e) {
        if (RoleHandler.isIsRoleGenerated()) {
            for (final Role role : RoleHandler.getRoleList().values()) {
                if (role instanceof GreatIntelligence) {
                    final GreatIntelligence great = (GreatIntelligence) role;
                    for (Snowman snowman : great.getEntities()) {
                        if (snowman == e.getEntity()) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
        if (e.getEntity() instanceof Player) {
            final Player player = (Player) e.getEntity();
            if (Main.currentGame.getNodamagePlayerList().contains(player.getUniqueId())
                    || Main.currentGame.getGamestate() == Gstate.Waiting
                    || Main.currentGame.getGamestate() == Gstate.Starting) {
                e.setCancelled(true);
                return;
            }
            if (RoleHandler.isIsRoleGenerated()) {
                if (RoleHandler.getRoleOf(player) != null) {
                    if(RoleHandler.getRoleOf(player)instanceof Trigger_OnTakingHit hit){
                        hit.TakingDamage(player,e);
                    }
                    if (RoleHandler.getRoleOf(player) instanceof Jenny_Flint) {
                        int total_peopleat100 = 0;
                        final Jenny_Flint flint = (Jenny_Flint) RoleHandler.getRoleOf(player);
                        for (UUID u : flint.getPercentmap().keySet()) {
                            if (flint.getPercentmap().get(u) == 100 && u != flint.getOwner()) {
                                total_peopleat100 += 1.0;
                            }
                        }
                        flint.getFirstBonus(BonusType.Force).setLevel(total_peopleat100);
                    }
                    if (RoleHandler.getRoleOf(player) instanceof Karvanista karvanista) {
                        if (karvanista.finished) {
                            for (final Proposal proposal : karvanista.allpacte) {
                                if (proposal instanceof Trigger_OnTakingHit hit) {
                                    hit.TakingDamage(player, e);
                                }
                            }
                        }
                    } else if (RoleHandler.getRoleOf(player).hasRoleState(RoleStateTypes.Karvanista)) {
                        KarvanistaRoleState component = (KarvanistaRoleState) RoleHandler.getRoleOf(player).getRoleState(RoleStateTypes.Karvanista);
                        for (Proposal proposal : component.proposal) {
                            if (proposal instanceof Trigger_OnTakingHit hit) {
                                hit.TakingDamage(player, e);
                            }
                        }
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
            } else if (RoleHandler.isIsRoleGenerated()) {
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
            if (f.getDamager() instanceof final Player damager) {
                // Thanks to package com.yahoo.brettbutcher98.PotionFix;
                // For the strengthfix
                if (damager.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                    float amplifier = 1;
                    for (final PotionEffect effect : damager.getActivePotionEffects()) {
                        if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                            amplifier = effect.getAmplifier() + 1;
                        }
                    }
                    e.setDamage((e.getDamage() / (1 + 1.299999952316284F * amplifier)) * ((100.0f + strengthrate * amplifier) / 100.0f));
                }
                if (f.getEntity() instanceof Player) {
                    final Player player = (Player) e.getEntity();
                    if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                        float amplifier = 1;
                        for (final PotionEffect effect : player.getActivePotionEffects()) {
                            if (effect.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) {
                                amplifier = effect.getAmplifier() + 1;
                            }
                        }
                        if (resistancerate * amplifier >= 100) {
                            e.setCancelled(true);

                        }
                        e.setDamage(e.getDamage() * (100 - (resistancerate * amplifier)) / 80.0);
                    }
                }
                if (RoleHandler.isIsRoleGenerated()) {
                    if (damager instanceof Player) {
                        Role role = RoleHandler.getRoleOf(damager);
                        double forcepercent = 100.0;
                        forcepercent += role.getPowerOfBonus(BonusType.Force);
                        e.setDamage(e.getDamage() * (forcepercent / 100.0));
                    }
                    if (f.getEntity() instanceof Player) {
                        Role role = RoleHandler.getRoleOf(damager);
                        boolean cancel = false;
                        if (role instanceof Trigger_OnHitPlayer) {
                            boolean tmp = ((Trigger_OnHitPlayer) role).OnHitPlayer((Player) f.getEntity(), f.getDamage(), f.getCause());
                            if (tmp) {
                                cancel = true;
                            }
                        }

                        if (NbtTagHandler.hasUUIDTAG(damager.getItemInHand())) {
                            if (NbtTagHandler.getUUIDTAG(damager.getItemInHand()) == 11 && f.getEntity() instanceof Player) {
                                PlayerUtility.addProperlyEffect((Player) f.getEntity(), new PotionEffect(PotionEffectType.CONFUSION, 10 * 20, 0, false, false));
                            }
                        }
                        if (cancel) {
                            e.setCancelled(true);
                            return;
                        }
                        FightHandler.Fight(new Fight(((Player)f.getEntity()).getUniqueId(),damager.getUniqueId()));
                    }

                }
            }
            final Entity damagerit = f.getDamager();
            final Entity damaged = f.getEntity();
            if (damagerit instanceof Player && damaged instanceof Player) {
                Player player = (Player) f.getDamager();
                Player dmg = (Player) f.getEntity();
                ItemStack currentItem = player.getItemInHand();
                if ((currentItem.getType() == Material.IRON_SWORD || currentItem.getType() == Material.WOOD_SWORD
                        || currentItem.getType() == Material.GOLD_SWORD
                        || currentItem.getType() == Material.DIAMOND_SWORD
                        || currentItem.getType() == Material.STONE_SWORD)
                        && Main.currentGame.getScenarios().contains(Scenarios.NoSword)) {
                    e.setCancelled(true);
                    player.sendMessage(Main.UHCTypo + "§cLe scénario " + Scenarios.NoSword.getName() + " est activé");
                    return;
                }
                if (ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode()) instanceof DWUHC
                        && RoleHandler.isIsRoleGenerated()) {
                    if ((currentItem.getType() == Material.IRON_SWORD || currentItem.getType() == Material.WOOD_SWORD
                            || currentItem.getType() == Material.GOLD_SWORD
                            || currentItem.getType() == Material.DIAMOND_SWORD
                            || currentItem.getType() == Material.STONE_SWORD)
                            && RoleHandler.getRoleOf(player) instanceof Supreme_Dalek) {
                        player.sendMessage(Main.UHCTypo + "+1PE");
                        ((Supreme_Dalek) RoleHandler.getRoleOf(player)).addPe(1);
                    }
                }
                if (NbtTagHandler.hasUUIDTAG(currentItem)) {
                    if (NbtTagHandler.getUUIDTAG(currentItem) == 3) {
                        player.setItemInHand(new ItemStack(Material.AIR));
                        if(RoleHandler.getRoleOf(player)instanceof CyberPlanner planner &&RoleHandler.getRoleOf(dmg).getDefaultCamp()==Camps.DoctorCamp && !RoleHandler.getRoleOf(dmg).hasRoleState(RoleStateTypes.Purified)){
                            RoleHandler.getRoleOf(dmg).addRoleState(new InfectedRoleState(RoleStateTypes.Infected));
                            RoleHandler.getRoleOf(dmg).setCamp(Camps.EnnemiDoctorCamp);
                            player.sendMessage(Main.UHCTypo + "Vous avez infecté " + dmg.getName());
                            dmg.sendMessage(Main.UHCTypo + "Vous avez été infecté par §4" + player.getName());
                            planner.infected=dmg.getUniqueId();
                        }else{
                            player.sendMessage(Main.UHCTypo + "L'infection a échoué.");
                        }

                    }
                }
            }
        } else if (e.getCause().equals(DamageCause.PROJECTILE)) {
            if (e.getEntity() instanceof Player) {
                Player player = (Player) e.getEntity();
                EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
                if (event.getDamager() instanceof Projectile) {
                    Projectile proj = (Projectile) event.getDamager();
                    if (proj.getShooter() instanceof Player) {
                        Player shooter = (Player) proj.getShooter();
                        if (RoleHandler.isIsRoleGenerated()) {
                            Role role = RoleHandler.getRoleOf(shooter);
                            boolean cancel = false;

                            if (role instanceof Trigger_OnHitPlayer) {
                                boolean tmp = ((Trigger_OnHitPlayer) role).OnHitPlayer(player, e.getDamage(), e.getCause());
                                if (cancel == false && tmp == true) {
                                    cancel = true;
                                }
                            }

                            if (cancel == true) {
                                e.setCancelled(true);
                                return;
                            }
                            FightHandler.Fight(new Fight(player.getUniqueId(),shooter.getUniqueId()));
                            if (role instanceof SoldatUNIT) {
                                SoldatUNIT soldat = (SoldatUNIT) role;
                                if (soldat.soldiertype == SoldierType.Garde) {

                                    CoolDown cap = soldat.generalCoolDown;
                                    if (((SoldatUNIT) role).isActivated()) {
                                        if (cap.getUtilisation() > 0) {
                                            cap.addUtilisation(-1);
                                            shooter.sendMessage(Main.UHCTypo + "§aVous avez touché votre cible avec une fleche empoisonnée. Utilisation restante: §4" + cap.getUtilisation());
                                            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 8 * 20, 0, false, false));
                                        }
                                    }
                                }
                            }
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
                    if (event.getDamager() instanceof Projectile proj) {
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
                        if (!RoleHandler.isIsRoleGenerated()) {
                            Bukkit.broadcastMessage(Main.UHCTypo+"Le joueur +§4" + player.getName()+ "§7 a été revive.");
                            player.setHealth(player.getMaxHealth());
                            PlayerUtility.PlayerRandomTPMap(player);
                            return;
                        }
                    } else {
                        if (!Cycle.hasPvpForced || (Main.currentGame.getTimer(Timer.PvPTime).getData() > Main.currentGame.getTime() && !Cycle.hasPvpForced)) {
                            player.setHealth(player.getMaxHealth());
                            PlayerUtility.PlayerRandomTPMap(player);
                            return;
                        }
                    }
                }
                if (RoleHandler.isIsRoleGenerated()) {
                    boolean revive = false;
                    Role role = RoleHandler.getRoleOf(player);
                    if (role instanceof Trigger_OnOwnerDeath) {
                        boolean bool = ((Trigger_OnOwnerDeath) role).onOwnerDeath(player, damager);
                        if (revive == false && bool == true) revive = true;
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
                        PlayerUtility.PlayerRandomTPMap(player);
                        return;
                    }
                    if (damager != null) {
                        Role r2 = RoleHandler.getRoleOf(damager);
                        if (r2 instanceof Trigger_OnKill) {
                            ((Trigger_OnKill) r2).onKill(damager, player);
                        }
                        if (r2.hasRoleState(RoleStateTypes.Karvanista)) {
                            KarvanistaRoleState component = (KarvanistaRoleState) r2.getRoleState(RoleStateTypes.Karvanista);
                            for (final Proposal proposal : component.proposal) {
                                if (proposal.IsActivated) {
                                    if (proposal instanceof Trigger_OnKill) {
                                        ((Trigger_OnKill) proposal).onKill(player, damager);
                                    }
                                }
                            }
                        }

                    }
                }
                // To cancel death heal the player and add return statement
                // Don't forget to add a break statement at each end of a mode death declaration
                Location deathLocation = player.getLocation();
                if (deathLocation == null)
                    return;
                Mode mode = ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode());
                if (mode instanceof DelayedDeath && RoleHandler.isIsRoleGenerated()) {
                    DelayedModeDeath delayed = new DelayedModeDeath(mode, deathLocation, damager, player, 10);
                    player.setGameMode(GameMode.SPECTATOR);
                    delayed.runTaskTimer(main, 0, 20L);
                } else {
                    player.getWorld().playSound(player.getLocation(), Sound.WITHER_SPAWN, 20, 0);
                    mode.OnKillMethod(deathLocation, player, damager);
                    if(damager==null){
                        PlayerEventHandler.Event("Suicide",player,player.getLocation());
                    }else{
                        PlayerEventHandler.Event(PlayerEvents.Kill,damager,player.getLocation());
                    }
                }
                if (Main.getPlayerlist().contains(player.getUniqueId()))
                    return;
                if (damager != null) {
                    if (Main.currentGame.getKillList().containsKey(damager.getUniqueId())) {
                        Main.currentGame.getKillList().put(damager.getUniqueId(), Main.currentGame.getKillList().get(damager.getUniqueId()) + 1);
                    } else {
                        Main.currentGame.getKillList().put(damager.getUniqueId(), 1);
                    }
                }

                if (Main.currentGame.getMode() instanceof CampMode) {
                    if (damager != null) {
                        HistoricData data = RoleHandler.getHistoric().getEntry(player.getUniqueId());
                        data.setCause(new DeathCause (damager,advancedCause));
                        RoleHandler.getHistoric().setEntry(player.getUniqueId(), data);
                    } else {
                        HistoricData data = RoleHandler.getHistoric().getEntry(player.getUniqueId());
                        data.setCause(new DeathCause());
                        RoleHandler.getHistoric().setEntry(player.getUniqueId(), data);
                    }
                }

            }
        }
    }
}
