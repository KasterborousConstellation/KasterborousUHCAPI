package fr.supercomete.enums;
import org.bukkit.ChatColor;
public enum Camps {
	DoctorCamp("Camp du Docteur",ChatColor.AQUA),
	EnnemiDoctorCamp("Ennemis du Docteur",ChatColor.RED),
	DuoKarvanista("Duo Karvanista",ChatColor.GOLD),
	Neutral("Neutre",ChatColor.WHITE),
    Division("La Division", ChatColor.DARK_PURPLE),
	CloverHeart("Alliance Clover & Heart",ChatColor.BLUE),
	Spade("Spade",ChatColor.RED),
	SoloBlackCloverUHC("Solo",ChatColor.WHITE)
	;
	private final String Name;
	private final ChatColor color;
	Camps(String name,ChatColor color){
		this.color=color;
		this.Name=name;
	}
	public ChatColor getColor() {
		return color;
	}
	public String getName() {
		return Name;
	}
	public String getColoredName() {
		return color+Name;
	}
}