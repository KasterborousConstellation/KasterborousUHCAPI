package fr.supercomete.head.Inventory.inventoryapi.content;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
public class ItemStackUtility {
    public static ItemStack createColoredItem(Material material, int amount, short data, String name, List<String>description, ItemFlag... flags){
        ItemStack stacks = new ItemStack(material);
        ItemMeta meta = stacks.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(description);
        for(final ItemFlag f : flags){
            meta.addItemFlags(f);
        }
        stacks.setDurability(data);
        stacks.setAmount(amount);
        stacks.setItemMeta(meta);
        return stacks;
    }
    public static ItemStack createColoredItem(Material material,short data,String name,List<String> description){
        return createColoredItem(material,1,data,name,description);
    }
    public static ItemStack createItem(Material material,int amount,String name,List<String> description, ItemFlag... flags){
        return createColoredItem(material,amount, (short) 0,name,description,flags);
    }
    public static ItemStack createItem(Material material,int amount,String name,List<String> description){
        return createColoredItem(material,amount, (short) 0,name,description);
    }
    public static ItemStack createItem(Material material,String name,List<String> description){
        return createColoredItem(material,1, (short) 0,name,description);
    }
}