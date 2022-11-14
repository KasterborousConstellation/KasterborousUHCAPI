package fr.supercomete.head.API;

import fr.supercomete.head.GameUtils.Fights.Fight;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public interface FightProvider {
    boolean isInCombat(Player player);
    Player getLastFightStartedWith(Player player);
    boolean isInCombatWith(Player player1,Player player2);
    ArrayList<Fight> getFightOf(Player player);
}
