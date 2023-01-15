package fr.supercomete.head.API;

import fr.supercomete.head.GameUtils.Game;
import fr.supercomete.head.structure.StructureHandler;
import fr.supercomete.head.world.BiomeGenerator;

import java.util.ArrayList;

import java.util.Map;
import java.util.UUID;

public interface GameProvider {
    Game getCurrentGame();
    ArrayList<UUID> getPlayerList();
    BiomeGenerator getBiomeGenerator();
    Map<UUID ,Integer> getDiamondLimit();
    StructureHandler getStructureHandler();

}