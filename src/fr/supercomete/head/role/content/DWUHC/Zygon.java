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
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_WhileAnyTime;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;

public final class Zygon extends DWRole implements HasAdditionalInfo,Trigger_WhileAnyTime{
	private UUID uuid;
	private boolean stole=false;
	
	public Zygon(UUID owner) {
		super(owner);
		this.setUuid(owner);
	}
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	@Override
	public String[] getAdditionnalInfo() {
		return new String[] {("    §7Votre modèle est "+((this.getUuid()==this.getOwner())?"§6Aucun":"§a"+Bukkit.getPlayer(this.getUuid()).getName()))};
	}
	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("§7Vous avez l'effet §frésistance§7 permanent.","§7Vous pouvez cibler une personne qui deviendra votre modèle avec la commande '/dw target'. Vous rejoindrez le camp de votre modèle. Si le camp de votre modèle change, le votre aussi. Si votre modèle vient à mourir vous retournerez dans le Camp Neutre. ");
	}
	@Override
	public Camps getDefaultCamp() {
		return Camps.Neutral;
	}
	@Override
	public String askName() {
		return "Zygon";
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFkN2M4MTZmYzhjNjM2ZDdmNTBhOTNhMGJhN2FhZWZmMDZjOTZhNTYxNjQ1ZTllYjFiZWYzOTE2NTVjNTMxIn19fQ==";
	}
	@Override
	public Status[] AskStatus() {
		return new Status[] {};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Cible: "+PlayerUtility.getNameByUUID(uuid)};
    }

    @Override
	public void WhileAnyTime(Player player) {
		PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*3, 0, false, false));
	}
	public boolean isStole() {
		return stole;
	}
	public void setStole(boolean stole) {
		this.stole = stole;
	}

}
