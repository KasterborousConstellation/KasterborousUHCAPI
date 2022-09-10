package fr.supercomete.head.GameUtils.Events.GameEvents;

import fr.supercomete.head.GameUtils.Events.GameEvents.GameEventException.OutOfBoundGameEventChance;
import fr.supercomete.head.GameUtils.Scenarios.Compatibility;
import fr.supercomete.head.core.Main;

import java.util.Random;

public abstract class Event {
    private final Compatibility compatibility;
    private int executionTime;
    private int max;
    private int min;
    private int chance;
    public abstract String getName();
    public abstract String getDescription();
    public Event(final Compatibility compatibility,final int min,final int max,int chance){
        if(chance<0||chance>100){
            try{
                throw new OutOfBoundGameEventChance();
            }catch (Exception e){
                e.printStackTrace();
                chance = 100;
            }
        }
        this.compatibility=compatibility;
        this.min = min;
        this.max=max;
        this.chance=chance;
    }
    public int getMax(){
        return max;
    }
    public int getMin(){
        return min;
    }
    public void addMin(int add){
        this.min+=add;
    }
    public void addMax(int add){
        this.max+=add;
    }
    public void setExecutionTime(){
        this.executionTime = new Random().nextInt(max-min)+min;
    }

    public void setChance(int chance){
        this.chance=chance;
    }
    public void addChance(int add){
        this.chance=this.chance+add;
    }
    public int getChance(){
        return chance;
    }
    public boolean IsCompatible() {
        return this.compatibility.IsCompatible(Main.currentGame.getMode());
    }
    public abstract void onExecutionTime();
    public int getExecutionTime(){
        return executionTime;
    }
}