package fr.supercomete.head.GameUtils.GameMode.ModeHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.Exception.AlreadyRegisterdScenario;
import fr.supercomete.head.Exception.AlreadyRegisteredConfigurable;
import fr.supercomete.head.Exception.UnregisteredModeException;
import fr.supercomete.head.GameUtils.Game;
import fr.supercomete.head.GameUtils.GameConfigurable.KasterBorousConfigurable;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.Command;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Role;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
public class ModeAPI {
	private static final ArrayList<Mode> registeredModes = new ArrayList<>();
    private static final ArrayList<KasterborousScenario> registered_scenarios = new ArrayList<>();
    private static final LinkedList<KasterBorousConfigurable> configurables = new LinkedList<>();
    public static Game getCurrentGame(){
        return Main.currentGame;
    }
    public static LinkedList<KasterBorousConfigurable>getConfigurables(){
        return configurables;
    }
    public static ArrayList<KasterborousScenario> getScenarios(){
        return registered_scenarios;
    }
    public static void RegisterScenarios(KasterborousScenario... scenarios){
        RegisterScenarios(new ArrayList<>(Arrays.asList(scenarios)));
    }

    public static void RegisterScenarios(ArrayList<KasterborousScenario>scenarios){
        for(final KasterborousScenario scenario: registered_scenarios){
            for(final KasterborousScenario compared : scenarios){
                if(scenario.equals(compared)){
                    try{
                        throw new AlreadyRegisterdScenario("Duplicate scenario registration");
                    }catch (AlreadyRegisterdScenario e){
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
        registered_scenarios.addAll(scenarios);
    }
    public static void RegisterConfigurable(KasterBorousConfigurable... scenarios){
        RegisterConfigurable(new ArrayList<>(Arrays.asList(scenarios)));
    }
    public static void RegisterConfigurable(ArrayList<KasterBorousConfigurable>scenarios){
        for(final KasterBorousConfigurable scenario: configurables){
            for(final KasterBorousConfigurable compared : scenarios){
                if(scenario.equals(compared)){
                    try{
                        throw new AlreadyRegisteredConfigurable("Duplicate configurable registration");
                    }catch (AlreadyRegisteredConfigurable e){
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
        configurables.addAll(scenarios);
    }
	public static Mode getPrimitiveModeByClass(Class<?> claz) {
		for(Mode mode : registeredModes) {
			if(mode.getClass().equals(claz))return mode;
		}
		try {
			throw(new UnregisteredModeException());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Class<?> getRoleClassByString(String str){
		for(Class<?> cl : Main.currentGame.getMode().getRegisteredrole()) {
			Role role = getRoleByClass(cl);
			if(role.getName().equals(str)) {
				return cl;
			}
		}
		return null;
	}
	
	public static Role getRoleByClass(Class<?> claz) {
		/*if(!(claz.getSuperclass().equals(Role.class)||claz.getSuperclass().equals(NakimeCastleRole.class)||claz.getSuperclass().equals(DWRole.class))) {
			try {
				throw new InvalidRoleClassException();
			} catch (InvalidRoleClassException e) {
				e.printStackTrace();
				return null;
			}
		}*/
		try {
			return (Role) claz.getConstructors()[0].newInstance(UUID.randomUUID());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static CopyOnWriteArrayList<Class<?>> getRoleinModebyCamp(Mode mode,Camps camp){
		@SuppressWarnings("unchecked")
		CopyOnWriteArrayList<Class<?>> formated = (CopyOnWriteArrayList<Class<?>>) mode.getRegisteredrole().clone();
		for(Class<?> claz : formated) {
			if(getRoleByClass(claz).getCamp()!=camp) {
				formated.remove(claz);
			}
		}
		return formated;
	}
	private static Role getRoleByClass(Class<?> claz,UUID uuid) {
		try {
			return (Role) claz.getConstructors()[0].newInstance(uuid);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void registerMode(Mode mode) {
		registeredModes.add(mode);
        if(mode instanceof Command){
            final Command command =(Command) mode;

            ((CraftServer) Main.INSTANCE.getServer()).getCommandMap().register(command.getCommand().getName(), command.getCommand());
        }
	}

	public static boolean isModeRegistered(Class<?> cls) {
		for (Mode mode : registeredModes) {
			if (mode.getClass().equals(cls)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isModeRegistered(Mode mode) {
		for (Mode m : registeredModes) {
			if (m.getClass().equals(mode.getClass())) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Mode> getRegisteredModes() {
		return registeredModes;
	}
	public static Mode getModeByIntRepresentation(int rep) {
		return registeredModes.get(rep);
	}
	//We have to represent a mode by an int because Mode class is abstract and can't be saved as a Gson
    public static Mode getMode(Class<?> mode){
	    for(Mode mode_ : registeredModes){
	        if(mode_.getClass().equals(mode)){
	            return mode_;
            }
        }
        try {
            throw new UnregisteredModeException("The mode: "+mode.getClass()+" isn't registered.");
        }catch (UnregisteredModeException e) {
            e.printStackTrace();
        }
	    return null;
    }
	public static int getIntRepresentation(Mode mode) {
		int i =0;
		for(Mode m : ModeAPI.getRegisteredModes()) {
			if(m.getClass().equals(mode.getClass())) {
				return i;
			}
			i++;
		}
		try {
			throw new UnregisteredModeException("Can't convert " +mode.getClass()+" to int because "+mode.getClass()+" is unregistered in "+ ModeAPI.class,new Throwable());
		}catch (UnregisteredModeException e) {
			e.printStackTrace();
			return 0;
		}
	}
}