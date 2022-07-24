package fr.supercomete.head.role;

import java.util.UUID;

public abstract class NakimeCastleRole extends Role {
	private NakimeRoleType roletype;
	public NakimeCastleRole(UUID owner,NakimeRoleType nakimeroletype) {
		super(owner);
		 this.setRoletype(nakimeroletype);
	}
	public NakimeRoleType getRoletype() {
		return roletype;
	}
	public void setRoletype(NakimeRoleType roletype) {
		this.roletype = roletype;
	}	
}
