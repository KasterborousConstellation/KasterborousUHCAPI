package fr.supercomete.head.GameUtils;
import java.util.*;
import java.util.Map.Entry;

import fr.supercomete.head.GameUtils.Enchants.EnchantLimit;
import fr.supercomete.head.GameUtils.Enchants.EnchantType;
import fr.supercomete.head.GameUtils.Events.GameEvents.Event;
import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEvent;
import fr.supercomete.head.GameUtils.GameConfigurable.KasterBorousConfigurable;
import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;
import fr.supercomete.head.GameUtils.schemes.ColorScheme;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import fr.supercomete.enums.GenerationMode;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.Time.BoundedWatchTime;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.GameUtils.Time.WatchTime;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.core.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class Game {
	private String emode;
	private Gstate gamestate = Gstate.Waiting;
	private int maxNumberOfplayer = 0;
	private String GameName = "UHC #" + new Random().nextInt(10000);
	private double FirstBorder = 2000;
	private double CurrentBorder = FirstBorder;
	private double FinalBorder = 350;
	private double BorderSpeed = 4;
	private int Time = 0;
	private int Episode = 0;
	private int Groupe = 5;
	private ArrayList<WatchTime> timelist = new ArrayList<>();
	private ArrayList<KasterborousScenario> scenarios = new ArrayList<>();
	private HashMap<String, Integer> roleCompoMap = new HashMap<>();
	private ArrayList<Configurable> configList = new ArrayList<>();
	private HashMap<UUID, Integer> KillList = new HashMap<>();
	private ArrayList<UUID> nodamagePlayerList = new ArrayList<>();
	private ArrayList<Offline_Player> offlinelist = new ArrayList<>();
	private HashMap<Material, Boolean> armorhash = new HashMap<>();

	private HashMap<UUID, ArrayList<ItemStack>> fullinv= new HashMap<>();
	private GenerationMode genmode = GenerationMode.None;
	private ArrayList<PlayerEvent> events = new ArrayList<>();
    private ArrayList<EnchantLimit>limites= new ArrayList<>();
    private ArrayList<Event> GameEvents = new ArrayList<>();
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
        final KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
		for(Timer timer :Timer.values()) {
			if(timer.getCompatibility().IsCompatible(api.getModeProvider().getModeByName(emode))&&timer.getBound()==null)
			    timelist.add(new WatchTime(timer));
		}
		for(Timer timer : Timer.values()) {
			if(timer.getCompatibility().IsCompatible(api.getModeProvider().getModeByName(emode))&&timer.getBound()!=null) {
				timelist.add(new BoundedWatchTime(timer,timer.getBound()));
			}
		}
		new BukkitRunnable(){
            @Override
            public void run() {
                ArrayList<EnchantLimit>limites= new ArrayList<>();
                limites.add(new EnchantLimit("Sharpness", Enchantment.DAMAGE_ALL, EnchantType.Iron,4));
                limites.add(new EnchantLimit("Sharpness",Enchantment.DAMAGE_ALL,EnchantType.Diamond,3));
                limites.add(new EnchantLimit("Protection",Enchantment.PROTECTION_ENVIRONMENTAL,EnchantType.Diamond,2));
                limites.add(new EnchantLimit("Protection",Enchantment.PROTECTION_ENVIRONMENTAL,EnchantType.Iron,3));
                limites.add(new EnchantLimit("Thorns",Enchantment.THORNS,EnchantType.Iron,0));
                limites.add(new EnchantLimit("Thorns",Enchantment.THORNS,EnchantType.Diamond,0));
                limites.add(new EnchantLimit("Smite",Enchantment.DAMAGE_UNDEAD,EnchantType.Iron,4));
                limites.add(new EnchantLimit("Smite",Enchantment.DAMAGE_UNDEAD,EnchantType.Diamond,4));
                limites.add(new EnchantLimit("Bane of Arthropods",Enchantment.DAMAGE_ARTHROPODS,EnchantType.Iron,4));
                limites.add(new EnchantLimit("Bane of Arthropods",Enchantment.DAMAGE_ARTHROPODS,EnchantType.Diamond,4));
                limites.add(new EnchantLimit("Fire Protection",Enchantment.PROTECTION_FIRE,EnchantType.Diamond,4));
                limites.add(new EnchantLimit("Fire Protection",Enchantment.PROTECTION_FIRE,EnchantType.Iron,4));
                limites.add(new EnchantLimit("Blast Protection",Enchantment.PROTECTION_EXPLOSIONS,EnchantType.Diamond,4));
                limites.add(new EnchantLimit("Blast Protection",Enchantment.PROTECTION_EXPLOSIONS,EnchantType.Iron,4));
                limites.add(new EnchantLimit("Projectile Protection",Enchantment.PROTECTION_PROJECTILE,EnchantType.Iron,4));
                limites.add(new EnchantLimit("Projectile Protection",Enchantment.PROTECTION_PROJECTILE,EnchantType.Diamond,4));


                limites.add(new EnchantLimit("Power",Enchantment.ARROW_DAMAGE,EnchantType.Bow,3));
                limites.add(new EnchantLimit("Punch",Enchantment.ARROW_KNOCKBACK,EnchantType.Bow,0));
                limites.add(new EnchantLimit("Flame",Enchantment.ARROW_FIRE,EnchantType.Bow,0));
                limites.add(new EnchantLimit("Infinity",Enchantment.ARROW_INFINITE,EnchantType.Bow,0));

                limites.add(new EnchantLimit("Luck of the sea",Enchantment.LUCK,EnchantType.Rod,3));
                limites.add(new EnchantLimit("Lure",Enchantment.LURE,EnchantType.Rod,3));

                limites.add(new EnchantLimit("Efficiency",Enchantment.DIG_SPEED,EnchantType.ALL,5));
                limites.add(new EnchantLimit("Unbreaking",Enchantment.DURABILITY,EnchantType.ALL,3));
                limites.add(new EnchantLimit("Fortune",Enchantment.LOOT_BONUS_BLOCKS,EnchantType.ALL,2));
                limites.add(new EnchantLimit("Looting",Enchantment.LOOT_BONUS_MOBS,EnchantType.ALL,2));
                limites.add(new EnchantLimit("Knockback",Enchantment.KNOCKBACK,EnchantType.ALL,1));
                limites.add(new EnchantLimit("Fire Aspect",Enchantment.FIRE_ASPECT,EnchantType.ALL,0));
                limites.add(new EnchantLimit("Feather Falling",Enchantment.PROTECTION_FALL,EnchantType.ALL,4));
                limites.add(new EnchantLimit("Depth Strider",Enchantment.DEPTH_STRIDER,EnchantType.ALL,0));
                limites.add(new EnchantLimit("Respiration",Enchantment.OXYGEN,EnchantType.ALL,3));
                limites.add(new EnchantLimit("Aqua Affinity",Enchantment.WATER_WORKER,EnchantType.ALL,1));
                Main.currentGame.setLimites(limites);
            }
        }.runTaskLater(main,1);
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

	public Game(String emode,Main main) {
		this.emode =emode;
		this.init(main);
        final KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
		for (int ps = 0; ps < api.getConfigurableProvider().getConfigurables().size(); ps++) {
			if (ps >= getConfigList().size()) {
                KasterBorousConfigurable conf = api.getConfigurableProvider().getConfigurables().get(ps);
				getConfigList().add(new Configurable(conf,conf.getBaseData(),conf.getType()));
			}
		}
	}
    public ArrayList<EnchantLimit> getLimite(EnchantType enchantType) {
        ArrayList<EnchantLimit>types = new ArrayList<>();
        for(EnchantLimit enchantLimit:getLimites()){
            if(enchantLimit.getType().equals(enchantType)){
                types.add(enchantLimit);
            }
        }
        return types;
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

	public HashMap<Class<?>, Integer> getRoleCompoMap() {
		HashMap<Class<?>, Integer> hash = new HashMap<>();
		for (Entry<String, Integer> entry : roleCompoMap.entrySet()) {
			hash.put(Bukkit.getServicesManager().load(KtbsAPI.class).getRoleProvider().getRoleClassByName(entry.getKey()), entry.getValue());
		}
		return hash;
	}
	public void setRoleCompoMap(HashMap<Class<?>, Integer> roleCompoMap) {
        HashMap<String, Integer> ret = new HashMap<String, Integer>();
		for (Entry<Class<?>, Integer> src : roleCompoMap.entrySet()) {
			ret.put(Objects.requireNonNull(Bukkit.getServicesManager().load(KtbsAPI.class).getRoleProvider().getRoleByClass(src.getKey())).getName(), src.getValue());
		}
		this.roleCompoMap = ret;
	}

	public boolean hasClassInRoleCompoMap(Class<?> claz) {
		for (String c : roleCompoMap.keySet()) {
			Class<?> cl = Bukkit.getServicesManager().load(KtbsAPI.class).getRoleProvider().getRoleClassByName(c);
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
	public ArrayList<KasterborousScenario> getScenarios() {
		return scenarios;
	}
	public void setScenarios(ArrayList<KasterborousScenario> scenarios) {
		this.scenarios = scenarios;
	}
	public String getGameName() {
		return GameName;
	}
    public boolean hasScenarios(KasterborousScenario scenario){
        for(KasterborousScenario scenario1 :this.getScenarios()){
            if(scenario1.equals(scenario)){
                return true;
            }
        }
        return false;
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
	public int getDataFrom(KasterBorousConfigurable config) {
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
		return Bukkit.getServicesManager().load(KtbsAPI.class).getModeProvider().getModeByName(this.emode);
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
		return getMode().getScheme();
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

    public ArrayList<Event> getGameEvents() {
        return GameEvents;
    }

    public void setGameEvents(ArrayList<Event> gameEvents) {
        GameEvents = gameEvents;
    }
}
