package fr.supercomete.head.role.RoleState;

import java.util.ArrayList;

import fr.supercomete.head.role.KarvanistaPacte.Proposal;

public class KarvanistaRoleState extends PermanentRoleSate {
	public ArrayList<Proposal>proposal = new ArrayList<>();
	public KarvanistaRoleState(ArrayList<Proposal>proposal) {
        super(RoleStateTypes.Karvanista);
        this.proposal=proposal;
	}

}
