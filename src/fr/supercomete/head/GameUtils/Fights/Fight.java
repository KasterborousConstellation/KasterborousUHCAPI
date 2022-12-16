package fr.supercomete.head.GameUtils.Fights;

import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEvent;
import fr.supercomete.head.GameUtils.Events.PlayerEvents.PlayerEventHandler;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Triggers.Trigger_OnFightEnd;
import fr.supercomete.head.role.Triggers.Trigger_onFightBegin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
public class Fight {
    private final int begin;
    private final UUID first;
    private final UUID second;
    public Fight(UUID first,UUID second) {
        this.first = first;
        this.second = second;
        this.begin = Main.currentGame.getTime();
    }
    public void sendBegin(CopyOnWriteArrayList<Fight> fights){

        sendMessage(first,second,true,fights);
        PlayerEventHandler.Event("Fight "+Bukkit.getPlayer(second).getName(),Bukkit.getPlayer(first),Bukkit.getPlayer(first).getLocation());
    }
    public void update(CopyOnWriteArrayList<Fight>fights){
        if(Main.currentGame.getTime()- this.begin > 20){
            FightHandler.currentFight.remove(this);
            sendMessage(first,second,false,fights);
        }
    }
    public boolean hasFightOtherThan(Player player, Fight other,CopyOnWriteArrayList<Fight>fights){
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
    private void sendMessage(UUID uu,UUID uu2,boolean bool,CopyOnWriteArrayList<Fight>fights){
            if(Bukkit.getPlayer(uu)!=null){
                if(!hasFightOtherThan(Bukkit.getPlayer(uu),this,fights)) {
                    Bukkit.getPlayer(uu).sendMessage((bool) ? "§cVous êtes en combat." : "§cVous n'êtes plus en combat.");
                }
                if(bool){
                    if(RoleHandler.getRoleOf(Bukkit.getPlayer(uu))!=null&&RoleHandler.getRoleOf(Bukkit.getPlayer(uu))instanceof Trigger_onFightBegin){
                        Trigger_onFightBegin begin = (Trigger_onFightBegin) RoleHandler.getRoleOf(Bukkit.getPlayer(uu));
                        begin.onFightBegin(this);
                    }
                }else{
                    if(RoleHandler.getRoleOf(Bukkit.getPlayer(uu))!=null&&RoleHandler.getRoleOf(Bukkit.getPlayer(uu))instanceof Trigger_OnFightEnd){
                        Trigger_OnFightEnd end = (Trigger_OnFightEnd) RoleHandler.getRoleOf(Bukkit.getPlayer(uu));
                        end.onFightEnd(this);
                    }
                }
            }
            if(Bukkit.getPlayer(uu2)!=null) {
                if (!hasFightOtherThan(Bukkit.getPlayer(uu2), this,fights)) {
                    Bukkit.getPlayer(uu2).sendMessage((bool) ? "§cVous êtes en combat." : "§cVous n'êtes plus en combat.");
                }
                if(bool){
                    if(RoleHandler.getRoleOf(Bukkit.getPlayer(uu2))!=null&&RoleHandler.getRoleOf(Bukkit.getPlayer(uu2))instanceof Trigger_onFightBegin){
                        Trigger_onFightBegin begin = (Trigger_onFightBegin)RoleHandler.getRoleOf(Bukkit.getPlayer(uu2));
                        begin.onFightBegin(this);
                    }
                }else{
                    if(RoleHandler.getRoleOf(Bukkit.getPlayer(uu2))!=null&&RoleHandler.getRoleOf(Bukkit.getPlayer(uu2))instanceof Trigger_OnFightEnd){
                        Trigger_OnFightEnd end = (Trigger_OnFightEnd) RoleHandler.getRoleOf(Bukkit.getPlayer(uu2));
                        end.onFightEnd(this);
                    }
                }
            }
    }
    public UUID getFirst(){
        return first;
    }
    public UUID getSecond(){
        return second;
    }
    public int getBegin(){
        return begin;
    }
}