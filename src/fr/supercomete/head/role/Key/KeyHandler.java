package fr.supercomete.head.role.Key;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.KeyType;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.RoleModifier.Companion;
import fr.supercomete.head.role.content.DWUHC.Bill_Potts;
import fr.supercomete.head.role.content.DWUHC.Davros;
import fr.supercomete.head.role.content.DWUHC.TheDoctor;
public class KeyHandler {
	private static int currentKey=0;
	public static void init() {
		currentKey=0;
	}
	public static boolean IsPossibleToGetKey() {
		return currentKey< Main.currentGame.getDataFrom(Configurable.LIST.TardisKey);
	}
	private static Bonus generateBonus() {
		int sh = new Random().nextInt(6);
        return switch (sh) {
            case 0 -> new Bonus(BonusType.Force, 7);
            case 1 -> new Bonus(BonusType.Speed, 5);
            case 2 -> new Bonus(BonusType.NoFall, 1);
            case 3 -> new Bonus(BonusType.Heart, 4);
            case 4 -> new Bonus(BonusType.Heart, 2);
            case 5 -> new Bonus(BonusType.Speed, 9);
            default -> null;
        };
    }
	public static TardisKey generateKey(DWRole role) {
		KeyType type = KeyType.values()[currentKey];
		TardisKey key = new TardisKey(type,generateBonus());
		if((role instanceof Davros || role instanceof Bill_Potts)) {
			if(key.getBonus().getType().equals(BonusType.NoFall))generateKey(role);
		}
		currentKey++;
		return key;
	}
	private static boolean IsThereAPlayer() {
		for(Entry<UUID,Role> entry:RoleHandler.getRoleList().entrySet()) {
			if(entry.getValue()instanceof Companion || entry.getValue() instanceof TheDoctor) {
				DWRole role = (DWRole)entry.getValue();
				if(role.getTardiskeys().size()<1) {
					return true;
				}
			}
		}
		return false;
	}
	private static ArrayList<UUID> getAllCandidate(){
		ArrayList<UUID>list = new ArrayList<>();
		for(Entry<UUID,Role> entry:RoleHandler.getRoleList().entrySet()) {
			if(entry.getValue()instanceof Companion && ((DWRole)entry.getValue()).getTardiskeys().size()<1) {
				list.add(entry.getKey());
			}
		}
		return list;
	}
	public static void GenerateAllKey(){
		while(IsPossibleToGetKey()&& IsThereAPlayer()) {
			if(currentKey==0&& RoleHandler.getWhoHaveRole(TheDoctor.class)!=null) {
				final DWRole role =(DWRole) RoleHandler.getRoleOf(RoleHandler.getWhoHaveRole(TheDoctor.class));
				final TardisKey key = generateKey(role);
				role.addTardisKey(key);
				if(Bukkit.getPlayer(RoleHandler.getWhoHaveRole(TheDoctor.class))!=null) {
					final Player player = Bukkit.getPlayer(RoleHandler.getWhoHaveRole(TheDoctor.class));
					player.sendMessage(Main.UHCTypo+"§eVous êtes porteur d'une clef du Tardis. A votre mort, la clef rejoindra a nouveau le Tardis et sera disponible pour un membre du Camp du Docteur si il n'est pas déjà porteur de clef.");
					RoleHandler.DisplayRole(player);
				}
			}else {
				final ArrayList<UUID> candidate = getAllCandidate();
				final UUID chosen = candidate.get(new Random().nextInt(candidate.size()));
				final DWRole role = (DWRole) RoleHandler.getRoleOf(chosen);
				final TardisKey key = generateKey(role);
				role.addTardisKey(key);
				if(Bukkit.getPlayer(chosen)!=null) {
					final Player player = Bukkit.getPlayer(chosen);
					player.sendMessage(Main.UHCTypo+"§eVous êtes porteur d'une clef du Tardis. A votre mort, la clef rejoindra a nouveau le Tardis et sera disponible pour un membre du Camp du Docteur si il n'est pas déjà porteur de clef.");
					RoleHandler.DisplayRole(player);
				}
				
			}
		}
		if(IsPossibleToGetKey() && !IsThereAPlayer()) {
			while(IsPossibleToGetKey()) {
				final TardisKey key = generateKey(null);
				TardisHandler.currentTardis.addKey(key);
			}
		}
	}
}