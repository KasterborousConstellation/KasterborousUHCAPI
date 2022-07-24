package fr.supercomete.head.role.content.DWUHC;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.supercomete.enums.Camps;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_OnRoletime;
import fr.supercomete.head.role.RoleModifier.InvisibleRoleWithArmor;
import fr.supercomete.nbthandler.NbtTagHandler;
import net.minecraft.server.v1_8_R3.EntityLiving;

public final class GreatIntelligence extends DWRole implements InvisibleRoleWithArmor,Trigger_OnRoletime {
	private HashMap<Location, Integer> snowman = new HashMap<Location, Integer>();
	private final ArrayList<Snowman> entities = new ArrayList<Snowman>();
	private boolean showing = true;
	private Location teleportedsnowman=null;
	public CoolDown freeze= new CoolDown(2, 2*60);
	public CoolDown snowmancooldown= new CoolDown(4, 0);
	public ArrayList<Snowman> getEntities() {
		return entities;
	}
	public void teleporttoSnowman(int id) {
		final Player player = Bukkit.getPlayer(super.getOwner());
		Location key = (Location) this.getSnowman().keySet().toArray()[id];
		if(Main.currentGame.getTime()-this.getSnowman().get(key)>15*60) {
			setShowing(false);
			setTeleportedsnowman(key);
			this.getSnowman().put(key, Main.currentGame.getTime());
			player.teleport(key);
			hide(player);
		}else player.sendMessage(Main.UHCTypo+"§cCe pouvoir est en cooldown.");
	}
	public GreatIntelligence(UUID owner) {
		super(owner);
	}
	@Override
	public Status[]AskStatus() {
		return new Status[]{Status.Humain};
	}

    @Override
    public String[] AskMoreInfo() {
	    ArrayList<String> first = new ArrayList<>();
	    for(Map.Entry<Location,Integer> entry : snowman.entrySet()){
	        first.add("Snowman ("+entry.getKey().getBlockX()+" "+entry.getKey().getBlockY()+" "+entry.getKey().getBlockZ()+") Utilisable: "+Main.getCheckMark(Main.currentGame.getTime()-this.getSnowman().get(entry.getKey())>15*60));
        }
        return Main.convertArrayToTable(String.class,first);
    }

    @Override
	public String askName() {
		return "La Grande Intelligence";
	}
	@Override
	public Camps getDefaultCamp() {
		return Camps.EnnemiDoctorCamp;
	}
	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("§7Vous pouvez contre 1/2♥ et cela jusqu'a 4fois, faire apparaitre un bonhomme de neige la où vous vous trouvez avec la commande '/dw snowman'. Les bonhommes de neiges sont invincible, et la modification des blocs est désactivé dans un rayon de 5blocs autour d'eux."
				,"Vous pouvez vous téléporter à un de vos bonhomme de neige avec la commande '/dw tempete'. Vous serez invisible dans le rayon de 5blocs autour du bonhomme de neige et pourrez frapper avec votre épée en étant invisible. Cependant lorsque vous sortez de cette zone vous obtiendrez l'effet lenteur 2 pendant 1min. La téléportation à un bonhomme de neige, rendra la téléportation a ce bonhomme de neige impossible pendant 15min."+snowmancooldown.formalizedUtilisation()
				," §7Vous pouvez 2 fois dans la partie créer une zone de 10x10 blocs autour de vous, qui dure 20s dans laquelle tous les joueurs du Camp du Docteur obtiendront l'effet de lenteur 2. "+freeze.formalizedUtilisation()
				
				);
	}
	@Override
	public ItemStack[] askItemStackgiven() {
		return null;
	}
	@Override
	public boolean AskIfUnique() {
		return true;
	}
	@Override
	public String AskHeadTag() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTk1MTNhNWZiMzU2YWNlYzc5ZDJhYzZlZGEzNzNkMDJjNjIwOTA5MGJlZjUyMGNiODM5M2Y1ZTdlZTY2ZjhmOCJ9fX0=";
	}
	public void addNewSnowman(Location loc, Main main) {
		snowman.put(loc, Main.currentGame.getTime() - (15 * 60));
		LivingEntity entity = ((LivingEntity) Bukkit.getPlayer(getOwner()).getWorld().spawnEntity(loc,
				EntityType.SNOWMAN));
		setAI(entity, false);
		new BukkitRunnable() {
			@Override
			public void run() {
				entity.setMaxHealth(10000);
				entity.setVelocity(new Vector(0, 0, 0));
				if (Main.currentGame.getGamestate() == Gstate.Waiting) {
					cancel();

				}
			}
		}.runTaskTimer(main, 0, 1);

		entities.add((Snowman) entity);

	}
	public HashMap<Location, Integer> getSnowman() {
		return snowman;
	}
	public void setSnowman(HashMap<Location, Integer> snowman) {
		this.snowman = snowman;
	}
	public static void setAI(LivingEntity entity, boolean hasAi) {
		EntityLiving handle = ((CraftLivingEntity) entity).getHandle();
		handle.getDataWatcher().watch(15, (byte) (hasAi ? 0 : 1));
	}
	/**
	 * @return the teleportedsnowman
	 */
	public Location getTeleportedsnowman() {
		return teleportedsnowman;
	}
	/**
	 * @param teleportedsnowman the teleportedsnowman to set
	 */
	public void setTeleportedsnowman(Location teleportedsnowman) {
		this.teleportedsnowman = teleportedsnowman;
	}
	/**
	 * @return the showing
	 */
	public boolean isShowing() {
		return showing;
	}
	/**
	 * @param showing the showing to set
	 */
	public void setShowing(boolean showing) {
		this.showing = showing;
	}
	@Override
	public void onRoleTime(Player player) {
		ItemStack item6=NbtTagHandler.createItemStackWithUUIDTag(InventoryUtils.getItem(Material.SNOW_BLOCK, "§bFreeze", null), 9);
		item6.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
		ItemMeta im =item6.getItemMeta();
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item6.setItemMeta(im);
		InventoryUtils.addsafelyitem(player, item6);
		
	}
}
