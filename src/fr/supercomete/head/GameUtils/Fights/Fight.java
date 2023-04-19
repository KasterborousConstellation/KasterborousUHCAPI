package fr.supercomete.head.GameUtils.Fights;
import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEventHandler;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Triggers.Trigger_OnFightEnd;
import fr.supercomete.head.role.Triggers.Trigger_onFightBegin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Fight {
    private final int begin;
    private final UUID first;
    private final UUID second;
    public Fight(UUID first,UUID second) {
        this.first = first;
        this.second = second;
        this.begin = Main.currentGame.getTime();
    }

    public UUID getFirst(){
        return first;
    }
    public UUID getSecond(){
        return second;
    }
    public int getBegin(){
        return begin;
    }
}