package fr.supercomete.head.role.content.DWUHC;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import fr.supercomete.head.PlayerUtils.PlayerUtility;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_WhileAnyTime;
public final class Pting extends DWRole implements Trigger_WhileAnyTime {
	private int nutrition=3000;
	public CoolDown peopleeat = new CoolDown(0, 3*60);
	public Pting(UUID owner){
		super(owner);
	}
	@Override
	public String askName(){
		return"Pting";
	}
	@Override
	public Camps getDefaultCamp(){
		return Camps.Neutral;
	}
	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("Vous avez à l'annonce des rôles 3000pts de nutrition et votre barre de nutrition se trouve au dessus de votre barre d'action.","Toute les secondes vous perdez 1pts d'énergie.",
				"§7Quand vous tuez quelqu'un vous obtenez 1850pts de nutrition",
				"Vous pouvez utiliser la commande '/dw eat <Joueur>', cela aura pour effet de vous ouvrir l'inventaire de ce joueur, ensuite vous pouvez manger un stack de bloc de l'inventaire de ce joueur. Manger des blocs vous donnera 650pts. La commande a un cooldown de 3min et si vous fermer l'inventaire sans manger de bloc, vous devrez attendre 3min pour re-manger."
				,"\"§7Vos points vous rapporte des effets. En dessous de 1500pts: Faiblesse I. Au dessus de 3000pts: Force I. Au dessus de 5000pts: Force I, Vitesse I. Au dessus de 7500pts: Force I, Vitesse I, Résistance I. Au dessus de 15000pts: Force II, Vitesse I, Résistance I"
				);
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDNkNDZjZmI3ZTQwZGJkMmVjYjE1NzhkMzFmNGQyMTI2MjVjOGVlN2VjYTQ0YWMyNTlmMzQ4MTVmMDRhNzE5ZCJ9fX0=";
	}
	public int getNutrition() {
		return nutrition;
	}
	public void setNutrition(int nutrition) {
		this.nutrition = nutrition;
	}
	@Override
	public Status[] AskStatus() {
		return new Status[] {};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Nutrition: "+nutrition};
    }

    @Override
	public void WhileAnyTime(Player player) {
		final Pting role = (Pting)RoleHandler.getRoleOf(player);
		role.setNutrition((role.getNutrition()>1)?(role.getNutrition()-1):0);
        PlayerUtility.sendActionbar(player, "§aNutrition: §r"+ role.getNutrition()+"pts");
		if(this.getNutrition() <= 1500) {
			player.removePotionEffect(PotionEffectType.WEAKNESS);
			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*6, 0, false, false));
		} 
		if(this.getNutrition() >= 3000 && this.getNutrition()< 15000){
			player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*6, 0, false, false));
		}
		if(this.getNutrition() >= 5000) {
			player.removePotionEffect(PotionEffectType.SPEED);
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*6, 0, false, false));
		}
		if(this.getNutrition() >= 7500) {
			player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*6, 0, false, false));
		}
		if(this.getNutrition() >= 15000) {
			player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*3, 1, false, false));
		}
	}
}