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
import fr.supercomete.head.role.RoleModifier.Companion;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;

public final class RoryWilliams extends DWRole implements Companion ,Trigger_WhileNight,HasAdditionalInfo{

	public RoryWilliams(UUID owner) {
		super(owner);
	}

	
	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("Vous avez le pseudo de Rory Williams.","Vous avez l'effet résistance pendant la nuit.","§7Vous avez un chat privé disponible avec '/dw chat <Message>'. Ce chat privé vous permet de parler à Amy en privé.");
	}

	@Override
	public Camps getDefaultCamp() {
		return Camps.DoctorCamp;
	}

	@Override
	public String askName() {
		return "Rory Williams";
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTlkNDFlZjUzY2U5ZTI4N2YxNjUxOGYyMWE4ZDNkMzcxOTA4ZWIzM2Q0ZDRiYWM5YTU4OGU0MDEzNDNkIn19fQ==";
	}

	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Humain};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Amy: "+RoleHandler.FormalizedGetWhoHaveRole(AmyPond.class)};
    }

    @Override
	public void WhileNight(Player player) {
		PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*3, 0, false, false));
		
	}

	@Override
	public String[] getAdditionnalInfo() {
		return new String[] {"§7Amy Pond est: "+RoleHandler.FormalizedGetWhoHaveRole(AmyPond.class)};
	}
}
