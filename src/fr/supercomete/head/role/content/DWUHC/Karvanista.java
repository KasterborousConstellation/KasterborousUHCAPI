package fr.supercomete.head.role.content.DWUHC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.role.KarvanistaPacte.*;
import fr.supercomete.head.role.Triggers.Trigger_OnAnyKill;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_WhileAnyTime;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;
import fr.supercomete.head.role.RoleModifier.PreAnnouncementExecute;

public final class Karvanista extends DWRole implements HasAdditionalInfo,PreAnnouncementExecute,Trigger_WhileAnyTime, Trigger_OnAnyKill {
	public ArrayList<Proposal> allpacte = new ArrayList<>();
	public UUID target;
	public boolean isConcluded() {
		return concluded;
	}
	public void setConcluded(boolean concluded) {
		this.concluded = concluded;
	}

	private int progress=0;
	public boolean concluded=false;
	public boolean finished=false;
	public boolean kill= false;
	public Karvanista(UUID owner) {
		super(owner);
	}
	public ArrayList<Proposal> getAllpacte() {
		return allpacte;
	}
	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	public void setAllpacte(ArrayList<Proposal> allpacte) {
		this.allpacte = allpacte;
	}
	public void init() {
		allpacte.clear();
		concluded=false;
		finished =false;
		progress=0;

		final ArrayList<UUID> pool = new ArrayList<>();
		for(final Entry<UUID, Role> entry : RoleHandler.getRoleList().entrySet()) {
			if(entry.getValue() instanceof final DWRole role) {
                if(role.hasStatus(Status.Humain)) {
					pool.add(role.getOwner());
				}
			}
		}
		if(pool.size()==1) {
			setTarget(pool.get(0));
		}else if(pool.size()==0){
			setTarget(null);
		}else {
			setTarget(pool.get(new Random().nextInt(pool.size()-1)));
		}
        allpacte.add(new BasicProposal(getOwner(), target));
        allpacte.add(new ConfusionProposal(getOwner(), target));
        allpacte.add(new DayBothSlownessProposal(getOwner(), target));
        allpacte.add(new StasisProposal(getOwner(), target));
        allpacte.add(new StrengthProposal(getOwner(), target));
        allpacte.add(new SpeedProposal(getOwner(), target));
        allpacte.add(new LifeBoostProposal(getOwner(),target));
        allpacte.add(new ShieldProposal(getOwner(), target));
        allpacte.add(new HasteProposal(getOwner(),target));
        allpacte.add(new IncreaseFireDamage(getOwner(), target));
        allpacte.add(new LifeIncreaseProposal(getOwner(), target));
        allpacte.add(new MediumLifeIncrease(getOwner(), target));
        allpacte.add(new LargeLifeIncrease(getOwner(), target));
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public void update() {
		final Player ally = Bukkit.getPlayer(target);
		final Player me = (RoleHandler.getWhoHaveRole(Karvanista.class)!=null)?Bukkit.getPlayer(RoleHandler.getWhoHaveRole(Karvanista.class)):null;
		if(finished) {
			for(final Proposal prop : allpacte) {
				if(prop.IsActivated)prop.tick(me, ally);
			}
		}
		if(ally==null || me ==null || RoleHandler.getRoleOf(me)==null || RoleHandler.getRoleOf(ally)==null)return;
		if(me.getWorld()==ally.getWorld() && me.getLocation().distance(ally.getLocation())<20 && concluded) {
			if(!finished) {
				progress++;
				if(progress >= 60) {
					finished=true;
					for(final Proposal prop : allpacte) {
						if(prop.IsActivated)prop.start(me, ally);
					}
				}
			}
		}
	}
	@Override
	public Status[] AskStatus() {
		return new Status[] {};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Allié: "+ PlayerUtility.getNameByUUID(target)};
    }

    @Override
	public String askName() {
		return "Karvanista";
	}

	@Override
	public Camps getDefaultCamp() {
		return Camps.Neutral;
	}
	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList("Il vous a été donné un humain à protéger. Ce joueur a forcement le status §4Humain§7. Il n'est pas forcement du Camp du Docteur.", "Vous pouvez constituer un pacte avec ce joueur avec la commande '/dw pacte', vous deciderez de comment gagner avec lui."," Si vous refusez de faire un pacte alors vous devrez gagner seul et lors de la mort de votre allié, vous mourrez. (Victoire impossible) ");
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
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzA1NzRkYzdiOWE4OTgzNWYxOTFhY2MwYjY5NWU4MTFlMjdiNjYyMjJlMDI1YzEwYTYwOTBmYjUxMmQ5MGZhYiJ9fX0=";
	}

	public UUID getTarget() {
		return target;
	}

	public void setTarget(UUID target) {
		this.target = target;
	}

	@Override
	public String[] getAdditionnalInfo() {
		String ally = "";
		if(target==null) {
			ally = "§6Aucun";
		}else {
			if(Bukkit.getPlayer(target)!=null && RoleHandler.getRoleList().containsKey(target)) {
				ally="§6"+Bukkit.getPlayer(target).getName();
			}else {
				if(Main.currentGame.hasOfflinePlayer(target)) {
					ally ="§6"+Main.currentGame.getOffline_Player(target);
					if(!RoleHandler.getRoleList().containsKey(target)) {
						ally= "§cMort";
					}
				}else {
					ally= "§cMort";
				}
			}
		}
		String add= (target!=null&&RoleHandler.getRoleList().containsKey(target))?"/ "+RoleHandler.getRoleOf(target).getName():"";
		return new String[]{"§7Votre allié est "+ally+"§7 "+add};
	}
	@Override
	public void PreAnnouncement() {
		init();		
	}
	@Override
	public void WhileAnyTime(Player player) {
	    update();
        if(kill && Bukkit.getPlayer(getOwner())!=null){
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"kill "+Bukkit.getPlayer(getOwner()).getName());
        }
	}
	public Proposal getProposal(Class<?>claz) {
		for(Proposal prop : allpacte) {
			if(prop.getClass().equals(claz))return prop;
		}
		return null;
	}

    @Override
    public void onOtherKill(Player player, Player killer) {
        if(target!=null&&!finished&&player.getUniqueId().equals(target)){
            kill = true;
        }
    }
}