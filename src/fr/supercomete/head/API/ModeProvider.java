package fr.supercomete.head.API;

import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;

import java.util.ArrayList;

public interface ModeProvider {
    boolean isModeRegistered(Mode mode);
    boolean isModeRegistered(Class<?>clz);
    void registerMode(Mode mode);
    ArrayList<Mode> getRegisteredModes();
    Mode getModeByName(String name);
    Mode getMode(Class<?>clz);
}
