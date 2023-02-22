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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TeamGUI extends KTBSInventory {
    private final Random r =new Random(System.currentTimeMillis());
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
        inv.setItem(4,InventoryUtils.createSkullItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg4MWNjMjc0N2JhNzJjYmNiMDZjM2NjMzMxNzQyY2Q5ZGUyNzFhNWJiZmZkMGVjYjE0ZjFjNmE4YjY5YmM5ZSJ9fX0=","§fAléatoire", Arrays.asList("§7Vous ajoute dans une équipe aléatoire.")));
        inv.setItem(0,InventoryUtils.createSkullItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGUwZThhY2FiYWQyN2Q0NjE2ZmFlOWU0NzJjMGRlNjA4NTNkMjAzYzFjNmYzMTM2N2M5MzliNjE5ZjNlMzgzMSJ9fX0=","§fAucun", Arrays.asList("§7Vous retire de votre équipe.")));
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
            case 0:
                Team te = api.getTeamProvider().getTeamOf(player);
                if(te != null) {
                    te.removeMember(player.getUniqueId());
                }
                break;
            case 4:
                ArrayList<Team> teams = new ArrayList<>();
                for(Team tea : api.getTeamProvider().getTeams()){
                    if(tea.getMembers().size()<tea.getMaxPlayerAmount()){
                        teams.add(tea);
                    }
                }
                if(teams.size()==0){
                    player.sendMessage(Main.UHCTypo+"§cToutes les équipes sont pleines.");
                    break;
                }
                int i = r.nextInt(teams.size());
                Team tea = teams.get(i);
                tea.addMembers(player.getUniqueId());
                Bukkit.broadcastMessage(Main.UHCTypo + player.getName() + " a rejoint l'équipe " + TeamManager.getColorOfShortColor(tea.getColor()) + tea.getTeamName());
                break;
            default:
                if (currentSlot > 8 && currentSlot < 9 + TeamManager.teamlist.size()) {
                    Team team = TeamManager.teamlist.get(currentSlot - 9);
                    Bukkit.broadcastMessage(Main.UHCTypo + player.getName() + " a rejoint l'équipe " + TeamManager.getColorOfShortColor(team.getColor()) + team.getTeamName());
                    team.addMembers(player.getUniqueId());
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
