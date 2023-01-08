package fr.supercomete.head.GameUtils;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
public class Team {
    private UUID team_id;
	private ArrayList<UUID> members;
	private String Prefix,Suffix,TeamName;
	private short color;
	private char Char;
	private int MaxPlayerAmount;
	private boolean anonymousteam;
	public Team(String TeamName,ArrayList<UUID> members,String Prefix,String Suffix,short color,char c,int MaxPlayerAmount,boolean anonymousteam) {
		this.TeamName=TeamName;
		this.members=members;
		this.Prefix=Prefix;
		this.Suffix=Suffix;
		this.setColor(color);
		this.Char=c;
		this.setMaxPlayerAmount(MaxPlayerAmount);
		this.anonymousteam=anonymousteam;
        this.team_id = UUID.randomUUID();
	}
	public String getTeamName() {
		return TeamName;
	}
	public void setTeamName(String teamName) {
		TeamName = teamName;
	}
	public ArrayList<UUID> getMembers() {
		return members;
	}
	public void setMembers(ArrayList<UUID> members) {
		this.members = members;
	}
	public void addMembers(UUID member) {
		if(this.members.contains(member)) {
			System.out.println("Error "+member+" is already in the Team");
		}
		if(members.size()+1>this.MaxPlayerAmount)return;
		this.members.add(member);
	}
    public UUID getTeam_id(){
        return team_id;
    }
	public void removeMember(UUID member) {
		this.members.remove(member);
	}
	public int getMemberIndex(UUID member) {
		return this.members.indexOf(member);
	}
	public String getPrefix() {
		return Prefix;
	}
	public void setPrefix(String prefix) {
		Prefix = prefix;
	}
	public String getSuffix() {
		return Suffix;
	}
	public void setSuffix(String suffix) {
		Suffix = suffix;
	}
	public boolean isMemberInTeam(UUID member) {
		return members.contains(member);
	}
	public short getColor() {
		return color;
	}
	public void setColor(short color) {
		this.color = color;
	}
	public char getChar() {
		return Char;
	}
	public void setChar(char c) {
		Char=c;
	}
	public int getMaxPlayerAmount() {
		return MaxPlayerAmount;
	}
	public void setMaxPlayerAmount(int maxPlayerAmount) {
		MaxPlayerAmount = maxPlayerAmount;
	}
	public ArrayList<String> getTeamItemLore(){
		ArrayList<String> lore=new ArrayList<String>();
		for(UUID uu:this.getMembers()) {
			String name =Bukkit.getPlayer(uu).getName();
			lore.add(TeamManager.getColorOfShortColor(getColor())+""+getChar()+name);
		}
		int l=lore.size();
		for(int i=0;i<this.MaxPlayerAmount-l;i++){
			lore.add("§7•");
		}
		lore.add(0,"§7Membres:");
		return lore;
	}
	public boolean isAnonymousteam() {
		return anonymousteam;
	}
	public void setAnonymousteam(boolean anonymousteam) {
		this.anonymousteam = anonymousteam;
	}
}