package fr.supercomete.head.GameUtils.GUI;

import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.content.EchoEchoUHC.Neo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class MangaGui extends GUI {
    private static final CopyOnWriteArrayList<MangaGui> allGui = new CopyOnWriteArrayList<>();
    private Inventory inv;
    private Player player;
    private Neo neo;
    public MangaGui(Main main) {
        this.player=null;
    }
    public MangaGui(Neo neo,Player player) {
        this.player=player;
        this.neo=neo;
        if (player != null)
            allGui.add(this);
    }
    protected Inventory generateinv() {
        Inventory tmp = Bukkit.createInventory(null, 54,"Collection de manga");
        for(int i=0;i<9;i++) {
            tmp.setItem(i, InventoryUtils.getItem(Material.STAINED_GLASS_PANE, " ", null));
        }
        for(int i=0;i<9;i++) {
            tmp.setItem(53-i, InventoryUtils.getItem(Material.STAINED_GLASS_PANE, " ", null));
        }
        for(int i = 0; i< Neo.Manga.values().length; i++){
            if(neo.mangas.contains(Neo.Manga.values()[i])){
                final Neo.Manga manga = Neo.Manga.values()[i];
                tmp.setItem(9+i,InventoryUtils.getItem(Material.BOOK,"§f"+manga.getName(), Collections.singletonList("§r§f"+manga.getBonus().getBonusToString())));
            }else{
                tmp.setItem(9+i,InventoryUtils.createColorItem(Material.WOOL,"§7Manga inconnu",1,(short)15));
            }
        }
        return tmp;
    }
    public void open() {
        this.inv = generateinv();
        player.openInventory(inv);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        for (MangaGui gui:allGui) {
            if (e.getInventory().equals(gui.inv)) {
                e.setCancelled(true);
                final int currentSlot = e.getSlot();
//				    final ClickType action = e.getClick();

            }
        }
    }
    // Optimization --> Forget GUI that have been closed >|<
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        allGui.removeIf(gui -> e.getInventory().equals(gui.inv));
    }
}
