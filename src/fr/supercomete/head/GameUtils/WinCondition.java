package fr.supercomete.head.GameUtils;

import java.util.ArrayList;
import java.util.UUID;

import fr.supercomete.head.core.Main;

public class WinCondition {
	@SuppressWarnings("unused")
	private static Main main;
	public WinCondition(Main main) {
		WinCondition.main=main;
	}
	public static int getNumberOfAliveTeam() {
		ArrayList<Team> aliveteam=new ArrayList<Team>();
		for(UUID uu:Main.getPlayerlist()) {
			if(!aliveteam.contains(TeamManager.getTeamOfUUID(uu))&&TeamManager.getTeamOfUUID(uu)!=null)aliveteam.add(TeamManager.getTeamOfUUID(uu));
		}
		return aliveteam.size();
	}
}
