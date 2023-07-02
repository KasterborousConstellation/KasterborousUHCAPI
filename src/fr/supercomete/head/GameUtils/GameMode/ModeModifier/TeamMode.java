package fr.supercomete.head.GameUtils.GameMode.ModeModifier;

import fr.supercomete.head.GameUtils.KTBS_Team;

public interface TeamMode extends GameModeModifier{
    int getNumberOfTeam();
    KTBS_Team createTeam(int team_id);
}
