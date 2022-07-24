package fr.supercomete.head.role.content.DWUHC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_OnRoletime;
import fr.supercomete.head.role.Triggers.Trigger_WhileAnyTime;
import fr.supercomete.head.role.Triggers.Trigger_WhileNight;
import fr.supercomete.nbthandler.NbtTagHandler;

public final class Captain_Jack_Harkness extends DWRole implements Trigger_WhileAnyTime,Trigger_OnRoletime,Trigger_WhileNight{
	private ArrayList<Location> loclist=new ArrayList<Location>();
	private ArrayList<Info> infos = new ArrayList<>();
	public Captain_Jack_Harkness(UUID owner) {
		super(owner);
	}
	
	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("Vous avez l'effet §cforce§7 pendant la nuit.",
				"§7Vous possèdez à l'annonce des rôles, cinq pièges représentés par des fils. Ils peuvent être placés sur la carte et quand un autre joueur marche dessus, le piège cassera et vous obtiendrez l'identité de cette personne ainsi que son Camp actuel",
				"Les pièges à leur activation inflige 3♥ de dégat sauf si le Camp du joueur est Neutre. Les pièges ne peuvent pas tuer et laisse les joueurs à 1/2♥ minimum.");
	}

	public ArrayList<Location> getLoclist() {
		return loclist;
	}
	public void setLoclist(ArrayList<Location> loclist) {
		this.loclist = loclist;
	}

	@Override
	public Camps getDefaultCamp() {
		return Camps.DoctorCamp;
	}

	@Override
	public String askName() {
		return "Captain Jack Harkness";
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWU5ZTE4NjM3MDU0NzUyNTA0ZDY2MDVmZTM0NmY1MTEwNTI3NzNiYjU5ZGFiNDVkMGNmMGQxYjU2NjVhMDM1MSJ9fX0=";
	}

	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Humain,Status.TimeTraveller};
	}

    @Override
    public String[] AskMoreInfo() {
	    ArrayList<String> strings = new ArrayList<>();
	    for(final Info info : infos){
	        strings.add("Piège "+info.getLocation().getX()+" "+info.getLocation().getY()+" "+info.getLocation().getZ());
	        strings.add("   §6"+info.getName()+": "+info.getCamp().getColoredName());
        }
        return Main.convertArrayToTable(String.class,strings);
    }

    @Override
	public void onRoleTime(Player player) {
		ItemStack item5=NbtTagHandler.createItemStackWithUUIDTag(InventoryUtils.getItem(Material.STRING, "§dPiège", null), 7);
		item5.setAmount(5);
		InventoryUtils.addsafelyitem(player, item5);
		
	}
	@Override
	public void WhileNight(Player player) {
		PlayerUtility.addProperlyEffect(player,new PotionEffect(PotionEffectType.INCREASE_DAMAGE,40,0,false,false));
		
	}

	@Override
	public void WhileAnyTime(Player player) {
		Captain_Jack_Harkness captain = (Captain_Jack_Harkness) RoleHandler.getRoleOf(player);
		if(captain.getLoclist().size()==0)return;
		for(UUID uu:Main.getPlayerlist()){
			Player pl = Bukkit.getPlayer(uu);
			if(uu==player.getUniqueId())continue;
			ArrayList<Location> l = captain.getLoclist();
			for(Location loc:l) {
				if(loc.distance(pl.getLocation())<4) {
					pl.getWorld().getBlockAt(loc).setType(Material.AIR);
					captain.getLoclist().remove(loc);
					pl.damage(0);
					if(RoleHandler.getRoleOf(player).getCamp()!=Camps.Neutral) 
						pl.setHealth((pl.getHealth()>6) ? pl.getHealth()-6:1);
					player.sendMessage(Main.UHCTypo+"Un de vos pièges a blessé un joueur: "+pl.getName()+". Le camp du joueur qui est passé sur ce piège est "+RoleHandler.getRoleOf(pl).getCamp().getColor()+(RoleHandler.getRoleOf(pl.getUniqueId())).getCamp().getName());
				    infos.add(new Info(loc,pl.getName(),RoleHandler.getRoleOf(pl).getCamp()));
				}
			}
		}
		
	}
	private static class Info{
	    private final Location location;
	    private final String name;
	    private final Camps camp;
	    public Info(final Location location,final String name,final Camps camp){
	        this.location=location;
	        this.camp=camp;
            this.name=name;
        }
        public String getName(){
	        return name;
        }
        public Camps getCamp(){
	        return camp;
        }
        public Location getLocation(){
	        return location;
        }
    }
}
 