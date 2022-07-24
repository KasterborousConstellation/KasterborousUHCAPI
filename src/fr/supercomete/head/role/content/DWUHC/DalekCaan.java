package fr.supercomete.head.role.content.DWUHC;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_OnKill;
import fr.supercomete.head.role.Triggers.Trigger_WhileNight;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;
import fr.supercomete.head.role.RoleModifier.PreAnnouncementExecute;
import fr.supercomete.nbthandler.NbtTagHandler;
public final class DalekCaan extends DWRole implements HasAdditionalInfo,PreAnnouncementExecute,Trigger_WhileNight,Trigger_OnKill{
	private UUID target;
	private boolean hasKilled= false;
	public DalekCaan(UUID owner) {
		super(owner);		
	}
	public UUID getTarget() {
		return target;
	}
	public void setTarget(UUID target) {
		this.target = target;
	}
	@Override
	public String[] getAdditionnalInfo() {
		return new String[] {("§7Votre cible est "+((this.getTarget()==this.getOwner())?"§6Aucun":"§a"+PlayerUtility.getNameByUUID(target))),"§cListe des autres Daleks: "+RoleHandler.getDalekList()};
		
	}
	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("§7Il vous a été attribué une cible que vous devez tuer, si cela est fait vous obtenez un activable: Fracture temporelle. Celui-ci vous donnes à son utilisation résistance et vitesse 1 pendant 10min.","La cible est forcement un de vos ennemis à l'annonce des rôles","A l'activation de votre Fracture Temporelle, tout le monde obtiendra vos coordonnée."
				,"Vous avez l'effet §cforce§7 pendant la nuit."
				," Vous avez la liste des autres Daleks.",
				"Vous avez la possibilité comme tout les membres du Camp des Ennemis du Docteur, de détruire les clefs contenues dans le TARDIS en restant proche de celui-ci."
				);
	}
	@Override
	public Camps getDefaultCamp() {
		return Camps.EnnemiDoctorCamp;
	}
	@Override
	public String askName() {
		return "Dalek Caan";
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTZmYTBlNjI2NGIzNWFmZjg5NzQ0ZmIxNGNmZTkzMTUzNWVkNzk4MjE0ZGRiNWNlNjEzZjI3YWQ1NTUwNTFlMCJ9fX0=";
	}
	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.TimeTraveller,Status.Clone};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Cible: "+((this.getTarget()==this.getOwner())?"§6Aucun":"§a"+PlayerUtility.getNameByUUID(target)),"Cible tuée: "+Main.getCheckMark(hasKilled)};
    }

    @Override
	public void PreAnnouncement() {
		ArrayList<UUID> arr = new ArrayList<>();
		for(final Role role : RoleHandler.getRoleList().values()) {
			if(role.getCamp()==Camps.DoctorCamp) {
				arr.add(role.getOwner());
			}
		}
		UUID uuid = (arr.size()>0)?arr.get(new Random().nextInt(arr.size())):getOwner();
		setTarget(uuid);
		
	}
	@Override
	public void WhileNight(Player player) {
		PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*3, 0, false, false));
	}
	@Override
	public void onKill(Player player, Player killed) {
		if(RoleHandler.getRoleOf(player) instanceof DalekCaan r) {
            if(r.getTarget().equals(killed.getUniqueId())) {
                hasKilled=true;
				ItemStack item_caan = InventoryUtils.getItem(Material.BLAZE_POWDER,"§6Fracture temporelle" ,Main.SplitCorrectlyString("Cet objet est utilisable une seule fois, et vous donnes un effet de résistance et de vitesse pendant 1m30s. Cet objet révèle votre position.", 40, "§7"));
				item_caan = NbtTagHandler.createItemStackWithUUIDTag(item_caan,6);
				InventoryUtils.addsafelyitem(player, item_caan);
			}
		}
	}
}