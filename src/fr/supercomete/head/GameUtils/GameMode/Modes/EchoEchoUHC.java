package fr.supercomete.head.GameUtils.GameMode.Modes;
import fr.supercomete.ServerExchangeProtocol.Rank.Rank;
import fr.supercomete.head.GameUtils.Command.EchoEchoCommand;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.Command.KasterborousCommand;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.*;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;


public class EchoEchoUHC extends Mode implements Groupable, DelayedDeath , CampMode, Permission, Command {

    public EchoEchoUHC() {
        super("§bEchoEcho UHC", Material.DIAMOND, Collections.singletonList(""));
    }
    @Override
    public void DecoKillMethod(Offline_Player player){

    }
    @Override
    public void OnKillMethod(Location deathLocation, Player player, @Nullable Player damager) {


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
    public void onEpisodeTime(Player player){

    }

    @Override
    public boolean WinCondition() {
        return false;
    }

    @Override
    public int getDeathDelay() {
        return 0;
    }

    @Override
    public void onSecondtick(int timer) {

    }

    @Override
    public Camps[] getPrimitiveCamps() {
        return new Camps[]{Camps.EchoEcho,Camps.Revolutionnaires,Camps.Solo};
    }

    @Override
    public Rank getPermission() {
        return Rank.Admin;
    }


    @Override
    public KasterborousCommand getCommand() {
        return new EchoEchoCommand("echo");
    }
}
