package fr.supercomete.head.API;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface CoordinateHelper {
    double Distance(Player player1,Player player2);
    String getDirectionArrow(Location location, Location towardto);
}