package fr.supercomete.head.GameUtils.GameMode.Modes;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.supercomete.head.PlayerUtils.Offline_Player;

public class Null_Mode extends Mode {
	public Null_Mode() {
		super("",Material.AIR,Arrays.asList(""));
	}

	@Override
	public void DecoKillMethod(Offline_Player player) {
		return;
	}

	@Override
	public void OnKillMethod(Location deathLocation, Player player, Player damager) {	
	}
	@Override
	public void onAnyTime(Player player) {	
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
		return false;
	}

	@Override
	public void onEpisodeTime(Player player) {
		// TODO Auto-generated method stub
		
	}
	
}
