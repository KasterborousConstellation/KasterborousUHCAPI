package fr.supercomete.head.GameUtils.Command;

import org.bukkit.entity.Player;

public interface SubCommand {
    String subCommand();
    boolean execute(Player sender,String[] args);
    String subCommandDescription();
}
