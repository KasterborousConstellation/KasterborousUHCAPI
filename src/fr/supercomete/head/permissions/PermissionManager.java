package fr.supercomete.head.permissions;
import fr.supercomete.head.Exception.MalformedPermissionException;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.UUID;
import java.util.logging.Level;

public class PermissionManager {
    private static HashMap<UUID, ArrayList<Permissions>> permissions = new HashMap<>();
    public static ArrayList<Permissions> host_perms= new ArrayList<>();
    public static ArrayList<Permissions> cohost_perms= new ArrayList<>();
    public static void sendDenyPermission(Player player){
        player.sendMessage(Main.UHCTypo+"§cVous n'avez pas le droit d'utiliser cette commande.");
    }
    public static void init() throws MalformedPermissionException {
        host_perms = load(Main.INSTANCE.getConfig().get("serverapi.permissions.host"));
        cohost_perms = load(Main.INSTANCE.getConfig().get("serverapi.permissions.cohost"));
        Bukkit.getLogger().log(Level.INFO,"Host permissions: "+host_perms+"");
        Bukkit.getLogger().log(Level.INFO,"Cohost permissions: "+cohost_perms+"");
    }
    public static ArrayList<Permissions> load(Object perm) throws MalformedPermissionException {
        if(perm instanceof String){
            final String perm_str =(String)perm;
            if(perm_str.equals("all")){
                return allPerms();
            }else{
                throw new MalformedPermissionException("An error has occured with the permissions config. Did you meant \"all\" ?");
            }
        }else{
            ArrayList<Permissions> tmp = new ArrayList<>();
            ArrayList<String> host_perm = (ArrayList<String>)perm;
            for(final String str : host_perm){
                try{
                    final Permissions permission = Permissions.valueOf(str);
                    tmp.add(permission);
                }catch (IllegalArgumentException e){
                    throw new MalformedPermissionException("An error has occured with the permissions config. Check the spelling of permissions.");
                }
            }
            return tmp;
        }

    }
    public static ArrayList<Permissions> allPerms(){
        return new ArrayList<>(Arrays.asList(Permissions.values()));
    }
    public static HashMap<UUID,ArrayList<Permissions>> getPerms(){
        return permissions;
    }
}