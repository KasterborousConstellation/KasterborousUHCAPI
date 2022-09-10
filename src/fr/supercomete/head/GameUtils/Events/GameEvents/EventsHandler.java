package fr.supercomete.head.GameUtils.Events.GameEvents;

import fr.supercomete.head.GameUtils.Events.GameEvents.NativeEvents.Exposed;
import fr.supercomete.head.GameUtils.Game;
import fr.supercomete.head.core.Main;

import java.util.ArrayList;
import java.util.Random;

public class EventsHandler {
    public static ArrayList<Event> registered = new ArrayList<>();
    /*
    register Default events
     */
    public static void init(){
        register(new Exposed(60,120,10));
    }
    /*
    Use this method to register a new event.
     */
    public static void register(Event event){
        registered.add(event);
    }
    public static void onLauch(){
        final Game game = Main.currentGame;
        float proba = new Random().nextFloat();
        proba *=100;
        for(Event event : registered){
            if(proba <= event.getChance()){
                event.setExecutionTime();
                game.getGameEvents().add(event);
            }
        }
    }
}