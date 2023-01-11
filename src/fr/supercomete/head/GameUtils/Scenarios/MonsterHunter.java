package fr.supercomete.head.GameUtils.Scenarios;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.TeamMode;
import fr.supercomete.head.GameUtils.Team;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.PlayerUtils.BonusHandler;
import fr.supercomete.head.core.KasterborousRunnable;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MonsterHunter implements KasterborousScenario {
    public HashMap<UUID,Objective>objectives=new HashMap<>();
    @Override
    public String getName() {
        return "Monster-Hunter";
    }

    @Override
    public Compatibility getCompatiblity() {
        return new Compatibility(CompatibilityType.WhiteList,new Class<?>[]{TeamMode.class});
    }

    @Override
    public Material getMat() {
        return Material.MOB_SPAWNER;
    }
    @Nullable
    @Override
    public List<KasterborousRunnable> getAttachedRunnable() {
        return Collections.singletonList(new KasterborousRunnable() {
            @Override
            public String name() {
                return "Monster-Hunter-Task";
            }

            @Override
            public void onAPILaunch(KtbsAPI api) {

            }

            @Override
            public void onAPIStop(KtbsAPI api) {

            }

            @Override
            public void onGameLaunch(KtbsAPI api) {
                if(!api.getScenariosProvider().IsScenarioActivated(getName())){
                    return;
                }
                objectives.clear();
                for(Team team : Bukkit.getServicesManager().load(KtbsAPI.class).getTeamProvider().getTeams()){
                    objectives.put(team.getTeam_id(), new Objective(team.getMaxPlayerAmount()));
                }
            }

            @Override
            public void onGameEnd(KtbsAPI api) {
                if(!api.getScenariosProvider().IsScenarioActivated(getName())){
                    return;
                }
                objectives.clear();
            }
            int tick =0;
            @Override
            public void onTick(Gstate gstate, KtbsAPI api) {
                if(!api.getScenariosProvider().IsScenarioActivated(getName())){
                    return;
                }
                tick=(tick+1)%20;
                if(tick==0){
                    for(final Team team : Bukkit.getServicesManager().load(KtbsAPI.class).getTeamProvider().getTeams()){
                        final Objective objective = objectives.get(team.getTeam_id());
                        if(objective.isCompleted()){
                            if(!objective.IsRewarded()){
                                objective.setRewarded(true);
                                for(final UUID uuid : team.getMembers()){
                                    BonusHandler.addBonus(uuid,new Bonus(BonusType.Heart,20));
                                }
                                Bukkit.broadcastMessage("§7L'équipe "+ TeamManager.getColorOfShortColor(team.getColor())+team.getTeamName()+"§7 a complété l'objectif Monster-Hunter");
                            }
                        }
                    }
                }
            }

            @Override
            public void onTimer(Timer timer) {

            }
        });
    }
}
