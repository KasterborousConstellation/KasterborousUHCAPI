package fr.supercomete.head.structure;

import org.bukkit.Material;

public class CustomBlock {
	private final Material material;
	private final byte data;
	public CustomBlock(Material material,byte data) {
		this.data=data;
		this.material=material;
	}
	public Material getMaterial() {
		return material;
	}
	public byte getData() {
		return data;
	}

}
