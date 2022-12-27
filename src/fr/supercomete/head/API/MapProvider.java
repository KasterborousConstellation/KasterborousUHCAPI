package fr.supercomete.head.API;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;

public interface MapProvider {
    MapHandler.Map getMap();
    boolean IsMapGenerated();
}
