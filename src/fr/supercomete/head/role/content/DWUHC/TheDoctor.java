package fr.supercomete.head.role.content.DWUHC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import fr.supercomete.head.PlayerUtils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_OnDayTime;
import fr.supercomete.head.role.Triggers.Trigger_OnOwnerDeath;
import fr.supercomete.head.role.RoleModifier.BonusHeart;
import fr.supercomete.head.role.RoleModifier.Companion;
import fr.supercomete.head.role.RoleModifier.PreAnnouncementExecute;

public final class TheDoctor extends DWRole implements BonusHeart,PreAnnouncementExecute,Trigger_OnDayTime,Trigger_OnOwnerDeath{
	private UUID companion;
	public int pts =12;
	public TheDoctor(UUID owner) {
		super(owner);
	}
	@Override
	public List<String> askRoleInfo() {
		String color="§b";
		String str=color;
		for(Role role: RoleHandler.getRoleList().values()){
			if(role instanceof Companion) {
				str+=color+role.getName()+",";					
			}
		}
		str = str.substring(str.length()-1);
		return Arrays.asList("Vous possèdez 12 coeurs permanents."," Vous avez §6"+pts+"§7pts de régénération.",
				"§7Après avoir perdu 3 coeurs (ou plus), vous pouvez utiliser la commande '/dw energy' pour vous régénérer 3 coeurs. Vous perdrez 2 points de régénération pour chaque utilisation. "
				,"§7Si il vous reste au moins 12 points de de régénération, vous pouvez utiliser à votre mort 12 d'entre eux pour échapper à la mort."
				,"§7Vous obtenez le nom d'un de vos compagnons parmi "+str+". §7Le nom de de votre compagnon est "+ PlayerUtility.getNameByUUID(companion)
				," §7Chaque jour vous regagnez 1 points de régénération."
				);
	}

	
	public UUID getCompanion() {
		return companion;
	}

	public void setCompanion(UUID companion) {
		this.companion = companion;
	}

	@Override
	public Camps getDefaultCamp() {
		return Camps.DoctorCamp;
	}

	@Override
	public String askName() {
		return "Le Docteur";
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTJkYmYyN2U4NjcxNTZlMzUxMzRjMWUzYTE3YWQyMmQ0MjMyOTJjODYwOTcyNmVmZWU1N2E3MzljYTUyOTgzMCJ9fX0=";
	}

	@Override
	public int getHPBonus() {
		return 4;
	}

	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.TimeTraveller};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Compagnion: "+PlayerUtility.getNameByUUID(companion),"Points de régénération: "+pts};
    }

    private UUID setRandomDoctorCompanion() {
		ArrayList<Role>roletlist=new ArrayList<Role>();
		for(Role r:RoleHandler.getRoleList().values()) {
			if(r instanceof Companion)
			roletlist.add(r);
		}
		
		if(roletlist.size()==0) {
			return null;
		}
		return roletlist.get((roletlist.size()!=1)?(new Random().nextInt(roletlist.size())):0).getOwner();
	}
	@Override
	public void PreAnnouncement() {
		this.setCompanion(setRandomDoctorCompanion());
	}
	@Override
	public void onDayTime(Player player) {
		pts+=1;
	}
	@Override
	public boolean onOwnerDeath(Player player, Player damager) {
		if (pts>=12) {
			pts-=12;
			return true;
		}
		return false;
	}
}
