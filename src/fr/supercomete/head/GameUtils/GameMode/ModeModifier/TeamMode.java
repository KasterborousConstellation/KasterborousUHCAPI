package fr.supercomete.head.GameUtils.GameMode.ModeModifier;

public interface TeamMode extends GameModeModifier{
	boolean IsTeamActivated();
	void setTeamActivated(boolean bool);
	int getTeamSize();
	void setTeamSize(int size);
	void setNumberofTeam(int number);
	int getNumberofTeam();
}