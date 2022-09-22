package fr.supercomete.head.GameUtils.GameMode.Modes;
import fr.supercomete.ServerExchangeProtocol.Rank.Rank;
import fr.supercomete.head.GameUtils.Command.EchoEchoCommand;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.Command.KasterborousCommand;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.*;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.Offline_Player;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.content.EchoEchoUHC.Lois;
import fr.supercomete.head.world.worldBorderHandler;
import fr.supercomete.head.world.worldgenerator;
import fr.supercomete.nbthandler.NbtTagHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Random;


public class EchoEchoUHC extends Mode implements Groupable , CampMode, Permission, Command,DelayedDeath {

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
    public void onGlobalAnytime(int time) {
        if(time - Main.currentGame.getTimer(Timer.RoleTime).getData() >0 && (time- Main.currentGame.getTimer(Timer.RoleTime).getData())% Main.currentGame.getDataFrom(Configurable.LIST.LoisFlagTime)==0 ){
            final Random random = new Random();
            double x =(random.nextDouble() *Main.currentGame.getCurrentBorder())- Main.currentGame.getCurrentBorder()/2;
            double z=(random.nextDouble() *Main.currentGame.getCurrentBorder()) - Main.currentGame.getCurrentBorder()/2;
            double y = worldBorderHandler.getMaxY(x,z, worldgenerator.currentPlayWorld);
            final Block block = worldgenerator.currentPlayWorld.getBlockAt(new Location(worldgenerator.currentPlayWorld,x,y+1.0,z));
            block.setType(Material.CHEST);
            for(final Player player : Bukkit.getOnlinePlayers()){
                if(RoleHandler.IsRoleGenerated()){
                    final Role role = RoleHandler.getRoleOf(player);
                    if(role instanceof Lois){
                        player.sendMessage(Main.UHCTypo+" §aUn drapeau est apparu en §b"+block.getX() +" "+block.getZ());
                    }
                }
            }
            final FlagType type = FlagType.values()[new Random().nextInt(FlagType.values().length)];
            ItemStack stack = InventoryUtils.getItem(Material.NETHER_STAR,"§b"+type.str+"",Main.SplitCorrectlyString(type.desc,30,"§7"));
            stack = NbtTagHandler.addAnyTag(stack,"FLAG",type.nbt);
            Chest c = (Chest) block.getState();
            c.getBlockInventory().setItem(13,stack);
        }
    }
    private enum FlagType{
        Info("§bInformation","§r§bVous donne l'information de si deux joueurs pris aléatoirement sont dans le même camp ou non",104),
        PVP("§cPVP","§r§fVous donne un effet aléatoire entre §bspeed §cforce §7résistance",105);
        private final String str;
        private final int nbt;
        private final String desc;
        FlagType(String str,String desc,int nbt){
            this.str=str;
            this.desc=desc;
            this.nbt=nbt;
        }
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

    @Override
    public int getDeathDelay() {
        return 10;
    }

    @Override
    public void onSecondtick(int second) {

    }
}
