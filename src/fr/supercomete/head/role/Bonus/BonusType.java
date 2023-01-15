package fr.supercomete.head.role.Bonus;
public enum BonusType {
	Force("Force","§c"),
	Speed("Vitesse","§b"),
	NoFall("NoFall","§ba"),
	Heart("Coeurs Bonus","§d")
	;
	private final String name;
    private final String color;
	BonusType(String name,String color){
		this.color=color;
        this.name=name;
	}
	public String getName() {
		return name;
	}
    public String getColor(){
        return color;
    }
}