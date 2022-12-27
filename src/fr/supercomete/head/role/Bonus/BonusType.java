package fr.supercomete.head.role.Bonus;
public enum BonusType {
	Force("Force"),
	Speed("Vitesse"),
	NoFall("NoFall"),
	Heart("Coeurs Bonus")
	;
	private final String name;
	BonusType(String name){
		this.name=name;
	}
	public String getName() {
		return name;
	}
}