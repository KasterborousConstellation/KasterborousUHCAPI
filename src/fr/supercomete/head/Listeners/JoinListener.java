package fr.supercomete.head.Listeners;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import fr.supercomete.head.permissions.PermissionManager;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import org.bukkit.scheduler.BukkitRunnable;

final class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if(player.isOp()){
            for(String string : Main.messages){
                player.sendMessage(string);
            }
        }
        if (Main.host == null) {
            Main.host = player.getUniqueId();
            PermissionManager.getPerms().put(Main.host,PermissionManager.host_perms);
            player.setOp(true);
        }
        if (Main.currentGame.isGameState(Gstate.Waiting)) {
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(Main.spawn);
            PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(((CraftPlayer) player).getHandle());
            new BukkitRunnable() {
                @Override
                public void run() {
                    for(Player player1:Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer) player1).getHandle().playerConnection.sendPacket(packet);
                        player.hidePlayer(player1);
                        player1.hidePlayer(player);
                        player.showPlayer(player1);
                        player1.showPlayer(player);
                    }
                }
            }.runTaskLater(Main.INSTANCE,3L);
            player.setMaxHealth(20);
            player.setHealth(20);
            player.setFoodLevel(20);
            player.getInventory().clear();
            PlayerUtility.GiveHotBarStuff(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 0));
            player.getInventory().setBoots(new ItemStack(Material.AIR));
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            player.getInventory().setChestplate(new ItemStack(Material.AIR));
            player.getInventory().setLeggings(new ItemStack(Material.AIR));

        } else {
            assert MapHandler.getMap() != null;
            if (!Main.playerlist.contains(player.getUniqueId())) {
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(new Location(MapHandler.getMap().getPlayWorld(), 0, 150, 0));
                PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(((CraftPlayer) player).getHandle());
                for(Player player1: Bukkit.getOnlinePlayers()){
                    ((CraftPlayer)player1).getHandle().playerConnection.sendPacket(packet);
                }
            } else if (Main.currentGame.hasOfflinePlayer(player)) {
                Offline_Player offPlayer = Main.currentGame.getOffline_Player(player);
                Bukkit.broadcastMessage("§7Le joueur " + offPlayer.getUsername() + " se reconnecte après §a" + offPlayer.getTimeElapsedSinceLastConnexion() + "s");
            } else {
                player.getInventory().clear();
                player.teleport(MapHandler.getMap().getPlayWorld().getSpawnLocation());
                PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(((CraftPlayer) player).getHandle());
                for(Player player1: Bukkit.getOnlinePlayers()){
                    ((CraftPlayer)player1).getHandle().playerConnection.sendPacket(packet);
                }
            }
        }
    }

}
