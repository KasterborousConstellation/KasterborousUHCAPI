package fr.supercomete.head.GameUtils;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.ChatColor;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.Modes.UHCClassic;
import fr.supercomete.head.core.Main;

public final class TeamManager {
	@SuppressWarnings("unused")
	private static Main main;
	public TeamManager(Main main) {
		TeamManager.main=main;
	}
	static char[] ListOfChar= {' ','♦','♥','♣'};
	public static void createTeams(int numberofteam) {
		ArrayList<Team> teamlist=Main.currentGame.getTeamList();
		teamlist.clear();
		boolean bol= !(Main.currentGame.getMode() instanceof UHCClassic);
		int total=numberofteam;
		for(int r=0;r<numberofteam/9+1;r++){
			int n=total;
			if(n>=0)n++;
			if(n>=3)n++;
			if(n>=6)n++;
			if(n>=7)n++;
			if(n>=8)n++;
			if(n>=13)n++;
			if(n>=15)n++;
			if(n>16)n=16;
		for(int i=0;i<n;i++) {
			if(i==6||i==8||i==15||i==0||i==3||i==13||i==7)continue;
			Team t= new Team(ListOfChar[r]+getNameOfShortColor((short)i), new ArrayList<UUID>(), "", "", (short)i, ListOfChar[r], Main.currentGame.getNumberOfPlayerPerTeam(),bol);
			teamlist.add(t);
		}
		total-= Math.min(n, 9);
		}
	}
	public static void setupTeams(){
		if(!Main.currentGame.IsTeamActivated()){
			Main.currentGame.getTeamList().clear();
		}else {
			createTeams(Main.currentGame.getTeamNumber());
			Main.currentGame.setMaxNumberOfplayer(Main.currentGame.getTeamNumber()*Main.currentGame.getNumberOfPlayerPerTeam());
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
            default:
			return ChatColor.WHITE;
		}
		
		
	}
	public static short getShortOfChatColor(ChatColor c) {
		switch (c) {
		case WHITE:
			return 0;
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
			return"Error";
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
		case 12:
			return"Error";
		case 13:
			return"Verte";
		case 14:
			return"Rouge";
		case 15:
			return"Error";
		default:
			return"Out of bound";
		}
	}
	public static Team getTeamOfUUID(UUID player) {
		if(!Main.currentGame.IsTeamActivated())return null;
		for(Team t:Main.currentGame.getTeamList()){
			if(t.isMemberInTeam(player)) {
				return t;
			}
		}
		return null;
	}
	public static void CompletingTeam(UUID player){
		if(!Main.currentGame.IsTeamActivated())return;
		if(TeamManager.getTeamOfUUID(player)!=null)return;
		for(Team t:Main.currentGame.getTeamList()){
			t.addMembers(player);
			if(t.isMemberInTeam(player))break;
		}
		return;	
	}
}