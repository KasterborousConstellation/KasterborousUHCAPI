
package fr.supercomete.head.GameUtils.GameMode.Modes;

import java.util.Arrays;

import fr.supercomete.head.GameUtils.GameConfigurable.Bound;
import fr.supercomete.head.Inventory.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.supercomete.head.GameUtils.KTBS_Team;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.TeamMode;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.world.scoreboardmanager;

public class UHCClassic extends Mode implements TeamMode{

	private int TeamSize=3;
	private int NumberOfTeam=4;
	public UHCClassic() {
		super("UHC Classique",Material.GOLDEN_APPLE,Arrays.asList("§rUHC Classique"));
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
    public boolean canBeChanged() {
        return true;
    }

    @Override
    public Bound TeamSizeBound() {
        return new Bound(1,10);
    }

    @Override
	public void DecoKillMethod(Offline_Player player) {
		String Team = "";
		if (TeamManager.getTeamOfUUID(player.getPlayer()) != null) {
			if (!TeamManager.getTeamOfUUID(player.getPlayer()).isAnonymousteam()) {
				KTBS_Team t = TeamManager.getTeamOfUUID(player.getPlayer());
				Team = TeamManager.getColorOfShortColor(t.getColor()).toString() + t.getChar() + " ";
			}
		}
		Bukkit.broadcastMessage(Main.UHCTypo + Team + player.getUsername() + "§r est mort.");
	}
	@Override
	public void OnKillMethod(Location deathLocation, Player player, Player damager) {
        InventoryUtils.dropInventory(player.getInventory(), deathLocation, player.getWorld());
		String Team = "";
		if (TeamManager.getTeamOfUUID(player.getUniqueId()) != null) {
			if (!TeamManager.getTeamOfUUID(player.getUniqueId()).isAnonymousteam()) {
				KTBS_Team t = TeamManager.getTeamOfUUID(player.getUniqueId());
				Team = TeamManager.getColorOfShortColor(t.getColor()).toString() + t.getChar() + " ";
			}
		}
		if (damager == null) {
			Bukkit.broadcastMessage(Main.UHCTypo + Team + player.getName() + "§r est mort.");
		} else {
			String DamagerTeam = "";
			if (TeamManager.getTeamOfUUID(damager.getUniqueId()) != null) {
				if (!TeamManager.getTeamOfUUID(damager.getUniqueId()).isAnonymousteam()) {
					KTBS_Team t = TeamManager.getTeamOfUUID(damager.getUniqueId());
					DamagerTeam = TeamManager.getColorOfShortColor(t.getColor()).toString() + t.getChar()
							+ " ";
				}
			}
			Bukkit.broadcastMessage(Main.UHCTypo + Team + player.getName() + "§r a été tué par§r "
					+ DamagerTeam + damager.getName() + ".");
		}
	}
	@Override
	public void onAnyTime(Player player) {

	}

    @Override
    public void onGlobalAnytime(int time) {

    }

    @Override
	public void onDayTime(Player player) {

	}
	@Override
	public void onNightTime(Player player) {

	}
	@Override
	public void onEndingTime(Player player) {

	}
	@Override
	public void onRoleTime(Player player) {

	}
	@Override
	public void OnStart(Player player) {

	}
	@Override
	public boolean WinCondition() {
        if(TeamManager.getNumberOfAliveTeam()==1) {
            KTBS_Team winner= TeamManager.getTeamOfUUID(Main.getPlayerlist().get(0));
            scoreboardmanager.titlemessage("Victoire de l'équipe "+TeamManager.getColorOfShortColor(winner.getColor())+winner.getChar()+TeamManager.getNameOfShortColor(winner.getColor()));
				return true;
        }
		return false;
	}
	@Override
	public void onEpisodeTime(Player player) {
		// TODO Auto-generated method stub
		
	}
	
}
