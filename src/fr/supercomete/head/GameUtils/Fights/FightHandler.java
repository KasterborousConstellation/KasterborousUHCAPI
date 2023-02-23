package fr.supercomete.head.GameUtils.Fights;
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
            fight.sendBegin(currentFight);
        }
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
            fight.update(currentFight);
        }

    }
}
