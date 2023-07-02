package fr.supercomete.head.Inventory.GUI;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.role.KasterBorousCamp;
import fr.supercomete.head.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;


import fr.supercomete.head.GameUtils.HistoricData;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleHandler;
public class RoleListGUI extends KTBSInventory {
	private String openType;
    ArrayList<KasterBorousCamp>primitives;
	public RoleListGUI(Player player,String openType) {
        super("Roles",54,player);
        CopyOnWriteArrayList<Class<?>> preformated = Bukkit.getServicesManager().load(KtbsAPI.class).getModeProvider().getMode(Main.currentGame.getMode().getClass()).getRegisteredrole();
        ArrayList<KasterBorousCamp> primitives = new ArrayList<>();
        for(Class<?> claz : preformated){
            try{
                Method method = claz.getMethod("getDefaultCamp",null);
                Role role = (Role) claz.getConstructors()[0].newInstance(UUID.randomUUID());
                KasterBorousCamp camp =(KasterBorousCamp) method.invoke(role);
                if(!primitives.contains(camp)){
                    primitives.add(camp);
                }
            }catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        this.primitives=primitives;
        if(Objects.equals(openType, "open")){
            this.openType=primitives.get(0).getName();
        }
	}

    @Override
    protected boolean denyDoubleClick() {
        return false;
    }

    @Override
	protected Inventory generateinventory(Inventory tmp) {
		for(int i = 0 ; i < 9 ; i++) {
			tmp.setItem(i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)0));
		}
		for(int i = 0 ; i < 9 ; i++) {
			tmp.setItem(53-i, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)0));
		}
		CampMode campmode = (CampMode) Main.currentGame.getMode();
		int i=0;
		for (KasterBorousCamp camp : primitives) {
			tmp.setItem(i, InventoryUtils.createColorItem(Material.WOOL, "§r" + camp.getColor() + camp.getName(), 1,TeamManager.getShortOfChatColor(camp.getColor())));
			i++;
		}
		int e = 0;
		for(Entry<UUID,HistoricData> entry : RoleHandler.getHistoric().getRoleList().entrySet()) {
			if(entry.getValue().getRole().getDefaultCamp().getName()==openType) {
				final ItemStack stack = new ItemStack(Material.SKULL_ITEM);
				stack.setDurability((short)3);
				final SkullMeta meta = (SkullMeta) stack.getItemMeta();
				meta.setDisplayName(ChatColor.WHITE+entry.getValue().getPlayer().getUsername());
				meta.setOwner(entry.getValue().getPlayer().getUsername());
				meta.setLore(RoleHandler.getRoleDescription(entry.getValue()));
				stack.setItemMeta(meta);
				tmp.setItem(9+e, stack);
				e++;
			}
		}
		return tmp;
	}

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        CampMode mode =(CampMode) Main.currentGame.getMode();
        if(slot<primitives.size()) {
            openType= primitives.get(slot).getName();
            refresh();
        }
        return true;
    }

    @Override
    protected boolean onClose(Player holder) {
        return false;
    }
}
