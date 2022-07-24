package fr.supercomete.head.role.content.DWUHC;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.Triggers.Trigger_OnRoletime;
import fr.supercomete.head.role.Triggers.Trigger_WhileAnyTime;
import fr.supercomete.head.role.RoleModifier.Companion;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;
import fr.supercomete.head.role.RoleState.InfectedRoleState;
import fr.supercomete.head.role.RoleState.RoleStateTypes;
import fr.supercomete.nbthandler.NbtTagHandler;

public final class Bill_Potts extends DWRole implements Companion,HasAdditionalInfo,Trigger_WhileAnyTime,Trigger_OnRoletime {
	public CoolDown infinitePotionCoolDown = new CoolDown(0, 90);
	public Bill_Potts(UUID owner) {
		super(owner);
	}

	
	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("Vous connaissez le nom du Docteur.","Vous avez 3 potions jetable de soin a l'annonce des rôles.","Vous avez une potion de soin jetable utilisable toute les 1m30s."
				,"Vous avez "+ChatColor.GREEN+"Nofall§7."," A la mort du Docteur vous basculerez dans le camp des "+Camps.EnnemiDoctorCamp.getColoredName()+"§7. Vous serez considéré comme transformé et obtiendrez l'effet force pendant la nuit. ");
	}
	@Override
	public Camps getDefaultCamp() {
		return Camps.DoctorCamp;
	}

	@Override
	public String askName() {
		return "Bill Potts";
	}

	@Override
	public ItemStack[] askItemStackgiven() {
		@SuppressWarnings("deprecation")
		ItemStack stack = new Potion(PotionType.INSTANT_HEAL, 1, true).toItemStack(1);
		stack = NbtTagHandler.createItemStackWithUUIDTag(stack,10);
		ItemMeta im = stack.getItemMeta();
		im.setDisplayName("§4"+"Soins");
		stack.setItemMeta(im);
		return new ItemStack[]{InventoryUtils.getBillPottsPotion(),stack};
	}

	@Override
	public boolean AskIfUnique() {
		return true;
	}

	@Override
	public String AskHeadTag() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjAzZTRmMTM3ZjJjNDY1ZGM2ODMxNzU2MjM2YjRjOTI3MTY2OWY0YmQwYjY1ODQ4ZTY1NWIzYzA1ZTZmNiJ9fX0=";
	}

	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Humain};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Potion utilisable: "+Main.getCheckMark(infinitePotionCoolDown.isAbleToUse())};
    }

    @Override
	public String[] getAdditionnalInfo() {
		return new String[] {"Le Docteur est "+RoleHandler.FormalizedGetWhoHaveRole(TheDoctor.class)};
	}
	@Override
	public void WhileAnyTime(Player player) {
		if(RoleHandler.getWhoHaveRole(TheDoctor.class)==null){
			if(!this.hasRoleState(RoleStateTypes.Infected)) {
				this.addRoleState(new InfectedRoleState(RoleStateTypes.Infected));
				this.setCamp(Camps.EnnemiDoctorCamp);
				player.sendMessage(Main.UHCTypo+"§cLe Docteur est mort, vous passez donc dans le camp des "+Camps.EnnemiDoctorCamp.getColoredName());
			}
		}
		
	}


	@Override
	public void onRoleTime(Player player) {
		this.addBonus(new Bonus(BonusType.NoFall,1));
		
	}
}
