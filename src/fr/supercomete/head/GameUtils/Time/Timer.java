package fr.supercomete.head.GameUtils.Time;
import javax.annotation.Nullable;

import fr.supercomete.head.GameUtils.GameMode.Modes.BlackCloverUHC;
import fr.supercomete.head.GameUtils.GameMode.Modes.DWUHC;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.GameMode.Modes.UHCClassic;
import fr.supercomete.head.GameUtils.Scenarios.Compatibility;
import fr.supercomete.head.GameUtils.Scenarios.CompatibilityType;
import fr.supercomete.head.core.Main;

public enum Timer {
	RealEpisodeTime("Durée d'un épisode",180,Compatibility.allModes,1200,TimerType.Literal,null,false),
	EpisodeTime("Durée d'un cycle jour/nuit",180,Compatibility.allModes,600,TimerType.Literal,null,false),
	PvPTime("PvP",60,Compatibility.allModes,1200,TimerType.TimeDependent,null,true),
	BorderTime("Début du mouvement de la bordure",60,Compatibility.allModes,4800,TimerType.TimeDependent,null,true),
	RoleTime("Roles",60,Compatibility.allModes,1200,TimerType.TimeDependent,null,true),
	InvincibilityTime("Fin de l'invincibilité",10,Compatibility.allModes,15,TimerType.TimeDependent,null,false),
	ChoiceDelay("Delai maximum de choix de version",30,Compatibility.allModes,5*60,TimerType.Literal,null,false),
	CyberiumTime("Apparition du Cyberium",60,new Compatibility( CompatibilityType.WhiteList,new Class[]{DWUHC.class}),60*50,TimerType.TimeDependent,Timer.RoleTime,true),
	TardisFirstSpawn("Première apparition du Tardis",60,new Compatibility( CompatibilityType.WhiteList,new Class[]{DWUHC.class}),40*60,TimerType.TimeDependent,null,true),
	TardisDelay("Temps avant le changement d'emplacement du Tardis",60,new Compatibility( CompatibilityType.WhiteList,new Class[]{DWUHC.class}),10*60,TimerType.Literal,null,false)
	;
	private final String name;
	private final int min;
	private final Compatibility compatibility;
	private final int baseTime;
	private final TimerType type;
	private final Timer bound;
	private final boolean draw;
	Timer(String name, int min, Compatibility compatibility, int baseTime, TimerType type, @Nullable Timer bound, boolean draw) {
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
	public Compatibility getCompatibility(){
		return compatibility;
	}
	public int getBaseTime(){
		return baseTime;
	}
    public boolean isDraw(){return draw;}
}