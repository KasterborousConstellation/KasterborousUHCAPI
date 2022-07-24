package fr.supercomete.head.Listeners;

import java.util.Random;

import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.GameMode.Modes.DWUHC;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Key.TardisHandler;
import fr.supercomete.head.role.content.DWUHC.GreatIntelligence;
import fr.supercomete.head.world.worldgenerator;
import net.minecraft.server.v1_8_R3.EntityExperienceOrb;
import net.minecraft.server.v1_8_R3.World;

final class BlockBreakListener implements Listener {
	private final Main main;
	public BlockBreakListener(Main main) {
		this.main=main;
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		final Player player = e.getPlayer();
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
					final GreatIntelligence great = (GreatIntelligence)role;
					for(Snowman snowman :great.getEntities()){
						if(snowman.getLocation().distance(currentblock.getLocation())<5.0) {
							e.setCancelled(true);
							return;
						}
					}
				}
			}
		}
		if(TardisHandler.IsTardisGenerated) {
			if(TardisHandler.TardisLocation.getWorld().equals(currentblock.getWorld())) {
				if(currentblock.getLocation().distance(TardisHandler.TardisLocation)<6) {
					e.setCancelled(true);
				}				
			}
		}
		if(currentblock.getType()==Material.STONE && Configurable.ExtractBool(Configurable.LIST.BlockTransform)&& !e.getPlayer().getItemInHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)){
		    for(ItemStack stack : e.getBlock().getDrops()){
		        stack.setType(Material.COBBLESTONE);
            }
        }
	}
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		Block currentblock = e.getBlock();
		if (currentblock.getType() == Material.DIAMOND_ORE) {
			if (Main.currentGame.getScenarios().contains(Scenarios.BloodDiamond)) {
				if (player.getHealth() > 1) {
					player.damage(1);
				}
				player.sendMessage(Main.UHCTypo + "Le scénario " + Scenarios.BloodDiamond.getName()
						+ " est activé. Ce scénario vous a infligé 1/2coeurs de dégat");
			}
			if (Main.currentGame.getScenarios().contains(Scenarios.DiamondLimit)) {

				int current = 0;
				if (Main.diamondmap.containsKey(player.getUniqueId()))
					current = Main.diamondmap.get(player.getUniqueId());
				Main.diamondmap.put(player.getUniqueId(), (current));
				if (Main.diamondmap.get(player.getUniqueId()) < Main.currentGame
						.getDataFrom(Configurable.LIST.DiamondLimit)) {
					Main.diamondmap.put(player.getUniqueId(), (current + 1));
                    PlayerUtility.sendActionbar(player,
							"§bLimite de diamants: " + "§r" + Main.diamondmap.get(player.getUniqueId()) + "§b/"
									+ Main.currentGame.getDataFrom(Configurable.LIST.DiamondLimit));
				} else {
					e.setCancelled(true);
					InventoryUtils.addsafelyitem(player, new ItemStack(Material.GOLD_INGOT));
					currentblock.setType(Material.AIR);
                    PlayerUtility.sendActionbar(player,
							"§cLimite atteinte, §bdiamant§c remplacé par de l'§eor §bLimite de diamants: " + "§r"
									+ Main.diamondmap.get(player.getUniqueId()) + "§b/"
									+ Main.currentGame.getDataFrom(Configurable.LIST.DiamondLimit));
					return;
				}
			}
			
			
			
		}
		if(currentblock.hasMetadata("unbreakable")) {
			e.setCancelled(true);
			return;
		}
		if(currentblock.getType().equals(Material.GRAVEL)) {
			ItemStack stack;
			int random = new Random().nextInt(100);
			if(random<=Main.currentGame.getDataFrom(Configurable.LIST.Gravel)) {
				stack = InventoryUtils.getItem(Material.FLINT,null ,null);
			}else {
				stack = InventoryUtils.getItem(Material.GRAVEL,null,null);
			}
			player.getWorld().dropItem(currentblock.getLocation(), stack);
			e.setCancelled(true);
			currentblock.setType(Material.AIR);
		}
		if(currentblock.getType().equals(Material.LEAVES)||currentblock.getType().equals(Material.LEAVES_2)) {
			ItemStack stack;
			int random = new Random().nextInt(100);
			if(random<=Main.currentGame.getDataFrom(Configurable.LIST.Apple)) {
				stack = InventoryUtils.getItem(Material.APPLE,null ,null);
				player.getWorld().dropItem(currentblock.getLocation(), stack);
				e.setCancelled(true);
				currentblock.setType(Material.AIR);
			}
		}
		if (Main.currentGame.getScenarios().contains(Scenarios.CutClean)) {
			if (currentblock.getType() == Material.IRON_ORE) {
				e.setCancelled(true);
				spawnOrb(currentblock.getLocation(), 1, 1);
				currentblock.setType(Material.AIR);
				player.getWorld().dropItem(currentblock.getLocation(), InventoryUtils.getItem(Material.IRON_INGOT, null, null));
			}
			if (currentblock.getType() == Material.GOLD_ORE) {
				spawnOrb(currentblock.getLocation(), 1, 3);
				e.setCancelled(true);
				currentblock.setType(Material.AIR);
				player.getWorld().dropItem(currentblock.getLocation(), InventoryUtils.getItem(Material.GOLD_INGOT, null, null));
			}

		}
		if (e.getBlock().getType() == Material.LOG && Main.currentGame.getScenarios().contains(Scenarios.Timber)) {
			if (e.getPlayer().getItemInHand().getType() == Material.DIAMOND_AXE
					|| e.getPlayer().getItemInHand().getType() == Material.WOOD_AXE
					|| e.getPlayer().getItemInHand().getType() == Material.STONE_AXE
					|| e.getPlayer().getItemInHand().getType() == Material.IRON_AXE
					|| e.getPlayer().getItemInHand().getType() == Material.GOLD_AXE) {
				if (e.getBlock().getType() == Material.LOG) {
					for (int y = e.getBlock().getY() - 1; y < e.getBlock().getY() + 10; y++) {
						for (int x = e.getBlock().getX() - 0; x <= e.getBlock().getX() + 0; x++) {
							for (int z = e.getBlock().getZ() - 0; z <= e.getBlock().getZ() + 0; z++) {
								Block b = main.getServer().getWorld(e.getBlock().getWorld().getName()).getBlockAt(x, y,
										z);
								if (b.getType() == Material.LOG)
									b.breakNaturally();
							}
						}
					}
				}
			}
			return;
		} else if (e.getBlock().getType() == Material.LOG_2
				&& Main.currentGame.getScenarios().contains(Scenarios.Timber)) {
			if (e.getPlayer().getItemInHand().getType() == Material.DIAMOND_AXE
					|| e.getPlayer().getItemInHand().getType() == Material.WOOD_AXE
					|| e.getPlayer().getItemInHand().getType() == Material.STONE_AXE
					|| e.getPlayer().getItemInHand().getType() == Material.IRON_AXE
					|| e.getPlayer().getItemInHand().getType() == Material.GOLD_AXE) {
				if (e.getBlock().getType() == Material.LOG_2) {
					for (int y = e.getBlock().getY() - 2; y < e.getBlock().getY() + 10; y++) {
						for (int x = e.getBlock().getX() - 2; x <= e.getBlock().getX() + 2; x++) {
							for (int z = e.getBlock().getZ() - 2; z <= e.getBlock().getZ() + 2; z++) {
								Block b = main.getServer().getWorld(e.getBlock().getWorld().getName()).getBlockAt(x, y,
										z);
								if (b.getType() == Material.LOG_2)
									b.breakNaturally();
							}
						}
					}
				}
			}
			return;
		}
	}
	//Taken from https://bukkit.org/threads/how-can-i-spawn-exp-orbs.92451/
		public void spawnOrb(Location l, int amount, int value) {
			  double x = l.getX(), y = l.getY(), z = l.getZ();
			  World w = ((CraftWorld)l.getWorld()).getHandle();
			  for(int i = 0; i < value; i++) {
			    w.addEntity(new EntityExperienceOrb(w, x, y, z, value));
			  }
		}
}
