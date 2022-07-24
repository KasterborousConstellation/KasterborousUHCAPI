package fr.supercomete.head.role.content.DWUHC;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import fr.supercomete.enums.Gstate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_OnKill;
import fr.supercomete.head.role.Triggers.Trigger_OnRoletime;
import fr.supercomete.head.role.Triggers.Trigger_WhileNight;
import fr.supercomete.head.role.Triggers.Trigger_onNightTime;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;
import fr.supercomete.nbthandler.NbtTagHandler;

public final class CyberBrouilleur extends DWRole implements Trigger_OnRoletime,Trigger_onNightTime,Trigger_WhileNight,Trigger_OnKill,HasAdditionalInfo {
    public boolean hasUsed=false;
    private boolean isactive=true;
	public CyberBrouilleur(UUID owner) {
		super(owner);	
		isactive=true;
	}



	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("§7Vous avez force 1 pendant la nuit. Cependant, si vous faite un kill, vous perdrez l'effet de force. Celui ci reprendra au début de la nuit suivante.",
				"A l’annonce des rôles, vous obtenez un activable. Si il est activé, vous brouillerez l'annonce de tous les rôles des morts et désactiverez l'accès à la composition de la partie jusqu’au prochain épisode. ",
				"Vous avez la liste des autres Cybermen",
				"A un moment de la partie le Cyberium apparaitra. Si vous arrivez à en prendre possesion, vous obtiendrez force permanente et tout les Cybermens pourront suivre votre position via le traqueur du Cyberium.",
				"Vous avez la possibilité comme tout les membres du Camp des Ennemis du Docteur, de détruire les clefs contenues dans le TARDIS en restant proche de celui-ci."
				);
	}
	@Override
	public Camps getDefaultCamp() {
		return Camps.EnnemiDoctorCamp;
	}


	@Override
	public String askName() {
		return "Cyber Brouilleur";
	}


	@Override
	public ItemStack[] askItemStackgiven() {
		return null;
	}


	@Override
	public boolean AskIfUnique() {
		return true;
	}


	@Override
	public String AskHeadTag() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWFhODI3MWM4MjFmNmM5ZmMwOGFmOTVkMjYyZDJhOTcwNmY2MWQ4ZGE0OTNkOGE1OTYyNDFjOTM2NGVhNGM1In19fQ==";
	}


	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Humain};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Force active: "+Main.getCheckMark(isactive),"Brouillage utilisé: "+Main.getCheckMark(hasUsed)};
    }

    @Override
	public void onRoleTime(Player player) {
		ItemStack item_=NbtTagHandler.createItemStackWithUUIDTag((InventoryUtils.getItem(Material.FEATHER, "§7Brouillage", Main.SplitCorrectlyString("§7Cliquez pour brouiller le rôle des morts et désactiver la visualisation de la composition jusqu'au prochain épisode", 40, "§7"))),5);
		InventoryUtils.addsafelyitem(player, item_);
	}

	@Override
	public void WhileNight(Player player) {
		if(isactive) {
			PlayerUtility.addProperlyEffect(player,new PotionEffect(PotionEffectType.INCREASE_DAMAGE,40,0,false,false));
		}
	}

	@Override
	public void onNightTime(Player player) {
		isactive=true;
		player.sendMessage(Main.UHCTypo+"Vous avez récupéré votre effet de force");
		
	}

	@Override
	public void onKill(Player player, Player killed) {
	    if(Main.currentGame.isGameState(Gstate.Night)){
            isactive=false;
            player.sendMessage(Main.UHCTypo+"Vous avez perdu votre effet de force");
        }
	}

	@Override
	public String[] getAdditionnalInfo() {
		return new String[] {"§cListe des autres Cybermen: "+RoleHandler.getCyberManList()};
	}
}
