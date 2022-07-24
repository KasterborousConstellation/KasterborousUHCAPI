package fr.supercomete.head.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.supercomete.head.GameUtils.GameMode.Modes.DWUHC;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Key.TardisHandler;
import fr.supercomete.head.role.content.DWUHC.Captain_Jack_Harkness;
import fr.supercomete.head.role.content.DWUHC.GreatIntelligence;
import fr.supercomete.head.world.worldgenerator;
import fr.supercomete.nbthandler.NbtTagHandler;

final class BlockPlaceListener implements Listener {
	private final Main main;
	public BlockPlaceListener(Main main) {
		this.main=main;
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		final Player player = e.getPlayer();
		final ItemStack currentItem = e.getItemInHand();
		final Block currentblock = e.getBlock();
		if(TardisHandler.IsTardisGenerated && 
				Main.currentGame.getMode()instanceof DWUHC && 
				player.getLocation().getWorld()==worldgenerator.structureworld&&
				player.getLocation().distance(Main.currentGame.getMode().getStructure().get(0).getLocation())<150
				) {
			e.setCancelled(true);
			new BukkitRunnable(){
				@Override
				public void run() {
					player.updateInventory();
				}
			}.runTaskLater(main, 0L);
		}
		if (RoleHandler.isIsRoleGenerated()) {
			for(final Role role : RoleHandler.getRoleList().values()) {
				if(role instanceof GreatIntelligence){
					GreatIntelligence great = (GreatIntelligence)role;
					for(Snowman snowman :great.getEntities()){
						if(snowman.getLocation().distance(currentblock.getLocation())<5.0) {
							e.setCancelled(true);
							return;
						}
					}
				}
			}
			if (RoleHandler.getRoleOf(player) instanceof Captain_Jack_Harkness && NbtTagHandler.hasUUIDTAG(currentItem) && NbtTagHandler.getUUIDTAG(currentItem) == 7 && currentItem.getType() == Material.STRING) {
                Captain_Jack_Harkness captain = (Captain_Jack_Harkness)RoleHandler.getRoleOf(player);
			    if (!Main.searchintoarrayLocationDistance(captain.getLoclist(), 30, currentblock.getLocation())) {
					e.setCancelled(true);
					player.sendMessage(Main.UHCTypo + "Ce piège est trop près d'un autre piège");
					return;
				}
                double minRange=-1;
                for(final Player p : Bukkit.getOnlinePlayers()){
                    if(p.getGameMode()!= GameMode.SPECTATOR){
                        if(Main.getPlayerlist().contains(p.getUniqueId())&&p.getUniqueId()!=player.getUniqueId()){
                            if(p.getWorld()==player.getWorld()){
                                if(minRange==-1|| minRange>p.getLocation().distance(player.getLocation())){
                                    minRange=p.getLocation().distance(player.getLocation());
                                }
                            }
                        }
                    }
                }
                if(minRange<30){
                    e.setCancelled(true);
                    player.sendMessage(Main.UHCTypo + "Ce piège est trop près d'un autre joueur.");
                    return;
                }
				captain.getLoclist().add(currentblock.getLocation());
				e.setCancelled(true);
				if(currentItem.getAmount()==1) {
					player.setItemInHand(null);
				}else
				currentItem.setAmount(currentItem.getAmount()-1);
				return;
			} else if (RoleHandler.getRoleOf(player) instanceof Captain_Jack_Harkness
					&& NbtTagHandler.hasUUIDTAG(currentItem) && NbtTagHandler.getUUIDTAG(currentItem) == 7
					&& currentItem.getType() == Material.STRING) {
				e.setCancelled(true);
				player.sendMessage(Main.UHCTypo + "Vous n'avez pas le bon rôle pour utiliser cet objet");
				return;
			}
		}
		if(TardisHandler.IsTardisGenerated) {
			if(TardisHandler.TardisLocation.getWorld().equals(currentblock.getWorld())) {
				if(currentblock.getLocation().distance(TardisHandler.TardisLocation)<6) {
					e.setCancelled(true);
				}				
			}
		}
	}
}
