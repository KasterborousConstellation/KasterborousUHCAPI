package fr.supercomete.head.Inventory;

import fr.supercomete.datamanager.FileManager.Fileutils;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.logging.Level;
public class InventoryManager {
    private static ArrayList<Inventory> inventories = new ArrayList<>();
    private static final File inventoryFile = new File(Main.INSTANCE.getDataFolder(), "inventory");
    public static void init() {
        if (!inventoryFile.exists()) {
            try {
                inventoryFile.mkdir();
                Fileutils.createFile(inventoryFile);
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE,"KTBS FAULT: IMPOSSIBLE TO CREATE INVENTORY FILE");
                return;
            }
        }
        ArrayList<File> files = new ArrayList<>(
                Arrays.asList(
                        Objects.requireNonNull(inventoryFile.listFiles())
                )
        );
        files.sort(Comparator.comparing(File::getName));
        for(int i =0;i<files.size();i++){
            File file = files.get(i);
            try {
                inventories.add(InventoryToBase64.fromBase64(Fileutils.loadContent(file)));
            } catch (IOException e) {
                file.delete();
            }
        }
        Bukkit.getLogger().log(Level.INFO,"INVENTORY LOADING LOADING COMPLETE");
    }
    public static void delete(int i){
        inventories.remove(i);
        Objects.requireNonNull(inventoryFile.listFiles())[i].delete();
    }
    public static void add(Inventory inventory){
        final String hash = InventoryToBase64.toBase64(inventory);
        if(hash.isEmpty()){
            return;
        }
        final File file = new File(inventoryFile,"Inventory"+Long.toHexString(System.currentTimeMillis()));
        try {
            Fileutils.createFile(file);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE,"KTBS FAULT: IMPOSSIBLE TO CREATE AN INVENTORY FILE: ("+file.getName()+")");
            return;
        }
        Fileutils.save(file,hash);
        inventories.add(inventory);
    }
}