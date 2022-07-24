package fr.supercomete.head.role.content.DWUHC;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurables;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_WhileAnyTime;
import fr.supercomete.head.role.Triggers.Trigger_WhileDay;
import fr.supercomete.head.role.Triggers.Trigger_WhileNight;
public final class Kate_Stewart extends DWRole implements Trigger_WhileAnyTime,Trigger_WhileDay,Trigger_WhileNight {
	private HashMap<UUID, ArrayList<Indice>> Indices = new HashMap<UUID, ArrayList<Indice>>();
	private HashMap<UUID, Integer> progression = new HashMap<UUID, Integer>();
	private int stewartspeed=1;
 	public Kate_Stewart(UUID owner) {
		super(owner);
		for(UUID pl:Main.getPlayerlist()) {
			if(pl!=owner)Indices.put(pl, new ArrayList<Kate_Stewart.Indice>());
		}
		for(UUID pl: Main.getPlayerlist())if(pl!=owner)progression.put(pl, 0);
		
	}
 	public HashMap<UUID, ArrayList<Indice>> getIndices() {
		return Indices;
	}
	public void setIndices(HashMap<UUID, ArrayList<Indice>> indices) {
		Indices = indices;
	}
	public HashMap<UUID, Integer> getProgression() {
		return progression;
	}
	public void setProgression(HashMap<UUID, Integer> progression) {
		this.progression = progression;
	}
	public void setProgressionOfPlayer(UUID player,int progression) {
		this.progression.put(player, progression);
	}
	public void updateProgression(UUID player) {
		if(player==super.getOwner())return;
		progression.put(player, progression.get(player)+stewartspeed);
		if(progression.get(player)>=100) {
			progression.put(player, progression.get(player)-100);
			Indice indice=generateIndice(player);
			Bukkit.getPlayer(super.getOwner()).sendMessage(Main.UHCTypo+"Le joueur §e"+Bukkit.getPlayer(player).getName()+"§7 a généré un "+indice.getName());
			addIndiceToPlayer(player, indice);
		}	
	}
 	public void addIndiceToPlayer(UUID player,Indice indice) {
 		if(Indices.containsKey(player)) {
 			Indices.get(player).add(indice);
 		}
 	}
 	private Indice generateIndice(UUID target) {
 		int random = new Random().nextInt(10);
 		switch (RoleHandler.getRoleOf(target).getCamp()) {
		case DoctorCamp:
			/* 3,4,5,6,7,8,9 Bleu 70%
			 * 1,2 Rouge 20%
			 * 0 Jaune 10%
			 */
			if(random>2) {
				return Indice.Bleu;
			}else if(random>0) {
				return Indice.Rouge;
			}else return Indice.Jaune;
			/* 3,4,5,6,7,8,9 Rouge 70%
			 * 1,2 Bleu 20%
			 * 0 Jaune 10%
			 */
		case EnnemiDoctorCamp:
			if(random>2) {
				return Indice.Rouge;
			}else if(random>0) {
				return Indice.Bleu;
			}else return Indice.Jaune;
			/* 0,1,2 Rouge 30%
			 * 3,4,5 Bleu 30%
			 * 6,7,8,9 Jaune 40%
			 */
		case Neutral:
			if(random>5) {
				return Indice.Jaune;
			}else if(random>2) {
				return Indice.Bleu;
			}else return Indice.Rouge;
		default:
			return Indice.Jaune;
		}
 	}
 	public ArrayList<Indice> getIndicesOfPlayer(UUID player) {
 		return Indices.get(player);
 	}
 	public enum Indice{
		Jaune("§eIndice Jaune","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzMyNDQzYmY2ODE4MWMwODJmMDY0NWU3MTdkYzViMjE5ZDZjOWFjYWU2NDljOTkwN2JjODFjYTdjYTJjN2Q4NCJ9fX0="),
		Rouge("§4Indice Rouge","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTYyMmZkNzIwNTAwNGZiZjlmZDE2NjMyNTJjYmNiZjQyMThmNDYxZjk0NGMwOTJlNzQ5ZjE1MDg4Yzg3ODQwMSJ9fX0="),
		Bleu("§1Indice Bleu","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzkxNmU0YmU3YTQxOWFiMGQwMmQzYjk1YTdkMmRiZDhlMThlNmE4M2I2NzMwZWYyZmQ3NDhkOTZkNWZjNjQyOSJ9fX0="),
		;
		private final String name;
        private final String headvalue;
		Indice(String name,String headvalue) {
			this.name=name;
			this.headvalue=headvalue;
		}
		public String getName() {
			return this.name;
		}
		public String getHeadValue() {
			return this.headvalue;
		}
	}
 	public int getNumberOfIndices() {
 		int count=0;
 		for(ArrayList<Indice> arr : this.Indices.values()) {
 			count+=arr.size();
 		}
 		return count;
 	}
	@Override
	public String askName() {
		return "Kate Stewart";
	}
	@Override
	public Camps getDefaultCamp() {
		return Camps.DoctorCamp;
	}
	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList( "Tout les soldats d'UNIT connaissent votre identité.",
				"§7Quand vous êtes à moins de 20blocs d'un joueurs, et cela toute les §a"+Main.currentGame.getDataFrom(Configurable.LIST.StewartUpdate)+"s§7 vous faites augmenter la progression de la fouille d'un joueur de 1%"
				,"Quand la fouille atteint 100%, la fouille de ce joueur reviens instantanément à 0% et vous donnes un indice sur le role."
				,"L'indice est soit §eJaune§7 soit §4Rouge§7 soit §1Bleu§7. Leur répartition selon le Camp est disponible dans le '/dw indice'"
				,"Vous pouvez voir les indices que vous avez obtenu avec la commande '/dw indice' ainsi que le nombre total d'indice. ",
				"A partir de 50 indices collectés vous obtenez l'effet vitesse de jour. A partir de 100 indices, vos fouilles progressent 2 fois plus vite. A partir de 200 indices vous obtenez vitesse la nuit ainsi que résistance."
				);
	}
	@Override
	public ItemStack[] askItemStackgiven() {
		return new ItemStack[] {};
	}
	@Override
	public boolean AskIfUnique() {
		return true;
	}
	@Override
	public String AskHeadTag() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRiNWI0NWI4MzY5OTVhNzUzYWY1NjczM2U1ZWRkMWI0N2UwYjA4ZTE5YzQ2MjdiNDdmZDE5ZTJhMjUwZTIzNiJ9fX0=";
	}
	public int getStewartspeed() {
		return stewartspeed;
	}
	public void setStewartspeed(int stewartspeed) {
		this.stewartspeed = stewartspeed;
	}
	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Humain};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Nombre d'indice: "+this.getNumberOfIndices()};
    }

    @Override
	public void WhileAnyTime(Player player) {
		if(this.getNumberOfIndices()>200) {
			PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*3, 0, false, false));
		}
		if(this.getNumberOfIndices()>=100) {
			this.setStewartspeed(this.getStewartspeed()*2);
		}
	}

	@Override
	public void WhileDay(Player player) {
		if(this.getNumberOfIndices()>=50) {
			PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.SPEED, 60, 0, false, false));
		}
		
	}
	@Override
	public void WhileNight(Player player) {
		if(this.getNumberOfIndices()>200) {
			PlayerUtility.addProperlyEffect(player, new PotionEffect(PotionEffectType.SPEED, 20*3, 0, false, false));
		}
	}
	
}