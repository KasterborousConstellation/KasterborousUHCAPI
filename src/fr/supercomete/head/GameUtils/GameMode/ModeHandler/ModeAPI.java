package fr.supercomete.head.GameUtils.GameMode.ModeHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.Exception.InvalidRoleClassException;
import fr.supercomete.head.Exception.UnregisteredModeException;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.NakimeCastleRole;
import fr.supercomete.head.role.Role;

public class ModeAPI {
	private static final ArrayList<Mode> registeredModes = new ArrayList<Mode>();
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
		if(!(claz.getSuperclass().equals(Role.class)||claz.getSuperclass().equals(NakimeCastleRole.class)||claz.getSuperclass().equals(DWRole.class))) {
			try {
				throw new InvalidRoleClassException();
			} catch (InvalidRoleClassException e) {
				e.printStackTrace();
				return null;
			}
		}
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
	public static Role getRoleByClass(Class<?> claz,UUID uuid) {
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
