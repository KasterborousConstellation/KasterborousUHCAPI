package fr.supercomete.head.role.content.DWUHC;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
import fr.supercomete.head.role.Triggers.Trigger_OnKill;
import fr.supercomete.head.role.Triggers.Trigger_OnRoletime;
import fr.supercomete.head.role.Triggers.Trigger_WhileDay;
import fr.supercomete.head.role.Triggers.Trigger_WhileNight;
import fr.supercomete.head.role.Triggers.Trigger_onNightTime;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;
import fr.supercomete.nbthandler.NbtTagHandler;

public final class CyberPlanner extends DWRole implements HasAdditionalInfo,Trigger_WhileDay,Trigger_WhileNight,Trigger_OnKill,Trigger_onNightTime,Trigger_OnRoletime {
	private boolean isactive=true;
	public UUID infected;
	public CyberPlanner(UUID owner) {
		super(owner);
		
	}


	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("§7Vous avez obtenu une §6houe§7 qui vous permet de transformer une personne en Cyberman. Cependant cette personne ne doit pas être purifié par Strax et doit faire partie du "+Camps.DoctorCamp.getColoredName()+"§7. Si il est infecté le joueur obtiendra l'effet force pendant la nuit §7 et passera dans le camp "+Camps.EnnemiDoctorCamp.getColor()+Camps.EnnemiDoctorCamp.getName(),
				"§7Vous avez l'effet §bvitesse§7 pendant le jour.",
				"§7Vous avez force 1 pendant la nuit. Cependant, si vous faites un kill, vous perdrez l'effet de force. Celui ci reprendra au début de la nuit suivante."
				,"Vous avez la liste des autres Cybermens."
				,"A un moment de la partie le Cyberium apparaitra. Si vous arrivez à en prendre possesion, vous obtiendrez force permanente et tout les Cybermens pourront suivre votre position via le traqueur du Cyberium.",
				"Vous avez la possibilité comme tout les membres du Camp des Ennemis du Docteur, de détruire les clefs contenues dans le TARDIS en restant proche de celui-ci."
				);
	}
	@Override
	public Camps getDefaultCamp() {
		return Camps.EnnemiDoctorCamp;
	}

	@Override
	public String askName() {
		return "Cyber-Planificateur";
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
	public String AskHeadTag(){
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODVjNGVmZmJhNGQ5OWY0MzczMTRjOGE4NzU1ODU2NzEzZmQ4NWRjZDE1YjM2OTBjNzQ5Y2UxZTQ0NDc0In19fQ==";
	}

	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Humain};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Force active: "+Main.getCheckMark(isactive),"Transformation: "+PlayerUtility.getNameByUUID(infected)};
    }

    @Override
	public String[] getAdditionnalInfo() {
		return new String[] {"§cListe des autres Cybermen: "+RoleHandler.getCyberManList()};
	}

	@Override
	public void WhileDay(Player player) {
		PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.SPEED, 20*4, 0, false, false));
	}
	public void WhileNight(Player player) {
		if(isactive) {
			PlayerUtility.addProperlyEffect(player,new PotionEffect(PotionEffectType.INCREASE_DAMAGE,40,0,false,false));
		}
	}

	@Override
	public void onNightTime(Player player) {
		isactive=true;
		player.sendMessage(Main.UHCTypo+"Vous avez récupéré votre effet de force");	
	}

	@Override
	public void onKill(Player player, Player killed) {
		isactive=false;
		player.sendMessage(Main.UHCTypo+"Vous avez perdu votre effet de force");
	}

	@Override
	public void onRoleTime(Player player) {
		ItemStack item3=NbtTagHandler.createItemStackWithUUIDTag(InventoryUtils.getItem(Material.GOLD_HOE, "§4Transformation", Arrays.asList("§7Tapez un joueur avec cet objet pour activer son effet")), 3);
		InventoryUtils.addsafelyitem(player, item3);
	}
}
