package fr.supercomete.head.Inventory.GUI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.supercomete.head.Inventory.GUI.ModeGUI;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import fr.supercomete.head.role.KasterBorousCamp;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.head.Exception.InvalidModeException;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Role;

public class RoleModeGUI extends KTBSInventory {
    private static KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
	private CampMode m;
	private int currentIndex = 0;
    private CopyOnWriteArrayList<Class<?>> preformated;
    private ArrayList<KasterBorousCamp>primitives;
	public RoleModeGUI(Mode mode, Player player) {
        super("§b"+((Mode)mode).getName() + " Role",54,player);
		if (mode instanceof CampMode) {
			this.m = (CampMode) mode;
            CopyOnWriteArrayList<Class<?>> preformated = api.getModeProvider().getMode(m.getClass()).getRegisteredrole();
            ArrayList<KasterBorousCamp>primitives = new ArrayList<>();
            for(Class<?> claz : preformated){
                try{
                    Method method = claz.getMethod("getCamp",null);
                    Role role = (Role) claz.getConstructors()[0].newInstance(UUID.randomUUID());
                    KasterBorousCamp camp =(KasterBorousCamp) method.invoke(role);
                    if(!primitives.contains(camp)){
                        primitives.add(camp);
                    }
                }catch (NoSuchMethodException |InvocationTargetException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
            this.primitives=primitives;
            this.preformated=preformated;
		} else {
			try {
				throw new InvalidModeException("Error in " + this.getClass(), new Throwable());
			} catch (InvalidModeException e) {
				e.printStackTrace();
			}
		}
	}

    @Override
    protected boolean denyDoubleClick() {
        return true;
    }

    @Override
	protected Inventory generateinventory(Inventory tmp) {

		for (int e = 0; e < 9; e++) {
			tmp.setItem(e, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1,
                    TeamManager.getShortOfChatColor(primitives.get(currentIndex).getColor())));
		}
		for (int e = 0; e < 9; e++) {
			tmp.setItem(53 - e, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1,
					TeamManager.getShortOfChatColor(primitives.get(currentIndex).getColor())));
		}
		int i = 0;

		for (KasterBorousCamp camp : primitives) {
			tmp.setItem(i, InventoryUtils.createColorItem(Material.WOOL, "§r" + camp.getColor() + camp.getName(), 1,
					TeamManager.getShortOfChatColor(camp.getColor())));
			i++;
		}
//		 Main.getRoleTypeList(primitives.get(index));

		CopyOnWriteArrayList<Class<?>> formated = new CopyOnWriteArrayList<Class<?>>();
		
		for(Class<?> clz : preformated) {
			if(api.getRoleProvider().getRoleByClass(clz).getCamp().equals(primitives.get(currentIndex))){
				formated.add(clz);
			}
		}
		for (int e =0;e<formated.size();e++) {
			Class<?> r = formated.get(e);
			Role rt = api.getRoleProvider().getRoleByClass(r);
			List<String> strl = (rt.AskIfUnique())
					? Arrays.asList("§3Camp: " + rt.getDefaultCamp().getColor() + rt.getDefaultCamp().getName(),
							InventoryUtils.ClickBool)
					: Arrays.asList("§3Camp: " + rt.getDefaultCamp().getColor() + rt.getDefaultCamp().getName(),
                    InventoryUtils.ClickTypoAdd + "1", InventoryUtils.ClickTypoRemove + "1");
			final ItemStack it =InventoryUtils.createSkullItem(rt.AskHeadTag(),
					rt.getDefaultCamp().getColor() + rt.getName() + " " + ((rt.AskIfUnique())
							? (Main.currentGame.hasClassInRoleCompoMap(r) ? "§aOn" : "§cOff")
							: "§rx" + ((Main.currentGame.hasClassInRoleCompoMap(r))
									? Main.currentGame.getRoleCompoMap().get(r)
									: 0)),strl);
			it.setAmount( ((Main.currentGame.hasClassInRoleCompoMap(r))?Main.currentGame.getRoleCompoMap().get(r): 0));
			tmp.setItem(e + 9,it);
		}
		tmp.setItem(49, InventoryUtils.getItem(Material.ARROW, "§7Retour", Collections.singletonList("§rRetour au règles")));
		return tmp;
	}

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        ClickType currentClick = action.getClick();
        if (slot == 49) {
            new ModeGUI((Mode) m, holder).open();
        } else {
            if (slot < primitives.size()) {
                currentIndex = (slot);
                refresh();
            } else if (slot >= 9
                    && slot < api.getRoleProvider().getRolesByCamp(api.getModeProvider().getMode(m.getClass()), primitives.get(currentIndex)).size()
                    + 9) {

                Class<?> r = api.getRoleProvider().getRolesByCamp(api.getModeProvider().getMode(m.getClass()), primitives.get(currentIndex))
                        .get(slot - 9);
                HashMap<Class<?>, Integer> array = Main.currentGame.getRoleCompoMap();
                Role rt = api.getRoleProvider().getRoleByClass(r);
                if (rt.AskIfUnique()) {
                    if (array.containsKey(r)) {
                        array.remove(r);
                    } else {
                        array.put(r, 1);
                    }
                } else {
                    if (currentClick.isRightClick()) {
                        if (array.containsKey(r)) {
                            array.put(r, array.get(r) + 1);
                        } else {
                            array.put(r, 1);
                        }
                    } else if (currentClick.isLeftClick()) {
                        if (array.containsKey(r)) {
                            if (array.get(r) > 1) {
                                array.put(r, array.get(r) - 1);
                            } else array.remove(r);
                        }
                    }
                }
                Main.currentGame.setRoleCompoMap(array);
                refresh();
            }
        }
        return true;
    }

    @Override
    protected boolean onClose(Player holder) {
        return false;
    }


}