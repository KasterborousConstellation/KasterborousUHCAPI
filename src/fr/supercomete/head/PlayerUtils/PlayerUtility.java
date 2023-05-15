package fr.supercomete.head.PlayerUtils;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.TeamMode;
import fr.supercomete.head.Inventory.InventoryManager;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.nbthandler.NbtTagHandler;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerUtility{
	private static Main main;
	public PlayerUtility(Main main) {
		PlayerUtility.main = main;
	}
	public static void giveStuff(ArrayList<UUID> playerlist){
        final Inventory inv=getInventory();
        final ItemStack[]it=inv.getContents();
        for(final UUID uu: playerlist){
            final Player pl = Bukkit.getPlayer(uu);
            for(int i=0;i<Math.min(it.length,36);i++){
                try{
                    pl.getInventory().setItem(i, it[i]);
                }catch (ArrayIndexOutOfBoundsException e){
                    Bukkit.getLogger().log(Level.WARNING,i+" AAAAAAA");
                }
            }
            new BukkitRunnable(){
                @Override
                public void run() {
                    pl.updateInventory();
                }
            }.runTaskLater(Main.INSTANCE,0L);
        }
	}
	public static String getNameByUUID(UUID target){
        String ally="";
	    if(target==null) {
            ally = "§6Aucun";
        }else {
            if(Bukkit.getPlayer(target)!=null) {
                ally="§6"+Bukkit.getPlayer(target).getName();
            }else {
                if(Main.currentGame.hasOfflinePlayer(target))
                    ally ="§6"+Main.currentGame.getOffline_Player(target).getUsername();
            }
        }
	    return ally;
    }
    public static Inventory getInventory() {
        Inventory selected = InventoryManager.getSelectedInventory();
        if(selected==null){
            return Bukkit.createInventory(null,9);
        }else{
            return selected;
        }
    }
	public static void GiveHotBarStuff(Player player) {
		if(player==null)return;
		if(Main.IsHost(player)) {
			ItemStack it = InventoryUtils.createSkullItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjkyMzUwYzlmMDk5M2VkNTRkYjJjNzExMzkzNjMyNTY4M2ZmYzIwMTA0YTliNjIyYWE0NTdkMzdlNzA4ZDkzMSJ9fX0=",ChatColor.DARK_PURPLE + "»" + ChatColor.LIGHT_PURPLE + " Menu " + ChatColor.DARK_PURPLE + "«",null);
			SkullMeta im = (SkullMeta) it.getItemMeta();
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			it.setItemMeta(im);
			player.getInventory().setItem(4, it);
			it = InventoryUtils.getItem(Material.SLIME_BALL, "§dTp Salle des rêgles", null);
			it = NbtTagHandler.addAnyTag(it, "RoomTp", 1);
			player.getInventory().setItem(1, it);
		}
        if(Main.currentGame.getMode()instanceof TeamMode){
            ItemStack it = InventoryUtils.createColorItem(Material.BANNER, "§bTeam", 1, (short) 0);
            ItemMeta im = it.getItemMeta();
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            it.setItemMeta(im);
            player.getInventory().setItem(0, it);
        }else{
            player.getInventory().setItem(0,null);
        }
	}
	public static Player getTarget(Player player,int range){
		Player target;
		List<Entity> nearbyE = player.getNearbyEntities(range, range, range);
		ArrayList<Player> Players = new ArrayList<>();
		for (Entity e : nearbyE) {
			if (e instanceof Player) {
                Players.add((Player) e);
			}
		}
		target=null;
		BlockIterator bItr = new BlockIterator(player, range);
		Block block;
		Location loc;
		int bx, by, bz;
		double ex, ey, ez;
		// Regarde a travers la ligne de vision du joueur
		while (bItr.hasNext()) {
			block = bItr.next();
			bx = block.getX();
			by = block.getY();
			bz = block.getZ();
			// Verifie les joueurs près du bloc de la ligne de vue
			for (Player e : Players) {
				loc = e.getLocation();
				ex = loc.getX();
				ey = loc.getY();
				ez = loc.getZ();
				if ((bx - .75 <= ex && ex <= bx + 1.75) && (bz - .75 <= ez && ez <= bz + 1.75)&& (by - 1 <= ey && ey <= by + 2.5)) {
					// Si le joueurs est assez proche alors on initialise la variable target et on sort de la boucle
					target=e;
					break;
				}
			}
		}
		return target;
	}

    public static void sendActionbar(Player p, String message) {
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(bar);
    }
}