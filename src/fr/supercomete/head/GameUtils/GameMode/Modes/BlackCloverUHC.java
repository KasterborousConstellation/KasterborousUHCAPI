package fr.supercomete.head.GameUtils.GameMode.Modes;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.Groupable;

public class BlackCloverUHC extends Mode implements CampMode,Groupable {

	public BlackCloverUHC() {
		super("BlackCloverUHC", Material.COAL_BLOCK, new ArrayList<String>());
	}

	@Override
	public void OnKillMethod(Location deathLocation, Player player, Player damager) {

	}

	@Override
	public void onAnyTime(Player player) {

	}

    @Override
    public void onGlobalAnytime() {

    }

    @Override
	public void onDayTime(Player player) {

	}

	@Override
	public void onNightTime(Player player) {

	}

	@Override
	public void onEndingTime(Player player) {

	}

	@Override
	public void onRoleTime(Player player) {

	}

	@Override
	public void OnStart(Player player) {

	}

	@Override
	public void onEpisodeTime(Player player) {
		
	}

	@Override
	public boolean WinCondition() {
		return false;
	}

	@Override
	public Camps[] getPrimitiveCamps() {
		return new Camps[] {Camps.CloverHeart,Camps.Spade,Camps.Solo};
	}

}
