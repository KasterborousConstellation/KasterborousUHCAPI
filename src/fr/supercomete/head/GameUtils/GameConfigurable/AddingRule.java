package fr.supercomete.head.GameUtils.GameConfigurable;

public class AddingRule {
	public static AddingRule BooleanAddingRule = new AddingRule(1);
	private boolean mass;
	private int add,massadd;
	public AddingRule(int add) {
		this.add = add;
		this.mass=false;
	}
	public AddingRule(int add,int massadd) {
		this.add=add;
		this.massadd=massadd;
		this.mass=true;
	}
	public boolean isMass() {
		return mass;
	}
	public void setMass(boolean mass) {
		this.mass = mass;
	}
	public int getAdd() {
		return add;
	}
	public void setAdd(int add) {
		this.add = add;
	}
	public int getMassadd() {
		return massadd;
	}
	public void setMassadd(int massadd) {
		this.massadd = massadd;
	}
	

}
