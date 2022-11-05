package fr.supercomete.head.role;
import org.bukkit.ChatColor;
public interface KasterBorousCamp {
    ChatColor getColor();
    String getName();
    default String getColoredName(){
        return getColor()+getName();
    }
    boolean singleplayervictory();
}
