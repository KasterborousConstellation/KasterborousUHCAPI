package fr.supercomete.head.Listeners;

import java.util.UUID;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.GameMode.Modes.DWUHC;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.CyberiumHandler;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.RoleState.MasterCyberium;
import fr.supercomete.head.role.RoleState.RoleStateTypes;
import fr.supercomete.head.role.content.DWUHC.TheMaster;
import fr.supercomete.head.world.worldgenerator;

final class PlayerAmorStandManipulateListeber implements Listener {

	@EventHandler
	public void onPlayerInteractEvent(PlayerArmorStandManipulateEvent e) {
		Entity currentEntity = e.getRightClicked();
		Player currentPlayer = e.getPlayer();
		if (currentEntity instanceof ArmorStand
				&& currentEntity.getCustomName().equalsIgnoreCase("§r§4Cyberium")) {
			e.setCancelled(true);
			if (RoleHandler.getRoleOf(currentPlayer) == null)
				return;
			UUID uu = currentPlayer.getUniqueId();
			if (DWUHC.generateCybermanRoleList().contains(RoleHandler.getRoleOf(currentPlayer).getClass())|| RoleHandler.getRoleOf(uu).hasRoleState(RoleStateTypes.Infected)) {
				CyberiumHandler.SetCyberiumHost(currentPlayer);
				CyberiumHandler.Cyberiumlocation = null;
				RoleHandler.sendmessagetoallCyberman("§rLe joueur §6" + currentPlayer.getName() + "§r a obtenu le §4Cyberium");
				worldgenerator.currentPlayWorld.getEntities().remove(currentEntity);
				currentEntity.remove();
			} else if (RoleHandler.getRoleOf(currentPlayer) instanceof TheMaster) {
				CyberiumHandler.SetCyberiumHost(currentPlayer.getUniqueId());
				CyberiumHandler.Cyberiumlocation = null;
				RoleHandler.sendmessagetoallCyberman("§4 Le Ma§te a obtenu le Cyberium !");
				worldgenerator.currentPlayWorld.getEntities().remove(currentEntity);
				currentEntity.remove();
				TheMaster role = (TheMaster) RoleHandler.getRoleOf(currentPlayer);
				role.addRoleState(new MasterCyberium(RoleStateTypes.MasterCyberium));
				role.setCamp(Camps.Neutral);
				currentPlayer.sendMessage(Main.UHCTypo+"§4Vous avez obtenu le Cyberium. Vous passez donc dans le Camps Neutre et devez gagner seul.");
			}else {
				RoleHandler.sendmessagetoallCyberman("§cUn joueur a détruit le Cyberium !");
				CyberiumHandler.Cyberiumlocation=null;
				CyberiumHandler.HostPlayer=null;
				RoleHandler.getRoleOf(currentPlayer).addBonus(new Bonus(BonusType.Force,15));
				currentPlayer.sendMessage("§aVous avez détruit le §4Cyberium§a, vous avez obtenu 15% de §cforce§a supplémentaire.");
				currentEntity.remove();
			}
			return;
		}
	}

}
