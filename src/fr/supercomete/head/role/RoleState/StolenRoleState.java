package fr.supercomete.head.role.RoleState;

import fr.supercomete.head.role.Role;

public class StolenRoleState extends Displayed_RoleState {
	private Role from;
	public StolenRoleState(RoleStateTypes type,String display,Role from) {
		super(type, display);
		this.setFrom(from);
	}
	public Role getFrom() {
		return from;
	}
	public void setFrom(Role from) {
		this.from = from;
	}
}
