package fr.supercomete.head.API;


import fr.supercomete.head.Exception.UnregisteredModeException;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.Command;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.KasterBorousCamp;
import fr.supercomete.head.role.Role;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Registerer implements RoleRegisterer,ModeRegisterer {
    private final ArrayList<Mode> registeredModes = new ArrayList<>();

    @Override
    public void registerRole(Mode mode, Class<?> role) {
        if(isModeRegistered(mode)){
            mode.RegisterRole(role);
        }else{
            try{
                throw new UnregisteredModeException();
            }catch (UnregisteredModeException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public Class<?> getRoleClassByName(String name) {
        for (Class<?> cl : Main.currentGame.getMode().getRegisteredrole()) {
            Role role = getRoleByClass(cl);
            if (Objects.requireNonNull(role).getName().equals(name)) {
                return cl;
            }
        }
        return null;
    }
    public CopyOnWriteArrayList<Class<?>> getRolesByCamp(Mode mode, KasterBorousCamp camp) {
        @SuppressWarnings("unchecked")
        CopyOnWriteArrayList<Class<?>> formatted = (CopyOnWriteArrayList<Class<?>>) mode.getRegisteredrole().clone();
        formatted.removeIf(clazz -> Objects.requireNonNull(getRoleByClass(clazz)).getCamp() != camp);
        return formatted;
    }
    public Role getRoleByClass(Class<?> claz) {
        try {
            return (Role) claz.getConstructors()[0].newInstance(UUID.randomUUID());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                 | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<Mode> getRegisteredModes() {
        return registeredModes;
    }

    @Override
    public Mode getModeByName(String name) {
        for (Mode mode : registeredModes) {
            if (mode.getName().equals(name)) {
                return mode;
            }
        }
        try {
            throw new UnregisteredModeException();
        } catch (UnregisteredModeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void registerMode(Mode mode) {
        registeredModes.add(mode);
        if (mode instanceof Command) {
            final Command command = (Command) mode;
            ((CraftServer) Main.INSTANCE.getServer()).getCommandMap().register(command.getCommand().getName(), command.getCommand());
        }
    }
    @Override
    public boolean isModeRegistered(Mode mode) {
        for (Mode m : registeredModes) {
            if (m.getClass().equals(mode.getClass())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isModeRegistered(Class<?> clz) {
        for (Mode mode : registeredModes) {
            if (mode.getClass().equals(clz)) {
                return true;
            }
        }
        return false;
    }
    public Mode getMode(Class<?> mode) {
        for (Mode mode_ : registeredModes) {
            if (mode_.getClass().equals(mode)) {
                return mode_;
            }
        }
        try {
            throw new UnregisteredModeException("The mode: " + mode.getClass() + " isn't registered.");
        } catch (UnregisteredModeException e) {
            e.printStackTrace();
        }
        return null;
    }

}
