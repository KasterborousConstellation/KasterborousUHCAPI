package fr.supercomete.head.GameUtils.Events.GameEvents.NativeEvents;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.Events.GameEvents.Event;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.Scenarios.Compatibility;
import fr.supercomete.head.GameUtils.Scenarios.CompatibilityType;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import org.bukkit.Bukkit;
import java.util.*;

public class Exposed extends Event {
    HashMap<Camps,ArrayList<Role>>map = new HashMap<>();
    public Exposed(int min, int max, int chance) {
        super(new Compatibility(CompatibilityType.WhiteList,new Class[]{CampMode.class}), min, max, chance);
    }

    @Override
    public String getName() {
        return "Exposed";
    }

    @Override
    public String getDescription() {
        return "Choisi un joueur qui verra son role exposé au milieu de trois autres rôles qui ne lui appartiennent pas.";
    }

    @Override
    public void onExecutionTime() {
        if(RoleHandler.IsRoleGenerated()){
            Random random = new Random();
            int ind = random.nextInt(RoleHandler.getRoleList().size());
            UUID uuid = (UUID) RoleHandler.getRoleList().keySet().toArray()[ind];
            int max_iteration=50;
            final Role role = (Role) RoleHandler.getRoleList().values().toArray()[ind];
            map.put(role.getCamp(),new ArrayList<>(Collections.singletonList(role)));
            ArrayList<Role> list = new ArrayList<>(RoleHandler.getRoleList().values());
            int iteration= 0;
            while(map.size()<2&& iteration<max_iteration){
                map.clear();
                for(int i = 0;i<Math.min(3,RoleHandler.getRoleList().size());i++){
                    int index = random.nextInt(list.size());
                    final Role chosen =list.get(index);
                    if(!map.containsKey(chosen.getDefaultCamp())){
                        map.put(chosen.getCamp(),new ArrayList<>(Collections.singletonList(chosen)));
                    }else {
                        ArrayList<Role> roles = map.get(chosen.getCamp());
                        if (!roles.contains(chosen)){
                            roles.add(chosen);
                            map.put(chosen.getCamp(), roles);
                        }
                    }
                }
                iteration++;
            }
            ArrayList<Role> display = new ArrayList<>();
            for(Camps camps : map.keySet()){
                display.addAll(map.get(camps));
            }
            ArrayList<Role> fin = new ArrayList<>();
            for(int i = 0; i < Math.min(4,display.size());i++){
                fin.add(display.get((display.size()==1)?0:random.nextInt(display.size())));
            }
            Bukkit.broadcastMessage("§6Exposed");
            Bukkit.broadcastMessage("Le role du joueur "+ PlayerUtility.getNameByUUID(uuid)+"§f est parmi ces roles: ");
            for(Role r : fin){
                Bukkit.broadcastMessage("  -"+r.getName());
            }
        }
    }
}
