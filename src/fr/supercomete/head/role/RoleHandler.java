package fr.supercomete.head.role;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import fr.supercomete.commands.RolesCommand;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.NRGMode;
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
	private static HashMap<UUID, Role> RoleList=new HashMap<>();
	private static Historic historic;

    private static boolean isValid(UUID uuid){
        return Bukkit.getPlayer(uuid)==null || !(Bukkit.getPlayer(uuid).isOnline()) || Bukkit.getPlayer(uuid).getGameMode()== GameMode.SPECTATOR || Main.bypass.contains(uuid);
    }
	public static void GiveRole(){
		ArrayList<UUID> uu=new ArrayList<>();
		HashMap<Class<?>,Integer> rolelist=Main.currentGame.getRoleCompoMap();
		//Validate
        for(final UUID uud:Main.getPlayerlist()) {
			if(isValid(uud)) {
				Main.playerlist.remove(uud);
			}else
                uu.add(uud);
		}
		RoleList.clear();
		Collections.shuffle(uu);
        Main.INSTANCE.setPlayerlist(uu);
        final NRGMode mode =(NRGMode) Main.currentGame.getMode();
        final HashMap<UUID,Class<?>>mapped_role=mode.getRoleGenerator().map(rolelist,new LinkedList<>(uu));
        final HashMap<UUID,Role> finally_role = new HashMap<>();
        RoleList=finally_role;
        for(final Map.Entry<UUID,Class<?>>entry:mapped_role.entrySet()){
            finally_role.put(entry.getKey(),Build(entry.getValue(), entry.getKey()));
            for (ItemStack item : getRoleOf(entry.getKey()).getItemStackGiven()) {
                InventoryUtils.addsafelyitem(Bukkit.getPlayer(entry.getKey()), item);
            }
        }
		setHistoric(new Historic());
		setIsRoleGenerated(true);
		for(final Role role : RoleList.values()) {
			if(role instanceof PreAnnouncementExecute) {
				((PreAnnouncementExecute)role).PreAnnouncement();
			}
		}
		for(final UUID unique :RoleList.keySet()) {
			DisplayRole(Bukkit.getPlayer(unique));
		}
	}
	public static void removePlayer(UUID player) {
        RoleList.remove(player);
	}
    public static Role Build(Class<?> claz,UUID owner) {
        try {
            return (Role) claz.getConstructors()[0].newInstance(owner);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                 | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
	public static Role getRoleOf(Player player) {
		if(player==null)return null;
		if(!RoleList.containsKey(player.getUniqueId()))return null;
		return RoleList.get(player.getUniqueId());
	}
	public static Role getRoleOf(final UUID uuid) {
		return RoleList.get(uuid);
	}

	public static UUID getWhoHaveRole(Class<?> rt){
		Role r;
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
        if(!(role instanceof SchemaRole)){
            player.sendMessage("---------------------------------------------");
            player.sendMessage("§lVotre rôle est "+role.getCamp().getColor()+role.getName()+" "+addDisplay);
            player.sendMessage("  §lCamp: "+role.getCamp().getColor()+role.getCamp().getName());
        }

        if(role.getAddon()!=null){
            role.getAddon().DisplayHead(player);
        }

        if(!role.askRoleInfo().isEmpty()){
            if(!(role instanceof SchemaRole)) {
                player.sendMessage("Description:");
                for(String str:role.askRoleInfo()) {
                    player.sendMessage("  "+str);
                }
            }else{
                for(final String string :role.askRoleInfo()){
                    player.sendMessage(string);
                }
            }
        }

		
		if(role instanceof HasAdditionalInfo) {
		    HasAdditionalInfo rrole=(HasAdditionalInfo) role;
            for(String str :rrole.getAdditionnalInfo()) {
				player.sendMessage(str);
			}
		}
        if(!(role instanceof SchemaRole)){
            player.sendMessage("---------------------------------------------");
        }
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
        if (Main.currentGame.getMode() instanceof NRGMode) {
            RolesCommand.display(player);
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