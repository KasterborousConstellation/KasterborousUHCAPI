package fr.supercomete.head.role;

import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.core.Main;

import java.util.ArrayList;

public class CommandUse {
    private final ArrayList<String>names = new ArrayList<>();
    private final ArrayList<Integer>times = new ArrayList<>();
    private final String command;
    public CommandUse(String command){
        this.command=command;
    }
    public void addUse(String name){
        this.names.add(name);
        this.times.add(Main.currentGame.getTime());
    }
    public String generate(){
        StringBuilder build = new StringBuilder(command + "  ");
        for(int i =0;i<names.size();i++){
            String name = names.get(i);
            int time = times.get(i);
            build.append("§6(§f").append(name).append(",").append(TimeUtility.transform(time, "§a")).append("§6) ");
        }
        return build.toString();
    }
}
