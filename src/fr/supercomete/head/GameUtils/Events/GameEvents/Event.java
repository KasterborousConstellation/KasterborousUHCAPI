package fr.supercomete.head.GameUtils.Events.GameEvents;

import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.core.Main;

import java.util.Random;

public abstract class Event {
    private final Mode[] compatibility;
    private final int executionTime;
    public Event(final Mode[]compatibility,final int min,final int max){
        this.compatibility=compatibility;
        this.executionTime = new Random().nextInt(min,max);
    }
    public boolean isCompatible(){
        return Main.containmod(compatibility,Main.currentGame.getMode());
    }
    public abstract void onExecutionTime();
    public int getExecutionTime(){
        return executionTime;
    }
}