package fr.supercomete.head.GameUtils.GameMode.ModeHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import com.sun.istack.internal.NotNull;
import fr.supercomete.head.API.GameProvider;
import fr.supercomete.head.API.HostProvider;
import fr.supercomete.head.API.KtbsProvider;
import fr.supercomete.head.API.MapProvider;
import fr.supercomete.head.Exception.AlreadyRegisterdScenario;
import fr.supercomete.head.Exception.AlreadyRegisteredConfigurable;
import fr.supercomete.head.Exception.UnregisteredModeException;
import fr.supercomete.head.GameUtils.Game;
import fr.supercomete.head.GameUtils.GameConfigurable.KasterBorousConfigurable;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.Command;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;
import fr.supercomete.head.core.KasterborousRunnable;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.KasterBorousCamp;
import fr.supercomete.head.role.Role;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;


public class KtbsAPI {
    private KtbsProvider provider;
    private static final ArrayList<KasterborousRunnable> runnables = new ArrayList<>();
    private static final ArrayList<Mode> registeredModes = new ArrayList<>();
    private static final ArrayList<KasterborousScenario> registered_scenarios = new ArrayList<>();
    private static final LinkedList<KasterBorousConfigurable> configurables = new LinkedList<>();
    public HostProvider getHostProvider(){
        return provider;
    }
    public GameProvider getGameProvider(){
        return provider;
    }
    public MapProvider getMapProvider(){return provider;}
    public static Game getCurrentGame() {
        return Main.currentGame;
    }

    public static LinkedList<KasterBorousConfigurable> getConfigurables() {
        return configurables;
    }

    public static ArrayList<KasterborousScenario> getScenarios() {
        return registered_scenarios;
    }

    public static void RegisterScenarios(KasterborousScenario... scenarios) {
        RegisterScenarios(new ArrayList<>(Arrays.asList(scenarios)));
    }

    public static ArrayList<KasterborousRunnable> getRunnables() {
        return runnables;
    }

    public static void RegisterRunnable(ArrayList<KasterborousRunnable> scenarios) {
        for (final KasterborousRunnable scenario : runnables) {
            for (final KasterborousRunnable compared : scenarios) {
                if (scenario.equals(compared)) {
                    try {
                        throw new AlreadyRegisterdScenario("Duplicate runnable registration");
                    } catch (AlreadyRegisterdScenario e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
        runnables.addAll(scenarios);
    }

    public static void RegisterScenarios(ArrayList<KasterborousScenario> scenarios) {
        for (final KasterborousScenario scenario : registered_scenarios) {
            for (final KasterborousScenario compared : scenarios) {
                if (scenario.equals(compared)) {
                    try {
                        throw new AlreadyRegisterdScenario("Duplicate scenario registration");
                    } catch (AlreadyRegisterdScenario e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
        registered_scenarios.addAll(scenarios);
    }

    public static void RegisterConfigurable(KasterBorousConfigurable... scenarios) {
        RegisterConfigurable(new ArrayList<>(Arrays.asList(scenarios)));
    }

    public static void RegisterConfigurable(ArrayList<KasterBorousConfigurable> scenarios) {
        for (final KasterBorousConfigurable scenario : configurables) {
            for (final KasterBorousConfigurable compared : scenarios) {
                if (scenario.equals(compared)) {
                    try {
                        throw new AlreadyRegisteredConfigurable("Duplicate configurable registration");
                    } catch (AlreadyRegisteredConfigurable e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
        configurables.addAll(scenarios);
    }

    public static Mode getPrimitiveModeByClass(Class<?> claz) {
        for (Mode mode : registeredModes) {
            if (mode.getClass().equals(claz)) return mode;
        }
        try {
            throw (new UnregisteredModeException());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getRoleClassByString(String str) {
        for (Class<?> cl : Main.currentGame.getMode().getRegisteredrole()) {
            Role role = getRoleByClass(cl);
            if (Objects.requireNonNull(role).getName().equals(str)) {
                return cl;
            }
        }
        return null;
    }

    public static Role getRoleByClass(Class<?> claz) {
        try {
            return (Role) claz.getConstructors()[0].newInstance(UUID.randomUUID());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                 | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static CopyOnWriteArrayList<Class<?>> getRoleinModebyCamp(Mode mode, KasterBorousCamp camp) {
        @SuppressWarnings("unchecked")
        CopyOnWriteArrayList<Class<?>> formatted = (CopyOnWriteArrayList<Class<?>>) mode.getRegisteredrole().clone();
        formatted.removeIf(clazz -> Objects.requireNonNull(getRoleByClass(clazz)).getCamp() != camp);
        return formatted;
    }
    private static Role getRoleByClass(@NotNull Class<?> clazz, UUID uuid) {
        try {
            return (Role) clazz.getConstructors()[0].newInstance(uuid);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                 | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void registerMode(Mode mode) {
        registeredModes.add(mode);
        if (mode instanceof Command) {
            final Command command = (Command) mode;
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

    public static Mode getModeByName(String rep) {
        for (Mode mode : registeredModes) {
            if (mode.getName().equals(rep)) {
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
    public static Mode getMode(Class<?> mode) {
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