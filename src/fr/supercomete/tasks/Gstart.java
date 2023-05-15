package fr.supercomete.tasks;
import java.util.ArrayList;
import java.util.UUID;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
public class Gstart extends BukkitRunnable{
	private int timer =10;
	private final Main main;
	private final ArrayList<Location> location;
	public Gstart(Main main, ArrayList<Location> loc) {
		this.main = main;
		this.location=loc;
	}
	@Override
	public void run() {
		if(timer <=0) {
			if(!Main.currentGame.isGameState(Gstate.Starting))cancel();
			Bukkit.broadcastMessage(Main.UHCTypo+"§rDébut de la partie");
			Main.currentGame.setGamestate(Gstate.Playing);
			Cycle cycle = new Cycle();
			cycle.runTaskTimer(this.main, 0, 20L);
			NoDamage nodamage= new NoDamage(Main.currentGame.getTimer(Timer.InvincibilityTime).getData(),Main.getPlayerlist());
			nodamage.runTaskTimer(main, 0, 20L);
			for(Location loc:location){
				double locx=loc.getX();
				double locz=loc.getZ();
				for(int xl=-2;xl<3;xl++){
					for(int yl=-2;yl<3;yl++){
						for(int zl=-2;zl<3;zl++){
                            MapHandler.getMap().getPlayWorld().getBlockAt(new Location(MapHandler.getMap().getPlayWorld(), xl+locx, 145+yl, zl+locz)).setType(Material.AIR);
						}
					}
				}
			}
            //Essai de fix le bug de desync d'inventaire en espérant que ça marche.
			for(UUID uuid:Main.getPlayerlist()) {
                Main.currentGame.getKillList().put(uuid, 0);
                Player player = Bukkit.getPlayer(uuid);
                if(player==null){
                    continue;
                }
                PlayerInventory inventory = player.getInventory();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        inventory.clear();
                        inventory.setHelmet(new ItemStack(Material.AIR));
                        inventory.setChestplate(new ItemStack(Material.AIR));
                        inventory.setLeggings(new ItemStack(Material.AIR));
                        inventory.setBoots(new ItemStack(Material.AIR));
                        if (player.getGameMode() != GameMode.SPECTATOR)
                            player.setGameMode(GameMode.SURVIVAL);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.setMaxHealth(20);
                                player.setHealth(20);
                                player.setFoodLevel(40);
                                player.setExp(0);
                                player.setLevel(0);
                            }
                        }.runTaskLater(Main.INSTANCE, 1L);
                    }
                }.runTaskLater(Main.INSTANCE, 1L);
            }
            new BukkitRunnable(){
                @Override
                public void run() {
                    PlayerUtility.giveStuff(Main.getPlayerlist());
                }
            }.runTaskLater(Main.INSTANCE,2L);
			cancel();
		}
		if((timer>0&&timer <=5)|| timer ==10)Bukkit.broadcastMessage(Main.UHCTypo+"§rDébut de la partie dans §c"+timer);
		timer--;
	}
}