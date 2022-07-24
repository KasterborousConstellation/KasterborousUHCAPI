package fr.supercomete.head.role.content.DWUHC;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import fr.supercomete.head.core.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_OnRoletime;
import fr.supercomete.head.role.Triggers.Trigger_WhileAnyTime;
import fr.supercomete.nbthandler.NbtTagHandler;

public final class Vastra extends DWRole implements Trigger_WhileAnyTime,Trigger_OnRoletime {
	public CoolDown gap = new CoolDown(0, 5*60);
	public boolean instant=false;
	public Vastra(UUID owner) {
		super(owner);
	}


	@Override
	public List<String>askRoleInfo() {
		return Arrays.asList(
				"Vous avez l'effet §bvitesse§7 permanent. ",
				"§7Vous obtenez un activable qui vous donne l'effet vitesse 3,résistance et faiblesse pendant 1min. Cet activable vous redonnera toute votre vie. "
				,"§7Vous pouvez toute les §65min§7 avec la commande '/dw gap <Joueur>', connaître le nombre de pomme d'or d'un joueur."
				);
	}

	@Override
	public Camps getDefaultCamp() {
		return Camps.DoctorCamp;
	}

	@Override
	public String askName() {
		return "Vastra";
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTliMzkzMjBlNWI0YTU5ZTcxMTZlNWI3MjQyNWZjOTExNDI2NmZmZjYzODU0NGUzYzNkZGI1ZTM3NWNlZjMyIn19fQ==";
	}

	@Override
	public Status[] AskStatus() {
		return new Status[] {};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Vitesse instantanée utilisée: "+Main.getCheckMark(instant),"/dw gap Utilisable: "+ Main.getCheckMark(gap.isAbleToUse())};
    }

    @Override
	public void WhileAnyTime(Player player) {
		PlayerUtility.addProperlyEffect(player,new PotionEffect(PotionEffectType.SPEED,40,0,false,false));
	}
	@Override
	public void onRoleTime(Player player) {
		ItemStack item=NbtTagHandler.createItemStackWithUUIDTag((InventoryUtils.getItem(Material.FEATHER, "§rBoost de vitesse",null)),1);
		InventoryUtils.addsafelyitem(player, item);		
	}
	
}