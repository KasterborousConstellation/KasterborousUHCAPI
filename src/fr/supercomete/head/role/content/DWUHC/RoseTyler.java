package fr.supercomete.head.role.content.DWUHC;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import fr.supercomete.head.core.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Camps;
import fr.supercomete.enums.Choice;
import fr.supercomete.head.GameUtils.GameMode.Modes.DWUHC;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.Triggers.Trigger_OnKill;
import fr.supercomete.head.role.Triggers.Trigger_WhileAnyTime;
import fr.supercomete.head.role.RoleModifier.Companion;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;

public final class RoseTyler extends DWRole implements Companion, Trigger_WhileAnyTime, Trigger_OnKill,HasAdditionalInfo {
	private boolean haskilled=false;
	public RoseTyler(UUID owner) {
		super(owner);
	}

	@Override
	public List<String> askRoleInfo() {
		
		if (this.getChoice() == Choice.None) {
			return Arrays.asList("");
		} else if (this.getChoice() == Choice.Humaine) {
			return Arrays.asList("Vous avez 15♥ permanent","Vous avez l'effet §bvitesse§7 permanent");
		} else if (this.getChoice() == Choice.BadWolf) {
			return Arrays.asList("Vous avez l'effet §frésistance§7 permanent","Si vous arrivez à tuer un dalek, vous obtiendrez 2♥ permanent supplémentaire, et le pseudo du Docteur.");
		} else {
			return Arrays.asList("");
		}
	}

	@Override
	public Camps getDefaultCamp() {
		return Camps.DoctorCamp;
	}

	@Override
	public String askName() {
		return "Rose Tyler";
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2M5Njg1MjhjYTY4ODRkZTFkYWM1NzU5MTNjOGQwMzM5Yjc0ZmYxZTY4MjI5ZmI4YzJiYzJkNzBjZmFkZGJjZiJ9fX0=";
	}

	@Override
	public Status[] AskStatus() {
		return new Status[] { Status.Humain };
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{(this.getChoice()==Choice.BadWolf)?"Kill sur un Dalek: "+ Main.getCheckMark(haskilled):""};
    }

    @Override
	public void WhileAnyTime(Player player) {
		if (this.getChoice() == Choice.Humaine) {
			PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.SPEED, 20 * 3, 0, false, false));
		} else if (this.getChoice() == Choice.BadWolf) {
			PlayerUtility.addProperlyEffect(player,
					new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 3, 0, false, false));
		}
	}

	@Override
	public void onKill(Player player, Player killed) {
		if (DWUHC.generateDalekRoleList().contains(RoleHandler.getRoleOf(killed).getClass())&& this.getChoice()==Choice.BadWolf) {
			Role r = RoleHandler.getRoleOf(player);
			haskilled=true;
			RoleHandler.DisplayRole(player);
			
			r.addBonus(new Bonus(BonusType.Heart, 4));
		}

	}

	@Override
	public String[] getAdditionnalInfo() {
		return new String[] {(haskilled)?"Le pseudo du Docteur est "+RoleHandler.FormalizedGetWhoHaveRole(TheDoctor.class):""};
	}
}
