package fr.supercomete.head.role.content.DWUHC;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import fr.supercomete.head.role.*;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.Triggers.Trigger_OnRoletime;
import fr.supercomete.head.role.Triggers.Trigger_WhileAnyTime;
import fr.supercomete.head.role.Triggers.Trigger_onEpisodeTime;
import fr.supercomete.head.role.RoleModifier.PreAnnouncementExecute;
public final class Davros extends DWRole implements Trigger_WhileAnyTime,Trigger_OnRoletime,Trigger_onEpisodeTime,PreAnnouncementExecute, HasAdditionalInfo {
	private final ArrayList<UUID> list = new ArrayList<UUID>();
	private int advancement = 0;
	private ArrayList<String>ally= new ArrayList<>();
	public CoolDown cooldown = new CoolDown(1, 0);
	public CommandUse commandUse = new CommandUse("/dw cell");
	public Davros(UUID owner){
		super(owner);
		
	}
	@Override
	public Status[] AskStatus(){
		return new Status[]{};
	}

    @Override
    public String[] AskMoreInfo() {
	    ArrayList<String> ret = new ArrayList<>();
	    ret.add(commandUse.generate());
	    ret.add("Alliés connu: ");
	    ret.addAll(ally);
        return Main.convertArrayToTable(String.class,ret);
    }

    @Override
	public String askName(){
		return "Davros";
	}
	@Override
	public Camps getDefaultCamp(){
		return Camps.EnnemiDoctorCamp;
	}
	@Override
	public List<String> askRoleInfo(){
		return Arrays.asList("Vous avez §bvitesse§7 pendant toute la partie."
				," §7Vous pouvez avec la commande '/dw cell <Joueur>' donner 1♥ supplémentaire a un joueur. (Impossible de le faire sur vous-même). Le joueur ciblé doit être à moins de 30blocs de vous."
				,"Chaque épisode vous obtenez une charge supplémentaire pour donner 1♥ supplémentaire a un joueur."+cooldown.formalizedUtilisation(),
				"§7Vous obtenez chaque épisode un allié de votre Camp. Cet allié n'est pas forcement Dalek mais doit forcement gagner avec vous (qui doit gagner avec vous au moment de l'annonce des rôles, un joueur ayant changé de Camp peut donc se trouver dans votre liste), et donc du Camp des Ennemis du Docteur. Il ne peut donc pas être role solo.",
                "Vous n'apparaissez pas dans la liste des Daleks, cependant vous obtenez tout les épisodes un joueur dans votre camp, qu'il soit Dalek ou Cyberman, ou bien autre mais du camp des ennemis du Docteur."
				);
	}
	@Override
	public ItemStack[]askItemStackgiven(){
		return null;
	}
	@Override
	public boolean AskIfUnique(){
		return true;
	}
	@Override
	public String AskHeadTag(){
		return"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzgyZWU0OWI5ZDFlMjljYWI1N2E3NzExOWI5ZTk3OGQyMDI0MTlkOGNiMmQyNzc2MTNjNjE1NDRjMzgyMTI5MyJ9fX0=";
	}
	@Override
	public void WhileAnyTime(Player player) {
		PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.SPEED, 20*3, 0, false, false));	
	}
	@Override
	public void onRoleTime(Player player) {
		this.addBonus(new Bonus(BonusType.NoFall,1));
	}
	@Override
	public void onEpisodeTime(Player player) {
		cooldown.addUtilisation(1);
		if(player!=null)
            player.sendMessage(Main.UHCTypo+"Vous obtenez une charge de votre capacité.");
		UUID uuid=null;
		if(advancement< list.size()) {
			while(!RoleHandler.getRoleList().containsKey(uuid)) {
				uuid = list.get(advancement);
				advancement++;
			}
			String username ="";
			if(Bukkit.getPlayer(uuid)==null) {
				username = Main.currentGame.getOffline_Player(uuid).getUsername();
				
			}else {
				username = Bukkit.getPlayer(uuid).getName();
			}
			ally.add("  "+username);
            if(player!=null)
                player.sendMessage("§cLe joueur §6"+username+" est dans votre camp.");
		}
	}
	@Override
	public void PreAnnouncement() {
		for(Entry<UUID,Role> entry: RoleHandler.getRoleList().entrySet()) {
			if(entry.getValue().getCamp()==Camps.EnnemiDoctorCamp)list.add(entry.getKey());
		}
	}

    @Override
    public String[] getAdditionnalInfo() {
	    ArrayList<String> strings = new ArrayList<>();
	    strings.add("§7Vos alliés sont: ");
	    strings.addAll(ally);
        return Main.convertArrayToTable(String.class,strings);
    }
}