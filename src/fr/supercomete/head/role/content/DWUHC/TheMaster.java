package fr.supercomete.head.role.content.DWUHC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.GameMode.Modes.DWUHC;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_WhileNight;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;
import fr.supercomete.head.role.RoleModifier.PreAnnouncementExecute;
import fr.supercomete.head.role.RoleState.RoleStateTypes;

public final class TheMaster extends DWRole implements HasAdditionalInfo,PreAnnouncementExecute,Trigger_WhileNight {
	private ArrayList<UUID> daleks;
	private ArrayList<UUID> Cybermans;

	public TheMaster(UUID owner) {
		super(owner);
		this.setDaleks(new ArrayList<UUID>());
		this.setCybermans(new ArrayList<UUID>());
	}

	public ArrayList<UUID> getDaleks() {
		return daleks;
	}

	public void setDaleks(ArrayList<UUID> daleks) {
		this.daleks = daleks;
	}

	public ArrayList<UUID> getCybermans() {
		return Cybermans;
	}

	public void setCybermans(ArrayList<UUID> cybermans) {
		Cybermans = cybermans;
	}

	@Override
	public String[] getAdditionnalInfo() {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("  §7Les daleks que vous connaisez sont: ");
		for (UUID uuid : getDaleks()) {
			arr.add("    §4-" + Bukkit.getPlayer(uuid).getName());
		}
		arr.add("  §7Les Cybermens que vous connaisez sont: ");
		for (UUID uuid : getCybermans()) {
			arr.add("    §4-" + Bukkit.getPlayer(uuid).getName());
		}
		return Main.convertArrayToTable(String.class,arr);
	}

	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("Vous avez l'effet §bvitesse§7 pendant la nuit.","Vous connaissez 2 Daleks ainsi que 2 Cybermans.","Si vous arrivez a obtenir le Cyberium vous passerez dans le Camp Neutre et devrez gagner seul, et obtiendrez §bvitesse§7 et §cforce§7 pendant le jour ainsi que §frésistance§7 pendant la nuit.","Vous avez la possibilité comme tout les membres du Camp des Ennemis du Docteur, de détruire les clefs contenues dans le TARDIS en restant proche de celui-ci.");
	}

	@Override
	public Camps getDefaultCamp() {
		return Camps.EnnemiDoctorCamp;
	}
	@Override
	public String askName() {
		return "Le Maitre";
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTM1YjhmYzk3NTIzYWUyZjczMWQzZjRjNzJkZTc1OWFhNTE4YTU0NzI3MjQ0ZTFkNWRiNzNhNzM1NmQ5ZTQifX19";
	}

	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.TimeTraveller};
	}

    @Override
    public String[] AskMoreInfo() {
	    ArrayList<String> string = new ArrayList<>();
	    string.add("Cyberium trouvé: "+Main.getCheckMark(hasRoleState(RoleStateTypes.MasterCyberium)));
        string.add("Daleks:");
	    for(UUID uu:daleks){
	        string.add("  "+PlayerUtility.getNameByUUID(uu));
        }
	    string.add("Cybermen:");
        for(UUID uu:Cybermans){
            string.add("  "+PlayerUtility.getNameByUUID(uu));
        }
        return Main.convertArrayToTable(String.class,string);
    }

    private ArrayList<UUID> getTwoCyberman(){
		ArrayList<UUID> cyberreturn= new ArrayList<UUID>();
		for(Role r:RoleHandler.getRoleList().values()){
			if((DWUHC.generateCybermanRoleList().contains(r.getClass())||r.hasRoleState(RoleStateTypes.Infected))){
				cyberreturn.add(r.getOwner());
			}
		}
		ArrayList<UUID> returning= new ArrayList<UUID>();
		for(int e =0;e<2;e++) {
			if(cyberreturn.size()==0)return returning;
			int i = (cyberreturn.size()<=1)?0:new Random().nextInt(cyberreturn.size());
			returning.add(cyberreturn.get(i));
			cyberreturn.remove(i);
		}
		return returning;
	}
	private ArrayList<UUID> getTwoDalek(){
		ArrayList<UUID> cyberreturn= new ArrayList<UUID>();
		for(Role r: RoleHandler.getRoleList().values()){
			if(DWUHC.generateDalekRoleList().contains(r.getClass())){
				cyberreturn.add(r.getOwner());
			}
		}
		ArrayList<UUID> returning= new ArrayList<UUID>();
		for(int e =0;e<2;e++) {
			if(cyberreturn.size()==0)return returning;
			int i = (cyberreturn.size()<=1)?0:new Random().nextInt(cyberreturn.size());
			returning.add(cyberreturn.get(i));
			cyberreturn.remove(i);
		}
		return returning;
	}
	@Override
	public void PreAnnouncement() {
		this.setCybermans(getTwoCyberman());
		this.setDaleks(getTwoDalek());
		
	}

	@Override
	public void WhileNight(Player player) {
		PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.SPEED, 20*3, 0, false, false));
	}
}