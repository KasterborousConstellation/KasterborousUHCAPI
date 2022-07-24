package fr.supercomete.head.GameUtils.GameConfigurable;

import org.bukkit.Material;

public enum BindingType {
	Object("object","Objets",Material.STICK),
	Block("block","Blocs",Material.COBBLESTONE),
	Player("player","Joueurs",Material.SKULL_ITEM),
	Effect("effect","Effets",Material.IRON_SWORD),
	World("world","Monde",Material.GRASS),
	Chat("chat","Chat",Material.SIGN)
	;
	private final String type;
	private final String name;
	private final Material material;
	BindingType(String type, String name, Material material) {
		this.type=type;
		this.name=name;
		this.material=material;
	}
	public Material getMaterial() {
		return material;
	}
	public String getType() {
		return type;
	}
	public String getName() {
		return name;
	}
}
