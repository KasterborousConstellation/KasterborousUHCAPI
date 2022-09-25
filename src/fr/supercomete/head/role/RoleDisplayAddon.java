package fr.supercomete.head.role;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class RoleDisplayAddon {
    private ArrayList<String> head;
    private ArrayList<String>feat;
    public RoleDisplayAddon(ArrayList<String>head,ArrayList<String>bottom){
        this.head=head;
        this.feat=bottom;
    }
    public void DisplayHead(Player player){
        for(String string:head){
            player.sendMessage(string);
        }
    }
    public void DisplayBottom(Player player){
        for(String string:feat){
            player.sendMessage(string);
        }
    }
}
