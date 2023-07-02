package fr.supercomete.head.GameUtils;
import java.util.ArrayList;
import java.util.UUID;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.TeamMode;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import fr.supercomete.head.core.Main;
import org.bukkit.scoreboard.Team;

public final class TeamManager {
	@SuppressWarnings("unused")

    public static ArrayList<KTBS_Team> teamlist = new ArrayList<>();


	public static void setupTeams(){
        teamlist.clear();
        KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
        Mode mode = api.getGameProvider().getCurrentGame().getMode();
        if(mode instanceof TeamMode){
            TeamMode teamMode = (TeamMode) mode;
            for(int i =0;i<teamMode.getNumberOfTeam();i++){
                teamlist.add(teamMode.createTeam(i));
            }
        }
	}
	public static ChatColor getColorOfShortColor(short sh) {
		switch (15-sh) {
            case 1:
			return ChatColor.GOLD;
		case 2:
            case 6:
                return ChatColor.LIGHT_PURPLE;
		case 3:
			return ChatColor.AQUA;
		case 4:
			return ChatColor.YELLOW;
		case 5:
			return ChatColor.GREEN;
            case 7:
            case 12:
                return ChatColor.DARK_GRAY;
		case 8:
			return ChatColor.GRAY;
		case 9:
			return ChatColor.DARK_AQUA;
		case 10:
			return ChatColor.DARK_PURPLE;
		case 11:
			return ChatColor.BLUE;
            case 13:
			return ChatColor.DARK_GREEN;
		case 14:
			return ChatColor.RED ;
        case 15:
            return ChatColor.BLACK;
            default:
			return ChatColor.WHITE;
		}
		
		
	}
	public static short getShortOfChatColor(ChatColor c) {
		switch (c) {
		case GOLD:
			return 1;
		case LIGHT_PURPLE:
			return 2;
		case AQUA:
			return 3;
		case YELLOW:
			return 4;
		case GREEN:
			return 5;
		case DARK_GRAY:
			return 7;
		case GRAY:
			return 8;
		case DARK_AQUA:
			return 9;
		case DARK_PURPLE:
			return 10;
		case BLUE:
			return 11;
		case DARK_GREEN:
			return 13;
		case RED:
			return 14 ;
		default:
			return 0;
		}
	}
	public static String getNameOfShortColor(short sh) {
		switch (15-sh) {
		case 0:
			return"Blanche";
		case 1:
			return"Orange";
		case 2:
            return "Magenta";
        case 15:
            return "Noire";
        case 12:
            return"Marron";
		case 3:
			return"Bleue claire";
		case 4:
			return"Jaune";
		case 5:
			return"Verte claire";
		case 6:
			return"Rose";
		case 7:
			return"Grise";
		case 8:
			return"Grise claire";
		case 9:
			return"Cyan";
		case 10:
			return"Violette";
		case 11:
			return"Bleue";
        case 13:
			return"Verte";
		case 14:
			return"Rouge";
        default:
            return"Out of bound";
		}
	}
	public static KTBS_Team getTeamOfUUID(UUID player) {
		for(KTBS_Team t:teamlist){
			if(t.isMemberInTeam(player)) {
				return t;
			}
		}
		return null;
	}
	public static void CompletingTeam(UUID player){
		if(TeamManager.getTeamOfUUID(player)!=null)return;
		for(KTBS_Team t:teamlist){
			t.addMembers(player);
			if(t.isMemberInTeam(player))break;
		}
	}
    public static int getNumberOfAliveTeam() {
        ArrayList<KTBS_Team> aliveteam=new ArrayList<KTBS_Team>();
        for(UUID uu:Main.getPlayerlist()) {
            if(!aliveteam.contains(TeamManager.getTeamOfUUID(uu))&&TeamManager.getTeamOfUUID(uu)!=null)aliveteam.add(TeamManager.getTeamOfUUID(uu));
        }
        return aliveteam.size();
    }
}