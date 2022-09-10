package fr.supercomete.head.GameUtils.GameMode.Modes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nullable;

import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.role.RoleHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.Exception.InvalidRoleClassException;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.NakimeCastleRole;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.structure.Structure;
public abstract class Mode {
    public final void ModeDefaultOnDeath(final Offline_Player player,Location deathLocation){

        InventoryUtils.dropInventory(player.getInventory(),deathLocation,deathLocation.getWorld());
        player.getInventory().clear();
        Main.playerlist.remove(player.getPlayer());
        if(Main.currentGame.getMode()instanceof CampMode){
            RoleHandler.DisplayDeath(player);
            RoleHandler.removePlayer(player.getPlayer());
        }
    }
    public final void ModeDefaultOnDeath(final Player player,final Player damager,Location deathLocation){
        Mode.GoldenHeadImplement(player, damager);
        Mode.KillSwitchImplement(player, damager);
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
        ModeDefaultOnDeath(new Offline_Player(player),deathLocation);
    }
	private String name;
	private Material material;
	private List<String>description;
	private CopyOnWriteArrayList<Class<?>> registeredrole = new CopyOnWriteArrayList<Class<?>>();
	private ArrayList<Structure> structure = new ArrayList<Structure>();
	public Mode(String name,Material material,List<String> description) {
		this.name=name;
		this.material=material;
		this.description=description;
	}
	public abstract void OnKillMethod(Location deathLocation,Player player ,@Nullable Player damager);
	public abstract void onAnyTime(Player player);
    public abstract void onGlobalAnytime();
	public abstract void onDayTime(Player player);
	public abstract void onNightTime(Player player);
	public abstract void onEndingTime(Player player);
	public abstract void onRoleTime(Player player);
	public abstract void OnStart(Player player);
	public abstract void onEpisodeTime(Player player);
	public abstract boolean WinCondition();
	public void DecoKillMethod(Offline_Player player) {
		Bukkit.broadcastMessage("Fatal error, non-implemented decokillmethod for "+this.getClass());
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
	public List<String> getDescription() {
		return description;
	}
	public void setDescription(List<String> description) {
		this.description = description;
	}
	public CopyOnWriteArrayList<Class<?>> getRegisteredrole() {
		return registeredrole;
	}
	public void setRegisteredrole(CopyOnWriteArrayList<Class<?>> registeredrole) {
		this.registeredrole = registeredrole;
	}
	public void RegisterRole(Class<?> role) {
		registeredrole.add(role);
	}
	/**
	 * @return the structure
	 */
	public ArrayList<Structure> getStructure() {
		return structure;
	}
	/**
	 * @param structure the structure to set
	 */
	public void setStructure(ArrayList<Structure> structure) {
		this.structure = structure;
	}
	public void setStructureLocation(Location location,String structurename) {
		for(final Structure structure: this.structure) {
			if(structure.getStructurename().equalsIgnoreCase(structurename)) {
				structure.setLocation(location);
				return;
			}
		}
	}
	public final static void GoldenHeadImplement(Player player, Player damager) {
		if (Main.currentGame.getScenarios().contains(Scenarios.GoldenHead)) {
			if (damager != null) {
				ItemStack item_ = InventoryUtils.createColorItem(Material.SKULL_ITEM, null, 1, (short) 3);
				SkullMeta im = (SkullMeta) item_.getItemMeta();
				im.setOwner(player.getName());
				item_.setItemMeta(im);
				InventoryUtils.addsafelyitem(player, item_);
			}
		}
	}

	public final static void KillSwitchImplement(Player player, Player damager) {
		if (Main.currentGame.getScenarios().contains(Scenarios.KillSwitch)) {
			if (damager != null) {
				damager.teleport(player.getLocation());
			}
		}
	}
} 