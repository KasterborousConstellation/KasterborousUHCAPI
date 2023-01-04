package fr.supercomete.head.API;
import fr.supercomete.head.Exception.AlreadyRegisterdScenario;
import fr.supercomete.head.Exception.AlreadyRegisteredConfigurable;
import fr.supercomete.head.Exception.UnregisteredModeException;
import fr.supercomete.head.GameUtils.Fights.Fight;
import fr.supercomete.head.GameUtils.Fights.FightHandler;
import fr.supercomete.head.GameUtils.Game;
import fr.supercomete.head.GameUtils.GameConfigurable.KasterBorousConfigurable;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.Command;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;
import fr.supercomete.head.PlayerUtils.EffectHandler;
import fr.supercomete.head.PlayerUtils.KTBSEffect;
import fr.supercomete.head.core.KasterborousRunnable;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.KasterBorousCamp;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.structure.StructureHandler;
import fr.supercomete.head.world.BiomeGenerator;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
public class KtbsProvider implements PotionEffectProvider,FightProvider,HostProvider,GameProvider,MapProvider,RoleProvider,ModeProvider,ConfigurableProvider,KTBSRunnableProvider,ScenariosProvider{
    private int call = 0;
    private void update(){
        call++;
    }
    private final ArrayList<Mode> registeredModes = new ArrayList<>();
    @Override
    public ArrayList<UUID> getCohosts() {
        update();
        return Main.cohost;
    }

    @Override
    public UUID getHost() {
        update();
        return Main.host;
    }

    @Override
    public boolean isHost(Player player) {
        assert Main.host!=null;
        update();
        return Main.host.equals(player.getUniqueId());
    }

    @Override
    public boolean isHost(UUID uuid) {
        assert Main.host!=null;
        update();
        return Main.host.equals(uuid);
    }

    @Override
    public boolean isCohost(UUID uuid) {
        update();
        return Main.cohost.contains(uuid);
    }
    int getCalls(){
        return call;
    }
    @Override
    public boolean isCohost(Player player) {
        update();
        return Main.cohost.contains(player.getUniqueId());
    }

    @Override
    public boolean isBypassed(Player player) {
        update();
        return (isCohost(player)||isHost(player))&&Main.bypass.contains(player.getUniqueId());
    }

    @Override
    public boolean isBypassed(UUID uuid) {
        update();
        return (isCohost(uuid)||isHost(uuid))&&Main.bypass.contains(uuid);
    }

    @Override
    public Game getCurrentGame() {
        update();
        return Main.currentGame;
    }

    @Override
    public ArrayList<UUID> getPlayerList() {
        update();
        return Main.getPlayerlist();
    }

    @Override
    public BiomeGenerator getBiomeGenerator() {
        update();
        return Main.generator;
    }

    @Override
    public Map<UUID, Integer> getDiamondLimit() {
        update();
        return Main.diamondmap;
    }

    @Override
    public StructureHandler getStructureHandler() {
        update();
        return Main.structurehandler;
    }

    @Override
    public MapHandler.Map getMap() {
        update();
        return MapHandler.getMap();
    }

    @Override
    public boolean IsMapGenerated() {
        update();
        return MapHandler.getMap()!=null;
    }

    @Override
    public void registerRole(Mode mode, Class<?> role) {
        update();
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
        update();
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
        update();
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
        update();
        @SuppressWarnings("unchecked")
        CopyOnWriteArrayList<Class<?>> formatted = (CopyOnWriteArrayList<Class<?>>) mode.getRegisteredrole().clone();
        formatted.removeIf(clazz -> Objects.requireNonNull(getRoleByClass(clazz)).getCamp() != camp);
        return formatted;
    }

    @Override
    public Role getRoleOf(Player player) {
        return RoleHandler.getRoleOf(player);
    }

    @Override
    public Role getRoleOf(UUID uuid) {
        return RoleHandler.getRoleOf(uuid);
    }

    @Override
    public UUID getWhoHaveRole(Class<?> clz) {
        return RoleHandler.getWhoHaveRole(clz);
    }

