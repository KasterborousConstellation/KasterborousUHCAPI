package fr.supercomete.head.Inventory;

import fr.supercomete.datamanager.FileManager.Fileutils;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.logging.Level;
public class InventoryManager {
    public static int selected = 0;
    private static ArrayList<Inventory> inventories = new ArrayList<>();
    private static final File inventoryFile = new File(Main.INSTANCE.getDataFolder(), "inventory");
    public static final ItemStack blank_inventory = InventoryUtils.createSkullItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE5ZDY0NjEyYmE4ZDFjMDJlZTI3MGQ4NDUxOWFkMGNkNzMxNzViYzQ1ZTdkZGEzZjYzOTY4NmIyY2U2NDU5NiJ9fX0=","§7Vide",Arrays.asList("§7Emplacement vide d'inventaire"));
    public static final ItemStack create_inventory = InventoryUtils.createSkullItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQ4NjA0YjllMTk1MzY3Zjg1YTIzZDAzZDlkZDUwMzYzOGZjZmIwNWIwMDMyNTM1YmM0MzczNDQyMjQ4M2JkZSJ9fX0=","§aCréer",Arrays.asList("§7Cliquez ici pour créer un inventaire personnalisé."));
    public static void init() {
        for(int i =0;i<9;i++){
            inventories.add(null);
        }
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
        for (int i = 0; i<Math.min(files.size(),9);i++) {
            File file = files.get(i);
            int index = Integer.parseInt(file.getName().charAt(file.getName().length()-5)+"");
            try {
                inventories.set(index,InventoryToBase64.fromBase64(Fileutils.loadContent(file)));
            } catch (IOException e) {
                file.delete();
            }
        }
        Bukkit.getLogger().log(Level.INFO,"INVENTORY LOADING LOADING COMPLETE");
    }
    public static Inventory getSelectedInventory(){
        return inventories.get(selected);
    }
    public static void delete(int i){
        inventories.set(i,null);
        for(File file : inventoryFile.listFiles()){
            if((file.getName().charAt(file.getName().length() - 5) + "").equals(i + "")){
                file.delete();
            }
        }
    }
    public static ArrayList<Inventory> list(){
        return inventories;
    }
    public static void set(Inventory inventory,int i){
        if(i>8){
            return;
        }
        save(inventory,i);
        inventories.set(i,inventory);
    }
    public static void save(Inventory inventory,int i){
        final String hash = InventoryToBase64.toBase64(inventory);
        if(hash.isEmpty()){
            return;
        }
        final File file = new File(inventoryFile,"Inventory#"+i+".inv");
        try {
            Fileutils.createFile(file);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE,"KTBS FAULT: IMPOSSIBLE TO CREATE AN INVENTORY FILE: ("+file.getName()+")");
            return;
        }
        Fileutils.save(file,hash);
    }
    private static String[] array = new String[]
            {
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmE2ZmUwZTVhNTkyNWU1MjQxODA0YWFmYWM2YzRmMjFhMzcwY2MyZmZjNGQ4NzY1N2RlNTcxMjRkNmIyODIwIn19fQ==",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzEzYmQxNDFmMjE1ZjNkODMyNjJmMzc3NTg3Mzk5NzIyZTFjOGNlNjg4NjBmNDNmOGIyOTlkMmNiNDMzMTUifX19",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWUzZGNkYmVlYTM1ZjdlY2IxNjY3NGFjNmZmZWQ3OTA2Nzc1MTM5ZTIyYzc4YmY3Mjk1Mzg2ZDMxOTRlOWY2In19fQ==",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWYxZTY5ZWM1MmFiMWUzYmQxODU1OTFjMTY0MmI1Yjc0NDk4ZTExYTUzMTQwZWZmNTVmNTMyMTk4OGZjOTk2In19fQ==",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDI5MzJiNjZkZWNhZWZmNmViZGM3YzViZTZiMjQ2N2FhNmYxNGI3NDYzODhhMDZhMmUxZTFhODQ2M2U5ZTEyMiJ9fX0=",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTY1ZTI0ZWI2NTVkNTA3NzYzMjU0YjQzNjRlOWZlM2MzNmI3ZGJhMzY2YzYzNDczNzZmOTdiYzk3ZTVjMCJ9fX0=",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTc5YWRkM2U1OTM2YTM4MmE4ZjdmZGMzN2ZkNmZhOTY2NTNkNTEwNGViY2FkYjBkNGY3ZTlkNGE2ZWZjNDU0In19fQ==",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQzYzc5Y2Q5YzJkMzE4N2VhMDMyNDVmZTIxMjhlMGQyYWJiZTc5NDUyMTRiYzU4MzRkZmE0MDNjMTM0ZTI3In19fQ==",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODAyMWU3M2Q1ZjU3NTM5ZDNhOWY1YmM4NDI2Y2IyOGE3MzdiZWYxODZjMTU1ZWM2NTYzZmIzYzExMWMyYjRiMyJ9fX0="
            };
    public static ItemStack getInventoryHead(int i,boolean selected){
        ItemStack stack = InventoryUtils.createSkullItem(
                selected?"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI0MzE5MTFmNDE3OGI0ZDJiNDEzYWE3ZjVjNzhhZTQ0NDdmZTkyNDY5NDNjMzFkZjMxMTYzYzBlMDQzZTBkNiJ9fX0=":array[i],
                "§7Inventaire #"+(i+1),
                Arrays.asList("§aClique gauche pour sélectionner pour la partie","§cShift + Clique droit pour supprimer")
        );
        if(selected){
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(meta.getDisplayName() +" §a[§bSélectionné§a]");
            meta.addEnchant(Enchantment.ARROW_INFINITE,1,true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            stack.setItemMeta(meta);
        }
        return stack;
    }
}