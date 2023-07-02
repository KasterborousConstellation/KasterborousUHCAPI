package fr.supercomete.head.GameUtils.GameMode.ModeModifier;
import fr.supercomete.head.GameUtils.GameConfigurable.Bound;
import fr.supercomete.head.GameUtils.KTBS_Team;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.Inventory.GUI.TeamConfig;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;

public interface SimpleTeamMode extends TeamMode,SpecialTab{
	int getTeamSize();
	void setTeamSize(int size);
	void setNumberofTeam(int number);


    boolean canBeChanged();
    Bound TeamSizeBound();
    @Override
    default KTBS_Team createTeam(int team_id){
        char[] ListOfChar= {'❤','♦','♠','♣'};
        return new KTBS_Team(ListOfChar[team_id/9]+TeamManager.getNameOfShortColor((short)team_id), new ArrayList<>(), "", "", (short)(team_id%16), ListOfChar[team_id/9], getTeamSize(),false);
    }
    @Override
    default KTBSInventory getLinkedTab(Player player){
        return new TeamConfig(player) ;
    }
    @Override
    default ItemStack getLinkedItem(){
        return InventoryUtils.createColorItem(Material.BANNER, "§rTeams", 1, (short)0);
    }
}