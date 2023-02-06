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
import fr.supercomete.commands.*;
import fr.supercomete.head.Exception.KTBSNetworkFailure;
import fr.supercomete.head.GameUtils.Events.GameEvents.EventsHandler;
import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEventHandler;
import fr.supercomete.head.GameUtils.Fights.FightHandler;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.Permission;
import fr.supercomete.head.GameUtils.GameMode.Modes.*;
import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;
import fr.supercomete.head.GameUtils.Scenarios.MonsterHunter;
import fr.supercomete.head.GameUtils.Scenarios.StarterTools;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.GameUtils.Time.TimerType;
import fr.supercomete.head.PlayerUtils.BonusHandler;
import fr.supercomete.head.PlayerUtils.EffectHandler;
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
import fr.supercomete.ServerExchangeProtocol.File.PlayerAccountManager;
import fr.supercomete.ServerExchangeProtocol.Rank.Rank;
import fr.supercomete.ServerExchangeProtocol.Server.Server;
import fr.supercomete.ServerExchangeProtocol.Server.ServerManager;
import fr.supercomete.datamanager.FileManager.ProfileSerializationManager;
import fr.supercomete.enums.GenerationMode;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.Game;
import fr.supercomete.head.GameUtils.Lag;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.GameUtils.WinCondition;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.Inventory.InventoryHandler;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Listeners.ListenersRegisterer;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleBuilder;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.structure.StructureHandler;
import fr.supercomete.head.world.BiomeGenerator;
import fr.supercomete.head.world.WorldGarbageCollector;
import fr.supercomete.head.world.scoreboardmanager;
import fr.supercomete.head.world.worldgenerator;
import fr.supercomete.head.world.ScoreBoard.ScoreBoardManager;
import fr.supercomete.tasks.GAutostart;

