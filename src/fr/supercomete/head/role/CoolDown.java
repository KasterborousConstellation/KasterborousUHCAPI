package fr.supercomete.head.role;

import fr.supercomete.head.core.Main;

public class CoolDown {
	private int utilisation,cooldown;
	private int lastuse;
	public CoolDown(int utilisation,int cooldown) {
		this.utilisation=utilisation;
		this.cooldown=cooldown;
		this.lastuse=-cooldown;
	}
	public int getUtilisation() {
		return utilisation;
	}
	public void setUtilisation(int utilisation) {
		this.utilisation = utilisation;
	}
	public void addUtilisation(int add) {
		this.utilisation=utilisation+add;
	}
	public void removeUtilisation(int add) {
		if(this.utilisation-add>=0)this.addUtilisation(-add);else this.utilisation=0;
	}
	public int getCooldown() {
		return cooldown;
	}
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	public int getLastuse(){
		return lastuse;
	}
	public int getRemainingTime() {
		int c=lastuse+cooldown-Main.currentGame.getTime();
		if(c<0)c=0;
		return c;
	}
	public void setLastuse(int lastuse) {
		this.lastuse = lastuse;
	}
	public boolean isAbleToUse(){
		return lastuse+cooldown<=Main.currentGame.getTime();
	}
	public String formalizedUtilisation(){
		return "§4(§cUtilisation §crestante: §r"+this.utilisation+"§4)";
	}
	public void setUseNow() {
		this.lastuse=Main.currentGame.getTime();
	}
	
}