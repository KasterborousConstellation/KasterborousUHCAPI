package fr.supercomete.tasks;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.role.Triggers.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.supercomete.enums.Choice;
import fr.supercomete.enums.GenerationMode;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.ModeAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.GameMode.Modes.DWUHC;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.GameUtils.Time.TimerType;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.CyberiumHandler;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Key.KeyHandler;
import fr.supercomete.head.role.Key.Tardis;
import fr.supercomete.head.role.Key.TardisHandler;
import fr.supercomete.head.role.content.DWUHC.ClaraOswald;
import fr.supercomete.head.world.worldgenerator;
import fr.supercomete.tasks.particles.TardisParticle;
public class Cycle extends BukkitRunnable{
	private final Main main;
	public Cycle(Main main) {
		this.main=main;
		hasPvpForced= false;
		hasBordureForced=false;
		hasForceRole=false;
		Main.currentCycle=this;
	}
	int days=0;int time=0;int timer;
	public static boolean hasPvpForced=false;
	public static boolean hasBordureForced=false;
	public static boolean hasForceRole=false;
	//AnnoncedTimes 
	private final int[] annonced=new int[] {10*60,5*60,3*60,60,30,10,5,4,3,2,1};
	@Override
	public void run() {

        if(Main.getPlayerlist().size()>0){
            if (time == 0) {
                TardisHandler.currentTardis = null;
                TardisParticle.PrinInstance = null;
                TardisHandler.TardisLocation = null;
                timer = ((Main.currentGame.getTimer(Timer.EpisodeTime)).getData() / 2) - 1;
                ArrayList<Player> pllist = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers())
                    if (player.getGameMode() != GameMode.SPECTATOR) pllist.add(player);
                for (Player pl : pllist) {
                    ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode()).OnStart(pl);
                }
            }
            if ((time == Main.currentGame.getTimer(Timer.BorderTime).getData() || main.isForcebordure()) && !hasBordureForced) {
                hasBordureForced = true;
                Bukkit.broadcastMessage(Main.UHCTypo + "La bordure est en mouvement");
            }
            if (!Main.currentGame.isGameState(Gstate.Waiting) && !Main.currentGame.isGameState(Gstate.Starting) && time > 0) {
                Main.currentGame.setEpisode(days);
                Main.currentGame.setTime(time);
            }
            if (TardisHandler.currentTardis != null && Main.currentGame.getMode() instanceof DWUHC) {
                ArrayList<Player> near = new ArrayList<Player>();
                for (UUID uu : Main.getPlayerlist()) {
                    if (Bukkit.getPlayer(uu) != null) {
                        if (Bukkit.getPlayer(uu).getWorld() == TardisHandler.TardisLocation.getWorld()) {
                            if (Bukkit.getPlayer(uu).getLocation().distance(TardisHandler.TardisLocation) < 10) {
                                near.add(Bukkit.getPlayer(uu));
                            }
                        }
                    }
                }
                TardisHandler.currentTardis.update(near);
                TardisHandler.currentTardis.updateinside();
            }
            if (Main.currentGame.getMode() instanceof DWUHC) {
                if (time == Main.currentGame.getTimer(Timer.TardisFirstSpawn).getData()) {
                    TardisHandler.IsTardisGenerated = true;
                    TardisHandler.currentTardis = new Tardis();
                    TardisHandler.placeTardis();
                    KeyHandler.GenerateAllKey();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(Main.UHCTypo + "§bLe Tardis vient d'apparaitre");
                        if (RoleHandler.getRoleOf(player) != null && RoleHandler.getRoleOf(player) instanceof ClaraOswald clara) {
                            clara.NotifyTardis(true);
                        }
                    }
                }
                if (time - Main.currentGame.getTimer(Timer.TardisFirstSpawn).getData() > 0 && (time - Main.currentGame.getTimer(Timer.TardisFirstSpawn).getData())%(Main.currentGame.getTimer(Timer.TardisDelay).getData()) == 0) {
                    TardisHandler.placeTardis();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (RoleHandler.getRoleOf(player) != null && RoleHandler.getRoleOf(player) instanceof final ClaraOswald clara) {
                            clara.NotifyTardis(false);
                        }
                    }
                }
            }
            //Any While Night While Day
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!Main.getPlayerlist().contains(player.getUniqueId())) {
                    player.setGameMode(GameMode.SPECTATOR);
                } else {
                    if (Main.currentGame.getTime() > 10) {
                        ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode()).onAnyTime(player);
                        if (RoleHandler.getRoleOf(player) != null) {
                            Role role = RoleHandler.getRoleOf(player);
                            if (role instanceof Trigger_WhileAnyTime) {
                                ((Trigger_WhileAnyTime) role).WhileAnyTime(player);
                            }
                            if (role instanceof Trigger_WhileDay) {
                                if (Main.currentGame.getGamestate().equals(Gstate.Day)) {
                                    ((Trigger_WhileDay) role).WhileDay(player);
                                }
                            }
                            if (role instanceof Trigger_WhileNight) {
                                if (Main.currentGame.getGamestate().equals(Gstate.Night)) {
                                    ((Trigger_WhileNight) role).WhileNight(player);
                                }
                            }
                        }
                    }
                }
            }
            if (time % Main.currentGame.getTimer(Timer.RealEpisodeTime).getData() == 0) {
                days++;
                for (UUID uu : Main.getPlayerlist()) {
                    Player player = Bukkit.getPlayer(uu);
                    ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode()).onEpisodeTime(player);
                }
                for(Role role : RoleHandler.getRoleList().values()){
                    if(role instanceof Trigger_onEpisodeTime episodeTime){
                        episodeTime.onEpisodeTime(Bukkit.getPlayer(role.getOwner()));
                    }
                }
                Bukkit.broadcastMessage("§eEpisode §r" + days + "");
            }
            if (timer == (Main.currentGame.getTimer(Timer.EpisodeTime)).getData() / 2) {

                Main.currentGame.setGamestate(Gstate.Day);
                worldgenerator.currentPlayWorld.setTime(1000);
                Bukkit.broadcastMessage(Main.UHCTypo + "§6Le jour se §elève§6.");
                for (UUID uu : Main.getPlayerlist()) {
                    Player player = Bukkit.getPlayer(uu);
                    ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode()).onDayTime(player);
                    if (!(RoleHandler.getRoleOf(player) == null)) {
                        Role role = RoleHandler.getRoleOf(player);
                        if (role instanceof Trigger_OnDayTime) {
                            ((Trigger_OnDayTime) role).onDayTime(player);
                        }
                    }

                }
            }
            if ((time == Main.currentGame.getTimer(Timer.PvPTime).getData() || main.isForcedpvp()) && hasPvpForced == false) {
                hasPvpForced = true;
                worldgenerator.currentPlayWorld.setPVP(true);
                Bukkit.broadcastMessage(Main.UHCTypo + "§6Le PVP est activé");
                if (Main.currentGame.getScenarios().contains(Scenarios.FinalHeal)) Main.finalheal();
            }
            if (timer == (Main.currentGame.getTimer(Timer.EpisodeTime)).getData()) {
                Main.currentGame.setGamestate(Gstate.Night);
                worldgenerator.currentPlayWorld.setTime(18000);
                Bukkit.broadcastMessage(Main.UHCTypo + " §9La nuit §btombe§9.");
                timer = 0;
                for (UUID uu : Main.getPlayerlist()) {
                    Player player = Bukkit.getPlayer(uu);
                    ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode()).onNightTime(player);
                    if (!(RoleHandler.getRoleOf(player) == null)) {
                        Role role = RoleHandler.getRoleOf(player);
                        if (role instanceof Trigger_onNightTime) {
                            ((Trigger_onNightTime) role).onNightTime(player);
                        }
                    }
                }
            }

            if (time % 5 == 0 && time > 20) {
                if (!(Main.devmode)) {
                    if (ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode()).WinCondition()) {
                        main.StopGame(null);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode()).onEndingTime(player);
                        }
                        cancel();
                    }
                }
                if (RoleHandler.isIsRoleGenerated()) {
                    for (Role role : RoleHandler.getRoleList().values()) {
                        role.updateRoleState();
                    }
                }
            }
            if (Main.currentGame.getMode() instanceof CampMode) {
                if (time == (Main.currentGame.getTimer(Timer.RoleTime).getData() + Main.currentGame.getTimer(Timer.ChoiceDelay).getData())) {
                    for (Role r : RoleHandler.getRoleList().values()) {
                        if (r.getChoices().size() > 0 && r.getChoice() == Choice.None) {
                            r.ExecuteChoice(r.getChoices().get(new Random().nextInt(r.getChoices().size())));
                        }
                    }
                }
            }

            if (Main.currentGame.getMode() instanceof CampMode && (time == Main.currentGame.getTimer(Timer.RoleTime).getData() || main.isForceRole()) && hasForceRole == false) {
                hasForceRole = true;
                if (ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode()) instanceof CampMode) {
                    RoleHandler.GiveRole();
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode()).onRoleTime(player);
                    if (!(RoleHandler.getRoleOf(player) == null)) {
                        Role role = RoleHandler.getRoleOf(player);
                        if (role instanceof Trigger_OnRoletime) {
                            ((Trigger_OnRoletime) role).onRoleTime(player);
                        }
                    }

                }
            }

            if (Main.currentGame.getMode() instanceof DWUHC && time == Main.currentGame.getTimer(Timer.CyberiumTime).getData()) {
                CyberiumHandler.giveAllCybermanCyberriumCompass();
                CyberiumHandler.GenerateLocationOfDropCyberium(true, null);
            }
            //Annonce des temps avant les timers
            for (int ann : this.annonced) {
                String str = TimeUtility.transform(ann, "§b", "§b", "§b");
                for (Timer timer : Timer.values()) {
                    if (Main.currentGame.getTimer(timer) != null) {
                        if (timer.getType() == TimerType.TimeDependent && timer.isDraw() && Main.currentGame.getTimer(timer).getData() - time == ann) {
                            Bukkit.broadcastMessage(Main.UHCTypo + timer.getName() + " dans " + str);
                        }
                    }

                }
            }
            if (time % 20 == 0) {
                for (Offline_Player offplayer : Main.currentGame.getOfflinelist()) {
                    ArrayList<UUID> uuid = new ArrayList<UUID>();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        uuid.add(player.getUniqueId());
                    }
                    if (!uuid.contains(offplayer.getPlayer())) {
                        if (offplayer.getTimeElapsedSinceDeconnexion() > Main.currentGame.getDataFrom(Configurable.LIST.DecTime)) {
                            if (Main.getPlayerlist().contains(offplayer.getPlayer()))
                                Main.currentGame.getMode().DecoKillMethod(offplayer);
                        }
                    }
                }
            }
            if (Main.currentGame.getMode() instanceof DWUHC && time > Main.currentGame.getTimer(Timer.CyberiumTime).getData() && time % 5 == 0) {
                if (CyberiumHandler.HostPlayer != null) {
                    CyberiumHandler.setallCybermanCompassToLocation(Bukkit.getPlayer(CyberiumHandler.HostPlayer).getLocation());
                } else if (CyberiumHandler.Cyberiumlocation != null) {
                    CyberiumHandler.setallCybermanCompassToLocation(CyberiumHandler.Cyberiumlocation);
                } else {
                    for (UUID uu : RoleHandler.getRoleList().keySet()) {
                        if (!DWUHC.generateCybermanRoleList().contains(RoleHandler.getRoleList().get(uu).getClass()))
                            continue;
                        Bukkit.getPlayer(uu).setCompassTarget(Bukkit.getPlayer(uu).getLocation());
                    }
                }
            }
            timer++;
            time++;
        }
        if (main.getGenmode() == GenerationMode.None || Main.currentGame.isGameState(Gstate.Waiting)||Main.currentCycle!=this) {
            Main.currentGame.setGamestate(Gstate.Waiting);
            for (Player player : Bukkit.getOnlinePlayers()) {
                ModeAPI.getModeByIntRepresentation(Main.currentGame.getEmode()).onEndingTime(player);
            }
            cancel();
        }
	}
}