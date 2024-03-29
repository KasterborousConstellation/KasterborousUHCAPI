package fr.supercomete.head.role.Bonus;
public class Bonus {
	private BonusType type;
	private int level;
	public Bonus(BonusType type,int level) {
		this.type = type;
		this.level=level;
	}
    public String getBonusToString() {
        switch (getType()) {
            case Force:
                return "§4Force §c+" + getLevel() + "%";
            case Speed:
                return "§bVitesse §1+" + getLevel() + "%";
            case Heart:
                return "§dCoeurs Bonus §5+" + (double)getLevel()/2.0 + "§d♥";
            case NoFall :
                return "§aNoFall";
            case Damage_Resistance:
                return "§7Résistance §r"+getLevel()+"%";
        }
        return null;
    }
	public BonusType getType() {
		return type;
	}
	public void setType(BonusType type) {
		this.type = type;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}