package fr.supercomete.head.PlayerUtils;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.supercomete.head.Inventory.InventoryToBase64;
import fr.supercomete.head.core.Main;
public class Offline_Player {
	private UUID player;
	private String Serializedinventory;
	private Location location;
	private long lastdeconnexion;
	private String username;
	public Offline_Player(Player player) {
		this.player = player.getUniqueId();
		setInventory(player.getInventory());
		this.location = player.getLocation();
		this.username=player.getName();
		this.lastdeconnexion = Main.currentGame.getTime();
	}
	public UUID getPlayer() {
		return player;
	}
	
	public void setPlayer(UUID player) {
		this.player = player;
	}
	public Inventory getInventory() {
		try {
			return InventoryToBase64.fromBase64(Serializedinventory);
		} catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}
	public void setInventory(Inventory inventory) {
		String r = "";
		r= InventoryToBase64.toBase64(inventory);
		this.Serializedinventory =r;
	}
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public long getLastdeconnexion() {
		return lastdeconnexion;
	}

	public void setLastdeconnexion(long lastdeconnexion) {
		this.lastdeconnexion = lastdeconnexion;
	}

	public String getUsername() {
		return username;
	}
	public long getTimeElapsedSinceDeconnexion() {
		return Main.currentGame.getTime() - lastdeconnexion;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
