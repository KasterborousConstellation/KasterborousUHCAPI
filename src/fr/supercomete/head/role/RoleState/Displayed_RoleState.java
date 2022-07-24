package fr.supercomete.head.role.RoleState;

public class Displayed_RoleState extends PermanentRoleSate {
	private String display;
	public Displayed_RoleState(RoleStateTypes type,String display) {
		super(type);
		this.setDisplay(display);
	}
	public String getDisplay() {
		String str = "";
		str = display;
		if(this instanceof StolenRoleState)
		str= str.replace("#From", ((StolenRoleState)this).getFrom().getName());
		return str;
	}
	public void setDisplay(String display) {
		this.display = display;
	}

}
