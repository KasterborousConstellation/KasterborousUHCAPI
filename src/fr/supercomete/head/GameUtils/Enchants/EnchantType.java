package fr.supercomete.head.GameUtils.Enchants;
import org.bukkit.Material;
public enum EnchantType {
    Iron(Material.IRON_INGOT,"Fer"),
    Diamond(Material.DIAMOND,"Diamant"),
    Bow(Material.BOW,"Arc"),
    Rod(Material.FISHING_ROD,"Canne à pêche"),
    ALL(Material.BARRIER,"Général")
    ;
    private final Material material;
    private final String name;
    EnchantType(Material material,String name){
        this.material=material;
        this.name=name;
    }
    public Material getMaterial(){
        return material;
    }
    public String getName(){
        return name;
    }
}
