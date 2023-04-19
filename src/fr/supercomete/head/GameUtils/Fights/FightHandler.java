package fr.supercomete.head.GameUtils.Fights;
import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEventHandler;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Triggers.Trigger_OnFightEnd;
import fr.supercomete.head.role.Triggers.Trigger_onFightBegin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.concurrent.CopyOnWriteArrayList;

public class FightHandler {
    public static CopyOnWriteArrayList<Fight> currentFight = new CopyOnWriteArrayList<>();

    public static void Fight(final Fight fight){
        if(fight.getFirst().equals(fight.getSecond()))return;
        if(hasSameFight(fight)){
            currentFight.set(getIdOfSameFight(fight),fight);
        }else{
            currentFight.add(fight);
            sendBegin(fight,currentFight);
        }
    }
    public static boolean hasFightOtherThan(Player player, Fight other, CopyOnWriteArrayList<Fight> fights){
        for(final Fight fight: fights){
            if(fight.equals(other)){
                continue;
            }
            if(fight.getFirst().equals(player.getUniqueId())){
                return true;
            }
            if(fight.getSecond().equals(player.getUniqueId())){
                return true;
            }
        }
        return false;
    }
    public static boolean hasFight(Player player){
        for(final Fight fight: currentFight){
            if(fight.getFirst().equals(player.getUniqueId())){
                return true;
            }
            if(fight.getSecond().equals(player.getUniqueId())){
                return true;
            }
        }
        return false;
    }
    public static Player getLastDamagerof(Player player){
        Fight LastFight = null;
        for(Fight fight : currentFight){
            if(fight.getFirst()==player.getUniqueId()||fight.getSecond()==player.getUniqueId()){
                if(LastFight==null || LastFight.getBegin()<fight.getBegin()){
                    LastFight=fight;
                }
            }
        }
        if(LastFight==null){
            return null;
        }
        if(LastFight.getSecond().equals(player.getUniqueId())){
            if(Bukkit.getPlayer(LastFight.getFirst())!=null){
                return Bukkit.getPlayer(LastFight.getFirst());
            }
        }else if(LastFight.getFirst().equals(player.getUniqueId())){
            if(Bukkit.getPlayer(LastFight.getSecond())!=null){
                return Bukkit.getPlayer(LastFight.getSecond());
            }
        }
        return null;
    }
    private static int getIdOfSameFight(final Fight fight){
        int e=0;
        for(final Fight it:currentFight){
            if(fight.getFirst().equals(it.getFirst()) && fight.getSecond().equals(it.getSecond()))
                return e;

            if(fight.getFirst().equals(it.getSecond())&&fight.getSecond().equals(it.getFirst()))
                return e;
            e++;
        }
        return -1;
    }
    public static void sendBegin(Fight fight,CopyOnWriteArrayList<Fight> fights){
        FightHandler.sendMessage(fight,true,fights);
        PlayerEventHandler.Event("Fight "+Bukkit.getPlayer(fight.getSecond()).getName(),Bukkit.getPlayer(fight.getFirst()),Bukkit.getPlayer(fight.getFirst()).getLocation());
    }
    public static void update(Fight fight,CopyOnWriteArrayList<Fight>fights){
        if(Main.currentGame.getTime()- fight.getBegin() > 20){
            FightHandler.currentFight.remove(fight);
            FightHandler.sendMessage(fight,false,fights);
        }
    }
    public static void sendMessage(Fight fight ,boolean bool,CopyOnWriteArrayList<Fight>fights){
        if(Bukkit.getPlayer(fight.getFirst())!=null){
            if(!FightHandler.hasFightOtherThan(Bukkit.getPlayer(fight.getFirst()),fight,fights)) {
                Bukkit.getPlayer(fight.getFirst()).sendMessage((bool) ? "§cVous êtes en combat." : "§cVous n'êtes plus en combat.");
            }
            if(bool){
                if(RoleHandler.getRoleOf(Bukkit.getPlayer(fight.getFirst()))!=null&&RoleHandler.getRoleOf(Bukkit.getPlayer(fight.getFirst()))instanceof Trigger_onFightBegin){
                    Trigger_onFightBegin begin = (Trigger_onFightBegin) RoleHandler.getRoleOf(Bukkit.getPlayer(fight.getFirst()));
                    begin.onFightBegin(fight);
                }
            }else{
                if(RoleHandler.getRoleOf(Bukkit.getPlayer(fight.getFirst()))!=null&&RoleHandler.getRoleOf(Bukkit.getPlayer(fight.getFirst()))instanceof Trigger_OnFightEnd){
                    Trigger_OnFightEnd end = (Trigger_OnFightEnd) RoleHandler.getRoleOf(Bukkit.getPlayer(fight.getFirst()));
                    end.onFightEnd(fight);
                }
            }
        }
        if(Bukkit.getPlayer(fight.getFirst())!=null) {
            if (!FightHandler.hasFightOtherThan(Bukkit.getPlayer(fight.getFirst()), fight,fights)) {
                Bukkit.getPlayer(fight.getFirst()).sendMessage((bool) ? "§cVous êtes en combat." : "§cVous n'êtes plus en combat.");
            }
            if(bool){
                if(RoleHandler.getRoleOf(Bukkit.getPlayer(fight.getFirst()))!=null&&RoleHandler.getRoleOf(Bukkit.getPlayer(fight.getFirst()))instanceof Trigger_onFightBegin){
                    Trigger_onFightBegin begin = (Trigger_onFightBegin)RoleHandler.getRoleOf(Bukkit.getPlayer(fight.getFirst()));
                    begin.onFightBegin(fight);
                }
            }else{
                if(RoleHandler.getRoleOf(Bukkit.getPlayer(fight.getFirst()))!=null&&RoleHandler.getRoleOf(Bukkit.getPlayer(fight.getFirst()))instanceof Trigger_OnFightEnd){
                    Trigger_OnFightEnd end = (Trigger_OnFightEnd) RoleHandler.getRoleOf(Bukkit.getPlayer(fight.getFirst()));
                    end.onFightEnd(fight);
                }
            }
        }
    }
    public static boolean hasSameFight(final Fight fight){
        for(final Fight it:currentFight){
            if(fight.getFirst().equals(it.getFirst()) && fight.getSecond().equals(it.getSecond()))
                return true;

            if(fight.getFirst().equals(it.getSecond())&&fight.getSecond().equals(it.getFirst()))
                return true;
        }
        return false;
    }

    public static void reset(){
        currentFight.clear();
    }
    public static void update(){
        for(final Fight fight:currentFight){
            update(fight,currentFight);
        }
    }
}
