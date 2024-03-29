package fr.supercomete.head.core;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import fr.supercomete.autoupdater.UpdateChecker;
import fr.supercomete.commands.*;
import fr.supercomete.head.GameUtils.Events.GameEvents.EventsHandler;
import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEventHandler;
import fr.supercomete.head.GameUtils.Fights.FightHandler;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.TeamMode;
import fr.supercomete.head.GameUtils.GameMode.Modes.*;
import fr.supercomete.head.GameUtils.Scenarios.*;
import fr.supercomete.head.GameUtils.KTBS_Team;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.GameUtils.Time.TimerType;
import fr.supercomete.head.Inventory.InventoryManager;
import fr.supercomete.head.Inventory.inventoryapi.Inventoryapi;
import fr.supercomete.head.PlayerUtils.BonusHandler;
import fr.supercomete.head.PlayerUtils.EffectHandler;
import fr.supercomete.head.Exception.MalformedPermissionException;
import fr.supercomete.head.permissions.PermissionManager;
import fr.supercomete.head.permissions.Permissions;
import fr.supercomete.head.schema.ScoreBoardSchemaHandler;
import fr.supercomete.head.Exception.BadExtensionException;
import fr.supercomete.head.Exception.MalformedSchemaException;
import fr.supercomete.head.schema.utility.SchemaEnvironment;
import fr.supercomete.head.world.worldgenerator;
import fr.supercomete.tasks.Cycle;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import fr.supercomete.datamanager.FileManager.ProfileSerializationManager;
import fr.supercomete.enums.GenerationMode;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Game;
import fr.supercomete.head.GameUtils.Lag;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.Listeners.ListenersRegisterer;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.structure.StructureHandler;
import fr.supercomete.head.world.BiomeGenerator;
import fr.supercomete.head.world.WorldGarbageCollector;
import fr.supercomete.head.world.scoreboardmanager;
import fr.supercomete.tasks.GAutostart;

