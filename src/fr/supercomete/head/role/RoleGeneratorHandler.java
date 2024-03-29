package fr.supercomete.head.role;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.KTBS_Team;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;

import java.util.*;
public class RoleGeneratorHandler {
    public static RoleGenerator StandardGenerator(){
        return new RoleGenerator() {
            @Override
            public HashMap<UUID, Class<?>> map(HashMap<Class<?>, Integer> roles, LinkedList<UUID>players) {
                HashMap<UUID,Class<?>> mapped = new HashMap<>();
                for (final UUID uuid : players) {
                    int random = (roles.size() <= 1) ? 0 : this.random.nextInt(roles.size());
                    Class<?> rt = (Class<?>) roles.keySet().toArray()[random];
                    mapped.put(uuid, rt);
                    if (roles.get(rt) <= 1) {
                        roles.remove(rt);
                    } else {
                        int amount = roles.get(rt);
                        roles.put(rt, (amount - 1));
                    }
                }
                return mapped;
            }

            @Override
            public ArrayList<String> displayCompo(ArrayList<Role> roles) {
                final ArrayList<KasterBorousCamp> camps = new ArrayList<>();
                for(final Role role:roles){
                    if(!camps.contains(role.getDefaultCamp())){
                        camps.add(role.getDefaultCamp());
                    }
                }
                final ArrayList<String> strings=new ArrayList<>();
                strings.add(Main.UHCTypo+"§6Composition §r(§a"+roles.size()+"§r)");
                final HashMap<KasterBorousCamp, HashMap<String,Integer>> campsMap = new HashMap<>();
                for(final KasterBorousCamp cmp:camps){
                    campsMap.put(cmp,new HashMap<>());
                }
                for(final Role role:roles){
                    final HashMap<String,Integer> sub=campsMap.get(role.getDefaultCamp());
                    if(sub.containsKey(role.getName())){
                        sub.put(role.getName(),sub.get(role.getName())+1);
                    }else{
                        sub.put(role.getName(),1);
                    }
                    campsMap.put(role.getDefaultCamp(),sub);
                }
                for(final KasterBorousCamp camp:camps){
                    if (campsMap.get(camp).size() != 0) {
                        strings.add(camp.getColor() + " ➤" + camp.getName()+"§r ("+camp.getColor()+Main.CountIntegerValue(campsMap.get(camp))+"§r)");
                        for (final Map.Entry<String, Integer> e1 : campsMap.get(camp).entrySet()) {
                            strings.add("  -" + camp.getColor() + e1.getKey()+ " §rx" + e1.getValue());
                        }
                    }
                }
                return strings;
            }
        };
    }
    public static RoleGenerator getRolePerTeam(){
        return new RoleGenerator() {
            @Override
            public HashMap<UUID, Class<?>> map(HashMap<Class<?>, Integer> hashMap, LinkedList<UUID> linkedList) {
                final KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
                ArrayList<Class<?>>claz = new ArrayList<>();
                HashMap<UUID,Class<?>> mapped = new HashMap<>();
                for(final Map.Entry<Class<?>,Integer>entry:hashMap.entrySet()){
                    for(int i =0;i< entry.getValue();i++){
                        claz.add(entry.getKey());
                    }
                }
                Collections.shuffle(claz);
                final ArrayList<KTBS_Team> teams = api.getTeamProvider().getTeams();
                for(final KTBS_Team team : teams){
                    Collections.shuffle(claz);
                    for(int i = 0;i<team.getMembers().size();i++){
                        final UUID member = team.getMembers().get(i);
                        mapped.put(member,claz.get(i));
                    }
                }
                return mapped;
            }

            @Override
            public ArrayList<String> displayCompo(ArrayList<Role> roles) {
                final ArrayList<KasterBorousCamp> camps = new ArrayList<>();
                for(final Role role:roles){
                    if(!camps.contains(role.getDefaultCamp())){
                        camps.add(role.getDefaultCamp());
                    }
                }
                final ArrayList<String> strings=new ArrayList<>();
                strings.add(Main.UHCTypo+"§6Composition §r(§a"+roles.size()+"§r)");
                final HashMap<KasterBorousCamp, HashMap<String,Integer>> campsMap = new HashMap<>();
                for(final KasterBorousCamp cmp:camps){
                    campsMap.put(cmp,new HashMap<>());
                }
                for(final Role role:roles){
                    final HashMap<String,Integer> sub=campsMap.get(role.getDefaultCamp());
                    if(sub.containsKey(role.getName())){
                        sub.put(role.getName(),sub.get(role.getName())+1);
                    }else{
                        sub.put(role.getName(),1);
                    }
                    campsMap.put(role.getDefaultCamp(),sub);
                }
                for(final KasterBorousCamp camp:camps){
                    if (campsMap.get(camp).size() != 0) {
                        strings.add(camp.getColor() + " ➤" + camp.getName()+"§r ("+camp.getColor()+Main.CountIntegerValue(campsMap.get(camp))+"§r)");
                        for (final Map.Entry<String, Integer> e1 : campsMap.get(camp).entrySet()) {
                            strings.add("  -" + camp.getColor() + e1.getKey()+ " §rx" + e1.getValue());
                        }
                    }
                }
                return strings;
            }
        };
    }
}