package fr.supercomete.head.PlayerUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import fr.supercomete.head.role.RoleHandler;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockIterator;

import fr.supercomete.head.Inventory.InventoryToBase64;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.world.worldgenerator;
import fr.supercomete.nbthandler.NbtTagHandler;
import fr.supercomete.tasks.NoDamage;
import net.md_5.bungee.api.ChatColor;
public class PlayerUtility{
	private static Main main;
	public PlayerUtility(Main main) {
		PlayerUtility.main = main;
	}
	public static void giveStuff(ArrayList<UUID> playerlist){
		try {
			Inventory inv=InventoryToBase64.fromBase64(main.getConfig().getString("serverapi.stuff.actualconfig"));
			ItemStack[]it=inv.getContents();
			for(UUID uu: playerlist){
				Player pl = Bukkit.getPlayer(uu);
				for(int i=0;i<it.length;i++){
					pl.getInventory().setItem(i, it[i]);
				}
				pl.updateInventory();
			}
		}catch(IOException e){e.printStackTrace();}
	}
	public static void saveStuff(Player player) {
		Inventory inv=player.getInventory();
		String s= "";
		s =InventoryToBase64.toBase64(inv);
		main.getConfig().set("serverapi.stuff.actualconfig", s);
		main.saveConfig();
	}
	public static String getNameByUUID(UUID target){
        String ally="";
	    if(target==null) {
            ally = "§6Aucun";
        }else {
            if(Bukkit.getPlayer(target)!=null) {
                ally="§6"+Bukkit.getPlayer(target).getName();
            }else {
                if(Main.currentGame.hasOfflinePlayer(target))
                    ally ="§6"+Main.currentGame.getOffline_Player(target).getUsername();
            }
        }
	    return ally;
    }
	public static Inventory getInventory() {
		try {
			return InventoryToBase64.fromBase64(main.getConfig().getString("serverapi.stuff.actualconfig"));
		} catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}
	public static void GiveHotBarStuff(Player player) {
		if(player==null)return;
		if(Main.IsHost(player)) {
			ItemStack it = InventoryUtils.createSkullItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjkyMzUwYzlmMDk5M2VkNTRkYjJjNzExMzkzNjMyNTY4M2ZmYzIwMTA0YTliNjIyYWE0NTdkMzdlNzA4ZDkzMSJ9fX0=",ChatColor.DARK_PURPLE + "»" + ChatColor.LIGHT_PURPLE + " Menu " + ChatColor.DARK_PURPLE + "«",null);
			SkullMeta im = (SkullMeta) it.getItemMeta();
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			it.setItemMeta(im);
			player.getInventory().setItem(4, it);
			it = InventoryUtils.getItem(Material.SLIME_BALL, "§dTp Salle des rêgles", null);
			it = NbtTagHandler.addAnyTag(it, "RoomTp", 1);
			player.getInventory().setItem(1, it);
		}
		if (Main.currentGame.IsTeamActivated()) {
			ItemStack it = InventoryUtils.createColorItem(Material.BANNER, "§bTeam", 1, (short) 3);
			ItemMeta im = it.getItemMeta();
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			it.setItemMeta(im);
			player.getInventory().setItem(0, it);
		} else
			player.getInventory().setItem(0, null);
	}
	public static Player getTarget(Player player,int range){
		Player target;
		List<Entity> nearbyE = player.getNearbyEntities(range, range, range);
		ArrayList<Player> livingE = new ArrayList<Player>();
		for (Entity e : nearbyE) {
			if (e instanceof Player) {
				livingE.add((Player) e);
			}
		}
		target=null;
		BlockIterator bItr = new BlockIterator(player, range);
		Block block;
		Location loc;
		int bx, by, bz;
		double ex, ey, ez;
		// loop through player's line of sight
		while (bItr.hasNext()) {
			block = bItr.next();
			bx = block.getX();
			by = block.getY();
			bz = block.getZ();
			// check for entities near this block in the line of sight
			for (Player e : livingE) {
				loc = e.getLocation();
				ex = loc.getX();
				ey = loc.getY();
				ez = loc.getZ();
				if ((bx - .75 <= ex && ex <= bx + 1.75) && (bz - .75 <= ez && ez <= bz + 1.75)&& (by - 1 <= ey && ey <= by + 2.5)) {
					// entity is close enough, set target and stop
					target=e;
					break;
				}
			}
		}
		return target;
	}
	public static void PlayerRandomTPMap(Player player) {
		ArrayList<UUID> uu = new ArrayList<UUID>();
		uu.add(player.getUniqueId());
		Main.currentGame.setNodamagePlayerList(uu);
		NoDamage nodamage = new NoDamage(main, 20);
		nodamage.runTaskTimer(main, 0, 20L);
		player.teleport(new Location(worldgenerator.currentPlayWorld,new Random().nextInt((int)(Main.currentGame.getCurrentBorder()/2-(Main.currentGame.getCurrentBorder())/4)),150,new Random().nextInt((int)(Main.currentGame.getCurrentBorder()/2-(Main.currentGame.getCurrentBorder())/4))));
	}
	public static void addProperlyEffect(@Nullable Player player, PotionEffect effect) {
		/*
		 * Add a potion effect only if the player has the same potioneffect with a lower amplifier or if the player hasn't this potioneffect
		 */
        if(player==null)return;
		if(player.hasPotionEffect(effect.getType())) {
			int amplifier = 0;
			for(final PotionEffect pot : player.getActivePotionEffects()) {
				if(pot.getType().equals(effect.getType()))amplifier=pot.getAmplifier();
			}
			if(!(amplifier>effect.getAmplifier())) {
				player.removePotionEffect(effect.getType());
				player.addPotionEffect(effect);
			}
		}else {
			player.addPotionEffect(effect);
		}
	}
    public static void sendActionbar(Player p, String message) {
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(bar);
    }
}