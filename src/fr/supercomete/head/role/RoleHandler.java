package fr.supercomete.head.role;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import fr.supercomete.commands.RolesCommand;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.head.GameUtils.Historic;
import fr.supercomete.head.GameUtils.HistoricData;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;
import fr.supercomete.head.role.RoleModifier.PreAnnouncementExecute;
import fr.supercomete.head.role.RoleState.Displayed_RoleState;
import fr.supercomete.head.role.RoleState.RoleState;
public class RoleHandler {
	public static boolean IsHiddenRoleNCompo;
	private static boolean IsRoleGenerated;
	private static HashMap<UUID, Role> RoleList=new HashMap<UUID, Role>();
	private static Historic historic;
    private static final Random r = new Random();
	public static void GiveRole(){
        r.setSeed(System.currentTimeMillis());
		ArrayList<UUID> uu=new ArrayList<UUID>();
		HashMap<Class<?>,Integer> rolelist=Main.currentGame.getRoleCompoMap();
		for(UUID uud:Main.getPlayerlist()) {
			if(Bukkit.getPlayer(uud)==null || !(Bukkit.getPlayer(uud).isOnline()) || Bukkit.getPlayer(uud).getGameMode()==GameMode.SPECTATOR ||Main.bypass.contains(uud)) {
				Main.playerlist.remove(uud);
			}else
                uu.add(uud);
		}
		RoleList.clear();
		Collections.shuffle(uu);
        for (UUID uuid : uu) {
            int random = (rolelist.size() <= 1) ? 0 : r.nextInt(rolelist.size());
            Class<?> rt = (Class<?>) rolelist.keySet().toArray()[random];
            RoleList.put(uuid, RoleBuilder.Build(rt, uuid));
            if (rolelist.get(rt) <= 1) {
                rolelist.remove(rt);
            } else {
                int amount = rolelist.get(rt);
                rolelist.put(rt, (amount - 1));
            }
            for (ItemStack item : getRoleOf(uuid).getItemStackGiven()) {
                InventoryUtils.addsafelyitem(Bukkit.getPlayer(uuid), item);
            }
        }
		setHistoric(new Historic());
		setIsRoleGenerated(true);
		for(Role role : RoleList.values()) {
			if(role instanceof PreAnnouncementExecute) {
				((PreAnnouncementExecute)role).PreAnnouncement();
			}
		}
		
		for(UUID unique :RoleList.keySet()) {
			DisplayRole(Bukkit.getPlayer(unique));
		}
	}
	public static void removePlayer(UUID player) {
        RoleList.remove(player);
	}
	
	public static Role getRoleOf(Player player) {
		if(player==null)return null;
		if(!RoleList.containsKey(player.getUniqueId()))return null;
		return RoleList.get(player.getUniqueId());
	}
	public static Role getRoleOf(UUID uuid) {
		return RoleList.get(uuid);
	}

	public static UUID getWhoHaveRole(Class<?> rt){
		Role r=null;
		try {
			r = (Role) (rt.getConstructors()[0].newInstance(UUID.randomUUID()));
		} catch (InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|SecurityException e) {
			e.printStackTrace();
            return null;
		}
        if(!r.AskIfUnique())return null;
		
		for(Role rl:RoleList.values()) {
			if(rl.getClass().equals(rt))return rl.getOwner();
		}
		return null;
	}