public class Main extends JavaPlugin {
	public final static String UHCTypo = "§aEchosia"+"§7 » ";
	public final static String ScoreBoardUHCTypo = ChatColor.GREEN+"Echosia ";
	private final String ServerId = getConfig().getString("serverapi.serverconfig.ServerId");
	private final String DiscordLink = getConfig().getString("serverapi.serverconfig.DiscordLink");
	private static boolean nodamage = true;
	private static boolean forcedpvp = false;
	private static boolean forcebordure = false;
	private static boolean forcerole = false;
	public static ArrayList<UUID> playerlist = new ArrayList<>();
	public int Selected = 0;
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
    public static boolean KTBSNetwork_Connected;
    public static Location spawn;
    @Override
    public void onDisable(){
        KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
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
        Bukkit.getServer().setWhitelist(true);
        INSTANCE=this;
        spawn= new Location(Bukkit.getWorld("world"), INSTANCE.getConfig().getInt("serverapi.spawn.x"),INSTANCE.getConfig().getInt("serverapi.spawn.y"),INSTANCE.getConfig().getInt("serverapi.spawn.z"));
        /*
        Initialisation du network KTBS
         */
        try{
            Server server = ServerManager.getCurrentPluginServer();
            Bukkit.broadcastMessage("§6KTBS_Network : §aConnected");
            KTBSNetwork_Connected=true;
        }catch (Exception e){
            Bukkit.broadcastMessage("§6KTBS_Network: §cDisconnected");
            KTBSNetwork_Connected=false;
        }
        KtbsAPI api = new KtbsAPI();
        Bukkit.getServicesManager().register(KtbsAPI.class,api,this, ServicePriority.Lowest);
		generator = new BiomeGenerator();
		structurehandler= new StructureHandler(this);
		new ScoreBoardManager(this);
		new PlayerUtility(this);
		host = null;
		Date Compiledate = null;
		Compiledate = getClassBuildTime();
        for(final Scenarios scenarios: Scenarios.values()){
            api.getScenariosProvider().RegisterScenarios(scenarios);
        }
        api.getScenariosProvider().RegisterScenarios(new StarterTools());
        api.getScenariosProvider().RegisterScenarios(new MonsterHunter());
        for(final Configurable.LIST conf : Configurable.LIST.values()){
            api.getConfigurableProvider().RegisterConfigurable(conf);
        }
		api.getModeProvider().registerMode(new Null_Mode());
		UHCClassic uhcclassic = new UHCClassic();	
		api.getModeProvider().registerMode(uhcclassic);
		Bukkit.broadcastMessage("§dVersion: 0.9.1 Build("+Compiledate.getDate()+"/"+(Compiledate.getMonth()+1)+") §1Beta-Ouverte");
		currentGame=new Game((new Null_Mode()).getName(),this);
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
		getCommand("roles").setExecutor(new RolesCommand(this));
		getCommand("compo").setExecutor(new RolesCommand(this));
		getCommand("doc").setExecutor(new docCommand(this));
		getCommand("docs").setExecutor(new docCommand(this));
		getCommand("liens").setExecutor(new docCommand(this));
		getCommand("admingenerate").setExecutor(new GenerateCommand(this));
		getCommand("blseed").setExecutor(new BlackSeedCommand(this));
		getCommand("blacklistseed").setExecutor(new BlackSeedCommand(this));
		getCommand("Rolelist").setExecutor(new RolelistCommand(this));
		getCommand("createstructure").setExecutor(new createStructureCommand(this));
		getCommand("fullinv").setExecutor(new FullInvCommand(this));
		getCommand("helpop").setExecutor(new HelpopCommand(this));
		getCommand("disperse").setExecutor(new DisperseCommand(this));
        getCommand("bypass").setExecutor(new BypassCommand(this));
        getCommand("tpin").setExecutor(new TpInCommand(this));
        getCommand("timeleft").setExecutor(new TimeLeftCommand(this));
        getCommand("ti").setExecutor(new TeamInventory());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        scoreboardmanager score = new scoreboardmanager(this);
		score.ChangeScoreboard();
		new InventoryHandler(this);
		new RoleBuilder(this);
		RoleHandler.IsHiddenRoleNCompo = false;
		new worldgenerator(this);
		new TeamManager();
		new WinCondition(this);
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
        new BukkitRunnable(){
            @Override
            public void run() {

                //Indique le lancement de l'api
                for(final KasterborousRunnable run: api.getKTBSRunnableProvider().getRunnables()){
                    run.onAPILaunch(api);
                }
                for(KasterborousScenario scenario : api.getScenariosProvider().getRegisteredScenarios()){
                    if(api.getScenariosProvider().IsScenarioActivated(scenario.getName())){
                        if(scenario.getAttachedRunnable()!=null){
                            for(KasterborousRunnable runnable: scenario.getAttachedRunnable()){
                                runnable.onAPILaunch(api);
                            }
                        }

                    }
                }
                //Verifie les erreurs du systeme KTBS network

                for(Mode mode : Bukkit.getServicesManager().load(KtbsAPI.class).getModeProvider().getRegisteredModes()){
                    if(mode instanceof Permission && !KTBSNetwork_Connected){
                        try {
                            throw new KTBSNetworkFailure();
                        } catch (KTBSNetworkFailure e) {
                            e.printStackTrace();
                            Bukkit.getLogger().log(Level.WARNING,"THIS IS A KASTERBOROUS SYSTEM FAULT.\n" +
                                    "IF YOU SEE THAT MESSAGE: A MODE REQUIRING A CONNECTION TO KASTERBOROUS NETWORK HAS BEEN LOADED UNSUCESSFULLY \n" +
                                    "IF YOU SHOULD BE CONNECTED TO KTBS_NETWORK, PLEASE CONTACT SUPERCOMETE. OTHERWISE, REMOVE THIS MODE.");
                            Bukkit.getServer().shutdown();
                        }
                    }
                }
            }
        }.runTaskLater(this,30L);
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
        bypass.removeIf(uu -> !(Main.IsHost(uu) || Main.IsCohost(uu)));
    }
    public static boolean IsHost(UUID player) {
        return (host!=null && host.equals(player))||(KTBSNetwork_Connected&&PlayerAccountManager.getPlayerAccount(player).hasRank(Rank.Admin));
    }
	public static boolean IsHost(Player player) {
		return (host!=null && host.equals(player.getUniqueId()))||(KTBSNetwork_Connected&&PlayerAccountManager.getPlayerAccount(player.getName()).hasRank(Rank.Admin));
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
	public boolean loadServerProtocol() {
		try {
			@SuppressWarnings("unused")
			Server server = ServerManager.getCurrentPluginServer();
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	public void StartGame(Player player) {
		if (this.getGenmode().equals(GenerationMode.Generating)) {
			player.sendMessage(UHCTypo + "§cLa génération de la carte est déjà en cours");
			return;
		}
        FightHandler.reset();
        PlayerEventHandler.resetEvents();
		Main.currentGame.getFullinv().clear();
		if (this.forcebordure) {
			this.forcebordure = false;
		}
		if(this.forcerole) {
			this.forcerole = false;
		}
		if (this.forcedpvp) {
			this.forcedpvp = false;
		}

		
		if (CountIntegerValue(currentGame.getRoleCompoMap()) < countnumberofplayer() && Main.currentGame.getMode() instanceof CampMode) {
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
            ScoreBoardManager.reset();
			allplayereffectclear();
			Main.currentGame.setGamestate(Gstate.Starting);
			Main.currentGame.setFirstBorder(Main.currentGame.getCurrentBorder());
            assert MapHandler.getMap() != null;
            MapHandler.getMap().getPlayWorld().setGameRuleValue("naturalRegeneration", "false");
            MapHandler.getMap().getPlayWorld().setGameRuleValue("doFireTick", "false");
            MapHandler.getMap().getPlayWorld().setGameRuleValue("doMobSpawning", "true");
            MapHandler.getMap().getPlayWorld().setGameRuleValue("doDaylightCycle", "false");
            MapHandler.getMap().getPlayWorld().setGameRuleValue("keepinventory", "true");
            MapHandler.getMap().getPlayWorld().setDifficulty(Difficulty.HARD);
			GAutostart start = new GAutostart(this);
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
        ScoreBoardManager.reset();
		Main.currentGame.getFullinv().clear();
		RoleHandler.setHistoric(null);
		nodamage = true;
		Main.currentGame.setGamestate(Gstate.Waiting);
		Main.currentGame.setGenmode(GenerationMode.None);
		RoleHandler.setIsRoleGenerated(false);
		RoleHandler.setRoleList(new HashMap<UUID, Role>());
        if (forcebordure) {
            forcebordure = false;
        }
        if (forcedpvp) {
            forcedpvp = false;
        }
        if(forcerole) {
            forcerole = false;
        }
        allplayereffectclear();

        Main.currentGame.setTime(0);
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
                    if(Main.currentGame.isGameState(Gstate.Waiting))
                        pl.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 0));
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
	public Location getLoc() {
		return spawn;
	}
	public void setLoc(Location loc) {
		this.spawn = loc;
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
	public void updateSlotsInventory(Player player) {
		String name = (Main.currentGame.getMaxNumberOfplayer() == 0) ? "§rSlots:§a Illimité" 
				: "§rSlots:§a " + Main.currentGame.getMaxNumberOfplayer();
		player.getOpenInventory().setItem(13, InventoryUtils.getItem(Material.PAPER, name, null));
	}
	public static void allplayereffectclear() {
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (pl != null)
				for (PotionEffect effect : pl.getActivePotionEffects()) {
					pl.removePotionEffect(effect.getType());
				}
		}
	}
	public boolean isNodamage(boolean bol) {
		return nodamage == bol;
	}
	public boolean getNodamage() {
		return nodamage;
	}
	public void setNodamage(boolean nodamage) {
		this.nodamage = nodamage;
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
	public void updateTimerInventory(Player player) {
		int i = 18;
        for(int e = 18;e<45;e++){
            player.getOpenInventory().setItem(e,null);
        }
		for(Timer t:getCompatibleTimer()){
            if(t.getType()== TimerType.TimeDependent &&(Main.currentGame.getTimer(t).getData() - Main.currentGame.getTime()) >0){
                player.getOpenInventory().setItem(i, InventoryUtils.getItem(Material.PAPER,generateNameTimer(t),null));
                i++;
            }
            if(t.getType()== TimerType.Literal){
                player.getOpenInventory().setItem(i, InventoryUtils.getItem(Material.PAPER,generateNameTimer(t),null));
                i++;
            }
        }
		player.getOpenInventory().setItem(13, InventoryUtils.getItem(Material.PAPER,
				"§r" + this.generateNameTimer(this.getCompatibleTimer().get(this.Selected)), null));
		player.getOpenInventory().getItem(18 + this.Selected).setType(Material.COMPASS);
	}
	public void updateTeamsInventory(final Player player) {
		player.getOpenInventory().setItem(11, InventoryUtils.getItem(Material.WOOL, "§bNombre de joueur par équipe: §4"+TeamManager.NumberOfPlayerPerTeam,Arrays.asList(InventoryHandler.ClickTypoAdd+"1",InventoryHandler.ClickTypoRemove+"1")));
		player.getOpenInventory()
				.setItem(15, InventoryUtils.getItem(Material.PAPER,
						"§bNombre d'équipes: §a" + TeamManager.TeamNumber,
						Arrays.asList(InventoryHandler.ClickTypoAdd + "1", InventoryHandler.ClickTypoRemove + "1")));
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
		String[] splitedwordinput = input.split(" ");
		String constructor = "";
		for (int i = 0; i < splitedwordinput.length; i++) {
			constructor = constructor + " " + splitedwordinput[i];
			if (i< splitedwordinput.length-1 &&constructor.length()+splitedwordinput[i+1].length() >= nCharperline) {
				lineoutput.add(((LineColor != null) ? LineColor : "") + constructor);
				constructor = "";
			}
		}
		if (!constructor.isEmpty())
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
	public void updateWhiteListInventory(Player player) {
		short color = (short) ((Bukkit.hasWhitelist()) ? 5 : 14);
		String booleans = (Bukkit.hasWhitelist()) ? "§aOn" : "§cOff";
		player.getOpenInventory().setItem(13,
				InventoryUtils.createColorItem(Material.WOOL, "§bWhiteList: " + booleans, 1, color));
	}
	public static String TranslateBoolean(boolean b) {
		return (b) ? "§aOn" : "§cOff";
	}
	public String getServerId() {
		return ServerId;
	}
	public String getDiscordLink() {
		return DiscordLink;
	}
	public boolean isForcedpvp() {
		return forcedpvp;
	}

	public void setForcedpvp(boolean forcedpvp) {
		this.forcedpvp = forcedpvp;
	}

	public boolean isForcebordure() {
		return forcebordure;
	}

	public void setForcebordure(boolean forcebordure) {
		this.forcebordure = forcebordure;
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
		this.forcerole = forcerole;
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

	public static boolean searchintoarrayLocationDistance(List<Location> list,double distance,Location testedLocation) {
		if(list.size()==0)return true;
		double mindistance=distance;
		for(Location loc : list) {
			if((loc.distance(testedLocation))<mindistance) {
				mindistance=loc.distance(testedLocation);
			}
		}
        return mindistance >= distance;
	}//Return true if the shortest distance in a list<location> is greater or equal than the tested distance
}