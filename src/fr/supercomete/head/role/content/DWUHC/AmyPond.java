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
import fr.supercomete.head.role.Triggers.Trigger_WhileDay;
import fr.supercomete.head.role.RoleModifier.Companion;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;

public final class AmyPond extends DWRole implements Companion,HasAdditionalInfo,Trigger_WhileDay{
	
	public AmyPond(UUID owner) {
	    super(owner);
	}

	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("Vous avez le pseudo de Rory","Pendant le jour vous avez l'effet §cforce§7","§7Vous avez un chat privé disponible avec '/dw chat <Message>' qui vous permet de parler avec Rory");
	}

	@Override
	public Camps getDefaultCamp() {
		return Camps.DoctorCamp;
	}
	 
	@Override
	public String askName() {		 
		return "Amy Pond";
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
		return"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTE5YTU5ZjdkNjI1MzM5ZGEwZmI1NmU5NGZjZGY2Y2E5YWNlYjg1ZGJjOGM4ODNjNzNmMDM4ZTg5M2FmNDA0MSJ9fX0=";
	}

	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Humain};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Rory: "+RoleHandler.FormalizedGetWhoHaveRole(RoryWilliams.class)};
    }

    @Override
	public String[] getAdditionnalInfo() {
		return new String[] {"Le nom de Rory Williams est "+RoleHandler.FormalizedGetWhoHaveRole(RoryWilliams.class),};
	}

	@Override
	public void WhileDay(Player player) {
		PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.INCREASE_DAMAGE,20*3,0,false,false));
	}
}