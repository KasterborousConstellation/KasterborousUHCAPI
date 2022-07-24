package fr.supercomete.head.GameUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import fr.supercomete.head.GameUtils.Enchants.EnchantHandler;
import fr.supercomete.head.GameUtils.Enchants.EnchantLimit;
import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.enums.GenerationMode;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.ModeAPI;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.Time.BoundedWatchTime;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.GameUtils.Time.WatchTime;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.core.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class Game {
	private int emode;
	private Gstate gamestate = Gstate.Waiting;
	private int maxNumberOfplayer = 0;
	private String GameName = "UHC #" + new Random().nextInt(10000);
	private double FirstBorder = 600;
	private double CurrentBorder = FirstBorder;
	private double FinalBorder = 200;
	private double BorderSpeed = 4;
	private int Time = 0;
	private int Episode = 0;
	private int TeamNumber = 4;
	private int NumberOfPlayerPerTeam = 2;
	private int Groupe = 6;
	private ArrayList<WatchTime> timelist = new ArrayList<>();
	private boolean IsTeamActivated = false;
	private ArrayList<Team> TeamList = new ArrayList<>();
	private ArrayList<Scenarios> scenarios = new ArrayList<>();
	private HashMap<String, Integer> roleCompoMap = new HashMap<>();
	private ArrayList<Configurable> configList = new ArrayList<>();
	private HashMap<UUID, Integer> KillList = new HashMap<>();
	private ArrayList<UUID> nodamagePlayerList = new ArrayList<>();
	private ArrayList<Offline_Player> offlinelist = new ArrayList<>();
	private HashMap<Material, Boolean> armorhash = new HashMap<>();
	private ColorScheme colorScheme =new ColorScheme(ChatColor.GREEN, ChatColor.AQUA, ChatColor.GRAY);
	private HashMap<UUID, ArrayList<ItemStack>> fullinv= new HashMap<>();
	private GenerationMode genmode = GenerationMode.None;
	private ArrayList<PlayerEvent> events = new ArrayList<>();
    private ArrayList<EnchantLimit>limites= new ArrayList<>();
    public boolean IsDisabledEnchant=true;
    public boolean IsDisabledAnvil=true;
	public void init(Main main) {
		getArmorhash().clear();
		getArmorhash().put(Material.DIAMOND_HELMET, true);
		getArmorhash().put(Material.DIAMOND_CHESTPLATE, true);
		getArmorhash().put(Material.DIAMOND_LEGGINGS, false);
		getArmorhash().put(Material.DIAMOND_BOOTS, true);
		getArmorhash().put(Material.IRON_HELMET, true);
		getArmorhash().put(Material.IRON_CHESTPLATE, true);
		getArmorhash().put(Material.IRON_LEGGINGS, true);
		getArmorhash().put(Material.IRON_BOOTS, true);

		for(Timer timer :Timer.values()) {
			if(Main.containmod(timer.getCompatibility(), getMode())&&timer.getBound()==null)
			timelist.add(new WatchTime(timer));
		}
		for(Timer timer : Timer.values()) {
			if(Main.containmod(timer.getCompatibility(), getMode())&&timer.getBound()!=null) {
				timelist.add(new BoundedWatchTime(timer,timer.getBound()));
			}
		}
		new BukkitRunnable(){
            @Override
            public void run() {
                EnchantHandler.init();
            }
        }.runTaskLater(main,1);
	}
	public boolean isIsTeamActivated() {
		return IsTeamActivated;
	}
	public WatchTime getTimer(Timer timer) {
		for(WatchTime time : timelist) {
			if(time.getId()==timer) {
				return time;
			}
		}
		return null;
	}
	public ArrayList<WatchTime> getTimelist() {
		return timelist;
	}
	public void setTimelist(ArrayList<WatchTime> timelist) {
		this.timelist = timelist;
	}

	public Game(int indexmode,Main main) {
		this.setEmode(indexmode);
		this.init(main);
		
		for (int ps = 0; ps < Configurable.LIST.values().length; ps++) {
			if (ps >= getConfigList().size()) {
				getConfigList().add(new Configurable(Configurable.LIST.values()[ps],Configurable.LIST.values()[ps].getBaseData(),Configurable.LIST.values()[ps].getType()));
			}
		}
	}
	public boolean hasOfflinePlayer(Player player) {
		for (Offline_Player off : offlinelist) {
			if (off.getPlayer().equals(player.getUniqueId()))
				return true;
		}
		return false;
	}
	public boolean hasOfflinePlayer(UUID uuid) {
		for (Offline_Player off : offlinelist) {
			if (off.getPlayer().equals(uuid))
				return true;
		}
		return false;
	}
	public Offline_Player getOffline_Player(Player player) {
		for (Offline_Player off : offlinelist) {
			if (off.getPlayer().equals(player.getUniqueId()))
				return off;
		}
		return null;
	}
	public Offline_Player getOffline_Player(UUID player) {
		for (Offline_Player off : offlinelist) {
			if (off.getPlayer().equals(player))
				return off;
		}
		return null;
	}
	public boolean IsTeamActivated() {
		return IsTeamActivated;
	}
	public void setIsTeamActivated(boolean isTeamActivated) {
		IsTeamActivated = isTeamActivated;
	}
	public ArrayList<Team> getTeamList() {
		return TeamList;
	}
	public void setTeamList(ArrayList<Team> teamList) {
		TeamList = teamList;
	}
	public HashMap<Class<?>, Integer> getRoleCompoMap() {
		HashMap<Class<?>, Integer> hash = new HashMap<>();
		for (Entry<String, Integer> entry : roleCompoMap.entrySet()) {
			hash.put(ModeAPI.getRoleClassByString(entry.getKey()), entry.getValue());
		}
		return hash;
	}
	public void setRoleCompoMap(HashMap<Class<?>, Integer> roleCompoMap) {
        HashMap<String, Integer> ret = new HashMap<String, Integer>();
		for (Entry<Class<?>, Integer> src : roleCompoMap.entrySet()) {
			ret.put(ModeAPI.getRoleByClass(src.getKey()).getName(), src.getValue());
		}
		this.roleCompoMap = ret;
	}

	public boolean hasClassInRoleCompoMap(Class<?> claz) {
		for (String c : roleCompoMap.keySet()) {
			Class<?> cl = ModeAPI.getRoleClassByString(c);
            if (cl!=null&&cl.equals(claz))
				return true;
		}
		return false;
	}

	public int getEpisode() {
		return Episode;
	}

	public void setEpisode(int episode) {
		Episode = episode;
	}


	public int getTeamNumber() {
		return TeamNumber;
	}

	public void setTeamNumber(int teamNumber) {
		TeamNumber = teamNumber;
	}

	public int getNumberOfPlayerPerTeam() {
		return NumberOfPlayerPerTeam;
	}

	public void setNumberOfPlayerPerTeam(int numberOfPlayerPerTeam) {
		NumberOfPlayerPerTeam = numberOfPlayerPerTeam;
	}

	public int getDiamondlimit() {
		return getDataFrom(Configurable.LIST.DiamondLimit);
	}

	public double getFirstBorder() {
		return FirstBorder;
	}

	public void setFirstBorder(double firstBorder) {
		FirstBorder = firstBorder;
	}

	public double getCurrentBorder() {
		return CurrentBorder;
	}
	public void setCurrentBorder(double currentBorder) {
		CurrentBorder = currentBorder;
	}
	public double getFinalBorder() {
		return FinalBorder;
	}
	public void setFinalBorder(double finalBorder) {
		FinalBorder = finalBorder;
	}
	public double getBorderSpeed() {
		return BorderSpeed;
	}
	public void setBorderSpeed(double borderSpeed) {
		BorderSpeed = borderSpeed;
	}
	public Gstate getGamestate() {
		return gamestate;
	}
	public boolean isGameState(Gstate state) {
		return this.gamestate == state;
	}
	public void setGamestate(Gstate gamestate) {
		this.gamestate = gamestate;
	}
	public int getMaxNumberOfplayer() {
		return maxNumberOfplayer;
	}
	public void setMaxNumberOfplayer(int maxNumberOfplayer) {
		this.maxNumberOfplayer = maxNumberOfplayer;
	}
	public ArrayList<Scenarios> getScenarios() {
		return scenarios;
	}
	public void setScenarios(ArrayList<Scenarios> scenarios) {
		this.scenarios = scenarios;
	}
	public String getGameName() {
		return GameName;
	}
	public void setGameName(String gameName) {
		GameName = gameName;
	}
	public int getTime() {
		return Time;
	}
	public void setTime(int time) {
		Time = time;
	}
	public ArrayList<Configurable> getConfigList() {
		return configList;
	}
	public void setConfigList(ArrayList<Configurable> configList) {
		this.configList = configList;
	}
	public int getDataFrom(Configurable.LIST config) {
		for (Configurable conf : this.getConfigList()) {
			if (conf.getId().equals(config))
				return conf.getData();
		}
		return 0;
	}
	public String getStatus() {
	    switch (this.gamestate) {
            case Waiting :
                return "§aEn attente";
            case Day: case Night: case Playing:
                return "§cPartie en cours";
            case Finish :
                return "§ePartie terminée";
            case Starting :
                return "§3Lancement en cours";
        }
        return "";
	}
	public Mode getMode() {
		return ModeAPI.getModeByIntRepresentation(this.emode);
	}


	public int getGroupe() {
		return Groupe;
	}

	public void setGroupe(int groupe) {
		Groupe = groupe;
	}

	public HashMap<UUID, Integer> getKillList() {
		return KillList;
	}

	public void setKillList(HashMap<UUID, Integer> killList) {
		KillList = killList;
	}

	public ArrayList<UUID> getNodamagePlayerList() {
		return nodamagePlayerList;
	}

	public void setNodamagePlayerList(ArrayList<UUID> nodamagePlayerList) {
		this.nodamagePlayerList = nodamagePlayerList;
	}

	public ColorScheme getColorScheme() {
		return colorScheme;
	}

	public void setColorScheme(ColorScheme colorScheme) {
		this.colorScheme = colorScheme;
	}

	public ArrayList<Offline_Player> getOfflinelist() {
		return offlinelist;
	}

	public void setOfflinelist(ArrayList<Offline_Player> offlinelist) {
		this.offlinelist = offlinelist;
	}

	public HashMap<Material, Boolean> getArmorhash() {
		return armorhash;
	}

	public void setArmorhash(HashMap<Material, Boolean> armorhash) {
		this.armorhash = armorhash;
	}

	public int getEmode() {
		return emode;
	}

	public void setEmode(int emode) {
		this.emode = emode;
	}
	/**
	 * @return the genmode
	 */
	public GenerationMode getGenmode() {
		return genmode;
	}
	/**
	 * @param genmode the genmode to set
	 */
	public void setGenmode(GenerationMode genmode) {
		this.genmode = genmode;
	}
	/**
	 * @return the fullinv
	 */
	public HashMap<UUID, ArrayList<ItemStack>> getFullinv() {
		return fullinv;
	}
	/**
	 * @param fullinv the fullinv to set
	 */
	public void setFullinv(HashMap<UUID, ArrayList<ItemStack>> fullinv) {
		this.fullinv = fullinv;
	}


    public ArrayList<PlayerEvent> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<PlayerEvent> events) {
        this.events = events;
    }
    public void addEvent(PlayerEvent event){
	    this.events.add(event);
    }

    public ArrayList<EnchantLimit> getLimites() {
        return limites;
    }

    public void setLimites(ArrayList<EnchantLimit> limites) {
        this.limites = limites;
    }
}
