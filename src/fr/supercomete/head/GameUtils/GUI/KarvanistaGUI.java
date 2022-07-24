package fr.supercomete.head.GameUtils.GUI;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.supercomete.head.role.RoleState.KarvanistaRoleState;
import fr.supercomete.head.role.RoleState.RoleStateTypes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.RoleHandler;

import fr.supercomete.head.role.KarvanistaPacte.Proposal;
import fr.supercomete.head.role.content.DWUHC.Karvanista;
public class KarvanistaGUI extends GUI{
	private static final CopyOnWriteArrayList<KarvanistaGUI> allGui = new CopyOnWriteArrayList<KarvanistaGUI>();
	private Inventory inv;
	
	private Player player;
	public KarvanistaGUI( Main main) {
		this.player=null;
	}
	public KarvanistaGUI(Player player) {
		this.player=null;
		this.player=player;
		if (player != null)
			allGui.add(this);
	}
	private ArrayList<Proposal>getProposal(Player player){
		if(RoleHandler.getRoleOf(player)instanceof Karvanista)
			return ((Karvanista)RoleHandler.getRoleOf(player)).allpacte;
		else
			return ((KarvanistaRoleState)RoleHandler.getRoleOf(player).getRoleState(RoleStateTypes.Karvanista)).proposal;
	}
	private int calc(Player player) {
		int e = 5;
		
		for(Proposal prop : getProposal(player)) {
			if(prop.IsActivated) {
				e-=prop.getCost();
			}
		}
		return e;
	}
	private boolean isconcluded(Player player) {
		if(RoleHandler.getRoleOf(player)instanceof Karvanista) {
			return ((Karvanista)RoleHandler.getRoleOf(player)).isConcluded();
		}else return true;
	}
	protected Inventory generateinv() {
		Inventory tmp = Bukkit.createInventory(null, 54,"Pacte");
		for(int e=0;e<9;e++) {
			tmp.setItem(e, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
		}
		for(int r=45;r<54;r++) {
			tmp.setItem(r, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)2));
		}
		int e =9;
		for(final Proposal proposal :getProposal(player)) {
			if(!isconcluded(player)) {
				tmp.setItem(e, proposal.getItemStack());
				e++;
			}else {
				if(proposal.IsActivated) {
					tmp.setItem(e, proposal.getItemStack());
					e++;
				}
			}
			
			
		}
		
		if(!isconcluded(player)) {
			tmp.setItem(4, InventoryUtils.createColorItem(Material.YELLOW_FLOWER, "Points de pacte", this.calc(player), (short)0));
			tmp.setItem(49, InventoryUtils.createColorItem(Material.WOOL, "§aConfirmer le pacte", 1, (short)5));
		}
		
		return tmp;
	}
	public void open() {
		this.inv = generateinv();
		player.openInventory(inv);
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		for (KarvanistaGUI gui:allGui) {
			if (e.getInventory().equals(gui.inv)) {
				e.setCancelled(true);
				int currentSlot = e.getSlot();
				ClickType action = e.getClick();
				switch (currentSlot) {
				case 49:
					if(!isconcluded(gui.player)) {
						if(gui.calc(gui.player)>0) {
							if(action.isShiftClick()) {
								((Karvanista)RoleHandler.getRoleOf(gui.player)).setConcluded(true);
								gui.player.sendMessage(Main.UHCTypo+"§aVous avez conclu votre pacte, a partir de maintenant vous pouvez commencer a persuader. Vous pouvez revoir les conditions de votre pacte avec '/dw pacte'");
							}else
								gui.player.sendMessage(Main.UHCTypo+"§cIl vous reste des points de pacte. Pour confirmer votre pacte quand même, shift + click sur le bouton de confirmation.");
						}else if(gui.calc(gui.player)==0) {
							((Karvanista)RoleHandler.getRoleOf(gui.player)).setConcluded(true);
							gui.player.sendMessage(Main.UHCTypo+"§aVous avez conclu votre pacte, a partir de maintenant vous pouvez commencer a persuader. Vous pouvez revoir les conditions de votre pacte avec '/dw pacte'");
						}else{
                            gui.player.sendMessage(Main.UHCTypo+"§cVous ne pouvez pas conclure votre pacte si vous avez un nombre négatif de points de pacte");
                        }
					}
					break;
				default:
					if(currentSlot>9 && currentSlot< 9 + getProposal(gui.player).size() && !isconcluded(gui.player)) {
						getProposal(gui.player).get(-9+currentSlot).setActivated(!getProposal(gui.player).get(-9+currentSlot).IsActivated);
						gui.open();
					}
					break;
				}
			}
		}
	}
	// Optimization --> Forget GUI that have been closed >|<
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
        allGui.removeIf(gui -> e.getInventory().equals(gui.inv));
	}
}