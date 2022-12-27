package fr.supercomete.enums;
import javax.annotation.Nullable;


import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import fr.supercomete.head.Inventory.InventoryUtils;
public enum BiomeGeneration {
	Roofed("Roofed",Material.LOG_2,(short)1,Biome.ROOFED_FOREST,27),
	Forest("Forêt",Material.APPLE,(short)-1,Biome.FOREST,4),
	Taiga("Taiga",Material.LEAVES,(short)1,Biome.TAIGA,5)
    ;
	private final Material material;
	private final short modification;
	private final Biome biome;
	private final String name;
	private final int biomeint;
	BiomeGeneration(String name, Material material, @Nullable short modification, Biome targetedBiome, int biomeint) {
		this.material=material;
		this.modification=modification;	
		this.biome=targetedBiome;
		this.name=name;
		this.biomeint=biomeint;
	}
	public String getName() {
		return name;
	}
	public ItemStack getItem() {
		if((modification==-1)) {
			return InventoryUtils.getItem(material, null,null);
		}else
		    return InventoryUtils.createColorItem(material, null, 1, modification);
	}
	public Biome getTargetBiome() {
		return biome;
	}
	public int getBiomeInt() {
		return biomeint;
	}
}	