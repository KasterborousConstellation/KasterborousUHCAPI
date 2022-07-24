package fr.supercomete.head.role.Bonus;
public class Bonus {
	private BonusType type;
	private int level;
	public Bonus(BonusType type,int level) {
		this.type = type;
		this.level=level;
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