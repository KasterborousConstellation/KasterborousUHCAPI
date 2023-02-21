package fr.supercomete.head.Inventory.GUI;

import fr.supercomete.head.GameUtils.GUI.ModeGUI;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.TeamMode;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Collections;

public class TeamConfig extends KTBSInventory {
    private final KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
    public TeamConfig(Player player) {
        super("§dTeam Configuration",27 , player);
    }

    @Override
    protected boolean denyDoubleClick() {
        return true;
    }

    @Override
    protected Inventory generateinventory(Inventory inv) {
        inv.setItem(11, InventoryUtils.getItem(Material.WOOL, "§bNombre de joueur par équipe: §4" + api.getTeamProvider().getNumberOfMemberPerTeam(), Arrays.asList(InventoryUtils.ClickTypoAdd + "1", InventoryUtils.ClickTypoRemove + "1")));
        inv.setItem(15, InventoryUtils.getItem(Material.PAPER, "§bNombre d'équipes: §a" + api.getTeamProvider().getTeamNumber(), Arrays.asList(InventoryUtils.ClickTypoAdd + "1", InventoryUtils.ClickTypoRemove + "1")));
        inv.setItem(22, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
        return inv;
    }

    @Override
    protected boolean onClick(Player player, int currentSlot, KTBSAction action) {
        ClickType currentClick = action.getClick();
        switch (currentSlot) {
            case 11:
                TeamMode teammode =(TeamMode) Main.currentGame.getMode();
                if (currentClick.isRightClick()) {
                    if (TeamManager.NumberOfPlayerPerTeam < teammode.TeamSizeBound().getMax()) {
                        TeamManager.NumberOfPlayerPerTeam=(TeamManager.NumberOfPlayerPerTeam + 1);
                    }
                } else {
                    if (TeamManager.NumberOfPlayerPerTeam > teammode.TeamSizeBound().getMin()) {
                        TeamManager.NumberOfPlayerPerTeam=(TeamManager.NumberOfPlayerPerTeam - 1);
                    }
                }
                refresh();
                TeamManager.setupTeams();
                break;
            case 15:
                if (currentClick.isRightClick() && TeamManager.TeamNumber + 1 < 37)
                    TeamManager.TeamNumber=(TeamManager.TeamNumber + 1);
                if (currentClick.isLeftClick() && TeamManager.TeamNumber - 1 >= 2)
                    TeamManager.TeamNumber=(TeamManager.TeamNumber - 1);
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
