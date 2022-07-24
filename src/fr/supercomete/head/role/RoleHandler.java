package fr.supercomete.head.role;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import fr.supercomete.head.GameUtils.Time.TimeUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.enums.Camps;
import fr.supercomete.enums.Choice;
import fr.supercomete.head.GameUtils.Historic;
import fr.supercomete.head.GameUtils.HistoricData;
import fr.supercomete.head.GameUtils.GameMode.Modes.DWUHC;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Key.KeyHandler;
import fr.supercomete.head.role.Key.TardisKey;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;
import fr.supercomete.head.role.RoleModifier.PreAnnouncementExecute;
import fr.supercomete.head.role.RoleState.Displayed_RoleState;
import fr.supercomete.head.role.RoleState.RoleState;
import fr.supercomete.head.role.RoleState.RoleStateTypes;
import fr.supercomete.head.role.content.DWUHC.AmyPond;
import fr.supercomete.head.role.content.DWUHC.DannyPink;
import fr.supercomete.head.role.content.DWUHC.RoryWilliams;
import fr.supercomete.head.role.content.DWUHC.SoldatUNIT;
public class RoleHandler {
	public static boolean IsHiddenRoleNCompo;
	private static boolean IsRoleGenerated;
	private static HashMap<UUID, Role> RoleList=new HashMap<UUID, Role>();
	private static Historic historic;
	public static void GiveRole(){
		ArrayList<UUID> uu=new ArrayList<UUID>();
		SoldatUNIT.pseudorandom=new Random().nextInt(3);
		KeyHandler.init();
		HashMap<Class<?>,Integer> rolelist=Main.currentGame.getRoleCompoMap();
		for(UUID uud:Main.getPlayerlist()) {
			if(Bukkit.getPlayer(uud)==null || !(Bukkit.getPlayer(uud).isOnline())) {
				Main.playerlist.remove(uud);
			}else
			uu.add(uud);
		}
		RoleList.clear();
		Collections.shuffle(uu);
        for (UUID uuid : uu) {
            int random = (rolelist.size() <= 1) ? 0 : new Random().nextInt(rolelist.size());
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
	public static void sendmessagetoallCyberman(String message){
		for(UUID uu : RoleList.keySet()){
			if(!DWUHC.generateCybermanRoleList().contains(getRoleOf(uu).getClass()))continue;
			Bukkit.getPlayer(uu).sendMessage("§0[§4Cyberman§0]§r "+message);
		}
	}
	public static UUID getWhoHaveRole(Class<?> rt){
		Role r=null;
		try {
			r = (Role) (rt.getConstructors()[0].newInstance(UUID.randomUUID()));
		} catch (InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|SecurityException e) {
			e.printStackTrace();
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
		String addDisplay = " ";
		for(RoleState state : role.getRolestatelist()) {
			if(state instanceof Displayed_RoleState) {
				addDisplay+=((Displayed_RoleState)state).getDisplay();
			}
		}
		player.sendMessage("---------------------------------------------");
		player.sendMessage("§lVotre rôle est "+role.getCamp().getColor()+role.getName()+" "+addDisplay);
		player.sendMessage("  §lCamp: "+role.getCamp().getColor()+role.getCamp().getName());
		
		if(role instanceof DWRole dwrole) {
			String status="";
            if(dwrole.getStatus().length==0) {
				status ="§bAucun";
			}else {
				for(Status stat :dwrole.getStatus()) {
					status +=stat.getName()+"  ";
				}
			}
			player.sendMessage("  §lStatus: "+status);
		}
		
		if(!role.getRoleinfo().isEmpty()) { 
			player.sendMessage("Description:");
			for(String str:role.getRoleinfo()) {
				player.sendMessage("  "+str);
			}
		}
		
		if(role instanceof HasAdditionalInfo rrole) {
            for(String str :rrole.getAdditionnalInfo()) {
				player.sendMessage(str);
			}
		}
		if(role.getChoices().size()>0&&role.getChoice()==Choice.None) {
			player.sendMessage("  §dChoix de forme:");
			player.sendMessage("    §7Vous devez choisir une version de votre rôle");
			player.sendMessage("    §7avec /cversion <N°Version>.");
			player.sendMessage("    §7Vous avez "+ TimeUtility.transform(Main.currentGame.getTimer(Timer.ChoiceDelay).getData(), "§3", "§3", "§3")+" §7après l'annonce des rôles pour choisir.");
			player.sendMessage("    §7Si le délai est dépasser un choix aléatoire sera fait pour\n"+"    §7vous.");
			for(Choice c:role.getChoices()) {
				player.sendMessage("§3Choix "+(role.getChoices().indexOf(c)+1)+"  §6"+c.getName());
				for(String str:Main.SplitCorrectlyString(c.getDescription(),40,null)) {
					player.sendMessage("  §7"+str);
				}
			}
		}
		player.sendMessage("---------------------------------------------");
		if(role instanceof DWRole dwrole) {
            for(TardisKey key:dwrole.getTardiskeys()) {
				String content =getTardisKeyToString(key);
				
				player.sendMessage("§b"+key.getKeytype().getName()+": "+content);
			}
			
		}
	}
	
	public static String getTardisKeyToString(TardisKey key) {
        return switch (key.getBonus().getType()) {
            case Force -> "§4Force §c+" + key.getBonus().getLevel() + "%";
            case Speed -> "§bVitesse §1+" + key.getBonus().getLevel() + "%";
            case Heart -> "§dCoeurs Bonus §5+" + key.getBonus().getLevel() + "§d½♥";
            case NoFall -> "§aNoFall";
        };
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
		if (RoleHandler.isIsRoleGenerated()) {
			if (RoleHandler.IsHiddenRoleNCompo) {
				Bukkit.broadcastMessage("§7-----------------------------");
				Bukkit.broadcastMessage(Main.UHCTypo + "§6" + player.getUsername() + "§7 est mort. Il était §7"
						+ org.bukkit.ChatColor.MAGIC + "kkkkkkkkkkkk");
				Bukkit.broadcastMessage("§7-----------------------------");
			} else {
				String addDisplay = " ";
				for(RoleState state : RoleHandler.getRoleOf(player.getPlayer()).getRolestatelist()) {
					if(state instanceof Displayed_RoleState) {
						addDisplay+=((Displayed_RoleState)state).getDisplay();
					}
				}
				Bukkit.broadcastMessage("§7-----------------------------");
				Bukkit.broadcastMessage(Main.UHCTypo + "§6" + player.getUsername() + "§7 est mort. Il était "
						+ RoleHandler.getRoleOf(player.getPlayer()).getCamp().getColor()
						+ RoleHandler.getRoleOf(player.getPlayer()).getName()+addDisplay);
				Bukkit.broadcastMessage("§7-----------------------------");
			}
		} else {
			Bukkit.broadcastMessage("§7-----------------------------");
			Bukkit.broadcastMessage(Main.UHCTypo + "§6" + player.getUsername()+ " §7est mort sans aucun rôle");
			Bukkit.broadcastMessage("§7-----------------------------");
		}
	}
	public static boolean isIsRoleGenerated() {
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
	public static String getCyberManList(){
		String str="";
		String color="§4";
		for(Role r:RoleList.values()){
			if(DWUHC.generateCybermanRoleList().contains(r.getClass()) ||r.hasRoleState(RoleStateTypes.Infected) || r instanceof DannyPink){
				str=str+((str.isEmpty())?"":",")+Bukkit.getPlayer(r.getOwner()).getName();
			}
		}
		return color+str;
	}
	public static String getDalekList(){
		String str="";
		String color="§4";	
		for(Role r:RoleList.values()){
			if(DWUHC.generateDalekRoleList().contains(r.getClass())){
				str=str+((str.isEmpty())?"":",")+Bukkit.getPlayer(r.getOwner()).getName();
			}
		}
		return color+str;
	}
	public static void PondChat(ArrayList<String>str){
		for(UUID uu:RoleList.keySet()){
			Player player=Bukkit.getPlayer(uu);
			if(RoleList.get(uu)instanceof AmyPond ||RoleList.get(uu)instanceof RoryWilliams){
				String str2="";
				for(String str3:str){
					str2+=str3+" ";
				}
				player.sendMessage(str2);
			}
		}
	}
	public static Historic getHistoric() {
		return historic;
	}
	public static void setHistoric(Historic historic) {
		RoleHandler.historic = historic;
	}
	private static String transform(Camps camp) {
		if(camp!=Camps.Neutral && camp!=Camps.SoloBlackCloverUHC) {
			return "§6Victoire: "+camp.getColor()+camp.getName();
		}else {
			return "§6Victoire"+camp.getColor()+" §6Solo";
		}
	}
	public static ArrayList<String> getRoleDescription(HistoricData data){
		ArrayList<String> arr = new ArrayList<String>();
		
		final Role role = data.getRole();
		String addDisplay = " ";
		for(RoleState state : role.getRolestatelist()) {
			if(state instanceof Displayed_RoleState) {
				addDisplay+=((Displayed_RoleState)state).getDisplay();
			}
		}
		arr.add("§bRole: "+role.getCamp().getColor()+role.getName()+" "+addDisplay);
		arr.add(transform(role.getCamp()));
		for(String string: role.getMoreInfo()){
		    arr.add("  §7"+string);
        }
		if(role instanceof DWRole && ((DWRole) role).getTardiskeys().size()>0) {
			String clef = "";
			for(TardisKey key : ((DWRole)data.getRole()).getTardiskeys()) {
				clef += "§b"+key.getKeytype().getName()+": "+RoleHandler.getTardisKeyToString(key)+" ";
			}
			arr.add("§dClef: "+ clef);
		}
		arr.add(((data.getCause()==null)?"§aEn vie":data.getCause().getDeathCause()+" §a("+TimeUtility.transform(data.getCause().getTime(), "§a", "§a", "§a")+")"));
		return arr;
	}
	
}