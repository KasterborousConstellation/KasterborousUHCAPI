package fr.supercomete.head.role.Key;
import fr.supercomete.head.role.KeyType;
import fr.supercomete.head.role.Bonus.Bonus;
public class TardisKey {
	private KeyType keytype;
	private Bonus bonus;
	public TardisKey(KeyType type,Bonus bonus) {
		this.setKeytype(type);
		this.bonus=bonus;
	}
	
	
	
	public KeyType getKeytype() {
		return keytype;
	}
	public void setKeytype(KeyType keytype) {
		this.keytype = keytype;
	}
	public Bonus getBonus() {
		return bonus;
	}
	public void setBonus(Bonus bonus) {
		this.bonus = bonus;
	}
}