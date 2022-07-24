package fr.supercomete.head.GameUtils.GameMode.Modes;
import java.util.*;
import java.util.Map.Entry;

import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.role.Triggers.Trigger_onEpisodeTime;
import fr.supercomete.head.role.KarvanistaPacte.Proposal;
import fr.supercomete.head.role.content.DWUHC.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.Groupable;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.CyberiumHandler;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Key.Tardis;
import fr.supercomete.head.role.Key.TardisHandler;
import fr.supercomete.head.role.Key.TardisKey;
import fr.supercomete.head.world.scoreboardmanager;
public class DWUHC extends Mode implements CampMode,Groupable{
	private final Camps[] campslist;
	public DWUHC() {
		super("Doctor Who UHC",Material.LAPIS_BLOCK, Collections.singletonList("§6Doctor Who §4UHC"));
		this.campslist= new Camps[]{Camps.DoctorCamp,Camps.EnnemiDoctorCamp,Camps.Division,Camps.Neutral};
	}
	@Override
	public void DecoKillMethod(Offline_Player player) {
		RoleHandler.DisplayDeath(player);
		
			RoleHandler.removePlayer(player.getPlayer());
		
		InventoryUtils.dropInventory(player.getInventory(), player.getLocation(), player.getLocation().getWorld());
		Main.playerlist.remove(player.getPlayer());
	}
	@Override
	public Camps[] getPrimitiveCamps() {
		return campslist;
	}
	@Override
	public void OnKillMethod(Location deathLocation, Player player, Player damager) {
		
		if(CyberiumHandler.HostPlayer == player.getUniqueId()) {
			CyberiumHandler.SetCyberiumHost((UUID) null);
			CyberiumHandler.GenerateLocationOfDropCyberium(false, player);
		}
		RoleHandler.DisplayDeath(player);
		Mode.GoldenHeadImplement(player, damager);
		Mode.KillSwitchImplement(player, damager);
        InventoryUtils.dropInventory(player.getInventory(),deathLocation,player.getWorld());
		player.setGameMode(GameMode.SPECTATOR);
		if(((DWRole)RoleHandler.getRoleOf(player)).getTardiskeys().size()>0) {
			DWRole role = (DWRole)RoleHandler.getRoleOf(player);
			Bukkit.broadcastMessage(Main.UHCTypo+"§eUn porteur de clef est mort.");
			Tardis tardis = TardisHandler.currentTardis;
			for(TardisKey key: role.getTardiskeys()) {
				tardis.addKey(key);
			}
		}
		player.getInventory().clear();
		Main.playerlist.remove(player.getUniqueId());
		RoleHandler.removePlayer(player.getUniqueId());
	}
	@Override
	public void onAnyTime(Player player) {
		if(RoleHandler.getRoleOf(player)==null)return;
		//Switch Capacity
		if(RoleHandler.isIsRoleGenerated()==false)return;
		if(CyberiumHandler.HostPlayer == player.getUniqueId()) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999999, 0,false,false));
		}
		//Switch Role
		Role role = RoleHandler.getRoleOf(player);
		
		if(role instanceof Zygon) {
			Zygon r = (Zygon)RoleHandler.getRoleOf(player);
			if(r.getOwner()!=r.getUuid()) {
				if(RoleHandler.getRoleList().containsKey(r.getUuid())) {
					if(r.getCamp()!=RoleHandler.getRoleOf(r.getUuid()).getCamp()) {
						r.setCamp(RoleHandler.getRoleOf(r.getUuid()).getCamp());
						player.sendMessage(Main.UHCTypo+"§bVotre Camp vient de changer. Votre nouveau Camp est: "+r.getCamp().getColor()+r.getCamp().getName());
					}
				}else {
					r.setUuid(r.getOwner());
					if(r.getCamp()!=Camps.Neutral) {
						r.setCamp(Camps.Neutral);
						player.sendMessage(Main.UHCTypo+"§bVotre Camp vient de changer. Votre nouveau Camp est: "+r.getCamp().getColor()+r.getCamp().getName());
					}
				}
			}
		}
	}
	@SuppressWarnings("unused")
	@Override
	public void onDayTime(Player player) {
		RoleHandler.IsHiddenRoleNCompo=false;
		if(RoleHandler.getRoleOf(player)==null)return;
		//Switch Capacity
		//Switch Role
		Role role = RoleHandler.getRoleOf(player);
		
	}
	@Override
	public void onNightTime(Player player) {
		if(RoleHandler.getRoleOf(player)==null)return;
	}
	@Override
	public void onEndingTime(Player player) {
		
	}
	@Override
	public void onRoleTime(Player player) {
		if(RoleHandler.getRoleOf(player)==null)return;
		//Switch Role
		@SuppressWarnings("unused")
		Role role = RoleHandler.getRoleOf(player);
	}
	@Override
	public void OnStart(Player player) {
		if(Main.currentGame.getScenarios().contains(Scenarios.MasterLevel)) {
			player.setLevel(100000);
		}
	}
	@Override
	public boolean WinCondition() {
		if(RoleHandler.isIsRoleGenerated()) {
			if(RoleHandler.getRoleList().size()== 0 ) {
				scoreboardmanager.titlemessage("Victoire de la §4Mort");
				RoleHandler.setRoleList(new HashMap<UUID, Role>());
				return true;
			}
			boolean doctorcamp=false;
            boolean ennemisdoctor=false;
            boolean karvanista=false;
            boolean division=false;
            boolean neutral=false;
			for(Entry<UUID,Role> entry:RoleHandler.getRoleList().entrySet()){
				Camps camps = entry.getValue().getCamp();
				if(camps==Camps.DoctorCamp){
				    doctorcamp=true;
                }
                if(camps==Camps.EnnemiDoctorCamp){
                    ennemisdoctor=true;
                }
                if(camps==Camps.DuoKarvanista){
                    karvanista=true;
                }
                if(camps==Camps.Division){
                    division=true;
                }
                if(camps==Camps.Neutral){
                    neutral=true;
                }
			}
			if(doctorcamp&&!ennemisdoctor&&!karvanista&&!division&&!neutral){
                scoreboardmanager.titlemessage("Victoire du "+Camps.DoctorCamp.getColoredName());
                Bukkit.broadcastMessage("   Victoire du "+Camps.DoctorCamp.getColoredName());
                RoleHandler.setRoleList(new HashMap<UUID, Role>());
                RoleHandler.getHistoric().draw();
                return true;
            }
            if(!doctorcamp&&ennemisdoctor&&!karvanista&&!division&&!neutral){
                scoreboardmanager.titlemessage("Victoire des "+Camps.EnnemiDoctorCamp.getColoredName());
                Bukkit.broadcastMessage("   Victoire des "+Camps.EnnemiDoctorCamp.getColoredName());
                RoleHandler.setRoleList(new HashMap<UUID, Role>());
                RoleHandler.getHistoric().draw();
                return true;
            }
            if(!doctorcamp&&!ennemisdoctor&&karvanista&&!division&&!neutral){
                scoreboardmanager.titlemessage("Victoire du "+Camps.DuoKarvanista.getColoredName());
                Bukkit.broadcastMessage("   Victoire du "+Camps.DuoKarvanista.getColoredName());
                RoleHandler.setRoleList(new HashMap<UUID, Role>());
                RoleHandler.getHistoric().draw();
                return true;
            }
            if(!doctorcamp&&!ennemisdoctor&&!karvanista&&division&&!neutral){
                scoreboardmanager.titlemessage("Victoire de "+Camps.Division.getColoredName());
                Bukkit.broadcastMessage("   Victoire de "+Camps.Division.getColoredName());
                RoleHandler.setRoleList(new HashMap<UUID, Role>());
                RoleHandler.getHistoric().draw();
                return true;
            }
            if(!doctorcamp&&!ennemisdoctor&&!karvanista&&!division&&neutral){
                if(RoleHandler.getRoleList().size()==1){
                    Role role = (Role) RoleHandler.getRoleList().values().toArray()[0];
                    scoreboardmanager.titlemessage("Victoire de §f"+role.getName());
                    Bukkit.broadcastMessage("   Victoire de §f"+role.getName());
                    return true;
                }else{
                    return false;
                }

            }

		}
		return false;
	}
	@Override
	public void onEpisodeTime(Player player) {
		if(RoleHandler.isIsRoleGenerated()){
		    Role role =RoleHandler.getRoleOf(player);
		    if(role instanceof Karvanista ){
		        Karvanista karvanista = (Karvanista) role;
		        for(final Proposal proposal:karvanista.allpacte){
		            if(proposal.IsActivated){
		                if(proposal instanceof Trigger_onEpisodeTime ){
                            Trigger_onEpisodeTime trigger =(Trigger_onEpisodeTime)proposal;
		                    trigger.onEpisodeTime(player);
		                }
		            }
		        }
		    }
        }
	}
	
	
	
	
	
	public static ArrayList<Class<?>>generateDalekRoleList(){
		ArrayList<Class<?>> daleks= new ArrayList<Class<?>>();
		daleks.add(Dalek.class);
		daleks.add(DalekStrategic.class);
		daleks.add(Rusty.class);
		daleks.add(DalekSec.class);
		daleks.add(Supreme_Dalek.class);
		daleks.add(DalekCaan.class);
		return daleks;
	}
	public static ArrayList<Class<?>>generateCybermanRoleList(){
		ArrayList<Class<?>> cybermans= new ArrayList<Class<?>>();
		cybermans.add(Cyberman.class);
		cybermans.add(Cybermite.class);
		cybermans.add(CyberPlanner.class);
		cybermans.add(CyberBrouilleur.class);
		return cybermans;
	}
}