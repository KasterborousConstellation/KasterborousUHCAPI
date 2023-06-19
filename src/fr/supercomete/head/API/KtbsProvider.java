package fr.supercomete.head.API;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.Exception.AlreadyRegisterdScenario;
import fr.supercomete.head.Exception.AlreadyRegisteredConfigurable;
import fr.supercomete.head.Exception.InvalidCommandException;
import fr.supercomete.head.Exception.UnregisteredModeException;
import fr.supercomete.head.GameUtils.Fights.Fight;
import fr.supercomete.head.GameUtils.Fights.FightHandler;
import fr.supercomete.head.GameUtils.Game;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.GameConfigurable.KasterBorousConfigurable;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.Command;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.TeamMode;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;
import fr.supercomete.head.GameUtils.KTBS_Team;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.PlayerUtils.*;
import fr.supercomete.head.core.KasterborousRunnable;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.permissions.PermissionManager;
import fr.supercomete.head.permissions.Permissions;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.KasterBorousCamp;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.schema.SchemaFileHandler;
import fr.supercomete.head.schema.utility.SchemaCondition;
import fr.supercomete.head.schema.utility.SchemaVariable;
import fr.supercomete.head.structure.StructureHandler;
import fr.supercomete.head.world.BiomeGenerator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
public class KtbsProvider implements
        PlayerProvider,TeamProvider,PotionEffectProvider,
        FightProvider,HostProvider,GameProvider,MapProvider,
        RoleProvider,ModeProvider,ConfigurableProvider,
        KTBSRunnableProvider,ScenariosProvider,PermissionProvider,
        SchemaProvider
{
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
    public void finalHeal() {
        update();
        Main.finalheal();
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
    public void DisplayRole(Player player) {
        update();
        RoleHandler.DisplayRole(player);
    }

    @Override
    public boolean IsCompoHiden() {
        update();
        return RoleHandler.IsHiddenRoleNCompo;
    }

    @Override
    public void setCompoHiden(boolean hide) {
        update();
        RoleHandler.IsHiddenRoleNCompo=hide;
    }

    @Override
    public UUID getWhoHaveRole(Class<?> clz) {
        update();
        return RoleHandler.getWhoHaveRole(clz);
    }

    @Override
    public String FormalizedWhoHaveRole(Class<?> clz) {
        update();
        return RoleHandler.FormalizedGetWhoHaveRole(clz);
    }

    @Override
    public boolean isRoleGenerated() {
        update();
        return RoleHandler.IsRoleGenerated();
    }
    @SuppressWarnings("unchecked")
    @Override
    public HashMap<UUID, Role> getRoleMap() {
        update();
        return (HashMap<UUID, Role>) RoleHandler.getRoleList().clone();
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
        if (mode instanceof Command) {
            final Command command = (Command) mode;
            if(command.getCommand()==null){
                try {
                    throw new InvalidCommandException("An error occured while registering the Mode: "+mode.getName()+" Maybe you forgot to specify a KasterborousCommand ?");
                } catch (InvalidCommandException e) {
                    e.printStackTrace();
                    return;
                }
            }
            ((CraftServer) Main.INSTANCE.getServer()).getCommandMap().register(command.getCommand().getName(), command.getCommand());
        }
        registeredModes.add(mode);
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

    @Override
    public int getDataFrom(KasterBorousConfigurable configurable) {
        update();
        return Main.currentGame.getDataFrom(configurable);
    }

    @Override
    public boolean getBooleanDataFrom(KasterBorousConfigurable configurable) {
        update();
        return Configurable.ExtractBool(configurable);
    }

    @Override
    public boolean translateDataToBoolean(int data) {
        update();
        return Configurable.ExtractBool(data);
    }

    @Override
    public int getDataFrom(String name) {
        update();
        for(Configurable config:Main.currentGame.getConfigList()){
            if(config.getId().getName().equalsIgnoreCase(name)){
                return config.getData();
            }
        }
        return 0;
    }

    @Override
    public void setDataOf(KasterBorousConfigurable kasterBorousConfigurable,int data) {
        update();
        for(Configurable config:Main.currentGame.getConfigList()){
            if(config.getId().equals(kasterBorousConfigurable)){
                if(config.getId().getBound().Inbound(data)){
                    config.setData(data);
                }
            }
        }
    }

    @Override
    public void setDataOf(String name,int data) {
        update();
        for(Configurable config:Main.currentGame.getConfigList()){
            if(config.getId().getName().equals(name)){
                if(config.getId().getBound().Inbound(data)){
                    config.setData(data);
                }
            }
        }
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
    public boolean IsScenarioActivated(String name) {
        for(KasterborousScenario ks : Main.currentGame.getScenarios()){
            if(ks.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    @Override
    public @Nullable KasterborousScenario getActivatedScenario(String name) {
        for(KasterborousScenario ks : Main.currentGame.getScenarios()){
            if(ks.getName().equals(name)){
                return ks;
            }
        }
        return null;
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

    @Override
    public void addBonus(Player player, Bonus bonus) {
        update();
        BonusHandler.addBonus(player,bonus);
    }

    @Override
    public int getBonus(Player player, BonusType type) {
        update();
        return BonusHandler.getTotalOfBonus(player,type);
    }

    @Override
    public void addBonus(UUID uuid, Bonus bonus) {
        update();
        BonusHandler.addBonus(uuid,bonus);
    }

    @Override
    public int getBonus(UUID uuid, BonusType type) {
        update();
        return BonusHandler.getTotalOfBonus(uuid,type);
    }

    @Override
    public KTBS_Team getTeamOf(Player player) {
        if(!(getCurrentGame().getMode()instanceof TeamMode))
        {
            return null;
        }
        update();
        return TeamManager.getTeamOfUUID(player.getUniqueId());
    }

    @Override
    public KTBS_Team getTeamOf(UUID uuid) {
        if(!(getCurrentGame().getMode()instanceof TeamMode))
        {
            return null;
        }
        update();
        return TeamManager.getTeamOfUUID(uuid);
    }

    @Override
    public ChatColor convertShortToColor(short color) {
        if(!(getCurrentGame().getMode()instanceof TeamMode))
        {
            return null;
        }
        update();
        return TeamManager.getColorOfShortColor(color);
    }

    @Override
    public ArrayList<KTBS_Team> getTeams(){
        if(!(getCurrentGame().getMode()instanceof TeamMode))
        {
            return null;
        }
        update();
        return TeamManager.teamlist;
    }

    @Override
    public void resetTeams() {
        if(!(getCurrentGame().getMode()instanceof TeamMode))
        {
            return;
        }
        if(Main.currentGame.getGamestate()!= Gstate.Waiting){
            return;
        }
        update();
        TeamManager.setupTeams();
    }

    @Override
    public void setNumberOfMemberPerTeam(int number) {
        if(!(getCurrentGame().getMode()instanceof TeamMode))
        {
            return;
        }
        update();
        TeamManager.NumberOfPlayerPerTeam=number;
    }

    @Override
    public void setTeamNumber(int number) {
        if(!(getCurrentGame().getMode()instanceof TeamMode))
        {
            return;
        }
        update();
        TeamManager.TeamNumber=number;
    }

    @Override
    public int getTeamNumber() {
        if(!(getCurrentGame().getMode()instanceof TeamMode))
        {
            return-1;
        }
        update();
        return TeamManager.TeamNumber;
    }

    @Override
    public int getNumberOfMemberPerTeam() {
        if(!(getCurrentGame().getMode()instanceof TeamMode))
        {
            return -1;
        }
        update();
        return TeamManager.NumberOfPlayerPerTeam;
    }
    @Override
    public Inventory getStartInventory() {
        return PlayerUtility.getInventory();
    }

    @Override
    public String getNameFromUUID(UUID uuid) {
        return PlayerUtility.getNameByUUID(uuid);
    }

    @Override
    public void sendActionBarMessage(Player player, String message) {
        PlayerUtility.sendActionbar(player,message);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public Player getTargetedPlayer(Player player, int range) {
        return PlayerUtility.getTarget(player,range);
    }

    @Override
    public Offline_Player getOfflinePlayer(Player player) {
        update();
        return Main.currentGame.getOffline_Player(player);
    }

    @Override
    public Offline_Player getOfflinePlayer(UUID uuid) {
        update();
        return (Main.currentGame.getOffline_Player(uuid)==null)?
                new Offline_Player(Bukkit.getPlayer(uuid))
                :
                Main.currentGame.getOffline_Player(uuid)
                ;
    }

    @Override
    public ArrayList<Offline_Player> getOfflinePlayers() {
        update();
        return Main.currentGame.getOfflinelist();
    }

    @Override
    public boolean IsPlayerInGame(UUID uuid){
        update();
        if(Bukkit.getPlayer(uuid)==null){
            return false;
        }
        Player player =Bukkit.getPlayer(uuid);
        if(!player.isOnline()){
            return false;
        }
        return Main.getPlayerlist().contains(uuid);
    }

    @Override
    public boolean IsPlayerAlive(UUID uuid) {
        update();
        return Main.getPlayerlist().contains(uuid);
    }

    @Override
    public ArrayList<UUID> getAllowedPlayers(Permissions permission) {
        update();
        ArrayList<UUID> array = new ArrayList<>();
        for(Map.Entry<UUID,ArrayList<Permissions>> entry : PermissionManager.getPerms().entrySet()){
            for(Permissions perm : entry.getValue()){
                if(perm==permission){
                    array.add(entry.getKey());
                }
            }
        }
        return array;
    }

    @Override
    public void retrievePermission(Player player, Permissions permission) {
       retrievePermission(player.getUniqueId(),permission);
    }

    @Override
    public boolean IsAllowed(Player player, Permissions permission) {
        return IsAllowed(player.getUniqueId(),permission);
    }

    @Override
    public void givePermission(Player player, Permissions permission) {
        givePermission(player.getUniqueId(),permission);
    }

    @Override
    public void retrievePermission(UUID uuid, Permissions permission) {
        update();
        PermissionManager.getPerms().get(uuid).removeIf(perm -> perm==permission);
    }

    @Override
    public boolean IsAllowed(UUID uuid, Permissions permission) {
        update();
        return PermissionManager.getPerms().containsKey(uuid) && (PermissionManager.getPerms().get(uuid).contains(permission));
    }

    @Override
    public void givePermission(UUID uuid, Permissions permission) {
        update();
        if(!PermissionManager.getPerms().containsKey(uuid)){
            PermissionManager.getPerms().put(uuid,new ArrayList<>(Collections.singletonList(permission)));
            return;
        }
        if(!PermissionManager.getPerms().get(uuid).contains(permission)){
            PermissionManager.getPerms().get(uuid).add(permission);
        }
    }

    @Override
    public void sendDenyPermissionMessage(Player player) {
        update();
        PermissionManager.sendDenyPermission(player);
    }

    @Override
    public ArrayList<Permissions> getHostPermissions() {
        update();
        return PermissionManager.host_perms;
    }

    @Override
    public ArrayList<Permissions> getCoHostPermissions() {
        update();
        return PermissionManager.cohost_perms;
    }

    @Override
    public void register(String name, SchemaVariable variable) {
        update();
        Main.scoreboardEnvironment.register(name,variable);
    }

    @Override
    public void register(String name, SchemaCondition line_condition) {
        update();
        Main.scoreboardEnvironment.register(name,line_condition);
    }

    @Override
    public File loadFromRessources(String name, JavaPlugin loaded_from) {
        update();
        return SchemaFileHandler.getFile(name,loaded_from);
    }
}