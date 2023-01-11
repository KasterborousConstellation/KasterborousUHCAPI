package fr.supercomete.head.Listeners;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.Scenarios.MonsterHunter;
import fr.supercomete.head.GameUtils.Scenarios.Objective;
import fr.supercomete.head.GameUtils.Team;
import fr.supercomete.head.GameUtils.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;

final class EntityDeathListener implements Listener{
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		Entity entity = e.getEntity();
		if (entity.getType() == EntityType.ZOMBIE && Main.currentGame.getScenarios().contains(Scenarios.BetaZombie)) {
			ItemStack feather = InventoryUtils.getItem(Material.FEATHER, null, null);
			feather.setAmount(new Random().nextInt(3) + 1);
			entity.getWorld().dropItemNaturally(entity.getLocation(), feather);
		}
		if (entity.getType() == EntityType.COW && Main.currentGame.getScenarios().contains(Scenarios.CutClean)) {
			List<ItemStack> drops = e.getDrops();
			for (ItemStack drop : drops) {
				if (drop.getType() == Material.RAW_BEEF) {
					drop.setType(Material.COOKED_BEEF);
				}
			}
		}
		if (entity.getType() == EntityType.SHEEP && Main.currentGame.getScenarios().contains(Scenarios.CutClean)) {
			List<ItemStack> drops = e.getDrops();
			for (ItemStack drop : drops) {
				if (drop.getType() == Material.MUTTON) {
					drop.setType(Material.COOKED_MUTTON);
				}
			}
		}
		if (entity.getType() == EntityType.PIG && Main.currentGame.getScenarios().contains(Scenarios.CutClean)) {
			List<ItemStack> drops = e.getDrops();
			for (ItemStack drop : drops) {
				if (drop.getType() == Material.PORK) {
					drop.setType(Material.GRILLED_PORK);
				}
			}

		}
		if (entity.getType() == EntityType.RABBIT && Main.currentGame.getScenarios().contains(Scenarios.CutClean)) {
			List<ItemStack> drops = e.getDrops();
			for (ItemStack drop : drops) {
				if (drop.getType() == Material.RABBIT) {
					drop.setType(Material.COOKED_RABBIT);
				}
			}
		}
        if(e.getEntity().getKiller()!=null&&!Main.currentGame.isGameState(Gstate.Waiting)&&Bukkit.getServicesManager().load(KtbsAPI.class).getScenariosProvider().IsScenarioActivated("Monster-Hunter")){
            Player player = e.getEntity().getKiller();
            MonsterHunter hunter = (MonsterHunter) Bukkit.getServicesManager().load(KtbsAPI.class).getScenariosProvider().getActivatedScenario("Monster-Hunter");
            Team team = TeamManager.getTeamOfUUID(player.getUniqueId());
            Objective obj=hunter.objectives.get(team.getTeam_id());
            obj.kill(e.getEntityType());
        }
	}
}
