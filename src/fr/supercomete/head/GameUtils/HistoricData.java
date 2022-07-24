package fr.supercomete.head.GameUtils;

import org.bukkit.entity.Player;

import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.role.Role;

public class HistoricData {
	private Role role;
	private DeathCause cause;
	private Offline_Player player;
	public HistoricData(Role role,Player player) {
		this.cause=null;
		this.role=role;
		this.player = new Offline_Player(player);
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public DeathCause getCause() {
		return cause;
	}
	public void setCause(DeathCause cause) {
		this.cause = cause;
	}
	public Offline_Player getPlayer() {
		return player;
	}
	public void setPlayer(Offline_Player player) {
		this.player = player;
	}
}
