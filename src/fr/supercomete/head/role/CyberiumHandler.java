package fr.supercomete.head.role;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.head.GameUtils.GameMode.Modes.DWUHC;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.world.worldgenerator;
import fr.supercomete.nbthandler.NbtTagHandler;
public class CyberiumHandler {
	public static Entity entity=null;
	public static UUID HostPlayer=null;
	public static Location Cyberiumlocation=null;
	public static void reset() {
		CyberiumHandler.Cyberiumlocation=null;
		CyberiumHandler.HostPlayer=null;
		CyberiumHandler.entity=null;
	}
	
	
	
	public static void GenerateLocationOfDropCyberium(boolean firstdrop, Player optionalplayer) {
		Location location;
		if (firstdrop == false) {
			int randomness = 10;
			int x = (int) (new Random().nextInt(randomness) - randomness / 2 + optionalplayer.getLocation().getX());
			int y = 200;
			int z = (int) (new Random().nextInt(randomness) - randomness / 2 + optionalplayer.getLocation().getZ());
			location = new Location(optionalplayer.getWorld(), x, y, z);
			worldgenerator.currentPlayWorld.loadChunk(worldgenerator.currentPlayWorld.getChunkAt(location));
			worldgenerator.currentPlayWorld.spawnEntity(location,EntityType.ARMOR_STAND);
			RoleHandler.sendmessagetoallCyberman("L'ancien hôte a perdu le §4Cyberium§r, le §6Traceur§r a été mis à jour");
		} else {
			int x = new Random().nextInt((int) Main.currentGame.getFirstBorder())
					- (int) Main.currentGame.getFirstBorder() / 2 + 0;
			int y = 200;
			int z = new Random().nextInt((int) Main.currentGame.getFirstBorder())
					- (int) Main.currentGame.getFirstBorder() / 2 + 0;
			location = new Location(worldgenerator.currentPlayWorld, x, y, z);
			worldgenerator.currentPlayWorld.loadChunk(worldgenerator.currentPlayWorld.getChunkAt(location));
			worldgenerator.currentPlayWorld.spawnEntity(location,EntityType.ARMOR_STAND);
			RoleHandler.sendmessagetoallCyberman("Le §4Cyberium§r est apparu, le §6Traceur§r a été mis à jour");
		}
		ArrayList<Entity> elist=new ArrayList<Entity>();
		for(Entity e :worldgenerator.currentPlayWorld.getEntities()){
			if(e.getType()==EntityType.ARMOR_STAND || e instanceof ArmorStand){
				elist.add(e);
			}
		}
		entity=elist.get(elist.size()-1);
		ArmorStand ent=(ArmorStand)entity;
		ent.setHelmet(InventoryUtils.createSkullItem(
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNiNGU0M2MyZjg1Nzc3MDVmM2VlMWM1NzBmYmJmNGUyN2ZkZTljM2RjMGU0NzI3MjFhYWE4YjA1Mzc3NWJlYiJ9fX0=",
				null, null));
		ent.setVisible(false);
		ent.setCustomName("§r§4Cyberium");
		ent.setCustomNameVisible(true);
		Cyberiumlocation = location;
		return;
	}
	public static void giveAllCybermanCyberriumCompass(){
		ItemStack it = InventoryUtils.getItem(Material.COMPASS,"§4Cyberium §6Traceur",Arrays.asList("§r§fCette boussole pointe vers la position du §4Cyberium","§rSi le §4Cyberium§r a été pris, la boussole pointe vers l'hôte du §4Cyberium","§rCet objet ne fonctionne que pour les membres des §4Cybermens"));
		it = NbtTagHandler.createItemStackWithUUIDTag(it, 8);
		for(UUID uu: RoleHandler.getRoleList().keySet()){
			Player player = Bukkit.getPlayer(uu);
			if(player==null)continue;
			if(player.isOnline()&&DWUHC.generateCybermanRoleList().contains(RoleHandler.getRoleOf(player).getClass())){
				InventoryUtils.addsafelyitem(player,it);
			}
		}
		return;
	}
	public static void setallCybermanCompassToLocation(Location loc){
		for(UUID uu : RoleHandler.getRoleList().keySet()){
			if(!DWUHC.generateCybermanRoleList().contains(RoleHandler.getRoleList().get(uu).getClass()))continue;
			if(loc==null)return;
			Player player = Bukkit.getPlayer(uu);
			if(player == null ) return ;
			player.setCompassTarget(loc);
		}
		return;
	}
	public static void SetCyberiumHost(UUID player){
		if(player == null) {
			HostPlayer=null;
			return;
		}
		HostPlayer=player;
	}
	public static void SetCyberiumHost(Player player){
		if(player==null) {
			HostPlayer=null;
			return;
		}
		HostPlayer=player.getUniqueId();
		return;
	}
}