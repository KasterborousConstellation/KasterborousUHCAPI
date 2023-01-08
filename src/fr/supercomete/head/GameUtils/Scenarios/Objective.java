package fr.supercomete.head.GameUtils.Scenarios;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

import java.util.logging.Level;

public class Objective {
    private int zombie_kill=0, spider_kill=0, sqlt_kill=0,creepers_kill=0;
    private int teamsize;
    private boolean completed=false;
    public Objective(int teamsize){
        this.teamsize=teamsize;
    }

    public boolean kill(EntityType type){
        if(isCompleted()){
            return false;
        }
        switch (type){
            case ZOMBIE:
                zombie_kill++;
                break;
            case SKELETON:
                sqlt_kill++;
                break;
            case SPIDER:
                spider_kill++;
                break;
            case CREEPER:
                creepers_kill++;
                break;
        }
        return isCompleted();
    }
    public void setRewarded(boolean bool ){
        completed=bool;
    }
    public boolean IsRewarded(){
        return completed;
    }
    public boolean isCompleted(){
        return zombie_kill>=5*teamsize&&spider_kill>=5*teamsize&&sqlt_kill>=5*teamsize&&creepers_kill>=5*teamsize;
    }
}