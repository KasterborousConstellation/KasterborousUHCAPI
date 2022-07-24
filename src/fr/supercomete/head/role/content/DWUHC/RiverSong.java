package fr.supercomete.head.role.content.DWUHC;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import fr.supercomete.head.core.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_OnDayTime;
import fr.supercomete.head.role.Triggers.Trigger_OnRoletime;
import fr.supercomete.head.role.RoleModifier.BonusHeart;
import fr.supercomete.head.role.RoleModifier.Companion;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;
import fr.supercomete.nbthandler.NbtTagHandler;

public final class RiverSong extends DWRole implements BonusHeart,Companion,Trigger_OnDayTime,HasAdditionalInfo,Trigger_OnRoletime{
	private boolean exchange=false;
	
	public RiverSong(UUID owner) {
		super(owner);
	}
	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("Vous avez 12 coeurs permanents","§7Vous obtenez un Manipulateur de Vortex. Vous pouvez, en le déchargant, activer un effet d'invisibilité. Vous pouvez désactiver votre effet via Manipulateur de Vortex."
				,"Il se rechargera à chaque début d'épisode si il est déchargé"
				," §7Vous pouvez avec la commande '/dw echange', échanger 5 coeurs permanent obtenir le pseudo du Docteur et de Amy Pond."
				);
	}
	@Override
	public Camps getDefaultCamp() {
		return Camps.DoctorCamp;
	}
	@Override
	public String askName() {
		return "River Song";
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzNjNDg4ZTI2N2U5NzQxMmVmYTM5MjZhMzQ1NzVlMzM2YzgyZTVjZjY1MzdiNTdhMjQ1Y2Y2NjNiZmExYzYifX19";
	}
	@Override
	public int getHPBonus() {
		return 4;
	}
	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.TimeTraveller};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Echange: "+ Main.getCheckMark(exchange)};
    }

    @Override
	public void onDayTime(Player player) {
		int id = 0;
		for(ItemStack it:player.getInventory()) {
			if(it!=null) {
				if(NbtTagHandler.hasUUIDTAG(it)) {
					if(NbtTagHandler.getUUIDTAG(it)==2) {
						if(NbtTagHandler.hasAnyTAG(it, "VortexState")) {
							if((int)NbtTagHandler.getAnyTag(it, "VortexState")==0) {
								player.getInventory().setItem(id,NbtTagHandler.addAnyTag(NbtTagHandler.createItemStackWithUUIDTag((InventoryUtils.createColorItem(Material.INK_SACK, "§bManipulateur de Vortex [§aChargé§b]", 1, (short)14)),2),"VortexState", 1));
								break;
							}
						}
					}                            
				}
			}
			id++;
		}
	}
	public boolean isExchange() {
		return exchange;
	}
	public void setExchange(boolean exchange) {
		this.exchange = exchange;
	}
	@Override
	public String[] getAdditionnalInfo() {
		String[]strings;
		if(exchange) {
			strings=new String[] {"§7Le pseudo du Docteur est "+RoleHandler.FormalizedGetWhoHaveRole(TheDoctor.class),"§7Le pseudo de Amy Pond est "+RoleHandler.FormalizedGetWhoHaveRole(AmyPond.class)};
		}else {
			strings=new String[] {};
		}
		return strings;
	}
	@Override
	public void onRoleTime(Player player) {
		ItemStack item2=NbtTagHandler.addAnyTag(NbtTagHandler.createItemStackWithUUIDTag((InventoryUtils.createColorItem(Material.INK_SACK, "§bManipulateur de Vortex [§aChargé§b]", 1, (short)14)),2),"VortexState", 1);
		InventoryUtils.addsafelyitem(player, item2);
		
	}
}