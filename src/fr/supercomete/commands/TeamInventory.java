package fr.supercomete.commands;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.Team;
import fr.supercomete.head.GameUtils.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class TeamInventory implements CommandExecutor {
    HashMap<Team, Inventory> inventoryHashMap = new HashMap<Team, Inventory>();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            if(!Bukkit.getServicesManager().load(KtbsAPI.class).getScenariosProvider().getScenarios().contains(Scenarios.TeamInventory)){
                commandSender.sendMessage("§cLe scénario Team-Inventory est désactivé");
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
