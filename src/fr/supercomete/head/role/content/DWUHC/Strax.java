package fr.supercomete.head.role.content.DWUHC;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import fr.supercomete.head.role.CommandUse;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.Status;
public final class Strax extends DWRole {
	public CoolDown purify = new CoolDown(3, 0);
	public CommandUse command = new CommandUse("/dw purify");
	public Strax(UUID owner) {
		super(owner);
	}
	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList(
				"§7Vous pouvez trois fois par partie purifier un joueur avec '/dw purify <Joueur>'. Si le joueur ciblé n'est pas infecté, la purification empechera ce joueur d'être infecté, sinon si le joueur ciblé est déja infecté, ce joueur perdra la liste des Cybermen et son effet de force pendant la nuit"+purify.formalizedUtilisation(),
				"§7De plus la purification donne à la cible 1♥ supplémentaires."
				,"§7Vous voyez au dessus de leur tête la barre de vie de tout les joueurs en pourcentage"
				);
	}
	@Override
	public Camps getDefaultCamp() {
		return Camps.DoctorCamp;
	}
	@Override
	public String askName() {
		return "Strax";
	}
	@Override
	public ItemStack[] askItemStackgiven() {
		return null;
	}
	@Override
	public boolean AskIfUnique() {
		return true;
	}
	@Override
	public String AskHeadTag() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA2YTFmZmE0MzM0YzVmMjZjMDVhMGQwZjkyNmYwYzliYTBiNmM1NGQ3NTMzMWI2MzEwZDQ5YmFiZWRhYzlhNSJ9fX0=";
	}
	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Clone};
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{command.generate()};
    }
}