    @Override
    public String FormalizedWhoHaveRole(Class<?> clz) {
        return RoleHandler.FormalizedGetWhoHaveRole(clz);
    }

    @Override
    public boolean isModeRegistered(Mode mode) {
        update();
        for (Mode m : registeredModes) {
            if (m.getClass().equals(mode.getClass())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isModeRegistered(Class<?> clz) {
        update();
        for (Mode mode : registeredModes) {
            if (mode.getClass().equals(clz)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerMode(Mode mode) {
        update();
        registeredModes.add(mode);
        if (mode instanceof Command) {
            final Command command = (Command) mode;
            ((CraftServer) Main.INSTANCE.getServer()).getCommandMap().register(command.getCommand().getName(), command.getCommand());
        }
    }

    @Override
    public ArrayList<Mode> getRegisteredModes() {
        update();
        return registeredModes;
    }

    @Override
    public Mode getModeByName(String name) {
        update();
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
        update();
        for (Mode mode_ : registeredModes) {
            if (mode_.getClass().equals(mode)) {
                return mode_;
            }
        }
        try {
            throw new UnregisteredModeException("The mode class: " + mode + " isn't registered.");
        } catch (UnregisteredModeException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static final LinkedList<KasterBorousConfigurable> configurables = new LinkedList<>();
    @Override
    public LinkedList<KasterBorousConfigurable> getConfigurables() {
        update();
        return configurables;
    }

    @Override
    public void RegisterConfigurable(KasterBorousConfigurable... configurable) {
        RegisterConfigurable(new ArrayList<>(Arrays.asList(configurable)));
    }

    @Override
    public void RegisterConfigurable(ArrayList<KasterBorousConfigurable> configurable) {
        update();
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
        update();
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
        update();
        return runnables;
    }

    @Override
    public ArrayList<KasterborousScenario> getRegisteredScenarios() {
        update();
        return registered_scenarios;
    }

    @Override
    public void RegisterScenarios(KasterborousScenario... scenarios) {
        RegisterScenarios(new ArrayList<>(Arrays.asList(scenarios)));
    }

    @Override
    public void RegisterScenarios(ArrayList<KasterborousScenario> scenarios) {
        update();
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

    @Override
    public boolean isInCombat(Player player) {
        call++;
        return FightHandler.hasFight(player);
    }

    @Override
    public Player getLastFightStartedWith(Player player) {
        call++;
        return FightHandler.getLastDamagerof(player);
    }

    @Override
    public boolean isInCombatWith(Player player1, Player player2) {
        call++;
        return FightHandler.hasSameFight(new Fight(player1.getUniqueId(),player2.getUniqueId()));
    }

    @Override
    public ArrayList<Fight> getFightOf(Player player) {
        call++;
        ArrayList<Fight>fights = new ArrayList<>();
        for(final Fight fight: FightHandler.currentFight){
            if(fight.getFirst().equals(player.getUniqueId())||fight.getSecond().equals(player.getUniqueId())){
                fights.add(fight);
            }
        }
        return fights;
    }

    @Override
    public boolean hasNullifer(Player player) {
        if(!EffectHandler.effects.containsKey(player.getUniqueId())){
            return false;
        }
        for(KTBSEffect effect : EffectHandler.effects.get(player.getUniqueId())){
            if(effect.type==null){
                return true;
            }
        }
        return false;
    }

    @Override
    public void applyPotionEffect(Player player,KTBSEffect effect) {
        EffectHandler.apply(player,effect);
    }

    @Override
    public ArrayList<PotionEffect> getPotionEffect(Player player){
        ArrayList<PotionEffect>potions=new ArrayList<>();
        List<KTBSEffect> effects = EffectHandler.effects.get(player.getUniqueId());
        if(effects==null||effects.size()==0){
            return new ArrayList<>();
        }
        for(final KTBSEffect effect : EffectHandler.effects.get(player.getUniqueId())){
            if(effect.type!=null){
                potions.add(new PotionEffect(effect.type, effect.duration, effect.level,false,false));
            }
        }
        return potions;
    }
}
