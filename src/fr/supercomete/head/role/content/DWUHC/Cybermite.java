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
import fr.supercomete.head.role.Triggers.Trigger_OnKill;
import fr.supercomete.head.role.Triggers.Trigger_WhileAnyTime;
import fr.supercomete.head.role.Triggers.Trigger_onNightTime;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;

public final class Cybermite extends DWRole implements Trigger_WhileAnyTime,HasAdditionalInfo,Trigger_OnKill,Trigger_onNightTime{
	private boolean isactive=true;
	public Cybermite(UUID owner) {
		super(owner);
	}


	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("§7Quand vous retirez votre armure, vous obtenez un effet §1d'invisibilité§7. Cependant vous obtiendrez aussi un effet de faiblesse."
				,"§7Vous avez force 1 pendant la nuit. Cependant, si vous faite un kill, vous perdrez l'effet de force. Celui ci reprendra au début de la nuit suivante.",
				"Vous avez la liste des autres Cybermen.",
				" A un moment de la partie le Cyberium apparaitra. Si vous arrivez à en prendre possesion, vous obtiendrez force permanente et tout les Cybermens pourront suivre votre position via le traqueur du Cyberium."
				,"Vous avez la possibilité comme tout les membres du Camp des Ennemis du Docteur, de détruire les clefs contenues dans le TARDIS en restant proche de celui-ci."
				);
	}
	@Override
	public Camps getDefaultCamp() {
		return Camps.EnnemiDoctorCamp;
	}


	@Override
	public String askName() {
		return "Cybermite";
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWUwN2FlNDY3MmE4MjYzNTU2MzE1YWYyOTgxMmQ5YWRlOWYzYzAxY2QzMTY0YjQ3OTg5NDY2NTRmOTkxNzY4NSJ9fX0=";
	}

	@Override
	public Status[] AskStatus() {
		return new Status[] {};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Force active: "+Main.getCheckMark(isactive)};
    }

    @Override
	public void WhileAnyTime(Player player) {
		if(player.getInventory().getHelmet()==null &&player.getInventory().getChestplate()==null&&player.getInventory().getLeggings()==null&&player.getInventory().getBoots()==null) {
			PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.INVISIBILITY, 20*4, 0, false, false));
			PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.WEAKNESS, 20*4, 0, false, false));

		}
	}

	@Override
	public String[] getAdditionnalInfo() {
		return new String[] {"§cListe des autres Cybermen: "+RoleHandler.getCyberManList()};
	}


	@Override
	public void onKill(Player player, Player killed) {
		isactive=false;
		player.sendMessage(Main.UHCTypo+"Vous avez perdu votre effet de force");
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
}
