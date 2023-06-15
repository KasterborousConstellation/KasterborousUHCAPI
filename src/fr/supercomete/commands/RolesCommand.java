package fr.supercomete.commands;
import java.util.*;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.NRGMode;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleHandler;
public class RolesCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("roles") || cmd.getName().equalsIgnoreCase("compo")) {
				RoleHandler.showcompo(player);
			}
		}
		return false;
	}

	public static void display(Player player) {
        final NRGMode mode = (NRGMode) Main.currentGame.getMode();
        final ArrayList<String> array;
        if(RoleHandler.IsHiddenRoleNCompo){
            array = new ArrayList<>(Collections.singletonList(Main.UHCTypo + "§4Impossible la composition est cachée"));
        }else{
            if(RoleHandler.IsRoleGenerated()){
                array=mode.getRoleGenerator().displayCompo(new ArrayList<>(RoleHandler.getRoleList().values()));
            }else{
                final ArrayList<Role> roles=new ArrayList<>();
                final HashMap<Class<?>,Integer>map=Main.currentGame.getRoleCompoMap();
                for(final Map.Entry<Class<?>,Integer>entry: map.entrySet()){
                    for(int i =0;i<entry.getValue();i++){
                        roles.add(RoleBuilder.Build(entry.getKey(), UUID.randomUUID()));
                    }
                }
                array=mode.getRoleGenerator().displayCompo(roles);
            }
        }
        for(final String str:array){
            player.sendMessage(str);
        }
	}
}