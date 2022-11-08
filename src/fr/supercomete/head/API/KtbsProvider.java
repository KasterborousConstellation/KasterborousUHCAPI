package fr.supercomete.head.API;
import fr.supercomete.head.GameUtils.Game;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.MapHandler;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.structure.StructureHandler;
import fr.supercomete.head.world.BiomeGenerator;
import org.bukkit.entity.Player;
import java.util.ArrayList;

import java.util.Map;
import java.util.UUID;
public class KtbsProvider implements HostProvider,GameProvider,MapProvider{
    @Override
    public ArrayList<UUID> getCohosts() {
        return Main.cohost;
    }

    @Override
    public UUID getHost() {
        return Main.host;
    }

    @Override
    public boolean isHost(Player player) {
        assert Main.host!=null;
        return Main.host.equals(player.getUniqueId());
    }

    @Override
    public boolean isHost(UUID uuid) {
        assert Main.host!=null;
        return Main.host.equals(uuid);
    }

    @Override
    public boolean isCohost(UUID uuid) {
        return Main.cohost.contains(uuid);
    }

    @Override
    public boolean isCohost(Player player) {
        return Main.cohost.contains(player.getUniqueId());
    }

    @Override
    public boolean isBypassed(Player player) {
        return (isCohost(player)||isHost(player))&&Main.bypass.contains(player.getUniqueId());
    }

    @Override
    public boolean isBypassed(UUID uuid) {
        return (isCohost(uuid)||isHost(uuid))&&Main.bypass.contains(uuid);
    }

    @Override
    public Game getCurrentGame() {
        return Main.currentGame;
    }

    @Override
    public ArrayList<UUID> getPlayerList() {
        return Main.getPlayerlist();
    }

    @Override
    public BiomeGenerator getBiomeGenerator() {
        return Main.generator;
    }

    @Override
    public Map<UUID, Integer> getDiamondLimit() {
        return Main.diamondmap;
    }

    @Override
    public StructureHandler getStructureHandler() {
        return Main.structurehandler;
    }

    @Override
    public MapHandler.Map getMap() {
        return MapHandler.getMap();
    }

    @Override
    public boolean IsMapGenerated() {
        return MapHandler.getMap()!=null;
    }
}
