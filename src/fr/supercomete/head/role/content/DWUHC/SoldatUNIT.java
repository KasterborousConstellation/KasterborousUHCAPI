package fr.supercomete.head.role.content.DWUHC;
import java.util.*;
import java.util.Map.Entry;

import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import fr.supercomete.enums.Camps;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Triggers.Trigger_WhileAnyTime;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;
import fr.supercomete.head.role.RoleState.Displayed_RoleState;
import fr.supercomete.head.role.RoleState.RoleStateTypes;
import fr.supercomete.tasks.generatorcycle;
public final class SoldatUNIT extends DWRole implements Trigger_WhileAnyTime, HasAdditionalInfo {
	public static int pseudorandom = 0;
	private HashMap<UUID, Integer> enquetes;
    private boolean activated;
	public SoldierType soldiertype;
	public CoolDown generalCoolDown;
	public SoldatUNIT(UUID owner) {
		super(owner);
		this.soldiertype = SoldierType.values()[pseudorandom % SoldierType.values().length];
		this.addRoleState(new Displayed_RoleState(RoleStateTypes.UNITSoldier, "§e" + soldiertype.getName()));
        switch (soldiertype) {
            case Garde:
                setActivated(false);
                generalCoolDown = new CoolDown(10, 0);
                break;
            case Enqueteur :
                enquetes = new HashMap<>();
                generalCoolDown = new CoolDown(3, 0);
                break;
            case Medic:
                generalCoolDown = new CoolDown(2, 0);
                break;
        }
		pseudorandom++;
	}
	@Override
	public String askName() {
		return "Soldat d'UNIT";
	}
	@Override
	public Camps getDefaultCamp() {
		return Camps.DoctorCamp;
	}
	@Override
	public List<String> askRoleInfo() {
		List<String> additionnal;
		if (soldiertype == SoldierType.Enqueteur) {
			additionnal = Collections.singletonList("§7Vous pouvez avec la commande '/dw enquete <Joueur>', lancer une enquete sur un joueur. L'enquête dure §a"
                    + TimeUtility.transform(Main.currentGame.getDataFrom(Configurable.LIST.SoldierSpyTime), "", "", "")
                    + "§7 et a la fin de celle-ci vous obtiendrez soit les effets de cette personne, soit la première lettre de son rôle ou bien la dernière. Vous pouvez lancer plusieurs enquête en même temps.");
		} else if (soldiertype == SoldierType.Garde) {
			additionnal =Arrays.asList("§7Vous avez 10 flêches empoisonnées qui empoisonnent le joueur touché.","§7Vous avec la commande '/dw poison' activer ou désactiver le poison sur vos tir de fleches. Attention vous n'avez que 10 fleches empoisonnées, et une fleche est utilisée quand elle touche sa cible.");
		} else {
			additionnal = Arrays.asList("§7Vous régénerer automatiquement votre vie, 1/2♥ toute les 10s."," Vous pouvez avec la commande '/dw heal <Joueur>' faire remonter 2 fois dans la partie la vie d'un joueur a son maximum."+generalCoolDown.formalizedUtilisation());
		}
		ArrayList<String> list =new ArrayList<>();
		list.add("Vous avez 4 pommes en or supplémentaires à l'annonce des rôles. ");
		list.add("Vous connaissez Kate Stewart.");
        list.addAll(additionnal);
		return list;
	}
	@Override
	public ItemStack[] askItemStackgiven() {
		ItemStack it = new ItemStack(Material.GOLDEN_APPLE);
		it.setAmount(4);
		return new ItemStack[] { it };
	}
	@Override
	public boolean AskIfUnique() {
		return false;
	}
	@Override
	public String AskHeadTag() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODcwYWMyOTE2ODA5MGNhMDg5NGYwYzY4YTQ2M2JiZmQ2YmYyZThjMmVhNzhhMDg5NzFkZDA1YTM3ODM3ODI5ZiJ9fX0=";
	}
	public boolean isActivated() {
		return activated;
	}
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	public HashMap<UUID, Integer> getEnquetes() {
		return enquetes;
	}

	public enum SoldierType {
		Garde("Garde"), Enqueteur("Enquêteur"), Medic("Medecin");
		private final String name;
		SoldierType(String name) {
			this.name = name;
			
		}
		public String getName() {
			return name;
		}
	}
	@Override
	public Status[] AskStatus() {
		return new Status[] { Status.Humain };
	}

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"§6"+this.soldiertype.getName()};
    }

    @Override
	public void WhileAnyTime(Player player) {
		if (Main.currentGame.getTime() % 10 == 0 && soldiertype == SoldierType.Medic) {
			if (player.getHealth() < player.getMaxHealth()) {
                player.setHealth(Math.min(player.getHealth() + 1, player.getMaxHealth()));
			}
		}
		if (soldiertype == SoldierType.Enqueteur) {
			SoldatUNIT unit = (SoldatUNIT) RoleHandler.getRoleOf(player);
			StringBuilder hotbar = new StringBuilder();
			for(Entry<UUID, Integer> entry : unit.getEnquetes().entrySet()) {
				if (Bukkit.getPlayer(entry.getKey()) != null) {
					if (entry.getValue() >= Main.currentGame.getDataFrom(Configurable.LIST.SoldierSpyTime)) {
						if(!RoleHandler.getRoleList().containsKey(entry.getKey())) {
							player.sendMessage(Main.UHCTypo+"§cLe joueur ciblé par cette enquête est mort. Vous avez été remboursé.");
							unit.getEnquetes().remove(entry.getKey(), entry.getValue());
							generalCoolDown.addUtilisation(1);
							return;
						}
						int r = new Random().nextInt(3);
						switch (r) {
						case 0:
							player.sendMessage(Main.UHCTypo
									+ "§eVotre enquête vient de terminer. Elle vous a permit de découvrir les effets de §a"
									+ Bukkit.getPlayer(entry.getKey()).getName());
							player.sendMessage(Main.UHCTypo + "§4Effets: ");
							for (PotionEffect effect : Bukkit.getPlayer(entry.getKey()).getActivePotionEffects()) {
								player.sendMessage("     §c" + effect.getType().getName());
							}
							break;
						case 1:
							player.sendMessage(Main.UHCTypo
									+ "§eVotre enquête vient de terminer. Elle vous a permit de découvrir la dernière lettre du rôle de §a"
									+ Bukkit.getPlayer(entry.getKey()).getName());
							player.sendMessage("Rôle: §kkkkkkkkk§r§a"
									+ RoleHandler.getRoleOf(Bukkit.getPlayer(entry.getKey())).getName().charAt(
											RoleHandler.getRoleOf(Bukkit.getPlayer(entry.getKey())).getName().length()
													- 1));
							break;
						case 2:
							player.sendMessage(Main.UHCTypo
									+ "§eVotre enquête vient de terminer. Elle vous a permit de découvrir la première lettre du rôle de §a"
									+ Bukkit.getPlayer(entry.getKey()).getName());
							player.sendMessage("Rôle: §a"
									+ RoleHandler.getRoleOf(Bukkit.getPlayer(entry.getKey())).getName().charAt(0)
									+ "§kkkkkkkkk");
							break;
						default:
							break;
						}
						unit.getEnquetes().remove(entry.getKey());
					} else {
						unit.getEnquetes().put(entry.getKey(), entry.getValue() + 1);
						hotbar.append(generatorcycle.generateProgressBar(
                                (((double) entry.getValue())
                                        / ((double) Main.currentGame.getDataFrom(Configurable.LIST.SoldierSpyTime)) * 100.0),
                                20)).append(" ");
					}
				}
			}
            PlayerUtility.sendActionbar(player, hotbar.toString());
		}
	}
	@Override
	public String[] getAdditionnalInfo() {
		return new String[] {
				"Kate Stewart est §6" + ((RoleHandler.getWhoHaveRole(Kate_Stewart.class) == null) ? "Aucun"
						: Bukkit.getPlayer(RoleHandler.getWhoHaveRole(Kate_Stewart.class)).getName()) };
	}
}