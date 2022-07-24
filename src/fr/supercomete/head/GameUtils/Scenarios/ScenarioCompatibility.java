package fr.supercomete.head.GameUtils.Scenarios;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.GameModeModifier;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
public class ScenarioCompatibility {
    /*
    This class is used to specify the compatibility of scenarios.
    You can add a scenario that can only be added to some specified GameMode with a whitelist.
    Or you can add a scenario that can be used by all the GameMode with some exceptions with a blacklist.
    You can add a scenario that work only with some GameModeModifiers like Groupable. That would mean that the scenario can only be used by GameMode
    that use a group system.
     */
    public static ScenarioCompatibility allModes= new ScenarioCompatibility(ScenarioCompatibilityType.BlackList,new Mode[0]);
    private ScenarioCompatibilityType type;
    private Mode[] modes;
    private GameModeModifier[] modifiers;
    public ScenarioCompatibility(ScenarioCompatibilityType type, Mode[] modes){
        this.type=type;
        this.modes=modes;
    }
    public ScenarioCompatibility(ScenarioCompatibilityType type, GameModeModifier[] modifiers){
        this.type=type;
        this.modifiers=modifiers;
    }
    public boolean IsCompatible(Mode mode){
        assert (modes==null^modifiers==null);
        boolean isWhite=type== ScenarioCompatibilityType.WhiteList;
        if(modes!=null){
            for(Mode innerMode : modes){
                if(innerMode.getClass().equals(mode.getClass())){
                    return isWhite;
                }
            }
            return !isWhite;
        }
        for(GameModeModifier innerModifier : modifiers){
            if(innerModifier.getClass().equals(mode.getClass())){
                return isWhite;
            }
        }
        return !isWhite;
    }
}