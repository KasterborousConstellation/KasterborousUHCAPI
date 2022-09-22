package fr.supercomete.head.role.Key;
import java.util.*;

import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEventHandler;
import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEvents;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.world.worldgenerator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.content.DWUHC.Rusty;
import fr.supercomete.head.structure.Structure;
public class Tardis {
	private final int[][] locations = new int[][] {{25,23,13},{9,13,19},{18,22,32},{25,13,27}};
	private ArrayList<TardisKey> keys;
	public HashMap<UUID, Integer> timespanlist = new HashMap<>();
	private int percentDestruction;
	private static long tick;
	public Tardis() {
		setKeys(new ArrayList<>());
		setPercentDestruction(0);
	}
	public ArrayList<TardisKey> getKeys() {
		return keys;
	}
	public void setKeys(ArrayList<TardisKey> keys) {
		this.keys = keys;
	}
	public int getPercentDestruction() {
		return percentDestruction;
	}
	public void setPercentDestruction(int percentDestruction) {
		this.percentDestruction = percentDestruction;
	}
	public void addKey(TardisKey key) {
		keys.add(key);
	}
	public void takeKey(TardisKey key) {
		keys.remove(key);
	}
	public void removeKey(TardisKey key) {
		keys.remove(key);
	}
	public int getAmountOfKey() {
		return keys.size();
	}
	private void tick() {
		tick++;
	}
	public void teleport(Player player) {
		Structure struc = Main.currentGame.getMode().getStructure().get(0);
		player.teleport(struc.getPositionRelativeToLocation(this.locations[new Random().nextInt(locations.length-1)]));
	}
	private boolean IsBad(ArrayList<Player>near) {
		if(RoleHandler.IsRoleGenerated()) {
			for(Player player:near) {
				if(RoleHandler.getRoleOf(player).getCamp()==Camps.EnnemiDoctorCamp) {
					return true;
				}
			}
			return false;
		}else return false;
	}
	private void updateDestruction(ArrayList<Player>near) {
		if(percentDestruction>=100) {
			percentDestruction=0;
			if(keys.size()>0) {
				keys.remove(0);
			}
			Bukkit.broadcastMessage(Main.UHCTypo+"§cUne clef du Tardis a été détruite !");
		}
		if(RoleHandler.IsRoleGenerated()) {
			for(Player player:near) {
				if(RoleHandler.getRoleOf(player).getCamp()==Camps.EnnemiDoctorCamp || RoleHandler.getRoleOf(player)instanceof Rusty) {
                    PlayerUtility.sendActionbar(player, "§4Destruction d'une clef: §c"+percentDestruction+"%");
				}
			}
		}
	}
	public void update(ArrayList<Player>near){
		tick();
		if(IsBad(near)&&keys.size()>0) {
			if(tick%2==0) {
				percentDestruction++;
			}
		}else {
			if(tick%10==0) {
				if(percentDestruction>0) {
					percentDestruction--;
				}
			}
		}
		updateDestruction(near);
	}
    public void updateinside(){
	    for(final Player player : Bukkit.getOnlinePlayers()){
	        if(!timespanlist.containsKey(player.getUniqueId()))
                timespanlist.put(player.getUniqueId(),0);
	    }
	    for(Map.Entry<UUID,Integer> entry: timespanlist.entrySet()){
	        if(Bukkit.getPlayer(entry.getKey())!=null && Bukkit.getPlayer(entry.getKey()).isOnline()&&RoleHandler.getRoleList().containsKey(entry.getKey())){
	            Player player = Bukkit.getPlayer(entry.getKey());
	            if(player.getWorld()== worldgenerator.structureworld){
	                if(player.getLocation().distance(Main.currentGame.getMode().getStructure().get(0).getLocation())<100){
                        entry.setValue(entry.getValue()+1);
                        PlayerUtility.sendActionbar(player,"§rTemps avant ejection automatique: "+ TimeUtility.transform((Main.currentGame.getDataFrom(Configurable.LIST.TardisEjectionTime)-entry.getValue()),"§4","§4","§4"));
                        if(entry.getValue()> Main.currentGame.getDataFrom(Configurable.LIST.TardisEjectionTime)){
                            PlayerUtility.PlayerRandomTPMap(player);
                            player.sendMessage(Main.UHCTypo+"§cVous avez été éjecté du Tardis car vous êtes rester trop longtemps");
                            this.timespanlist.put(entry.getKey(),0);
                            PlayerEventHandler.Event(PlayerEvents.TardisEjection,player,player.getLocation());
                        }
                    }
                }

            }
        }
    }
}