package fr.supercomete.head.PlayerUtils;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.core.KasterborousRunnable;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class BonusHandler {
    private static HashMap<UUID, ArrayList<Bonus>>hash = new HashMap<>();
    public static void init(){
        Bukkit.getServicesManager().load(KtbsAPI.class).getKTBSRunnableProvider().RegisterRunnable(new ArrayList<>(Arrays.asList(
                new KasterborousRunnable(){

                    @Override
                    public String name() {
                        return "BonusHandler-Task";
                    }

                    @Override
                    public void onAPILaunch(KtbsAPI api) {

                    }

                    @Override
                    public void onAPIStop(KtbsAPI api) {

                    }

                    @Override
                    public void onGameLaunch(KtbsAPI api) {
                        reset();
                    }

                    @Override
                    public void onGameEnd(KtbsAPI api) {
                        reset();
                    }

                    @Override
                    public void onTick(Gstate gstate, KtbsAPI api) {

                    }

                    @Override
                    public void onTimer(Timer timer) {

                    }
                }))
        );
    }
    public static void reset(){
        hash.clear();
    }
    public static void addBonus(Player player,Bonus bonus){
        addBonus(player.getUniqueId(),bonus);

    }
    public static void addBonus(UUID uuid,Bonus bonus){
        if(hash.containsKey(uuid)){
            final ArrayList<Bonus> blist = hash.get(uuid);
            blist.add(bonus);
            hash.put(uuid,blist);
        }else{
            hash.put(uuid,new ArrayList<>(Collections.singletonList(bonus)));
        }
    }
    public static int getTotalOfBonus(UUID player, BonusType type){
        if(!hash.containsKey(player)){
            return 0;
        }else{
            int count = 0;
            for(Bonus bonus : hash.get(player)){
                if(bonus.getType().equals(type)){
                    count+=bonus.getLevel();
                }
            }
            return count;
        }
    }
    public static int getTotalOfBonus(Player player, BonusType type){
        return getTotalOfBonus(player.getUniqueId(),type);
    }

}