	public static void DisplayRole(Player player) {
		Role role=getRoleOf(player);
		if(role==null) {
			player.sendMessage(Main.UHCTypo+"§cVous n'avez pas de rôle");
			return;
		}
		StringBuilder addDisplay = new StringBuilder(" ");
		for(RoleState state : role.getRolestatelist()) {
			if(state instanceof Displayed_RoleState) {
				addDisplay.append(((Displayed_RoleState) state).getDisplay());
			}
		}
		player.sendMessage("---------------------------------------------");
		player.sendMessage("§lVotre rôle est "+role.getCamp().getColor()+role.getName()+" "+addDisplay);
		player.sendMessage("  §lCamp: "+role.getCamp().getColor()+role.getCamp().getName());
        if(role.getAddon()!=null){
            role.getAddon().DisplayHead(player);
        }


		if(!role.getRoleinfo().isEmpty()) { 
			player.sendMessage("Description:");
			for(String str:role.getRoleinfo()) {
				player.sendMessage("  "+str);
			}
		}
		
		if(role instanceof HasAdditionalInfo) {
		    HasAdditionalInfo rrole=(HasAdditionalInfo) role;
            for(String str :rrole.getAdditionnalInfo()) {
				player.sendMessage(str);
			}
		}
		player.sendMessage("---------------------------------------------");
        if(role.getAddon()!=null){
            role.getAddon().DisplayBottom(player);
        }
	}
	public static void DisplayDeath(Player player) {
		DisplayDeath(new Offline_Player(player));
	}
	public static boolean isOnlineAndHaveRole(UUID player){
	    if(Bukkit.getPlayer(player)==null)
	        return false;
	    if(RoleHandler.getRoleOf(player)==null){
	        return false;
        }
        return Main.playerlist.contains(player);
    }
	public static void DisplayDeath(Offline_Player player) {
		if (RoleHandler.IsRoleGenerated()) {
			if (RoleHandler.IsHiddenRoleNCompo) {
				Bukkit.broadcastMessage("§7-----------------------------");
				Bukkit.broadcastMessage("§6" + player.getUsername() + "§7 est mort. Il était §7"
						+ org.bukkit.ChatColor.MAGIC + "kkkkkkkkkkkk");
				Bukkit.broadcastMessage("§7-----------------------------");
			} else {
				StringBuilder addDisplay = new StringBuilder(" ");
				for(RoleState state : RoleHandler.getRoleOf(player.getPlayer()).getRolestatelist()) {
					if(state instanceof Displayed_RoleState) {
						addDisplay.append(((Displayed_RoleState) state).getDisplay());
					}
				}
				Bukkit.broadcastMessage("§7-----------------------------");
				Bukkit.broadcastMessage("§6" + player.getUsername() + "§7 est mort. Il était "
						+ RoleHandler.getRoleOf(player.getPlayer()).getCamp().getColor()
						+ RoleHandler.getRoleOf(player.getPlayer()).getName()+addDisplay);
				Bukkit.broadcastMessage("§7-----------------------------");
			}
		} else {
			Bukkit.broadcastMessage("§7-----------------------------");
			Bukkit.broadcastMessage("§6" + player.getUsername()+ " §7est mort sans aucun rôle");
			Bukkit.broadcastMessage("§7-----------------------------");
		}
	}
	public static boolean IsRoleGenerated() {
		return IsRoleGenerated;
	}
	public static void setIsRoleGenerated(boolean isRoleGenerated) {
		IsRoleGenerated = isRoleGenerated;
	}
	public static HashMap<UUID, Role> getRoleList() {
		return RoleList;
	}
	public static void setRoleList(HashMap<UUID, Role> roleList) {
		RoleList = roleList;
	}
	public static String FormalizedGetWhoHaveRole(Class<?> rt) {
		return"§6"+((RoleHandler.getWhoHaveRole(rt)==null)?"Aucun":Bukkit.getPlayer(RoleHandler.getWhoHaveRole(rt)).getName());
	}
    public static void showcompo(final Player player){
        if (Main.currentGame.getMode() instanceof CampMode) {
            if (RoleHandler.IsRoleGenerated()) {
                HashMap<Class<?>, Integer> map = new HashMap<>();
                for (Role r : RoleHandler.getRoleList().values()) {
                    int amount = (map.containsKey(r.getClass())) ? map.get(r.getClass()) + 1 : 1;
                    map.put(r.getClass(), amount);
                }
                if (!RoleHandler.IsHiddenRoleNCompo)
                    RolesCommand.display(map, player);
                else
                    player.sendMessage(Main.UHCTypo+"§4Impossible la composition est cachée");
            } else {
                RolesCommand.display(Main.currentGame.getRoleCompoMap(), player);
            }
        }
    }

	public static Historic getHistoric() {
		return historic;
	}
	public static void setHistoric(Historic historic) {
		RoleHandler.historic = historic;
	}
	private static String transform(KasterBorousCamp camp) {
        if(camp.singleplayervictory()){
            return "§6Victoire"+camp.getColor()+" §6Solo";
        }else{
            return "§6Victoire: "+camp.getColor()+camp.getName();
        }
	}
	public static ArrayList<String> getRoleDescription(HistoricData data){
		ArrayList<String> arr = new ArrayList<String>();
		
		final Role role = data.getRole();
		StringBuilder addDisplay = new StringBuilder(" ");
		for(RoleState state : role.getRolestatelist()) {
			if(state instanceof Displayed_RoleState) {
				addDisplay.append(((Displayed_RoleState) state).getDisplay());
			}
		}
		arr.add("§bRole: "+role.getCamp().getColor()+role.getName()+" "+addDisplay);
		arr.add(transform(role.getCamp()));
		for(String string: role.getMoreInfo()){
		    arr.add("  §7"+string);
        }
		arr.add(((data.getCause()==null)?"§aEn vie":data.getCause().getDeathCause()+" §a("+TimeUtility.transform(data.getCause().getTime(), "§a", "§a", "§a")+")"));
		return arr;
	}
	
}