package fr.supercomete.head.role.RoleState;

import fr.supercomete.head.core.Main;

public class PermanentRoleSate extends RoleState {

	public PermanentRoleSate(RoleStateTypes type) {
		super(type, Main.currentGame.getTime(), -1);
	}

	

}
