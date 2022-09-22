package fr.supercomete.head.GameUtils.GameMode.Modes;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class NakimeCastle extends Mode{

	public NakimeCastle(String name, Material material, List<String> description) {
		super(name, material, description);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void OnKillMethod(Location deathLocation, Player player, Player damager) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnyTime(Player player) {
		// TODO Auto-generated method stub

	}

    @Override
    public void onGlobalAnytime(int time) {

    }

    @Override
	public void onDayTime(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNightTime(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEndingTime(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoleTime(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnStart(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean WinCondition() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onEpisodeTime(Player player) {
		// TODO Auto-generated method stub
		
	}

}
