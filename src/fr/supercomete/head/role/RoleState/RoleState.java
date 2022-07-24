package fr.supercomete.head.role.RoleState;

public class RoleState {
	private int creationtime;
	private int durationtime;
	private RoleStateTypes roleStateType;
	public RoleState(RoleStateTypes type,int creationtime,int durationtime) {
		this.setRoleStateType(type);this.creationtime=creationtime;this.durationtime=durationtime;
	}
	public long getCreationtime() {
		return creationtime;
	}
	public void setCreationtime(int creationtime) {
		this.creationtime = creationtime;
	}
	public long getDurationtime() {
		return durationtime;
	}
	public void setDurationtime(int durationtime) {
		this.durationtime = durationtime;
	}
	public RoleStateTypes getRoleStateType() {
		return roleStateType;
	}
	public void setRoleStateType(RoleStateTypes roleStateType) {
		this.roleStateType = roleStateType;
	}
	
}
