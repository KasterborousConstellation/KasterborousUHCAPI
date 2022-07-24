package fr.supercomete.head.role;

import java.util.ArrayList;
import java.util.UUID;

import fr.supercomete.head.role.Key.TardisKey;
import fr.supercomete.head.role.RoleModifier.Companion;

public abstract class DWRole extends Role {
	private Status[] status;
	private ArrayList<TardisKey> tardiskeys= new ArrayList<TardisKey>();
	public DWRole(UUID owner) {
		super(owner);
		if(this instanceof Companion) {
			Status[] status2 = new Status[AskStatus().length + 1];
			System.arraycopy(AskStatus(), 0, status2, 0, AskStatus().length);
			status2[AskStatus().length] = Status.Companion;
			setStatus(status2);
		}else {
			setStatus(AskStatus());
		}
	}
	public abstract Status[] AskStatus();

	public Status[] getStatus() {
		return status;
	}
	
	public void setStatus(Status[] status) {
		this.status = status;
	}
	public boolean hasStatus(Status status) {
		for(Status stat : this.status) {
			if(stat.equals(status))return true;
		}
		return false;
	}
	public ArrayList<TardisKey> getTardiskeys() {
		return tardiskeys;
	}
	public void setTardiskeys(ArrayList<TardisKey> tardiskeys) {
		this.tardiskeys = tardiskeys;
	}
	public void addTardisKey(TardisKey key) {
		tardiskeys.add(key);
		this.addBonus(key.getBonus());
	}

}
