package fr.supercomete.head.GameUtils.GameMode.ModeModifier;

import fr.supercomete.head.role.RoleGenerator;

public interface NRGMode extends GameModeModifier{
    RoleGenerator getRoleGenerator();
    boolean showCompo();
}