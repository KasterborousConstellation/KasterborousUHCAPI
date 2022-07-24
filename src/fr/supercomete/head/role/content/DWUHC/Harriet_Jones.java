package fr.supercomete.head.role.content.DWUHC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.Triggers.Trigger_OnAnyKill;
import fr.supercomete.head.role.Triggers.Trigger_OnHitPlayer;
import fr.supercomete.head.role.Triggers.Trigger_OnKill;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;

public final class Harriet_Jones extends DWRole implements HasAdditionalInfo,Trigger_OnHitPlayer,Trigger_OnAnyKill,Trigger_OnKill {
	private ArrayList<UUID> targettedplayers = new ArrayList<UUID>();
	public CoolDown sources = new CoolDown(3, 0);
	public Harriet_Jones(UUID owner) {
		super(owner);
		targettedplayers = new ArrayList<UUID>();
		this.addBonus(new Bonus(BonusType.Force, 0));
	}
	public void addTargetPlayer(Player player) {
		targettedplayers.add(player.getUniqueId());
	}
	public void addTargetPlayer(UUID player) {
		targettedplayers.add(player);
	}
	public ArrayList<UUID> getTargettedplayers() {
		return targettedplayers;
	}
	public void setTargettedplayers(ArrayList<UUID> targettedplayers) {
		this.targettedplayers = targettedplayers;
	}
	public ArrayList<Info> info = new ArrayList<>();
	@Override
	public String[] getAdditionnalInfo() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("Sources: ");
        for(UUID uu:targettedplayers){
            final String add = (getInfo(uu)!=null)?getInfo(uu).generateinfo():"§aVivant";
            strings.add("  -"+PlayerUtility.getNameByUUID(uu)+" "+add);

        }
        return Main.convertArrayToTable(String.class,strings);
	}
	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("§7Vous obtenez une épée en diamant tranchant 3. à l'annonce des rôles",
				"§7A chaque kill vous avez §c5%§7 de force bonus.",
				"§7Vous pouvez 3 fois dans la partie, cibler un joueur avec la commande '/dw source <Joueur>'. Le joueur ciblé à sa mort, vous donnera le nom et role de son tueur."
				," Quand vous tapez un joueur avec un épée en diamant. Vous avez à chaque coup 1 chance sur 50 d'obtenir le rôle de cette personne. ");
	}
	@Override
	public Camps getDefaultCamp() {
		return Camps.DoctorCamp;
	}
	@Override
	public String askName() {
		return "Harriet Jones";
	}
	@Override
	public ItemStack[] askItemStackgiven() {
		return new ItemStack[] { InventoryUtils.getHarrietJonesItem() };
	}
	@Override
	public boolean AskIfUnique() {
		return true;
	}
	@Override
	public String AskHeadTag() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWQzOWMyYTUyOTc4YzE2MzlmYjhiNGVkMzdjZjMxNGY1NmRlYzE4YzY1OGUyOWNmOTc0ZGVhNmFjNWUzZWQ1OSJ9fX0=";
	}
	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Humain};
	}

    @Override
    public String[] AskMoreInfo() {
	    ArrayList<String> strings = new ArrayList<>();
	    strings.add("Sources: ");
	    for(UUID uu:targettedplayers){
	        final String add = (getInfo(uu)==null)?"§aVivant":getInfo(uu).generateinfo();
	        strings.add("  "+PlayerUtility.getNameByUUID(uu)+" "+add);

        }
        return Main.convertArrayToTable(String.class,strings);
    }

    @Override
	public boolean OnHitPlayer(Player hitted, double amount, DamageCause cause) {
		Player player = Bukkit.getPlayer(getOwner());
		if (player.getItemInHand().getType() == Material.DIAMOND_SWORD) {
			if (1 == new Random().nextInt(50)) {
				player.sendMessage(Main.UHCTypo + "Grace à la capacité "
							+"Sources aléatoires"
							+ " vous avez obtenu le rôle du joueur que vous avez frappé. Son rôle est "
							+ RoleHandler.getRoleOf(hitted).getDefaultCamp().getColor()
							+ RoleHandler.getRoleOf(hitted).getName());
				}
			}
		
		return false;
	}
	@Override
	public void onKill(Player player, Player killed) {
		RoleHandler.getRoleOf(player).addBonus(new Bonus(BonusType.Force, 5));
		player.sendMessage(Main.UHCTypo+"§7Vous avez gagné §c5%§7 de force");
	}
	@Override
	public void onOtherKill(Player player, Player killer) {	
		if (getTargettedplayers().contains(player.getUniqueId())&&killer!=null){
			Player harriet = Bukkit.getPlayer(getOwner());
			if(harriet!=null)
			    harriet.sendMessage(Main.UHCTypo + "§e" + player.getName() + "§7 est mort. Son tueur est §c"+ killer.getName() + "§7 son rôle est " + RoleHandler.getRoleOf(killer).getDefaultCamp().getColor()+ RoleHandler.getRoleOf(killer).getName());
            this.info.add(new Info(player.getUniqueId(),killer.getName(),RoleHandler.getRoleOf(killer)));
		}
	}

    public Info getInfo(UUID uuid){
	    for(Info info: this.info){
	        if(info.getSource().equals(uuid)){
	            return info;
            }
        }
	    return null;
    }
    private record Info(UUID source,String name, Role role) {
        public String generateinfo() {
            return "Le tueur est " + name + " et est " + role.getDefaultCamp().getColor() + role.getName();
        }
        public UUID getSource(){
            return source;
        }
    }
}