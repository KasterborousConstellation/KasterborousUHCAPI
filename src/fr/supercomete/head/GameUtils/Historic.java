package fr.supercomete.head.GameUtils;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.supercomete.head.role.RoleHandler;

public class Historic {
	private final HashMap<UUID, HistoricData> rolelist = new HashMap<>();
	
	public Historic() {
		for(UUID uu : RoleHandler.getRoleList().keySet()) {
			rolelist.put(uu, new HistoricData(RoleHandler.getRoleList().get(uu),Bukkit.getPlayer(uu)));
		}
	} 
	public HistoricData getEntry(UUID player){
		return rolelist.get(player);
	}
	public void setEntry(UUID player,HistoricData data) {
		rolelist.put(player, data);
	}
	public HashMap<UUID, HistoricData> getRoleList(){return rolelist;}
	public void draw() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			for(Entry<UUID,HistoricData> entry : rolelist.entrySet()){
				String head = (entry.getValue().getCause()!=null&&entry.getValue().getCause().getDeathCause()!=null)?"§a":ChatColor.STRIKETHROUGH+"§a";
				player.sendMessage(head+entry.getValue().getPlayer().getUsername()+" §d"+entry.getValue().getRole().getCamp().getColor()+" "+entry.getValue().getRole().getName());
			}
		}
	}
	
}
