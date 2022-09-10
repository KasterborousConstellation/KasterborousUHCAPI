package fr.supercomete.head.role.content.DWUHC;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.RoleModifier.PreAnnouncementExecute;
import fr.supercomete.head.role.Status;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;



public final class Jenny_Flint extends DWRole implements PreAnnouncementExecute {
	private HashMap<UUID, Integer> percentmap;
	public int amount=0;
	public Jenny_Flint(UUID owner) {
		super(owner);
		setPercentmap(new HashMap<UUID, Integer>());
		addBonus(new Bonus(BonusType.Force, 0));
	}

	public void setPercentMapping() {
		HashMap<UUID, Integer> inti = new HashMap<UUID, Integer>();
		for (UUID uu : Main.getPlayerlist()) {
			inti.put(uu, 0);
		}
		inti.remove(getOwner());
		setPercentmap(inti);
	}

	public HashMap<UUID, Integer> getPercentmap() {
		return percentmap;
	}

	public void setPercentmap(HashMap<UUID, Integer> percentmap) {
		this.percentmap = percentmap;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("Vous avez une pioche en diamant efficacité 3 et unbreakable à l'annonce des rôles.",
				"§7Une barre de progression s'affiche au dessus de la tête de tout les joueurs. En restant a moins de 10blocs de distance d'un joueur la barre de progression s'affiche et progresse. Quand elle atteint 100% vous saurez si ce joueur est §aVastra§7 ou non.",
				"§7Pour chaque joueur que vous avez fait monter à 100%. Vous gagnez 1% de force. §4Force §4Actuelle: §a"+(100+amount)+"%"
				);
	}
	@Override
	public Camps getDefaultCamp() {
		return Camps.DoctorCamp;
	}

	@Override
	public String askName() {
		return "Jenny Flint";
	}

	@Override
	public ItemStack[] askItemStackgiven() {
		ItemStack it = new ItemStack(Material.DIAMOND_PICKAXE);
		it.addEnchantment(Enchantment.DIG_SPEED, 3);
		ItemMeta meta = it.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.spigot().setUnbreakable(true);
		it.setItemMeta(meta);
		return new ItemStack[]{it};
	}

	@Override
	public boolean AskIfUnique() {
		return true;
	}

	@Override
	public String AskHeadTag() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY0NDhhODVlZTg2NGNkMTgyZThiOGQ3ZDBlYjNlNmQ1NTViOGMxNGFjNDJjZTVjYWE1OTE1NzMzOTkxMjUifX19";
	}

	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Humain};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[0];
    }

    @Override
	public void PreAnnouncement() {
		this.setPercentMapping();
	}
}
