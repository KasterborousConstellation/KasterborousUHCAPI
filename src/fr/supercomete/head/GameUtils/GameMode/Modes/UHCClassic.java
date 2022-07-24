
package fr.supercomete.head.GameUtils.GameMode.Modes;

import java.util.Arrays;
import java.util.UUID;

import fr.supercomete.head.Inventory.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.supercomete.head.GameUtils.Team;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.GameUtils.WinCondition;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.TeamMode;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.world.scoreboardmanager;

public class UHCClassic extends Mode implements TeamMode{
	private boolean IsTeamActivated=false;
	private int TeamSize=3;
	private int NumberOfTeam=4;
	public UHCClassic() {
		super("UHC Classique",Material.GOLDEN_APPLE,Arrays.asList("§rUHC Classique"));
	}
	@Override
	public boolean IsTeamActivated() {
		return IsTeamActivated;
	}
	@Override
	public void setTeamActivated(boolean bool) {
		this.IsTeamActivated=bool;
	}
	@Override
	public int getTeamSize() {
		return TeamSize;
	}
	@Override
	public void setTeamSize(int size) {
		TeamSize=size;
	}
	@Override
	public void setNumberofTeam(int number) {
		NumberOfTeam=number;
	}
	@Override
	public int getNumberofTeam() {
		return NumberOfTeam;
	}
	@Override
	public void DecoKillMethod(Offline_Player player) {
        InventoryUtils.dropInventory(player.getInventory(), player.getLocation(), player.getLocation().getWorld());
		String Team = "";
		if (Main.currentGame.IsTeamActivated() && TeamManager.getTeamOfUUID(player.getPlayer()) != null) {
			if (!TeamManager.getTeamOfUUID(player.getPlayer()).isAnonymousteam()) {
				Team t = TeamManager.getTeamOfUUID(player.getPlayer());
				Team = TeamManager.getColorOfShortColor(t.getColor()).toString() + t.getChar() + " ";
			}
		}
		Bukkit.broadcastMessage(Main.UHCTypo + Team + player.getUsername() + "§r est mort.");
		Main.playerlist.remove(player.getPlayer());
	}
	@Override
	public void OnKillMethod(Location deathLocation, Player player, Player damager) {
//		GoldenHeadImplement(player, damager);
//		KillSwitchImplement(player, damager);
        InventoryUtils.dropInventory(player.getInventory(), deathLocation, player.getWorld());
		String Team = "";
		if (Main.currentGame.IsTeamActivated() && TeamManager.getTeamOfUUID(player.getUniqueId()) != null) {
			if (!TeamManager.getTeamOfUUID(player.getUniqueId()).isAnonymousteam()) {
				Team t = TeamManager.getTeamOfUUID(player.getUniqueId());
				Team = TeamManager.getColorOfShortColor(t.getColor()).toString() + t.getChar() + " ";
			}
		}
		if (damager == null) {
			Bukkit.broadcastMessage(Main.UHCTypo + Team + player.getName() + "§r est mort.");
		} else {
			String DamagerTeam = "";
			if (Main.currentGame.IsTeamActivated()
					&& TeamManager.getTeamOfUUID(damager.getUniqueId()) != null) {
				if (!TeamManager.getTeamOfUUID(damager.getUniqueId()).isAnonymousteam()) {
					Team t = TeamManager.getTeamOfUUID(damager.getUniqueId());
					DamagerTeam = TeamManager.getColorOfShortColor(t.getColor()).toString() + t.getChar()
							+ " ";
				}
			}
			Bukkit.broadcastMessage(Main.UHCTypo + Team + player.getName() + "§r a été tué par§r "
					+ DamagerTeam + damager.getName() + ".");
		}
		Mode.GoldenHeadImplement(player, damager);
		Mode.KillSwitchImplement(player, damager);
		player.setGameMode(GameMode.SPECTATOR);
		player.getInventory().clear();
		Main.playerlist.remove(player.getUniqueId());
		
	}
	@Override
	public void onAnyTime(Player player) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDayTime(Player player) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNightTime(Player player) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onEndingTime(Player player) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onRoleTime(Player player) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void OnStart(Player player) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean WinCondition() {
		if(Main.currentGame.IsTeamActivated()) {
			if(WinCondition.getNumberOfAliveTeam()==1) {
				Team winner= TeamManager.getTeamOfUUID(Main.getPlayerlist().get(0));
				scoreboardmanager.titlemessage("Victoire de l'équipe "+TeamManager.getColorOfShortColor(winner.getColor())+winner.getChar()+TeamManager.getNameOfShortColor(winner.getColor()));
				return true;
			}
		}else if(Main.getPlayerlist().size()==1){
			UUID winner=Main.getPlayerlist().get(0);
			scoreboardmanager.titlemessage("Victoire de "+Bukkit.getPlayer(winner).getName());
			return true;
		}
		return false;
	}
	@Override
	public void onEpisodeTime(Player player) {
		// TODO Auto-generated method stub
		
	}
	
}
