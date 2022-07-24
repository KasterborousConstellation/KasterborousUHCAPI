package fr.supercomete.head.role.RoleState;

import fr.supercomete.head.role.RoleHandler;
import org.bukkit.Bukkit;

import fr.supercomete.head.role.Role;

public class InfectedRoleState extends Displayed_RoleState implements RoleStateOnAdd{

	public InfectedRoleState(RoleStateTypes type) {
		super(type,"§cTransformé§7");
	}

	@Override
	public void onAdd(Role role) {
		if(Bukkit.getPlayer(role.getOwner())!=null) {
			Bukkit.getPlayer(role.getOwner()).sendMessage("§cVous avez été transformé. Vous avez désormais §4force§c pendant la nuit et votre condition de victoire vient de changer.");
            RoleHandler.sendmessagetoallCyberman("Le joueur "+Bukkit.getPlayer(role.getOwner()).getName()+" a rejoint les Cybermen");
		}
	}
}
