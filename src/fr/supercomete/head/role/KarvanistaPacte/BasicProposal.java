package fr.supercomete.head.role.KarvanistaPacte;

import java.util.UUID;

import fr.supercomete.head.role.RoleState.KarvanistaRoleState;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.content.DWUHC.Karvanista;

public class BasicProposal extends Proposal {
	
	public BasicProposal(UUID karvanista, UUID ally) {
		super(karvanista, ally);
		IsActivated=true;
	}

	@Override
	public int AskCost() {
		return 0;
	}

	@Override
	public String AskName() {
		return "Base";
	}

	@Override
	public String AskDescription() {
		return "§7Ce composant de pacte est activé par defaut et ne peut pas être supprimé. Il vous fait gagner avec votre allié. Si il meurt ou que vous mourrez, l'autre restera en vie. Il faudra persuader votre allié en restant §65min§7 proche de lui à moins de 20blocs. Une barre de progression sera visible au dessus de sa tête. Si vous venez a mourir sans l'avoir persuader alors votre allié ne changement pas de camp. §c(Ce composant est §cmodifiable avec les autres composants)";
	}

	@Override
	public void tick(Player karvanista, Player ally) {
		//Nothing
	}

	@Override
	public void start(Player karvanista, Player ally) {
		final String message = "§6Le pacte de Karvanista vient d'être conclu '/dw pacte' pour voir les modalités du pacte. Vous devez maintenant gagner ensemble et éliminé tout les autres camps.";
		final String allymess="§6Vous êtes l'humain que Karvanista doit proteger. Vous devez maintenant gagner avec §c"+karvanista.getName()+"§6\n";
		karvanista.sendMessage(message);
		ally.sendMessage(allymess+message);
		RoleHandler.getRoleOf(karvanista).setCamp(Camps.DuoKarvanista);
		RoleHandler.getRoleOf(ally).setCamp(Camps.DuoKarvanista);
		RoleHandler.getRoleOf(ally).addRoleState(new KarvanistaRoleState(((Karvanista)RoleHandler.getRoleOf(karvanista)).getAllpacte()));
	}

	@Override
	public Material AskMaterial() {
		return Material.IRON_BLOCK;
	}

	
}