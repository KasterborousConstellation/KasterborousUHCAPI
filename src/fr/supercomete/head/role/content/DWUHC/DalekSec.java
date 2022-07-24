package fr.supercomete.head.role.content.DWUHC;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
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
import fr.supercomete.head.role.Triggers.Trigger_OnOwnerDeath;
import fr.supercomete.head.role.Triggers.Trigger_WhileAnyTime;
import fr.supercomete.head.role.Triggers.Trigger_WhileNight;

public final class DalekSec extends DWRole implements Trigger_WhileNight,Trigger_OnOwnerDeath,Trigger_WhileAnyTime{
	private boolean respawned=false;
	public DalekSec(UUID owner) {
		super(owner);
	}

	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("§7Vous obtenez l'effet vitesse quand un autre membre du Camp des Ennemis du Docteur se trouve dans un rayon de 5 blocs. (Attention le rôle §rRusty§7 passe pour un membre du Camp des Ennemis du Docteur). De plus, une personne est considérée comme membre du Camp des Ennemis du Docteur, si son camp de départ, avant choix et autres événements, était Camp des Ennemis du Docteur."
				,"§7Si Le Docteur vous tue, vous serez réssucité et vous passerez dans le Camp du Docteur."
				,"Vous avez l'effet §cforce§7 pendant la nuit."
				,"Vous avez la possibilité comme tout les membres du Camp des Ennemis du Docteur, de détruire les clefs contenues dans le TARDIS en restant proche de celui-ci."
				);
	}

	@Override
	public Camps getDefaultCamp() {
		return Camps.EnnemiDoctorCamp;
	}

	@Override
	public String askName() {
		return "Dalek Sec";
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODc1YmU4NjViNjZiNDBlZGE5ZGJmYjRmZTk0Yjc4ZWJmZWUxZjgyZmNhMmVlOWZlNDczZTVkYjY3ZTgyNDNkIn19fQ==";
	}
	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Clone};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Tuée par le Docteur: "+Main.getCheckMark(respawned)};
    }

    @Override
	public void WhileNight(Player player) {
		PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 0, false, false));
	}
	@Override
	public boolean onOwnerDeath(Player player, Player damager) {
		if(RoleHandler.getRoleOf(damager)instanceof TheDoctor&&!respawned) {
			RoleHandler.getRoleOf(player).setCamp(Camps.DoctorCamp);
			respawned=true;
			player.sendMessage(Main.UHCTypo+"§aLe Docteur vous a tué, vous devez maintenant gagner avec le Camp du Docteur");
			return true;
		}
		return false;
	}
	@Override
	public void WhileAnyTime(Player player) {
		for(UUID uu:RoleHandler.getRoleList().keySet()) {
			if(uu.equals(player.getUniqueId()))continue;
			double dist = Bukkit.getPlayer(uu).getLocation().distance(player.getLocation());
			if(dist<=10) {
				if(RoleHandler.getRoleOf(uu).getDefaultCamp()==Camps.EnnemiDoctorCamp || RoleHandler.getRoleOf(uu)instanceof Rusty) {
					player.removePotionEffect(PotionEffectType.SPEED);
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*5, 0, false, false));
				}
			}
		}
	}
}