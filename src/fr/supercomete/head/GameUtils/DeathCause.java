package fr.supercomete.head.GameUtils;

import org.bukkit.entity.Player;

import fr.supercomete.head.core.Main;

public class DeathCause {
	private String deathCause="§7PVE";
	private int time;
    private String killer;
	private EnvironnementalCause addCause;
	public DeathCause() {
		time = Main.currentGame.getTime();
	}
	public DeathCause(Player killer,EnvironnementalCause addCause) {
		super();
        this.killer=killer.getName();
		if(killer!=null)deathCause="§7Tué par "+killer.getName();
		deathCause+=" §a("+addCause.getCause()+")";
		time = Main.currentGame.getTime();
	}
	public String getDeathCause() {
		return deathCause;
	}
	public void setDeathCause(String deathCause) {
		this.deathCause = deathCause;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public boolean IsPve() {
		return deathCause=="§cPVE";
	}
    public boolean IsPlayer(){
        return killer !=null;
    }
    public String getKillerName(){
        return killer;
    }

    public enum EnvironnementalCause {
	    PVE("PVE"),
	    Bow("Arc"),
        Sword("Epée"),
        Fall("Chute"),
        Lava("Lave"),
        Fire("Feu"),
        Wither("Empoisonnement")

	    ;
	    private String cause;
	    EnvironnementalCause(String cause){
	        this.cause=cause;
        }
	    public String getCause(){
	        return cause;
        }
    }
}
