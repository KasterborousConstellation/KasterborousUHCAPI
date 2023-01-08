package fr.supercomete.head.API;

import fr.supercomete.head.Exception.UnableToProvideException;
import fr.supercomete.head.GameUtils.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public interface TeamProvider {
    Team getTeamOf(Player player);
    Team getTeamOf(UUID uuid);
    ChatColor convertShortToColor(short color);
    ArrayList<Team>getTeams();
    void resetTeams() throws UnableToProvideException; //Impossible en partie
    void setNumberOfMemberPerTeam(int number);
    void setTeamNumber(int number);
    int getTeamNumber();
    int getNumberOfMemberPerTeam();
}