package fr.supercomete.head.role.content.DWUHC;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import fr.supercomete.head.role.CommandUse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_WhileDay;
public final class DannyPink extends DWRole implements Trigger_WhileDay{
	public CoolDown cooldown = new CoolDown(3, 60*5);
	public CommandUse commandUse = new CommandUse("/dw spectate");
	public DannyPink(UUID owner) {
		super(owner);
	}
	@Override
	public String askName() {
		return "Danny Pink";
	}
	@Override
	public Camps getDefaultCamp() {
		return Camps.DoctorCamp;
	}
	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("§7Vous pouvez regarder l'inventaire d'un joueur avec la commande '/dw spectate <Joueur>'. Dans cet inventaire tout les objets sont ceux du joueur fouillé sauf le CyberTraceur qui lui est remplacé par de la cobblestone. Vous pouvez utiliser cette capacité toute les 5mins."+cooldown.formalizedUtilisation(),
				"Vous avez l'effet §bvitesse§7 pendant le jour.",
				"§4Attention vous apparaisez dans la liste des Cybermans, cependant vous n'en faite pas parti.§7"
				);
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmIwN2I0ODYxZjcwODdjODJjMWI5YmM4N2ZjYjc0ZmQ2MjNkNTYwNGZmZGU4Y2M1NDQwMzNmMWNmOGY2NjNmZSJ9fX0=";
	}
	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Humain};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{commandUse.generate()};
    }

    @Override
	public void WhileDay(Player player) {
		PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.SPEED, 20*3, 0, false, false));
	}
}