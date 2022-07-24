package fr.supercomete.head.GameUtils.Events.PlayerEvents;

public enum PlayerEvents {
    EnterTardis("Entrée Tardis"),
    LeaveTardis("Sortie Tardis"),
    TardisEjection("Ejection du Tardis"),
    GetKey("Obtention d'un clef"),
    Kill("Vient de faire un kill")
    ;
    private final String name;
    PlayerEvents(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
}