public final class Main extends JavaPlugin {
	public final static String UHCTypo = "§a[§6UHC§a]"+"§7 » ";
	public final String ServerId = getConfig().getString("serverapi.serverconfig.ServerId");
	public final String DiscordLink = getConfig().getString("serverapi.serverconfig.DiscordLink");
	private static boolean forcedpvp = false;
	private static boolean forcebordure = false;
	private static boolean forcerole = false;
	public static ArrayList<UUID> playerlist = new ArrayList<>();
	public static Game currentGame;
	public static boolean devmode;
	public static Map<UUID, Integer> diamondmap = new HashMap<>();
	public static ProfileSerializationManager manager = new ProfileSerializationManager();
	public static BiomeGenerator generator;
	public static StructureHandler structurehandler;
	public static UUID host;
	public static ArrayList<UUID>cohost = new ArrayList<>(4);
	public static ArrayList<UUID>bypass = new ArrayList<>();
	public static Cycle currentCycle=null;
    public static Main INSTANCE;
    public static Location spawn;
    public static ArrayList<String> messages = new ArrayList<>();
    private static  KtbsAPI api;
    public static SchemaEnvironment scoreboardEnvironment;
    @Override
    public void onDisable(){
        for(final KasterborousRunnable run: api.getKTBSRunnableProvider().getRunnables()){
            run.onAPIStop(Bukkit.getServicesManager().load(KtbsAPI.class));
        }
        for(KasterborousScenario scenario : api.getScenariosProvider().getRegisteredScenarios()){
            if(api.getScenariosProvider().IsScenarioActivated(scenario.getName())){
                if(scenario.getAttachedRunnable()!=null){
                    for(KasterborousRunnable runnable: scenario.getAttachedRunnable()){
                        runnable.onAPIStop(api);
                    }
                }
            }
        }

    }
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
        INSTANCE=this;
        /*
        Initialisation du network KTBS
         */
        worldgenerator.init();
        /*
        Update checker init
         */
        UpdateChecker.getLasterVersion(version ->{
        if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
            Bukkit.getLogger().log(Level.INFO,("Kasterborous UHC est à jour! Version: "+this.getDescription().getVersion()));
        } else {
            messages.add("§a*********************************************************************");
            messages.add("Kasterborous UHC est obsolete!");
            messages.add("Nouvelle version: " + version);
            messages.add("Version actuelle: " + this.getDescription().getVersion());
            messages.add("Mise à jour nécessaire: " + UpdateChecker.PLUGIN_URL);
            messages.add("§a*********************************************************************");
            Bukkit.getLogger().log(Level.WARNING,"*********************************************************************");
            Bukkit.getLogger().log(Level.WARNING,"Kasterborous UHC is outdated!");
            Bukkit.getLogger().log(Level.WARNING,"Nouvelle version: " + version);
            Bukkit.getLogger().log(Level.WARNING,"Version actuelle: " + this.getDescription().getVersion());
            Bukkit.getLogger().log(Level.WARNING,"Mise à jour nécessaire: " + UpdateChecker.PLUGIN_URL);
            Bukkit.getLogger().log(Level.WARNING,"*********************************************************************");
        }
        });
        /*
        Load KTBSInventoryAPI
         */
        Inventoryapi.init(this);
        /*
        Whitelist Config
         */
        Bukkit.getServer().setWhitelist(getConfig().getBoolean("serverapi.serverconfig.whitelist"));
        /*
        Spawn Config
         */
        spawn= new Location(Bukkit.getWorld("world"), INSTANCE.getConfig().getInt("serverapi.spawn.x"),INSTANCE.getConfig().getInt("serverapi.spawn.y"),INSTANCE.getConfig().getInt("serverapi.spawn.z"));
        /*
        API init
         */
        Bukkit.getServicesManager().register(KtbsAPI.class,new KtbsAPI(),this, ServicePriority.Lowest);
        api = Bukkit.getServicesManager().load(KtbsAPI.class);
        /*
        Schemas init
        */

        /*
        BiomeGenerator && strutureHandler
         */
		generator = new BiomeGenerator();
		structurehandler= new StructureHandler(this);
		host = null;
		Date Compiledate = null;
		Compiledate = getClassBuildTime();
        for(final Scenarios scenarios: Scenarios.values()){
            api.getScenariosProvider().RegisterScenarios(scenarios);
        }
        //api.getScenariosProvider().RegisterScenarios(new StarterTools());
        api.getScenariosProvider().RegisterScenarios(new MonsterHunter());
        for(final Configurable.LIST conf : Configurable.LIST.values()){
            api.getConfigurableProvider().RegisterConfigurable(conf);
        }
		api.getModeProvider().registerMode(new Null_Mode());
		UHCClassic uhcclassic = new UHCClassic();	
		api.getModeProvider().registerMode(uhcclassic);
		Bukkit.broadcastMessage("§dVersion: 1.5.3" +
                " Build("+Compiledate.getDate()+"/"+(Compiledate.getMonth()+1)+") §cSNAPSHOT");
		currentGame=new Game((new Null_Mode()).getName(),this);
        try {
            scoreboardEnvironment = new SchemaEnvironment();
            ScoreBoardSchemaHandler.init();
        }catch (IOException | BadExtensionException | MalformedSchemaException e) {
            Bukkit.getLogger().log(Level.SEVERE,"KTBS-SEVERE-FAULT: AN ERROR WAS THROWN DURING THE SCHEMA INITIALIZATION.");
            throw new RuntimeException(e);
        }
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 200L, 1L);// LagO'meter
		spawn = new Location(Bukkit.getWorld("world"), getConfig().getInt("serverapi.spawn.x"),
				getConfig().getInt("serverapi.spawn.y"), getConfig().getInt("serverapi.spawn.z"));
		loadconfig();
        BonusHandler.init();
		RoleHandler.setIsRoleGenerated(false);
		Bukkit.getServer().getWorld("world").setDifficulty(Difficulty.PEACEFUL);
		getCommand("menu").setExecutor(new MenuCommand(this));
		getCommand("rules").setExecutor(new RulesCommand(this));
		getCommand("team").setExecutor(new TeamCommand(this));
		getCommand("inv").setExecutor(new InvCommand());
		getCommand("h").setExecutor(new HostCommand(this));
		getCommand("role").setExecutor(new RoleCommand(this));
		getCommand("roles").setExecutor(new RolesCommand());
		getCommand("compo").setExecutor(new RolesCommand());
		getCommand("doc").setExecutor(new docCommand(this));
		getCommand("docs").setExecutor(new docCommand(this));
		getCommand("liens").setExecutor(new docCommand(this));
		getCommand("blseed").setExecutor(new BlackSeedCommand());
		getCommand("blacklistseed").setExecutor(new BlackSeedCommand());
		getCommand("Rolelist").setExecutor(new RolelistCommand(this));
		getCommand("fullinv").setExecutor(new FullInvCommand(this));
		getCommand("helpop").setExecutor(new HelpopCommand(this));
		getCommand("disperse").setExecutor(new DisperseCommand(this));
        getCommand("bypass").setExecutor(new BypassCommand(this));
        getCommand("stuffconfirm").setExecutor(new stuffconfirm(this));
        getCommand("tpin").setExecutor(new TpInCommand(this));
        getCommand("timeleft").setExecutor(new TimeLeftCommand(this));
        getCommand("ti").setExecutor(new TeamInventory());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        scoreboardmanager score = new scoreboardmanager(this);
		score.ChangeScoreboard();
		RoleHandler.IsHiddenRoleNCompo = false;
		WorldGarbageCollector.init(this);
		final PluginManager pm = getServer().getPluginManager();
		if(!ListenersRegisterer.Register(pm,this))Bukkit.broadcastMessage("§4Une erreur fatale est apparue pendant la phase d'initialisation d'écoute de Spigot");
		System.out.println("Activation du plugin");
		// SetupGoldenHeadCraft
		final ItemStack goldenHead = new ItemStack(Material.GOLDEN_APPLE);
		final ItemMeta gMeta = goldenHead.getItemMeta();
		gMeta.setDisplayName(ChatColor.AQUA + "Golden Head");
		gMeta.setLore(Collections.singletonList("§7La Golden Head restore 4 coeurs et donne 2 coueurs d'absorptions!"));
		goldenHead.setItemMeta(gMeta);
		final ShapedRecipe goldenHeadRecipe = new ShapedRecipe(goldenHead);
		goldenHeadRecipe.shape("@@@", "@#@", "@@@");
		goldenHeadRecipe.setIngredient('@', Material.GOLD_INGOT);
		goldenHeadRecipe.setIngredient('#', Material.SKULL_ITEM, 3);
		Bukkit.getServer().addRecipe(goldenHeadRecipe);
		Main.devmode=getConfig().getBoolean("serverapi.serverconfig.devmode");
        EffectHandler.init();
		if(devmode) {
			Bukkit.broadcastMessage("§eDevMode: §aOn");
		}
        EventsHandler.init();
        InventoryManager.init();
        try {
            PermissionManager.init();
        } catch (MalformedPermissionException e) {
            e.printStackTrace();
            PermissionManager.host_perms= PermissionManager.allPerms();
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                //Indique le lancement de l'api
                for (final KasterborousRunnable run : api.getKTBSRunnableProvider().getRunnables()) {
                    run.onAPILaunch(api);
                }
                for (KasterborousScenario scenario : api.getScenariosProvider().getRegisteredScenarios()) {
                    if (api.getScenariosProvider().IsScenarioActivated(scenario.getName())) {
                        if (scenario.getAttachedRunnable() != null) {
                            for (KasterborousRunnable runnable : scenario.getAttachedRunnable()) {
                                runnable.onAPILaunch(api);
                            }
                        }
                    }
                }
                //Choisi un host parmi les joueurs op
                if(host==null){
                    for(Player player : Bukkit.getOnlinePlayers()){
                        if(player.isOp()){
                            setHost(player);
                            break;
                        }
                    }
                }
            }
        }.runTaskLater(this,30L);
	}
    public static void setHost(Player player){
        Main.host = player.getUniqueId();
        PermissionManager.getPerms().put(Main.host,PermissionManager.host_perms);
    }
    private static Date getClassBuildTime() {
        Date d = null;
        Class<?> currentClass = new Object() {}.getClass().getEnclosingClass();
        URL resource = currentClass.getResource(currentClass.getSimpleName() + ".class");
        if (resource != null) {
            switch (resource.getProtocol()) {
                case "file":
                    try {
                        d = new Date(new File(resource.toURI()).lastModified());
                    } catch (URISyntaxException ignored) {
                    }
                    break;
                case "jar": {
                    String path = resource.getPath();
                    d = new Date(new File(path.substring(5, path.indexOf("!"))).lastModified());
                    break;
                }
                case "zip": {
                    String path = resource.getPath();
                    File jarFileOnDisk = new File(path.substring(0, path.indexOf("!")));

                    try (JarFile jf = new JarFile(jarFileOnDisk)) {
                        ZipEntry ze = jf.getEntry(path.substring(path.indexOf("!") + 2));
                        long zeTimeLong = ze.getTime();
                        d = new Date(zeTimeLong);
                    } catch (IOException | RuntimeException ignored) {
                    }
                    break;
                }
            }
        }
        return d;
    }
    public static void updateBypass(){
        bypass.removeIf(uu -> !(api.getPermissionProvider().IsAllowed(uu, Permissions.Allow_bypass)));
    }
    public static boolean IsHost(UUID player) {
       return host!=null && host.equals(player);
    }
	public static boolean IsHost(Player player) {
        return host!=null && host.equals(player.getUniqueId());
	}
	public static boolean IsCohost(Player player) {
		return cohost.contains(player.getUniqueId());
	}
    public static boolean IsCohost(UUID player) {
        return cohost.contains(player);
    }
	public void loadconfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	public static String getCheckMark(boolean bool) {
		return bool?"§a✔":"§c✖";
	}

	public void StartGame(Player player) {
        FightHandler.reset();
        PlayerEventHandler.resetEvents();
        currentGame.getFullinv().clear();
        forcebordure = false;
        forcerole = false;
        forcedpvp = false;
        if(BiomeGenerator.generation){
            player.sendMessage(UHCTypo+"§aDésactivez la génération de nouvelles graines avant de lancer une partie.");
            return;
        }
		if (this.getGenmode().equals(GenerationMode.Generating)) {
			player.sendMessage(UHCTypo + "§cLa génération de la carte est déjà en cours");
			return;
		}
        if(currentGame.getMode()instanceof TeamMode){
            final int i =countnumberofplayer();
            int e = 0;
            for(KTBS_Team team : TeamManager.teamlist){
                e+= team.getMaxPlayerAmount();
            }
            if(e<i){
                player.sendMessage(Main.UHCTypo+"Tous les joueurs ne pourront pas avoir une équipe. Veuillez ajouter de nouvelles équipes ou augmenter le nombre de place dans celles-ci");
                return;
            }
        }
		if (CountIntegerValue(currentGame.getRoleCompoMap()) < countnumberofplayer() && currentGame.getMode() instanceof CampMode) {
			player.sendMessage(UHCTypo + "§cIl n'y a pas assez de rôles pour commencer la partie  "
					+ currentGame.getRoleCompoMap().size() + "/" + countnumberofplayer() + "(Minimum)");
			return;
		}
		if (this.getGenmode().equals(GenerationMode.None)||this.getGenmode().equals(GenerationMode.WorldCreatedOnly)) {
			player.sendMessage(
					UHCTypo + "§cLa génération de la carte doit être faite avant de pouvoir lancer une partie");
			return;
		}
        EventsHandler.onLauch();
		diamondmap.replaceAll((k, v) -> 0);
		if (Main.currentGame.getGamestate() == Gstate.Waiting) {
			for (KasterborousScenario sc : Main.currentGame.getScenarios()) {
				if (!sc.getCompatiblity().IsCompatible(Main.currentGame.getMode())) {
					Main.currentGame.getScenarios().remove(sc);
					player.sendMessage(UHCTypo + "§cErreur, le scénario +" + sc.getName() + "est incompatible");
				}
			}
			allplayereffectclear();
			Main.currentGame.setGamestate(Gstate.Starting);
			Main.currentGame.setFirstBorder(Main.currentGame.getCurrentBorder());
            assert MapHandler.getMap() != null;
            MapHandler.getMap().getPlayWorld().setGameRuleValue("naturalRegeneration", "false");
            MapHandler.getMap().getPlayWorld().setGameRuleValue("doFireTick", "false");
            MapHandler.getMap().getPlayWorld().setGameRuleValue("doMobSpawning", "true");
            MapHandler.getMap().getPlayWorld().setGameRuleValue("doDaylightCycle", "false");
            MapHandler.getMap().getPlayWorld().setGameRuleValue("keepinventory", "true");
            MapHandler.getMap().getPlayWorld().setGameRuleValue("announceAdvancements","false");
            MapHandler.getMap().getPlayWorld().setDifficulty(Difficulty.HARD);
			GAutostart start = new GAutostart();
            MapHandler.getMap().getPlayWorld().setPVP(false);
			start.runTaskTimer(this, 0, 20);
			
			if (Main.currentGame.getTimer(Timer.PvPTime).getData() < Timer.PvPTime.getMinimal())
				Main.currentGame.getTimer(Timer.PvPTime).setData(Timer.PvPTime.getBaseTime());
			if (Main.currentGame.getTimer(Timer.BorderTime).getData() < Timer.BorderTime.getMinimal())
				Main.currentGame.getTimer(Timer.BorderTime).setData(Timer.BorderTime.getBaseTime());
			if (Main.currentGame.getTimer(Timer.RoleTime).getData() < Timer.RoleTime.getMinimal())
				Main.currentGame.getTimer(Timer.RoleTime).setData(Timer.RoleTime.getBaseTime());
		} else {
			player.sendMessage(UHCTypo + "§cIl y a déjà une partie en cours");
		}
	}
	public static void StopGame(Player player) {

		if (player != null) {
			if (Main.currentGame.getGamestate() == Gstate.Waiting) {
				player.sendMessage(UHCTypo + "§cIl n'y a aucune partie en cours");
				return;
			}
			if (Main.currentGame.getGamestate() == Gstate.Starting) {
				player.sendMessage(UHCTypo + "§cLa partie ne peut être stoppée pendant le démarrage");
				return;
			}
		}
		Main.currentGame.getFullinv().clear();
		RoleHandler.setHistoric(null);
		Main.currentGame.setGamestate(Gstate.Waiting);
		Main.currentGame.setGenmode(GenerationMode.None);
		RoleHandler.setIsRoleGenerated(false);
		RoleHandler.setRoleList(new HashMap<>());
        forcebordure = false;
        forcedpvp = false;
        forcerole = false;
        allplayereffectclear();
        currentGame.setTime(0);
        Bukkit.getWorld("world").setDifficulty(Difficulty.PEACEFUL);
		new BukkitRunnable(){
            @Override
            public void run() {
                for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                    pl.teleport(spawn);
                    pl.setGameMode(GameMode.ADVENTURE);
                    pl.getInventory().clear();
                    pl.getInventory().setHelmet(null);
                    pl.getInventory().setChestplate(null);
                    pl.getInventory().setLeggings(null);
                    pl.getInventory().setBoots(null);
                    pl.setMaxHealth(20);
                    if(Main.currentGame.isGameState(Gstate.Waiting)) {
                        pl.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 0));
                    }
                }
                MapHandler.setMapToNull();
            }
        }.runTaskLater(INSTANCE,15);
        new BukkitRunnable(){
            @Override
            public void run() {
                for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                    pl.setGameMode(GameMode.ADVENTURE);
                    if (Main.IsHost(pl)) {
                        PlayerUtility.GiveHotBarStuff(pl);
                    }
                }
            }
        }.runTaskLater(INSTANCE,22);
	}
	public static boolean containmod(Mode[] list, Mode testedMode) {
		for (Mode m : list) {
			if(m.getClass().equals(testedMode.getClass()))return true;
		}
		return false;
	}
	public void addScenarios(KasterborousScenario sc) {
		Main.currentGame.getScenarios().add(sc);
	}
	public void removeScenarios(KasterborousScenario sc) {
		Main.currentGame.getScenarios().remove(sc);
	}
	public static void allplayereffectclear() {
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (pl != null)
				for (PotionEffect effect : pl.getActivePotionEffects()) {
					pl.removePotionEffect(effect.getType());
				}
		}
	}
	public static ArrayList<UUID>  getPlayerlist() {
		return playerlist;
	}
	public void setPlayerlist(ArrayList<UUID> playerlist) {
		Main.playerlist = playerlist;
	}
	public void addPlayerList(UUID uu) {
		Main.playerlist.add(uu);
	}

	public static String transformScoreBoardType(int v, String MiddleColor, String NumberColor) {
		String m = (v / 60) +"";
		String s = (v % 60) +"";
		s = (s.length() == 1) ? "0" + s : s;
		m = (m.length() == 1) ? "0" + m : m;
		return NumberColor + m + MiddleColor + ":" + NumberColor + s;
	}
	public GenerationMode getGenmode() {
		return Main.currentGame.getGenmode();
	}
	public void setGenmode(GenerationMode genmode) {
		Main.currentGame.setGenmode(genmode);
	}
	public ArrayList<Timer> getCompatibleTimer() {
		ArrayList<Timer> compatible = new ArrayList<Timer>();
		for (Timer t : Timer.values()) {
			if (t.getCompatibility().IsCompatible(Main.currentGame.getMode()))
				compatible.add(t);
		}
		return compatible;
	}
	public String generateNameTimer(Timer t) {
		return "§r" + t.getName() + " §c" + TimeUtility.transform((t.getType()==TimerType.TimeDependent)?Main.currentGame.getTimer(t).getData()-Main.currentGame.getTime():Main.currentGame.getTimer(t).getData(), "§5", "§5", "§d");
	}

	public static int getNumberOfCompatibleScenarios(Mode m) {
		int count = 0;
		for (Scenarios sc : Scenarios.values()) {
			if (sc.getCompatiblity().IsCompatible(m)) {
				count++;
			}
		}
		return count;
	}

	public static ArrayList<String> SplitCorrectlyString(String input, int nCharperline, String LineColor) {
		ArrayList<String> lineoutput = new ArrayList<String>();
		final String[] splitedwordinput = input.split(" ");
		StringBuilder constructor = new StringBuilder();
		for (int i = 0; i < splitedwordinput.length; i++) {
			constructor.append(" ").append(splitedwordinput[i]);
			if (i< splitedwordinput.length-1 &&constructor.length()+splitedwordinput[i+1].length() >= nCharperline) {
				lineoutput.add(((LineColor != null) ? LineColor : "") + constructor);
				constructor = new StringBuilder();
			}
		}
		if (constructor.length() > 0)
			lineoutput.add(((LineColor != null) ? LineColor : "") + constructor);
		return lineoutput;
	}
	public static int countnumberofplayer() {
		int i = 0;
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (pl.getGameMode() != GameMode.SPECTATOR)
				i++;
		}
		return i;
	}
	public static void finalheal() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.setHealth(player.getMaxHealth());
		}
		Bukkit.broadcastMessage(UHCTypo + "§6Final§cHeal");
	}

	public static String TranslateBoolean(boolean b) {
		return (b) ? "§aOn" : "§cOff";
	}
	public boolean isForcedpvp() {
		return forcedpvp;
	}

	public void setForcedpvp(boolean forcedpvp) {
		Main.forcedpvp = forcedpvp;
	}

	public boolean isForcebordure() {
		return forcebordure;
	}

	public void setForcebordure(boolean forcebordure) {
		Main.forcebordure = forcebordure;
	}
	public static void DisplayToPlayerInChat(Player player, String todiplay, int charperline, ChatColor chat) {
		ArrayList<String> strl = SplitCorrectlyString(todiplay, charperline, chat.toString());
		for (String str : strl)
			player.sendMessage(str);
	}

	public static int CountIntegerValue(HashMap<?, Integer> map) {
		int add = 0;
		for (int i : map.values())
			add += i;
		return add;
	}
	public boolean isForceRole() {
		return forcerole;
	}
	public void setForcerole(boolean forcerole) {
		Main.forcerole = forcerole;
	}

	@SuppressWarnings("unchecked")
	public static <T>  T[] convertArrayToTable(Class<?> Tclass,ArrayList<T> array) {
		T[] str ;
		str = (T[]) Array.newInstance(Tclass, array.size());
		for(int i=0;i<array.size();i++) {
			str[i]=array.get(i);
		}
		return str;
	}
    /*
    Return true if the shortest distance in a list<location> is greater or equal than the tested distance
     */
	public static boolean searchintoarrayLocationDistance(List<Location> list,double distance,Location testedLocation) {
		if(list.size()==0)return true;
		double mindistance=distance;
		for(Location loc : list) {
			if((loc.distance(testedLocation))<mindistance) {
				mindistance=loc.distance(testedLocation);
			}
		}
        return mindistance >= distance;
	}
}