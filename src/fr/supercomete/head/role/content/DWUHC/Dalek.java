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
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;

public final class Dalek extends DWRole implements Trigger_OnKill,HasAdditionalInfo,Trigger_WhileNight {
	private int kill=0;
    public Dalek(UUID owner) {
		super(owner);
	}


	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList(
				"Vous avez l'effet §cforce§7 pendant la nuit.",
				"§7Vous obtenez a l'annonce des rôles la liste des Daleks."
				," §7A chaque kill vous avez §c3%§7 de force bonus."
				,"Vous avez la possibilité comme tout les membres du Camp des Ennemis du Docteur, de détruire les clefs contenues dans le TARDIS en restant proche de celui-ci."
				);
	}

	@Override
	public Camps getDefaultCamp() {
		return Camps.EnnemiDoctorCamp;
	}

	@Override
	public String askName() {
		return "Dalek";
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM4NDY5MzI4NDMzYzk1NjZmYjJjMmUyZWNiZjFkZjY4ZWUxMjc0NjUzOWI1NGJmOTg3MjIwZGE3ZThhYiJ9fX0=";
	}

	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Clone};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Force bonus: §c"+(kill*3)+"%"};
    }

    @Override
	public void onKill(Player player, Player killed) {
        kill++;
		RoleHandler.getRoleOf(player).addBonus(new Bonus(BonusType.Force, 3));
		player.sendMessage(Main.UHCTypo + "§7Vous avez gagné §c3%§7 de force");
	}
	@Override
	public String[] getAdditionnalInfo() {
		return new String[] {"§cListe des autres Daleks: "+RoleHandler.getDalekList()};
	}

	@Override
	public void WhileNight(Player player) {
		PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*3, 0, false, false));
	}
	
}
