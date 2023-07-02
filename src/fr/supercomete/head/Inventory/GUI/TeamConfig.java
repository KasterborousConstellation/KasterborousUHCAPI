package fr.supercomete.head.Inventory.GUI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.SimpleTeamMode;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.core.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import java.util.Arrays;
import java.util.Collections;
public class TeamConfig extends KTBSInventory {
    public TeamConfig(Player player) {
        super("§dTeam Configuration",27 , player);
    }

    @Override
    protected boolean denyDoubleClick() {
        return true;
    }

    @Override
    protected Inventory generateinventory(Inventory inv) {
        SimpleTeamMode teammode =(SimpleTeamMode) Main.currentGame.getMode();
        inv.setItem(11, InventoryUtils.getItem(Material.WOOL, "§bNombre de joueur par équipe: §4" + teammode.getTeamSize(), Arrays.asList(InventoryUtils.ClickTypoAdd + "1", InventoryUtils.ClickTypoRemove + "1")));
        inv.setItem(15, InventoryUtils.getItem(Material.PAPER, "§bNombre d'équipes: §a" + teammode.getNumberOfTeam(), Arrays.asList(InventoryUtils.ClickTypoAdd + "1", InventoryUtils.ClickTypoRemove + "1")));
        inv.setItem(22, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
        return inv;
    }

    @Override
    protected boolean onClick(Player player, int currentSlot, KTBSAction action) {
        SimpleTeamMode teammode =(SimpleTeamMode) Main.currentGame.getMode();
        ClickType currentClick = action.getClick();
        switch (currentSlot) {
            case 11:
                if(teammode.canBeChanged()){
                    if (currentClick.isRightClick()) {
                        if (teammode.getTeamSize() < teammode.TeamSizeBound().getMax()) {
                            teammode.setTeamSize(teammode.getTeamSize() + 1);
                        }
                    } else {
                        if (teammode.getTeamSize() > teammode.TeamSizeBound().getMin()) {
                            teammode.setTeamSize(teammode.getTeamSize() - 1);
                        }
                    }
                }
                refresh();
                TeamManager.setupTeams();
                break;
            case 15:
                if (currentClick.isRightClick() && teammode.getNumberOfTeam() + 1 < 37)
                    teammode.setNumberofTeam(teammode.getNumberOfTeam() + 1);
                if (currentClick.isLeftClick() && teammode.getNumberOfTeam() - 1 >= 2)
                    teammode.setNumberofTeam(teammode.getNumberOfTeam() - 1);
                refresh();
                TeamManager.setupTeams();
                break;
            case 22:
                new ModeGUI(Main.currentGame.getMode(), player).open();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected boolean onClose(Player holder) {
        return false;
    }
}
