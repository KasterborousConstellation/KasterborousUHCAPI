package fr.supercomete.head.Listeners;

import java.util.UUID;

import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEventHandler;
import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEvents;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.Triggers.Trigger_OnInteractWithUUIDItem;
import fr.supercomete.head.role.content.DWUHC.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.GUI.TardisGUI;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.GameMode.Modes.DWUHC;
import fr.supercomete.head.Inventory.InventoryHandler;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.KarvanistaPacte.ShieldProposal;
import fr.supercomete.head.role.KarvanistaPacte.StasisProposal;
import fr.supercomete.head.role.Key.TardisHandler;
import fr.supercomete.head.world.worldgenerator;
import fr.supercomete.nbthandler.NbtTagHandler;
import fr.supercomete.tasks.Freeze;

final class InteractEventListener implements Listener {
    private final Main main;

    public InteractEventListener(Main main) {
        this.main = main;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        final ItemStack currentItem = e.getItem();
        final Action action = e.getAction();
        if (TardisHandler.IsTardisGenerated && Main.currentGame.getMode() instanceof DWUHC) {
            if (player.getLocation().getWorld() == TardisHandler.TardisLocation.getWorld()) {
                if (player.getLocation().distance(TardisHandler.TardisLocation) < 7) {
                    if (action == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getLocation().equals(TardisHandler.TardisLocation)) {
                        TardisHandler.currentTardis.teleport(player);
                        PlayerEventHandler.Event(PlayerEvents.EnterTardis, player, TardisHandler.TardisLocation);
                    }
                }
            } else if (player.getLocation().getWorld() == worldgenerator.structureworld && action == Action.RIGHT_CLICK_BLOCK) {
                Location heart = Main.currentGame.getMode().getStructure().get(0).getPositionRelativeToLocation(new int[]{18, 21, 21});
                if (RoleHandler.isIsRoleGenerated() && e.getClickedBlock().getLocation().distance(heart) < 2.5) {
                    new TardisGUI((DWRole) RoleHandler.getRoleOf(player), player).open();
                }
            }
        }
        if (TardisHandler.IsTardisGenerated &&
                Main.currentGame.getMode() instanceof DWUHC &&
                player.getLocation().getWorld() == worldgenerator.structureworld &&
                player.getLocation().distance(Main.currentGame.getMode().getStructure().get(0).getLocation()) < 150
        ) {
            if (currentItem != null && (currentItem.getType() == Material.WATER_BUCKET || currentItem.getType() == Material.LAVA_BUCKET)) {
                player.sendMessage(Main.UHCTypo + "§cImpossible de placer des seaux dans le tardis");
                e.setCancelled(true);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.updateInventory();
                    }
                }.runTaskLater(main, 0L);
            }
        }
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
            switch (NbtTagHandler.getUUIDTAG(currentItem)) {
                case 1:
                    if (RoleHandler.isIsRoleGenerated()) {
                        if (RoleHandler.getRoleOf(player) instanceof Vastra) {
                            Vastra vastra =(Vastra)RoleHandler.getRoleOf(player);
                            vastra.instant=true;
                            player.setItemInHand(new ItemStack(Material.AIR));
                            player.removePotionEffect(PotionEffectType.SPEED);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60 * 20, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60 * 20, 2));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60 * 20, 0));
                        } else
                            player.sendMessage(Main.UHCTypo + "§c Vous n'avez pas le bon rôle pour utiliser cet item");
                    } else
                        player.sendMessage(Main.UHCTypo + "§cLes rôles n'ont pas été généré");
                    break;
                case 2:
                    if (RoleHandler.isIsRoleGenerated()) {
                        if (RoleHandler.getRoleOf(player) instanceof RiverSong) {
                            if ((int) NbtTagHandler.getAnyTag(player.getItemInHand(), "VortexState") == 1) {
                                final ItemStack it = NbtTagHandler.addAnyTag(
                                        NbtTagHandler.createItemStackWithUUIDTag(
                                                (InventoryUtils.createColorItem(Material.INK_SACK,
                                                        "§bManipulateur de Vortex [§6Actif§b]", 1, (short) 14)),
                                                2),
                                        "VortexState", 2);
                                player.setItemInHand(it);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 1), false);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 9999999, 3), false);
                                break;
                            }
                            if ((int) NbtTagHandler.getAnyTag(player.getItemInHand(), "VortexState") == 2) {
                                final ItemStack it = NbtTagHandler
                                        .addAnyTag(
                                                NbtTagHandler.createItemStackWithUUIDTag(
                                                        (InventoryUtils.createColorItem(Material.INK_SACK,
                                                                "§bManipulateur de Vortex [§cDéchargé§b]", 1, (short) 14)),
                                                        2),
                                                "VortexState", 0);
                                player.setItemInHand(it);
                                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                                player.removePotionEffect(PotionEffectType.WEAKNESS);
                                break;
                            }
                        } else
                            player.sendMessage(Main.UHCTypo + "§c Vous n'avez pas le bon rôle pour utiliser cet item");
                    }
                    break;
                case 4:
                    if (RoleHandler.isIsRoleGenerated()) {
                        if (RoleHandler.getRoleOf(player) instanceof Supreme_Dalek) {
                            if (action.equals(Action.RIGHT_CLICK_AIR)) {
                                InventoryHandler.openinventory(player, 21);
                            }
                        }
                    }
                    break;
                case 5:
                    if (RoleHandler.isIsRoleGenerated()) {
                        if (RoleHandler.getRoleOf(player) instanceof CyberBrouilleur ) {
                            CyberBrouilleur brouilleur =(CyberBrouilleur)RoleHandler.getRoleOf(player);
                            if (action.equals(Action.RIGHT_CLICK_AIR)) {
                                Bukkit.broadcastMessage(
                                        Main.UHCTypo + "§4Les rôles des morts ont été brouillés jusqu'à l'épisode suivant");
                                player.setItemInHand(new ItemStack(Material.AIR));
                                RoleHandler.IsHiddenRoleNCompo = true;
                                brouilleur.hasUsed=true;
                            }
                        }
                    }
                    break;
                case 6:
                    if (RoleHandler.isIsRoleGenerated()) {
                        if (RoleHandler.getRoleOf(player) instanceof DalekCaan) {
                            final DalekCaan role = (DalekCaan) RoleHandler.getRoleOf(player);
                            role.setTarget(player.getUniqueId());
                            player.removePotionEffect(PotionEffectType.SPEED);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 90, 0, false, false));
                            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 90, 0, false, false));
                            player.getInventory().setItemInHand(null);
                            main.createDalekCaan(player);
                            Bukkit.broadcastMessage("§1---------------------------------------------");
                            final DalekCaan dalekCaan = new DalekCaan(UUID.randomUUID());
                            Bukkit.broadcastMessage(dalekCaan.getDefaultCamp().getColor() + dalekCaan.getName()
                                    + "§b a utilisé §6Fracture Temporelle§b: §4x:§f " + player.getLocation().getBlockX()
                                    + " §4y:§f " + player.getLocation().getBlockY() + " §4z:§f "
                                    + player.getLocation().getBlockZ());
                            Bukkit.broadcastMessage("§1---------------------------------------------");

                        }
                    }
                case 9:
                    e.setCancelled(true);
                    if (RoleHandler.isIsRoleGenerated()) {
                        if (RoleHandler.getRoleOf(player) instanceof GreatIntelligence ) {
                            GreatIntelligence role = (GreatIntelligence) RoleHandler.getRoleOf(player);
                            final CoolDown freeze = role.freeze;
                            if (freeze.isAbleToUse()) {
                                if (freeze.getUtilisation() > 0) {
                                    freeze.setLastuse(Main.currentGame.getTime());
                                    final Freeze freezetask = new Freeze(player.getUniqueId(), 20);
                                    freezetask.runTaskTimer(main, 0, 20L);
                                    player.sendMessage(Main.UHCTypo + "Vous avez activé votre capacité de Ralentissement.");
                                    freeze.addUtilisation(-1);
                                } else
                                    player.sendMessage(Main.UHCTypo + "Vous n'avez plus d'utilisation de cette capacité.");
                            } else
                                player.sendMessage(Main.UHCTypo + "Vous ne pouvez pas utiliser cette capacité pour l'instant. Attendre encore: " + TimeUtility.transform(freeze.getRemainingTime(), "§c", "§c", "§c"));


                        }
                    }
                    break;
                case 10:
                    e.setCancelled(true);
                    if (RoleHandler.isIsRoleGenerated()) {
                        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            if (RoleHandler.getRoleOf(player) instanceof Bill_Potts) {
                                final Bill_Potts role = (Bill_Potts) RoleHandler.getRoleOf(player);
                                final CoolDown instanthealth = role.infinitePotionCoolDown;
                                if (instanthealth.isAbleToUse()) {
                                    e.getItem().setAmount(2);
                                    e.setCancelled(false);
                                    instanthealth.setUseNow();
                                } else {
                                    player.sendMessage(Main.UHCTypo + "§cVous ne pouvez pas utiliser cette capacité pour l'instant. Temps restant: " + TimeUtility.transform(instanthealth.getRemainingTime(), "§c", "§c", "§c"));
                                }
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        player.updateInventory();
                                    }
                                }.runTaskLater(main, 0L);
                            }
                        }
                    }

                    break;
                case 12:
                    e.setCancelled(true);
                    if (RoleHandler.isIsRoleGenerated()) {
                        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            if (RoleHandler.getRoleOf(player) instanceof Karvanista ) {
                                Karvanista karvanista = (Karvanista) RoleHandler.getRoleOf(player);
                                final Player target = PlayerUtility.getTarget(player, 20);
                                if (target == null) {
                                    player.sendMessage(Main.UHCTypo + "§cLe joueur n'est pas correctement indiqué.");
                                } else {
                                    if (karvanista.getProposal(StasisProposal.class).IsActivated) {
                                        if (player.getWorld() != worldgenerator.structureworld) {
                                            final StasisProposal proposal = (StasisProposal) karvanista.getProposal(StasisProposal.class);
                                            if (proposal.cooldown.isAbleToUse()) {
                                                proposal.cooldown.setUseNow();
                                                Block loc = target.getLocation().getBlock();
                                                for (int x = -2; x < 3; x++) {
                                                    for (int y = -2; y < 3; y++) {
                                                        for (int z = -2; z < 3; z++) {
                                                            target.getWorld().getBlockAt(loc.getLocation().add(x, y, z)).setType(Material.STAINED_GLASS);
                                                            Block block = target.getWorld().getBlockAt(loc.getLocation().add(x, y, z));
                                                            block.setType(Material.STAINED_GLASS);
                                                            block.setData((byte) 3);
                                                            block.setMetadata("unbreakable", new FixedMetadataValue(main, true));
                                                        }
                                                    }
                                                }
                                                for (int x = -1; x < 2; x++) {
                                                    for (int y = -1; y < 2; y++) {
                                                        for (int z = -1; z < 2; z++) {
                                                            target.getWorld().getBlockAt(loc.getLocation().add(x, y, z)).setType(Material.AIR);
                                                            target.getWorld().removeMetadata("unbreakable", main);
                                                        }
                                                    }
                                                }
                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        for (int x = -2; x < 3; x++) {
                                                            for (int y = -2; y < 3; y++) {
                                                                for (int z = -2; z < 3; z++) {
                                                                    Block block = target.getWorld().getBlockAt(loc.getLocation().add(x, y, z));
                                                                    block.setType(Material.AIR);
                                                                    block.removeMetadata("unbreakable", main);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }.runTaskLater(main, 20 * 20);
                                            } else {
                                                player.sendMessage(Main.UHCTypo + "§cIl vous reste encore " + TimeUtility.transform(proposal.cooldown.getRemainingTime(), "§4", "§4", "§4") + "§c avant de pouvoir utiliser cet objet.");
                                            }
                                        } else
                                            player.sendMessage(Main.UHCTypo + "§cVous ne pouvez pas utilisé cet objet dans ce monde.");
                                    }
                                }
                            } else {
                                player.sendMessage(Main.UHCTypo + "§cVous n'avez pas le bon role pour utiliser cet objet.");
                            }
                        }
                    }
                    break;
                case 13:
                    if (RoleHandler.isIsRoleGenerated()) {
                        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            if (RoleHandler.getRoleOf(player) != null && RoleHandler.getRoleOf(player) instanceof Karvanista ) {
                                Karvanista karvanista = (Karvanista) RoleHandler.getRoleOf(player);
                                if (karvanista.getProposal(ShieldProposal.class).IsActivated) {
                                    ShieldProposal proposal = (ShieldProposal) karvanista.getProposal(ShieldProposal.class);
                                    if (proposal.CanUse) {
                                        if (Bukkit.getPlayer(karvanista.getTarget()) != null && RoleHandler.getRoleList().containsKey(karvanista.getTarget()) && Bukkit.getPlayer(karvanista.getTarget()).isOnline()) {
                                            if (player.getWorld() != worldgenerator.structureworld && Bukkit.getPlayer(karvanista.getTarget()).getWorld() != worldgenerator.structureworld) {
                                                for (int x = -2; x < 3; x++) {
                                                    for (int y = -2; y < 3; y++) {
                                                        for (int z = -2; z < 3; z++) {
                                                            player.getWorld().getBlockAt(player.getLocation().add(x, y + 8, z)).setType(Material.STAINED_GLASS);
                                                            Block block = player.getWorld().getBlockAt(player.getLocation().add(x, y + 8, z));
                                                            block.setType(Material.STAINED_GLASS);
                                                        }
                                                    }
                                                }
                                                proposal.CanUse = false;
                                                final Player target = Bukkit.getPlayer(karvanista.getTarget());
                                                final Location location = player.getLocation();
                                                location.add(0, 8, 0);
                                                target.teleport(location);
                                            } else
                                                player.sendMessage(Main.UHCTypo + "Vous ne pouvez pas utilisé cet objet car vous et votre allié ne sont pas dans les bons mondes");
                                        } else
                                            player.sendMessage(Main.UHCTypo + "Impossible votre allié est déconnecté ou mort");
                                    } else
                                        player.sendMessage(Main.UHCTypo + "Impossible car vous avez déja utilisé cet objet pendant cet épisode.");
                                }
                            }
                        }
                    }

                    break;
                case 14:
                    if (RoleHandler.isIsRoleGenerated()) {
                        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
                            if (RoleHandler.getRoleOf(player) != null && RoleHandler.getRoleOf(player) instanceof Tecteun) {
                                final Tecteun tecteun = (Tecteun) RoleHandler.getRoleOf(player);
                                if (player.getWorld() == worldgenerator.currentPlayWorld) {
                                    player.setItemInHand(null);
                                    tecteun.createFluxZone(player, main);
                                }
                            }
                        }
                    }
                    break;
                case 15:
                    if(RoleHandler.isIsRoleGenerated()){
                        if(e.getAction()==Action.RIGHT_CLICK_AIR||e.getAction()==Action.RIGHT_CLICK_BLOCK){
                            final Role role = RoleHandler.getRoleOf(player);
                            if(role instanceof WeapingAngel){
                                WeapingAngel angel =(WeapingAngel)role;
                                Player target =PlayerUtility.getTarget(player,20);
                                if(target==null){
                                    player.sendMessage(Main.UHCTypo + "§cLe joueur n'est pas correctement indiqué.");
                                }else{
                                    if(angel.HasTemporalMark(target)){
                                        Location location = angel.getLocationOfTemporalMark(target);
                                        if(angel.coolDown.isAbleToUse()){
                                            angel.coolDown.setUseNow();
                                            player.teleport(location);
                                            target.teleport(location);
                                            player.sendMessage(Main.UHCTypo+"Vous êtes arrivé a la marque temporelle de "+target.getName()+" vous avez désormais pour 2min l'effet §cforce§7 et §bvitesse 2");
                                            PlayerUtility.addProperlyEffect(player,new PotionEffect(PotionEffectType.SPEED,20*60*2,1,false,false));
                                            PlayerUtility.addProperlyEffect(player,new PotionEffect(PotionEffectType.INCREASE_DAMAGE,20*60*2,0,false,false));
                                        }else player.sendMessage(Main.UHCTypo+"Vous devez attendre encore "+TimeUtility.transform(angel.coolDown.getRemainingTime(),"§4"));
                                    }else{
                                        player.sendMessage(Main.UHCTypo + "§cLe joueur n'a pas de marque temporelle.");
                                    }
                                }
                            }
                        }
                    }
                    break;
                default:
                    if(RoleHandler.isIsRoleGenerated()){
                        final Role role = RoleHandler.getRoleOf(player);
                        if(role instanceof Trigger_OnInteractWithUUIDItem){
                            ((Trigger_OnInteractWithUUIDItem)role).OnInteractWithUUIDItem(player,NbtTagHandler.getUUIDTAG(currentItem),e.getAction());
                        }
                    }
                    break;
            }
        }
    }
}
