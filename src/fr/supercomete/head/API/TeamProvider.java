package fr.supercomete.head.API;


import fr.supercomete.head.GameUtils.KTBS_Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public interface TeamProvider {
    KTBS_Team getTeamOf(Player player);
    KTBS_Team getTeamOf(UUID uuid);
    ChatColor convertShortToColor(short color);
    ArrayList<KTBS_Team>getTeams();
    void resetTeams(); //Impossible en partie
}