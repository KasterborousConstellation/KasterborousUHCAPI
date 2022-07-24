package fr.supercomete.head.GameUtils.GUI;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import fr.supercomete.enums.Camps;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Key.TardisHandler;
import fr.supercomete.head.role.Key.TardisKey;
public class TardisGUI extends GUI {
	private static final CopyOnWriteArrayList<TardisGUI> allGui = new CopyOnWriteArrayList<TardisGUI>();
	private Inventory inv;
	private DWRole role;
	private Player player;
	@SuppressWarnings("unused")
	private Main main;
	public TardisGUI(DWRole role, Main main) {
		this.main=main;
	}
	public TardisGUI(DWRole role, Player player) {
		this.role=role;
		this.player=null;
		this.player=player;
		if (player != null)
			allGui.add(this);
	}
	private String getBonusAsString(TardisKey key) {
		switch (key.getBonus().getType()) {
		case Force:
			return"§4Force §c+"+key.getBonus().getLevel()+"%";
		case Speed:
			return"§bVitesse §1+"+key.getBonus().getLevel()+"%";
		case Heart:
			return"§dCoeurs Bonus §5+"+key.getBonus().getLevel()+"§d½♥";
		case NoFall:
			return"§aNoFall";
		}
		return "§4Supercomete a encore fait une erreur";
	}
	@Override
	protected Inventory generateinv() {
		Inventory tmp = Bukkit.createInventory(null, 9,"§bTardis");
		int i =0;
		for(TardisKey key: TardisHandler.currentTardis.getKeys()) {
			ItemStack it = InventoryUtils.createColorItem(Material.INK_SACK, "§b"+key.getKeytype().getName(), 1, (short)3);
			ItemMeta im = it.getItemMeta();
			im.setLore(Arrays.asList("  "+this.getBonusAsString(key)));
			it.setItemMeta(im);
			tmp.setItem(i, it);
			i++;
		}
		return tmp;
	}
	@Override
	public void open() {
		this.inv = generateinv();
		player.openInventory(inv);
	}
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		for (TardisGUI gui:allGui) {
			if (e.getInventory().equals(gui.inv)) {
				allGui.remove(gui);
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		for (TardisGUI gui:allGui) {
			if (e.getInventory().equals(gui.inv)) {
				e.setCancelled(true);
				int currentSlot = e.getSlot();
				if(gui.player.getGameMode().equals(GameMode.SURVIVAL)) {
					if(currentSlot!=-1 &&currentSlot<TardisHandler.currentTardis.getKeys().size()) {
						if(gui.role.getCamp().equals(Camps.DoctorCamp)) {
							if(gui.role.getTardiskeys().size()==0) {
								TardisKey key =TardisHandler.currentTardis.getKeys().get(currentSlot);
								TardisHandler.currentTardis.removeKey(key);
								gui.role.addTardisKey(key);
								gui.player.sendMessage(Main.UHCTypo+"Vous avez obtenu une clef du Tardis.");
								gui.player.closeInventory();
								RoleHandler.DisplayRole(gui.player);
							}else gui.player.sendMessage(Main.UHCTypo+"§cVous avez déja une clef du Tardis.");
						}else gui.player.sendMessage(Main.UHCTypo+"§cVous n'êtes pas du Camp du Docteur, vous ne pouvez donc pas obtenir de clef du Tardis.");
					}
				}
			}		
		}
	}
}