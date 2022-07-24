package fr.supercomete.head.GameUtils.Events.PlayerEvents;

import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.world.worldgenerator;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerEvent {
    private final Offline_Player player;
    private final Location location;
    private final String name;
    private final int time;
    public PlayerEvent(String name, Offline_Player player , Location location){
        this.location=location;
        this.name=name;
        this.player=player;
        this.time = Main.currentGame.getTime();
        autoSend();
    }
    public TextComponent getMessage(){
        return InventoryUtils.createTextWithHoverTextAndExecution("§c"+"⚠" +"§a"+ player.getUsername()+":§b "+name,location.getX()+" "+location.getY()+" "+location.getZ(),"/tpin "+location.getWorld().getName()+" "+location.getX()+" "+location.getY()+" "+location.getZ());
    }
    public void autoSend(){
        for(UUID uu : Main.bypass){
            if(Bukkit.getPlayer(uu)!=null && Bukkit.getPlayer(uu).isOnline()){
                Bukkit.getPlayer(uu).spigot().sendMessage(getMessage());
            }
        }
    }
    public ItemStack getItem(){
        final ItemStack stack = InventoryUtils.createColorItem(Material.WOOL,"§r"+name,1,(short)3);
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(List.of("§r"+player.getUsername()+" "+"("+ TimeUtility.transform(time,"§6")+"§r)"));
        stack.setItemMeta(meta);
        return stack;
    }
}
