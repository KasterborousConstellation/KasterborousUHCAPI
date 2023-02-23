package fr.supercomete.head.GameUtils.Events.PlayerEvents;

public enum PlayerEvents {
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
