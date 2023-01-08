package fr.supercomete.head.GameUtils.GameMode.ModeModifier;

import fr.supercomete.head.GameUtils.GameConfigurable.Bound;

public interface TeamMode extends GameModeModifier{
	int getTeamSize();
	void setTeamSize(int size);
	void setNumberofTeam(int number);
	int getNumberofTeam();
    boolean canBeChanged();
    Bound TeamSizeBound();
}