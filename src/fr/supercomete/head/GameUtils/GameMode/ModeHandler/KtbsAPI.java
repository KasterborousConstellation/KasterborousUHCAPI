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
import fr.supercomete.head.GameUtils.GameConfigurable.KasterBorousConfigurable;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;
import fr.supercomete.head.core.KasterborousRunnable;


public class KtbsAPI {
    private final KtbsProvider provider;
    public KtbsAPI(){
        provider= new KtbsProvider();
    }
    private static final ArrayList<KasterborousRunnable> runnables = new ArrayList<>();
    private static final ArrayList<KasterborousScenario> registered_scenarios = new ArrayList<>();
    private static final LinkedList<KasterBorousConfigurable> configurables = new LinkedList<>();
    public HostProvider getHostProvider(){
        return provider;
    }
    public GameProvider getGameProvider(){
        return provider;
    }
    public MapProvider getMapProvider(){return provider;}

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






}