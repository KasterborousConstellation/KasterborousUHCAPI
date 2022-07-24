package fr.supercomete.head.GameUtils.Time;
import javax.annotation.Nullable;

import fr.supercomete.head.GameUtils.GameMode.Modes.BlackCloverUHC;
import fr.supercomete.head.GameUtils.GameMode.Modes.DWUHC;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.GameMode.Modes.UHCClassic;
import fr.supercomete.head.core.Main;

public enum Timer {
	RealEpisodeTime("Durée d'un épisode",180,new Mode[] {new UHCClassic(),new DWUHC(),new BlackCloverUHC()},1200,TimerType.Literal,null,false),
	EpisodeTime("Durée d'un cycle jour/nuit",180,new Mode[] {new UHCClassic(),new DWUHC(),new BlackCloverUHC()},600,TimerType.Literal,null,false),
	PvPTime("PvP",60,new Mode[] {new UHCClassic(),new DWUHC(),new BlackCloverUHC()},1200,TimerType.TimeDependent,null,true),
	BorderTime("Début du mouvement de la bordure",60,new Mode[] {new UHCClassic(),new DWUHC(),new BlackCloverUHC()},4800,TimerType.TimeDependent,null,true),
	RoleTime("Roles",60,new Mode[] {new DWUHC(),new BlackCloverUHC()},1200,TimerType.TimeDependent,null,true),
	InvincibilityTime("Fin de l'invincibilité",10,new Mode[] {new UHCClassic(),new DWUHC(),new BlackCloverUHC()},15,TimerType.TimeDependent,null,false),
	ChoiceDelay("Delai maximum de choix de version",30,new Mode[] {new DWUHC(),new BlackCloverUHC()},5*60,TimerType.Literal,null,false),
	CyberiumTime("Apparition du Cyberium",60,new Mode[] {new DWUHC()},60*50,TimerType.TimeDependent,Timer.RoleTime,true),
	TardisFirstSpawn("Première apparition du Tardis",60,new Mode[] {new DWUHC()},40*60,TimerType.TimeDependent,null,true),
	TardisDelay("Temps avant le changement d'emplacement du Tardis",60,new Mode[] {new DWUHC()},10*60,TimerType.Literal,null,false)
	;
	private final String name;
	private final int min;
	private final Mode[] compatibility;
	private final int baseTime;
	private final TimerType type;
	private final Timer bound;
	private final boolean draw;
	Timer(String name,int min,Mode[] compatibility,int baseTime,TimerType type,@Nullable Timer bound,boolean draw) {
		this.name =name;
		this.min=min;
		this.compatibility=compatibility;
		this.baseTime=baseTime;
		this.type=type;
		this.bound=bound;
		this.draw = draw;
	}
	public TimerType getType() {
		return type;
	}
	public String getName(){
		return this.name;
	}
	public int getMinimal(){
		return this.min;
	}
	public Timer getBound() {
		return bound;
	}
	public Mode[] getCompatibility(){
		return compatibility;
	}
	public boolean isCompatible(Mode mode){
	    return Main.containmod(compatibility,mode);
    }
	public int getBaseTime(){
		return baseTime;
	}
    public boolean isDraw(){return draw;}
}