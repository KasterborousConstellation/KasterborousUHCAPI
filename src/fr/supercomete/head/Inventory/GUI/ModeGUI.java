package fr.supercomete.head.Inventory.GUI;

import java.util.Collections;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSAction;
import fr.supercomete.head.Inventory.inventoryapi.content.KTBSInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import fr.supercomete.head.core.Main;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.Game;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.CampMode;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.TeamMode;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.GameMode.Modes.Null_Mode;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.PlayerUtility;


public class ModeGUI extends KTBSInventory {
    private static final KtbsAPI api = Bukkit.getServicesManager().load(KtbsAPI.class);
	private Mode m;
	public ModeGUI(Mode mode, Player player) {
        super("§dMenu Principal",54,player);
		this.m = mode;
	}

    @Override
    protected boolean denyDoubleClick() {
        return false;
    }
    @Override
    protected Inventory generateinventory(Inventory tmp){
		if(m instanceof Null_Mode) {
			for(int i=1;i<api.getModeProvider().getRegisteredModes().size();i++){
				tmp.setItem(i-1, InventoryUtils.getItem(api.getModeProvider().getRegisteredModes().get(i).getMaterial(),"§r§b"+api.getModeProvider().getRegisteredModes().get(i).getName(),api.getModeProvider().getRegisteredModes().get(i).getDescription()));
			}
		}else {
			tmp.setItem(0,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)3));
			tmp.setItem(8,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)3));
			tmp.setItem(53, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)3));
			tmp.setItem(45, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)3));
			tmp.setItem(9,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(1,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(7,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(17, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(46, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(36, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(52, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(44, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(47, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(2,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(6,  InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(51, InventoryUtils.createColorItem(Material.STAINED_GLASS_PANE, " ", 1, (short)11));
			tmp.setItem(48, InventoryUtils.createColorItem(Material.STAINED_CLAY, "§rStart", 1, (short)5));
			ItemStack it2=new ItemStack(Material.SKULL_ITEM);
			SkullMeta im2=(SkullMeta)it2.getItemMeta();
			it2.setDurability((short)3);
			im2.setOwner(getHolder().getName());
			if(Main.currentGame.getMode()instanceof TeamMode)im2.setLore(Collections.singletonList("§c⚠Automatique car les équipes sont activées"));
			im2.setDisplayName("§rSlots");
			it2.setItemMeta(im2);
			tmp.setItem(26, it2);
			tmp.setItem(4,InventoryUtils.getItem(Material.PAPER,"§rEvents",null));
			tmp.setItem(5, InventoryUtils.getItem(Material.ANVIL, "§rConfigurable",null));
			tmp.setItem(10, InventoryUtils.getItem(Material.BOOK, "§rWhitelist/Ouverture",null));
			tmp.setItem(16, InventoryUtils.getItem(Material.DIAMOND_CHESTPLATE, "§rEnchantement", null));
			tmp.setItem(37, InventoryUtils.getItem(Material.BOOK_AND_QUILL, "§rScenario", null));
			tmp.setItem(43, InventoryUtils.getItem(Material.CHEST, "§rStuff", null));
			tmp.setItem(18, InventoryUtils.getItem(Material.WATCH, "§rTimers", null));
			tmp.setItem(27, InventoryUtils.getItem(Material.BARRIER, "§rBordure", null));
			ItemStack i = InventoryUtils.createColorItem(Material.GRASS,"§rGénération", 1, (short)3);
			ItemMeta im = i.getItemMeta();
			im.setLore(Collections.singletonList("§cLa génération n'est pas sauvegardée dans les configurations"));
			tmp.setItem(3,  i);
			tmp.setItem(50, InventoryUtils.createColorItem(Material.STAINED_CLAY, "§rStop", 1, (short)14));
			if(m instanceof CampMode) {
				tmp.setItem(35, InventoryUtils.createColorItem(Material.STAINED_CLAY, "§rRôles", 1, (short)6));
			}else if(m instanceof TeamMode) {
				ItemStack titem =InventoryUtils.createColorItem(Material.BANNER, "§rTeams", 1, (short)0);
				if(!Main.currentGame.isGameState(Gstate.Waiting)) {
					ItemMeta itmTeam=titem.getItemMeta();
					itmTeam.setLore(Collections.singletonList("§cImpossible quand la partie a déjà commencer"));
				}
				tmp.setItem(35, titem);
			}
			tmp.setItem(45, InventoryUtils.getItem(Material.BARRIER, "§7Retour", Collections.singletonList("§rRetour au choix du mode de jeu")));
		}
		return tmp;
	}

    @Override
    protected boolean onClick(Player holder, int slot, KTBSAction action) {
        if(m instanceof Null_Mode) {
            if (!Main.currentGame.isGameState(Gstate.Waiting)) {
                holder.sendMessage(Main.UHCTypo + "§cVous ne pouvez pas changer de mode de jeux pendant une partie");
                return true;
            }
            if (slot < api.getModeProvider().getRegisteredModes().size() - 1) {
                Mode chosenMode = api.getModeProvider().getRegisteredModes().get(slot + 1);
                Main.currentGame = new Game(chosenMode.getName(), Main.INSTANCE);
                TeamManager.setupTeams();
                m=api.getModeProvider().getRegisteredModes().get(slot + 1);
                for(Player p : Bukkit.getOnlinePlayers()) {
                    PlayerUtility.GiveHotBarStuff(p);
                }
                refresh();
            }
        }else{
            switch (slot) {
                case 4:
                    new EventGUI(holder).open();
                    break;
                case 3:
                    if(Main.currentGame.getGamestate().equals(Gstate.Waiting)) {
                        new GenerationGUI(holder).open();
                    }else holder.sendMessage(Main.UHCTypo+"§cImpossible d'acceder à la génération pendant la partie");
                    break;
                case 48:
                    Main.INSTANCE.StartGame(holder);
                    break;
                case 26:
                    new SlotGUI(holder).open();
                    break;
                case 50:
                    Main.StopGame(holder);
                    break;
                case 10:
                    new WhitelistGUI(holder).open();
                    break;
                case 18:
                    new TimerGUI(holder).open();
                    break;
                case 45:
                    if (Main.currentGame.isGameState(Gstate.Waiting)) {
                        Main.currentGame = new Game((new Null_Mode()).getName(),Main.INSTANCE);
                        m=new Null_Mode();
                        refresh();
                    } else
                        holder.sendMessage(Main.UHCTypo + "§cErreur avant de changer de mode de jeux, veuillez mettre fin à la partie");
                    break;
                case 37:
                    new ScenarioGUI(holder).open();
                    break;
                case 27:
                    new BorderGUI(holder).open();
                    break;
                case 43:
                    new StuffConfigGUI(holder).open();
                    break;
                case 16:
                    new EnchantLimitGUI(holder,true).open();
                    break;
                case 5:
                    new ConfigurableGUI(holder,"Principale").open();
                    break;
                case 35:
                    if(m instanceof CampMode) {
                        new RoleModeGUI(api.getGameProvider().getCurrentGame().getMode(),holder).open();
                    }else if(m instanceof TeamMode) {
                        if(Main.currentGame.getGamestate()==Gstate.Waiting){
                            new TeamConfig(holder).open();
                        }else holder.sendMessage(Main.UHCTypo+"§cLes équipes ne peuvent pas être modifiés pendant la partie.");
                    }
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    @Override
    protected boolean onClose(Player holder) {
        return false;
    }
}