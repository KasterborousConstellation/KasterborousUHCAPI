package fr.supercomete.head.GameUtils.GameMode.ModeModifier;

import fr.supercomete.head.role.RoleGenerator;
import fr.supercomete.head.role.RoleGeneratorHandler;

public interface RolePerTeamMode extends TeamMode,NRGMode{
	void setNumberofTeam(int nteam);
	int getNumberOfTeam();
    default RoleGenerator getRoleGenerator(){
        return RoleGeneratorHandler.StandardGenerator();
    }
}
