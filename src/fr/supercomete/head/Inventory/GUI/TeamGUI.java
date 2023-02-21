package fr.supercomete.head.Inventory.GUI;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.Team;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamGUI extends KTBSInventory {
    private final KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
    public TeamGUI(Player player) {
        super("§dTeam", 54, player);
    }

    @Override
    protected boolean denyDoubleClick() {
        return true;
    }

    @Override
    protected Inventory generateinventory(Inventory inv) {
        for(int h=0;h<9;h++)inv.setItem(h, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)0));
        for(int h=45;h<54;h++)inv.setItem(h, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)0));

        for (int r = 9; r < 9 + api.getTeamProvider().getTeams().size(); r++) {
            int index = r - 9;
            Team t = api.getTeamProvider().getTeams().get(index);
            ItemStack TeamStack = InventoryUtils.createColorItem(Material.BANNER, TeamManager.getColorOfShortColor(t.getColor()).toString() + t.getChar() + " " + TeamManager.getNameOfShortColor(t.getColor()), 1, t.getColor());
            ItemMeta itm = TeamStack.getItemMeta();
            itm.setLore(t.getTeamItemLore());
            TeamStack.setItemMeta(itm);
            inv.setItem(r, TeamStack);
        }
        return inv;
    }

    @Override
    protected boolean onClick(Player player, int currentSlot, KTBSAction action) {
        switch (currentSlot) {
            default:
                if (currentSlot > 8 && currentSlot < 9 + TeamManager.teamlist.size()) {
                    Team team = TeamManager.teamlist.get(currentSlot - 9);
                    Bukkit.broadcastMessage(Main.UHCTypo + player.getName() + " a rejoint l'équipe "
                            + TeamManager.getColorOfShortColor(team.getColor()) + team.getTeamName());
                    for (Team t : TeamManager.teamlist)
                        if (t.isMemberInTeam(player.getUniqueId()))
                            t.removeMember(player.getUniqueId());// Remove The player if the player have join another
                    TeamManager.teamlist.get(currentSlot - 9).addMembers(player.getUniqueId());
                    player.closeInventory();
                }
                break;
        }
        return true;
    }

    @Override
    protected boolean onClose(Player holder) {
        return false;
    }
}
