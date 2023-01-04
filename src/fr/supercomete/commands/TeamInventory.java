package fr.supercomete.commands;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.Team;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class TeamInventory implements CommandExecutor {
    public static HashMap<Team, Inventory> inventoryHashMap = new HashMap<Team, Inventory>();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            if(!Main.currentGame.getScenarios().contains(Scenarios.TeamInventory)){
                commandSender.sendMessage("§cLe scénario Team-Inventory est désactivé");
                return true;
            }
            if(Main.currentGame.isGameState(Gstate.Waiting)){
                return true;
            }
            final Player player =(Player)commandSender;
            Team team = TeamManager.getTeamOfUUID(player.getUniqueId());
            if(team!=null){
                player.openInventory(inventoryHashMap.get(team));
            }
        }
        return false;
    }
}
