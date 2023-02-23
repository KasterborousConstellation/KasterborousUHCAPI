package fr.supercomete.head.Inventory.GUI;

import fr.supercomete.enums.BiomeGeneration;
import fr.supercomete.enums.GenerationMode;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.world.BiomeGenerator;
import fr.supercomete.head.world.worldgenerator;
import fr.supercomete.tasks.SeedFinderTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GenerationGUI extends KTBSInventory {


    public GenerationGUI(Player player) {
        super("§dGénération",54,player);
    }

    @Override
    protected boolean denyDoubleClick() {
        return true;
    }

    @Override
    protected Inventory generateinventory(Inventory tmp) {
        tmp.setItem(33, InventoryUtils.createColorItem(Material.STAINED_GLASS, "§bCréer le monde et prégénerer", 1, (short)3));
        tmp.setItem(9, InventoryUtils.getItem(Material.COAL_ORE, "§fMultiplicateur de Charbon §a"+ Main.generator.getCoalboost(), Arrays.asList(InventoryUtils.ClickTypoAdd+"1",InventoryUtils.ClickTypoRemove+"1")));
        tmp.setItem(18, InventoryUtils.getItem(Material.IRON_ORE, "§fMultiplicateur de Fer §a"+Main.generator.getIronboost(), Arrays.asList(InventoryUtils.ClickTypoAdd+"1",InventoryUtils.ClickTypoRemove+"1")));
        tmp.setItem(27, InventoryUtils.getItem(Material.LAPIS_ORE, "§fMultiplicateur de Lapis §a"+Main.generator.getLapisboost(), Arrays.asList(InventoryUtils.ClickTypoAdd+"1",InventoryUtils.ClickTypoRemove+"1")));
        tmp.setItem(36, InventoryUtils.getItem(Material.GOLD_ORE, "§fMultiplicateur d'or §a"+Main.generator.getGoldboost(), Arrays.asList(InventoryUtils.ClickTypoAdd+"1",InventoryUtils.ClickTypoRemove+"1")));
        tmp.setItem(10, InventoryUtils.getItem(Material.DIAMOND_ORE, "§fMultiplicateur de diamant §a"+Main.generator.getDiamondboost(), Arrays.asList(InventoryUtils.ClickTypoAdd+"1",InventoryUtils.ClickTypoRemove+"1")));
        tmp.setItem(19, InventoryUtils.getItem(Material.LAVA_BUCKET, "§4Lac de lave en surface: "+Main.TranslateBoolean(Main.generator.getLavaLake()), Arrays.asList("§7Defini si les lacs de lave se générent en surface",InventoryUtils.ClickBool)));
        tmp.setItem(29, InventoryUtils.createColorItem(Material.STAINED_GLASS, "§bCréer le monde", 1, (short)14));
        tmp.setItem(31, InventoryUtils.createColorItem(Material.STAINED_GLASS, "§bPrégénerer le monde", 1, (short)5));
        tmp.setItem(13, InventoryUtils.getItem(Material.IRON_DOOR, "§dMondes", Collections.singletonList("§bCliquer ici pour pouvoir changer de monde")));
        for(int i=0;i<9;i++) {
            tmp.setItem(i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 11));
        }
        ItemStack it = Main.generator.getBiome().getItem();
        ItemMeta meta = it.getItemMeta();
        meta.setDisplayName("§bBiome: §a"+Main.generator.getBiome().getName());
        ArrayList<String> strl =Main.SplitCorrectlyString("Défini le biome de la carte. Cliquer pour changer.", 45, "§7");
        strl.add("§cNombre de Seeds: §4"+ worldgenerator.getAmountOfSeed(Main.generator.getBiome()));
        meta.setLore(strl);
        it.setItemMeta(meta);
        tmp.setItem(22, it);
        tmp.setItem(17,InventoryUtils.getItem(Material.BARRIER,"§fGénérer plus de seed",Arrays.asList("§r§aVous pouvez générer plus seeds en cliquant ici.","§r§cCliquez ici une seconde fois pour arrêter la génération.")));
        for(int i=0;i<9;i++) {
            tmp.setItem(53-i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 11));
        }
        tmp.setItem(49, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au menu de configuration")));
        return tmp;
    }

    @Override
    protected boolean onClick(Player player, int currentSlot, KTBSAction action) {
        ClickType currentClick = action.getClick();
        switch (currentSlot) {
            case 17:
                if(!BiomeGenerator.generation && Main.currentGame.isGameState(Gstate.Waiting)){
                    Bukkit.broadcastMessage(Main.UHCTypo+"§aDébut de la génération de seed");
                    BiomeGenerator.generation=true;
                    SeedFinderTask finderTask = new SeedFinderTask();
                    finderTask.runTaskTimer(Main.INSTANCE,0,20*10);
                }else{
                    BiomeGenerator.generation=false;
                    Bukkit.broadcastMessage("§cFin de la génération");
                }
                break;
            case 29:
                if (!Main.currentGame.isGameState(Gstate.Waiting)) {
                    player.sendMessage(Main.UHCTypo + "§cImpossible de générer une carte pendant une partie");
                    return true;
                }
                if(Main.INSTANCE.getGenmode() != GenerationMode.Generating) {
                    worldgenerator.generateworld();
                }else {
                    player.sendMessage(Main.UHCTypo+"§cImpossible: Le monde ne peut pas être créé lors de la génération ou de la prégénération");
                }
                break;
            case 33:
                if (!Main.currentGame.isGameState(Gstate.Waiting)) {
                    player.sendMessage(Main.UHCTypo + "§cImpossible de générer une carte pendant une partie");
                    return true;
                }
                if (Main.INSTANCE.getGenmode() != GenerationMode.Generating && Main.INSTANCE.getGenmode()!=GenerationMode.WorldCreatedOnly) {
                    player.closeInventory();
                    worldgenerator.generateworld();
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            worldgenerator.pregen();

                        }
                    }.runTaskLater(Main.INSTANCE, 200L);


                }
                break;
            case 31:
                if (!Main.currentGame.isGameState(Gstate.Waiting)) {
                    player.sendMessage(Main.UHCTypo + "§cImpossible de pré-générer une carte pendant une partie");
                    return true;
                }
                if(Main.INSTANCE.getGenmode()==GenerationMode.WorldCreatedOnly) {
                    player.closeInventory();
                    worldgenerator.pregen();
                }else {
                    player.sendMessage(Main.UHCTypo+"§cImpossible: Le monde doit être seulement généré afin de lancer une prégénération");
                }
                break;
            case 19:
                Main.generator.setLavaLake(!Main.generator.getLavaLake());
                refresh();
                break;
            case 9:
                if (currentClick.isRightClick() && Main.generator.getCoalboost() < 5) {
                    Main.generator.setCoalboost(Main.generator.getCoalboost() + 1);
                } else if (currentClick.isLeftClick() && Main.generator.getCoalboost() > 1) {
                    Main.generator.setCoalboost(Main.generator.getCoalboost() - 1);
                }
                refresh();
                break;
            case 18:
                if (currentClick.isRightClick() && Main.generator.getIronboost() < 5) {
                    Main.generator.setIronboost(Main.generator.getIronboost() + 1);
                } else if (currentClick.isLeftClick() && Main.generator.getIronboost() > 1) {
                    Main.generator.setIronboost(Main.generator.getIronboost() - 1);
                }
                refresh();
                break;
            case 27:
                if (currentClick.isRightClick() && Main.generator.getLapisboost() < 5) {
                    Main.generator.setLapisboost(Main.generator.getLapisboost() + 1);
                } else if (currentClick.isLeftClick() && Main.generator.getLapisboost() > 1) {
                    Main.generator.setLapisboost(Main.generator.getLapisboost() - 1);
                }
                refresh();
                break;
            case 36:
                if (currentClick.isRightClick() && Main.generator.getGoldboost() < 5) {
                    Main.generator.setGoldboost(Main.generator.getGoldboost() + 1);
                } else if (currentClick.isLeftClick() && Main.generator.getGoldboost() > 1) {
                    Main.generator.setGoldboost(Main.generator.getGoldboost() - 1);
                }
                refresh();
                break;
            case 10:
                if (currentClick.isRightClick() && Main.generator.getDiamondboost() < 5) {
                    Main.generator.setDiamondboost(Main.generator.getDiamondboost() + 1);
                } else if (currentClick.isLeftClick() && Main.generator.getDiamondboost() > 1) {
                    Main.generator.setDiamondboost(Main.generator.getDiamondboost() - 1);
                }
                refresh();
                break;
            case 22:
                int ordinal = Main.generator.getBiome().ordinal();
                ordinal = (ordinal + 1) % BiomeGeneration.values().length;
                Main.generator.setBiome(BiomeGeneration.values()[ordinal]);
                ItemStack it = Main.generator.getBiome().getItem();
                ItemMeta meta = it.getItemMeta();
                meta.setDisplayName("§bBiome: §a" + Main.generator.getBiome().getName());
                ArrayList<String> strl = Main.SplitCorrectlyString("Défini le biome de la carte. Cliquer pour changer.",
                        45, "§7");
                strl.add("§cNombre de Seeds: §4" + worldgenerator.getAmountOfSeed(Main.generator.getBiome()));
                meta.setLore(strl);
                it.setItemMeta(meta);
                player.getOpenInventory().setItem(22, it);
                break;
            case 49:
                new ModeGUI(Main.currentGame.getMode(), player).open();
                break;
            case 13:
                if(Main.currentGame.getGenmode()==GenerationMode.Done ||Main.currentGame.getGenmode()==GenerationMode.WorldCreatedOnly) {
                    new WorldsGUI(player).open();
                }else {
                    player.sendMessage("§cCette action est impossible");
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected boolean onClose(Player holder) {
        return false;
    }
}
