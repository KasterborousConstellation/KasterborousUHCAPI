package fr.supercomete.commands;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.KTBS_Team;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class TeamInventory implements CommandExecutor {
    public static HashMap<KTBS_Team, Inventory> inventoryHashMap = new HashMap<KTBS_Team, Inventory>();
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
            KTBS_Team team = TeamManager.getTeamOfUUID(player.getUniqueId());
            if(team!=null){
                player.openInventory(inventoryHashMap.get(team));
            }
        }
        return false;
    }
}
