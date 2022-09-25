package fr.supercomete.head.Listeners;

import java.util.*;


import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.Triggers.Trigger_OnInteractWithUUIDItem;
import org.bukkit.Bukkit;

import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.Inventory.InventoryHandler;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;

import fr.supercomete.head.role.RoleHandler;

import fr.supercomete.nbthandler.NbtTagHandler;
final class InteractEventListener implements Listener {



    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        ItemStack currentItem = e.getItem();
        final Action action = e.getAction();

        if (currentItem == null)
            return;
        if (currentItem.getType() == Material.ENDER_PEARL) {
            if (!Configurable.ExtractBool(Configurable.LIST.EnderPearl)) {
                e.setCancelled(true);
                player.sendMessage("Cet objet est désactivé");
            }
        }
        if (currentItem.getType() == Material.LAVA_BUCKET) {
            if (!Configurable.ExtractBool(Configurable.LIST.LavaBucket) && action == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);
                player.sendMessage("Cet objet est désactivé");
            }
        }
        if (NbtTagHandler.hasAnyTAG(currentItem, "RoomTp")) {
            InventoryHandler.openinventory(player, 20);
        }
        if (currentItem.getType() == Material.BANNER && Main.currentGame.IsTeamActivated()
                && Main.currentGame.getGamestate() == Gstate.Waiting
                && currentItem.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
            player.performCommand("team");
        }
        if ((currentItem.getType() == Material.SNOW_BALL || currentItem.getType() == Material.EGG)
                && Main.currentGame.getScenarios().contains(Scenarios.NoSnowBall)) {
            e.setCancelled(true);
            player.sendMessage(Main.UHCTypo + "§cLe scénario " + Scenarios.NoSnowBall.getName() + " est activé");
            return;
        }
        if (currentItem.getType() == Material.FISHING_ROD
                && Main.currentGame.getScenarios().contains(Scenarios.NoRod)) {
            e.setCancelled(true);
            player.sendMessage(Main.UHCTypo + "§cLe scénario " + Scenarios.NoRod.getName() + " est activé");
            return;
        }
        if (currentItem.getType() == Material.BOW && Main.currentGame.getScenarios().contains(Scenarios.NoBow)) {
            e.setCancelled(true);
            player.sendMessage(Main.UHCTypo + "§cLe scénario " + Scenarios.NoBow.getName() + " est activé");
            return;
        }
        if (currentItem.getType() == Material.SKULL_ITEM
                && currentItem.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)
                && !currentItem.getItemMeta().hasItemFlag(ItemFlag.HIDE_PLACED_ON)
                && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            e.setCancelled(true);
            player.performCommand("menu");
            return;
        }
        if (NbtTagHandler.hasUUIDTAG(currentItem)) {
            if(RoleHandler.IsRoleGenerated()){
                final Role role = RoleHandler.getRoleOf(player);
                if(role instanceof Trigger_OnInteractWithUUIDItem){
                    ((Trigger_OnInteractWithUUIDItem)role).OnInteractWithUUIDItem(player,NbtTagHandler.getUUIDTAG(currentItem),e.getAction());
                }
            }
        }
        if(NbtTagHandler.hasAnyTAG(currentItem,"FLAG")){
            e.setCancelled(true);
            int id = (int) NbtTagHandler.getAnyTag(currentItem,"FLAG");
            if(id==104) {
                if (RoleHandler.IsRoleGenerated()) {
                    if (RoleHandler.getRoleList().size() > 1) {
                        final ArrayList<Player> players = new ArrayList<>();
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (RoleHandler.getRoleList().containsKey(p.getUniqueId())) {
                                players.add(p);
                            }
                        }
                        int random = (players.size() == 0) ? 0 : new Random().nextInt(players.size());
                        final Player r1 = players.get(random);
                        players.remove(r1);
                        random = (players.size() == 0) ? 0 : new Random().nextInt(players.size());
                        final Player r2 = players.get(random);
                        final Role role1 = RoleHandler.getRoleOf(r1);
                        final Role role2 = RoleHandler.getRoleOf(r2);
                        final String str = (role1.getCamp().equals(role2.getCamp())) ? "§asont dans le même camp" : "§cne sont pas dans le même camp";
                        player.sendMessage(Main.UHCTypo + r1.getName() + " et " + r2.getName() + " " + str);
                        if (player.getItemInHand().getAmount() > 1) {
                            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                        } else {
                            player.setItemInHand(null);
                        }

                    } else {
                        player.sendMessage(Main.UHCTypo + "§cVous ne pouvez pas utiliser cet objet.");
                    }
                }
            }else if(id ==105) {
                if (RoleHandler.IsRoleGenerated()) {
                    if (player.getItemInHand().getAmount() > 1) {
                        player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                    } else {
                        player.setItemInHand(null);
                    }
                    final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 20 * 60, 0, false, false);
                    final PotionEffect resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60, 0, false, false);
                    final PotionEffect force = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60, 0, false, false);
                    PlayerUtility.addProperlyEffect(player, Arrays.asList(speed, resistance, force).get(new Random().nextInt(3)));
                }
            }
        }
    }
}
