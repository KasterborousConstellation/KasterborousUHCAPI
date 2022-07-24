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
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_WhileNight;
import fr.supercomete.head.role.RoleModifier.BonusHeart;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;

public final class DalekStrategic extends DWRole implements BonusHeart ,Trigger_WhileNight,HasAdditionalInfo{
	
	public DalekStrategic(UUID owner) {
		super(owner);
	}


	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("Vous avez 11♥ permanents.","§7Vous obtenez §bvitesse§7 pendant la nuit.","§7Vous obtenez §cforce§7 pendant la nuit.",
				"Vous avez la liste des autres Daleks.",
				"Vous avez la possibilité comme tout les membres du Camp des Ennemis du Docteur, de détruire les clefs contenues dans le TARDIS en restant proche de celui-ci."
				
				);
	}

	@Override
	public Camps getDefaultCamp() {
		return Camps.EnnemiDoctorCamp;
	}

	@Override
	public String askName() {
		return "Dalek Strategique";
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg2NGY1YjRhZjMyNzAyNzNmZGM3YmMzMjhjN2MyYWI2MDQ4MjhmZTg2MmUzNTRlNmI0YjRkZjY3ZjJiNGZiYyJ9fX0=";
	}

	@Override
	public int getHPBonus() {
		return 2;
	}

	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Clone};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[0];
    }

    @Override
	public String[] getAdditionnalInfo() {
		return new String[] {"§cListe des autres Daleks: "+RoleHandler.getDalekList()};
	}
	
	@Override
	public void WhileNight(Player player) {
		PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.SPEED, 20*3, 0, false, false));
		PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*3, 0, false, false));
	}
}
