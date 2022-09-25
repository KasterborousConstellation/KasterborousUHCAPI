package fr.supercomete.head.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.RoleState.RoleState;
import fr.supercomete.head.role.RoleState.RoleStateOnAdd;
import fr.supercomete.head.role.RoleState.RoleStateTypes;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class Role{
	private UUID owner;
	private Camps camp;
	private ArrayList<Bonus> bonus = new ArrayList<>();
	private ArrayList<RoleState> rolestatelist = new ArrayList<RoleState>();
	public Role(UUID owner){
		this.setCamp(getDefaultCamp());
		this.setOwner(owner);
	}
    public RoleDisplayAddon getAddon(){
        return null;
    }
    public abstract String[] AskMoreInfo();
	public abstract String askName();
	public abstract Camps getDefaultCamp();
	public abstract List<String> askRoleInfo();
	public abstract ItemStack[] askItemStackgiven();
	public abstract boolean AskIfUnique();
	public abstract String AskHeadTag();
	public Camps getCamp() {
		return camp;
	}
	@NonNull
	public String getName() {
		return askName();
	}
	public void setCamp(Camps camp) {
		this.camp=camp;
	}
	public UUID getOwner() {
		return owner;
	}
	public void setOwner(UUID owner) {
		this.owner=owner;
	}
	public ArrayList<ItemStack> getItemStackGiven() {
		if(askItemStackgiven()==null)return new ArrayList<ItemStack>();
		return  new ArrayList<ItemStack>(Arrays.asList(this.askItemStackgiven()));
	}
	public ArrayList<String> getRoleinfo() {
		ArrayList<String>arr=new ArrayList<>();
		for(String str : askRoleInfo()) {
			ArrayList<String> sm = Main.SplitCorrectlyString(str, 50, "§7");
			sm.set(0, ">"+sm.get(0));
            arr.addAll(sm);
		}
		return arr;
	}
	public String[] getMoreInfo(){
	    return AskMoreInfo();
    }

	public ArrayList<RoleState> getRolestatelist() {
		return rolestatelist;
	}
	public void setRolestatelist(ArrayList<RoleState> rolestatelist) {
		this.rolestatelist = rolestatelist;
	}
	public void addRoleState(RoleState rolestate) {
		if(rolestate instanceof RoleStateOnAdd) {
			((RoleStateOnAdd)rolestate).onAdd(this);
		}
		this.rolestatelist.add(rolestate);
	}
	public void removeRoleState(RoleStateTypes type) {
		for(RoleState state : this.rolestatelist) {
			if(state.getRoleStateType()==type) {
				rolestatelist.remove(state);
				return;
			}
		}
	}
	public void updateRoleState() {
        this.rolestatelist.removeIf(state -> Main.currentGame.getTime() - state.getCreationtime() >= state.getDurationtime() && state.getDurationtime() != -1);
	}
	public RoleState getRoleState(RoleStateTypes type) {
		for(RoleState rolestate : this.getRolestatelist()) {
			if(rolestate.getRoleStateType().equals(type))return rolestate;
		}
		return null;
	}
	public boolean hasRoleState(RoleStateTypes rolestatetype) {
		for(RoleState state : this.rolestatelist) {
			if(state.getRoleStateType().equals(rolestatetype))return true;
		}
		return false;
	}

	public ArrayList<Bonus> getBonus() {
		return bonus;
	}

	public void setBonus(ArrayList<Bonus> bonus) {
		this.bonus = bonus;
	}
	
	public void addBonus(Bonus bonus) {
		this.bonus.add(bonus);
	}
	
	public int getPowerOfBonus(BonusType type) {
		int count = 0;
		for(Bonus b : getBonus()) {
			if (b.getType().equals(type)){
				count+=b.getLevel();
			}
		}
		return count;
	}
	public boolean hasBonus(BonusType type) {
		for(Bonus b : this.bonus) {
			if(b.getType().equals(type))return true;
		}
		return false;
	}
	public Bonus getFirstBonus(BonusType type) {
		for(Bonus b :this.getBonus()) {
			if(b.getType().equals(type))return b;
		}
		return null;
	}
}