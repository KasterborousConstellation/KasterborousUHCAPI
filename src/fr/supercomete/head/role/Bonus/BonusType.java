package fr.supercomete.head.role.Bonus;
public enum BonusType {
	Force("Force","§c"),
	Speed("Vitesse","§b"),
	NoFall("NoFall","§ba"),
	Heart("Coeurs Bonus","§d"),
    Damage_Resistance("Résistance","§7")
	;
	private final String name;
    private final String color;
	BonusType(String name,String color){
		this.color=color;
        this.name=name;
	}
	public final String getName() {
		return name;
	}
    public final String getColor(){
        return color;
    }
}