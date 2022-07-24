package fr.supercomete.head.role.content.DWUHC;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.Triggers.Trigger_OnKill;
import fr.supercomete.head.role.Triggers.Trigger_WhileNight;
import fr.supercomete.head.role.Triggers.Trigger_onNightTime;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;

public final class Cyberman extends DWRole implements Trigger_OnKill,HasAdditionalInfo,Trigger_WhileNight,Trigger_onNightTime {
	private boolean isactive=true;
	private int kill=0;
	public Cyberman(UUID owner) {
		super(owner);
	}

	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("§7Vous avez force 1 pendant la nuit. Cependant, si vous faite un kill, vous perdrez l'effet de force. Celui ci reprendra au début de la nuit suivante.","A chacun de vos kill vous obtenez §d3%§7 de §bvitesse§7."
				,"Vous avez la liste des autres Cybermans.","A un moment de la partie le Cyberium apparaitra. Si vous arrivez à en prendre possesion, vous obtiendrez force permanente et tout les Cybermens pourront suivre votre position via le traqueur du Cyberium.",
				"Vous avez la possibilité comme tout les membres du Camp des Ennemis du Docteur, de détruire les clefs contenues dans le TARDIS en restant proche de celui-ci.");
	}

	@Override
	public Camps getDefaultCamp() {
		return Camps.EnnemiDoctorCamp;
	}

	@Override
	public String askName() {
		return "Cyberman";
	}

	@Override
	public ItemStack[] askItemStackgiven() {
		return null;
	}

	@Override
	public boolean AskIfUnique() {
		return false;
	}

	@Override
	public String AskHeadTag() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzIxNmQ4YmZhZWQ5OWQxMGQ5MTc0OTE1NGE0ODExOGE3NTRkOTg2ZWIyYzBjZDc1OWY0NTk0MDliMTUyYjNlZSJ9fX0=";
	}

	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Humain};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Vitesse bonus: §b"+(kill*3)+"%","Force active: "+Main.getCheckMark(isactive)};
    }

    @Override
	public void onKill(Player player, Player killed) {
	    kill++;
		RoleHandler.getRoleOf(player).addBonus(new Bonus(BonusType.Speed, 3));
		player.sendMessage(Main.UHCTypo + "§7Vous avez gagné §c3%§7 de vitesse");
		isactive=false;
		player.sendMessage(Main.UHCTypo+"Vous avez perdu votre effet de force");
		
	}

	@Override
	public String[] getAdditionnalInfo() {
		return new String[] {"§cListe des autres Cybermen: "+RoleHandler.getCyberManList()};
	}
	@Override
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
}
