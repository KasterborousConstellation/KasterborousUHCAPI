package fr.supercomete.head.GameUtils.GameMode.ModeModifier;

import fr.supercomete.head.role.RoleGenerator;
import fr.supercomete.head.role.RoleGeneratorHandler;


public interface CampMode extends NRGMode{
    default RoleGenerator getRoleGenerator(){
        return RoleGeneratorHandler.StandardGenerator();
    }
}