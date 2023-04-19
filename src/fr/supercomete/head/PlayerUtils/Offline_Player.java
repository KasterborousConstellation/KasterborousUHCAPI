package fr.supercomete.head.PlayerUtils;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import fr.supercomete.head.core.Main;
import org.bukkit.inventory.PlayerInventory;

public class Offline_Player {
	private UUID player;
	private PlayerInventory inventory;
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
	public PlayerInventory getInventory() {
		return inventory;
	}
	public void setInventory(PlayerInventory inventory) {
		this.inventory = inventory;
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
