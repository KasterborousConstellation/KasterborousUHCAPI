package fr.supercomete.head.GameUtils.Scenarios;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.GameModeModifier;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import org.bukkit.Bukkit;

public class Compatibility {
    /*
    This class is used to specify the compatibility of scenarios.
    You can add a scenario that can only be added to some specified GameMode with a whitelist.
    Or you can add a scenario that can be used by all the GameMode with some exceptions with a blacklist.
    You can add a scenario that work only with some GameModeModifiers like Groupable. That would mean that the scenario can only be used by GameMode
    that use a group system.
     */
    public static Compatibility allModes= new Compatibility(CompatibilityType.BlackList,new Class<?>[0]);
    private CompatibilityType type;
    private Class<?>[] modes;
    public Compatibility(CompatibilityType type, Class<?>[] classes) {
        this.type=type;
        for(Class<?> claz: classes){
            if(!claz.isInterface()&& !claz.getSuperclass().equals(Mode.class)){
                try {
                    throw new CompatibilityException("This compatibility has been wrongly configured. A Class is not a mode and not a Gamemodifier");
                } catch (CompatibilityException e) {
                    e.printStackTrace();
                }
            }
            if(claz.isInterface() && !GameModeModifier.class.isAssignableFrom(claz)){
                try {
                    throw new CompatibilityException("This compatibility has been wrongly configured. A Class is not a mode and not a Gamemodifier");
                } catch (CompatibilityException e) {
                    e.printStackTrace();
                }
            }
        }
        this.modes=classes;
    }
    public boolean IsCompatible(Mode mode){
        assert (modes!=null);
        boolean isWhite=type== CompatibilityType.WhiteList;
        for(Class<?> innerMode : modes){
            if(innerMode.isInterface()){
                for(Class<?> interfaces :mode.getClass().getInterfaces()){
                    if(interfaces.equals(innerMode)){
                        return isWhite;
                    }
                }
            }else{
                if(innerMode.equals(mode.getClass())){
                    return isWhite;
                }
            }

        }
        return !isWhite;
    }
}