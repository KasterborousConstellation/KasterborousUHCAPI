package fr.supercomete.head.API;
import fr.supercomete.head.Exception.AlreadyRegisterdScenario;
import fr.supercomete.head.Exception.AlreadyRegisteredConfigurable;
import fr.supercomete.head.Exception.UnregisteredModeException;
import fr.supercomete.head.GameUtils.Game;
import fr.supercomete.head.GameUtils.GameConfigurable.KasterBorousConfigurable;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.Command;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;
import fr.supercomete.head.core.KasterborousRunnable;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.KasterBorousCamp;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.structure.StructureHandler;
import fr.supercomete.head.world.BiomeGenerator;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import java.util.concurrent.CopyOnWriteArrayList;

public class KtbsProvider implements HostProvider,GameProvider,MapProvider,RoleProvider,ModeProvider,ConfigurableProvider,KTBSRunnableProvider,ScenariosProvider{
    private final ArrayList<Mode> registeredModes = new ArrayList<>();
    @Override
    public ArrayList<UUID> getCohosts() {
        return Main.cohost;
    }

    @Override
    public UUID getHost() {
        return Main.host;
    }

    @Override
    public boolean isHost(Player player) {
        assert Main.host!=null;
        return Main.host.equals(player.getUniqueId());
    }

    @Override
    public boolean isHost(UUID uuid) {
        assert Main.host!=null;
        return Main.host.equals(uuid);
    }

    @Override
    public boolean isCohost(UUID uuid) {
        return Main.cohost.contains(uuid);
    }

    @Override
    public boolean isCohost(Player player) {
        return Main.cohost.contains(player.getUniqueId());
    }

    @Override
    public boolean isBypassed(Player player) {
        return (isCohost(player)||isHost(player))&&Main.bypass.contains(player.getUniqueId());
    }

    @Override
    public boolean isBypassed(UUID uuid) {
        return (isCohost(uuid)||isHost(uuid))&&Main.bypass.contains(uuid);
    }

    @Override
    public Game getCurrentGame() {
        return Main.currentGame;
    }

    @Override
    public ArrayList<UUID> getPlayerList() {
        return Main.getPlayerlist();
    }

    @Override
    public BiomeGenerator getBiomeGenerator() {
        return Main.generator;
    }

    @Override
    public Map<UUID, Integer> getDiamondLimit() {
        return Main.diamondmap;
    }

    @Override
    public StructureHandler getStructureHandler() {
        return Main.structurehandler;
    }

    @Override
    public MapHandler.Map getMap() {
        return MapHandler.getMap();
    }

    @Override
    public boolean IsMapGenerated() {
        return MapHandler.getMap()!=null;
    }

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

    @Override
    public Role getRoleByClass(Class<?> clz) {
        try {
            return (Role) clz.getConstructors()[0].newInstance(UUID.randomUUID());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                 | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CopyOnWriteArrayList<Class<?>> getRolesByCamp(Mode mode, KasterBorousCamp camp) {
        @SuppressWarnings("unchecked")
        CopyOnWriteArrayList<Class<?>> formatted = (CopyOnWriteArrayList<Class<?>>) mode.getRegisteredrole().clone();
        formatted.removeIf(clazz -> Objects.requireNonNull(getRoleByClass(clazz)).getCamp() != camp);
        return formatted;
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

    @Override
    public void registerMode(Mode mode) {
        registeredModes.add(mode);
        if (mode instanceof Command) {
            final Command command = (Command) mode;
            ((CraftServer) Main.INSTANCE.getServer()).getCommandMap().register(command.getCommand().getName(), command.getCommand());
        }
    }

    @Override
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

    @Override
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
    private static final LinkedList<KasterBorousConfigurable> configurables = new LinkedList<>();
    @Override
    public LinkedList<KasterBorousConfigurable> getConfigurables() {
        return configurables;
    }

    @Override
    public void RegisterConfigurable(KasterBorousConfigurable... configurable) {
        RegisterConfigurable(new ArrayList<>(Arrays.asList(configurable)));
    }

    @Override
    public void RegisterConfigurable(ArrayList<KasterBorousConfigurable> configurable) {
        for (final KasterBorousConfigurable scenario : configurables) {
            for (final KasterBorousConfigurable compared : configurable) {
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
        configurables.addAll(configurable);
    }
    private static final ArrayList<KasterborousRunnable> runnables = new ArrayList<>();

    @Override
    public void RegisterRunnable(ArrayList<KasterborousRunnable> ktbs_runnables) {
        for (final KasterborousRunnable scenario : runnables) {
            for (final KasterborousRunnable compared : ktbs_runnables) {
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
        runnables.addAll(ktbs_runnables);
    }
    private static final ArrayList<KasterborousScenario> registered_scenarios = new ArrayList<>();
    @Override
    public ArrayList<KasterborousRunnable> getRunnables() {
        return runnables;
    }

    @Override
    public ArrayList<KasterborousScenario> getScenarios() {
        return registered_scenarios;
    }

    @Override
    public void RegisterScenarios(KasterborousScenario... scenarios) {
        RegisterScenarios(new ArrayList<>(Arrays.asList(scenarios)));
    }

    @Override
    public void RegisterScenarios(ArrayList<KasterborousScenario> scenarios) {
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
}
