package fr.supercomete.head.GameUtils.Scenarios;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.GameModeModifier;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
public class ScenarioCompatibility {
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