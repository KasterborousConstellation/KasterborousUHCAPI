package fr.supercomete.head.role.content.DWUHC;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_OnRoletime;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;
import fr.supercomete.nbthandler.NbtTagHandler;

public final class Supreme_Dalek extends DWRole implements HasAdditionalInfo,Trigger_OnRoletime{
	public int PE=0;
	public Supreme_Dalek(UUID owner) {
		super(owner);
	}
	
	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList(
				"Chaque personne que vous heurtez avec une épée vous fait gagnez 1 PT par coup.",
				"Ils peuvent être échanger, avec un objet qui vous ai donné, contre des effets. Soit force pendant 1m20, soit vitesse pendant 1m40s, soit résistance pendant 40s. Un effet coute 20PE. Chaque effet coute 2 fois plus cher pour chaque effet que le joueur possède (sans compter les effets: Regeneration, Absorption, Vision Nocturne)."
				,"Vous avez la liste des autres Daleks."
				,"Vous avez la possibilité comme tout les membres du Camp des Ennemis du Docteur, de détruire les clefs contenues dans le TARDIS en restant proche de celui-ci."
				
				
				,"§aPE: §r"+PE
				);
	}
	@Override
	public Camps getDefaultCamp() {
		return Camps.EnnemiDoctorCamp;
	}
	@Override
	public String askName() {
		return "Dalek Supreme";
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGVkYzFjNzc2ZDRhZWFmYjc1Y2I4YjkzOGFmODllMjA5MDJkODY4NGI3NDJjNmE4Y2M3Y2E5MjE5N2FiN2IifX19";
	}
	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Clone};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Points d'énergie: "+getPE()};
    }

    @Override
	public String[] getAdditionnalInfo() {
		return new String[] {"§cListe des autres Daleks: "+RoleHandler.getDalekList()};
	}
	@Override
	public void onRoleTime(Player player) {
		ItemStack item4 = NbtTagHandler.createItemStackWithUUIDTag(InventoryUtils.getItem(Material.IRON_INGOT, "§4Echange d'énergie", Main.SplitCorrectlyString("Cet objet vous permet d'échanger vos PE contre des effets.", 40, "§7")), 4);
		InventoryUtils.addsafelyitem(player, item4);
		
	}
	public int getPE() {
		return PE;
	}
	public void setPE(int pE) {
		PE = pE;
	}
	public void addPe(int pe) {
		PE=PE+pe;
	}
}