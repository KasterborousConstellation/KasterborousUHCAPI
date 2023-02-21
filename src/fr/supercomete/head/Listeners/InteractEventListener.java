package fr.supercomete.head.Listeners;


import fr.supercomete.head.Inventory.GUI.TeleportGUI;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.Triggers.Trigger_OnInteractWithUUIDItem;

import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;




import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
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
            new TeleportGUI(player).open();
        }
        if (currentItem.getType() == Material.BANNER
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
                    ((Trigger_OnInteractWithUUIDItem)role).OnInteractWithUUIDItem(player,NbtTagHandler.getUUIDTAG(currentItem),e);
                }
            }
        }
    }
}
