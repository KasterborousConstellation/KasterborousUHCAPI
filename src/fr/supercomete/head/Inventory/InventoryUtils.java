package fr.supercomete.head.Inventory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import fr.supercomete.head.core.Main;
import fr.supercomete.nbthandler.NbtTagHandler;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
public class InventoryUtils {
	public static ArrayList<Material> AllHastyItem() {
		ArrayList<Material> AllHastyItem = new ArrayList<Material>();
		AllHastyItem.add(Material.DIAMOND_AXE);
		AllHastyItem.add(Material.GOLD_AXE);
		AllHastyItem.add(Material.IRON_AXE);
		AllHastyItem.add(Material.STONE_AXE);
		AllHastyItem.add(Material.WOOD_AXE);
		AllHastyItem.add(Material.DIAMOND_PICKAXE);
		AllHastyItem.add(Material.GOLD_PICKAXE);
		AllHastyItem.add(Material.IRON_PICKAXE);
		AllHastyItem.add(Material.STONE_PICKAXE);
		AllHastyItem.add(Material.WOOD_PICKAXE);
		AllHastyItem.add(Material.DIAMOND_SPADE);
		AllHastyItem.add(Material.GOLD_SPADE);
		AllHastyItem.add(Material.IRON_SPADE);
		AllHastyItem.add(Material.STONE_SPADE);
		AllHastyItem.add(Material.WOOD_SPADE);
		return AllHastyItem;
	}
    public static void dropInventory(Inventory inv, Location loc, World w) {
        for (ItemStack it : inv) {
            if (it == null)
                continue;
            if (it.getType() == Material.AIR|| NbtTagHandler.hasUUIDTAG(it))
                continue;
            w.dropItemNaturally(loc, it);
        }
        if(inv instanceof PlayerInventory){
            PlayerInventory e = (PlayerInventory) inv;
            if(e.getHelmet()!=null){
                w.dropItemNaturally(loc,e.getHelmet());
            }
            if(e.getChestplate()!=null){
                w.dropItemNaturally(loc,e.getChestplate());
            }
            if(e.getLeggings()!=null){
                w.dropItemNaturally(loc,e.getLeggings());
            }
            if(e.getBoots()!=null){
                w.dropItemNaturally(loc,e.getBoots());
            }
        }
    }
	public static ItemStack getItem(Material material, String customName, List<String> CustomLore) {
		ItemStack it = new ItemStack(material, 1);
		ItemMeta itM = it.getItemMeta();
		if (customName != null)
			itM.setDisplayName(customName);
		if (CustomLore != null)
			itM.setLore(CustomLore);
		it.setItemMeta(itM);
		return it;
	}
	public static ItemStack createColorItem(Material item, String name, int amount, short Short) {
		ItemStack is = new ItemStack(item, amount, Short);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}
	public static ItemStack createSkullItem(String value, String name, List<String> customlore) {
		ItemStack is = CustomHeads.create(value);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.setLore(customlore);
		is.setItemMeta(im);
		return is;
	}
	public static boolean testPlayerInvFull(Inventory inv) {
		for (ItemStack it : inv) {
			if (it == null) {
				return false;
			} else if (it.getType() == Material.AIR) {
				return false;
			}
		}
		return true;
	}
	public static void addsafelyitem(Player player, ItemStack it) {
		if(!testPlayerInvFull(player.getInventory())) {
			player.getInventory().addItem(it);
		}else {
			final HashMap<UUID, ArrayList<ItemStack>> stack = Main.currentGame.getFullinv();
			final ArrayList<ItemStack> arr = (stack.containsKey(player.getUniqueId()))?stack.get(player.getUniqueId()):new ArrayList<ItemStack>();
			arr.add(it);
			stack.put(player.getUniqueId(), arr);
			Main.currentGame.setFullinv(stack);
		}
	}
	public static void addOrDrop(Player player, ItemStack it) {
		if (!testPlayerInvFull(player.getInventory())) {
			player.getInventory().addItem(it);
		} else {
			dropAtPlayer(player, it);
		}
	}
	public static void dropAtPlayer(Player player, ItemStack it) {
		player.getWorld().dropItemNaturally(player.getLocation(), it);
	}
	public static TextComponent createTextWithHoverText(String text, String hover) {
		TextComponent message = new TextComponent(text);
		message.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
		return message;
	}
    public static TextComponent createTextWithHoverTextAndExecution(String text, String hover,String command) {
        TextComponent message = new TextComponent(text);
        message.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,command));
        return message;
    }
}