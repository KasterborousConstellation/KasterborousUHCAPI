package fr.supercomete.head.role.Triggers;

import fr.supercomete.head.world.ScoreBoard.SimpleScoreboard;
import org.bukkit.entity.Player;

public interface Trigger_OnScoreBoardUpdate {
    void onScoreBoardUpdate(final Player player, SimpleScoreboard scoreboard);
}
