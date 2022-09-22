package fr.supercomete.head.role.content.EchoEchoUHC;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.Fights.FightHandler;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.EchoRole;

import fr.supercomete.head.role.Triggers.Trigger_OnInteractWithUUIDItem;
import fr.supercomete.nbthandler.NbtTagHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Nicolas extends EchoRole implements  Trigger_OnInteractWithUUIDItem {
    private final CoolDown compasCooldown = new CoolDown(0,60);
    public Nicolas(UUID owner) {
        super(owner);
    }

    @Override
    public BranlusqueLevel getBranlusqueLevel() {
        return BranlusqueLevel.MasterMage;
    }

    @Override
    public float getMangaProbability() {
        return 0.2F;
    }

    @Override
    public String[] AskMoreInfo() {
        return new String[0];
    }

    @Override
    public String askName() {
        return "Nicolas";
    }

    @Override
    public Camps getDefaultCamp() {
        return Camps.Revolutionnaires;
    }

    @Override
    public List<String> askRoleInfo() {
        return Arrays.asList(
                "Vous devez gagner avec les "+Camps.Revolutionnaires.getColoredName(),
                "Vous avez un objet appellé 'Compas', qui lorsque vous l'utilisez vous perdez 1/2♥ par utilisation et gagnez 5% de force et 5% de vitesse. Vous pouvez utiliser cette capacité que lors d'un combat et elle a 1m de cooldown."
        );
    }

    @Override
    public ItemStack[] askItemStackgiven() {
        ItemStack compas = InventoryUtils.getItem(Material.NETHER_STAR,"§4Compas", Collections.singletonList("§r§fCliquer pendant un combat pour échanger 1/2♥ contre 5% de force et 5% de vitesse"));
        compas= NbtTagHandler.createItemStackWithUUIDTag(compas,104);
        return new ItemStack[]{compas};
    }

    @Override
    public boolean AskIfUnique() {
        return false;
    }

    @Override
    public String AskHeadTag() {
        return null;
    }

    @Override
    public void OnInteractWithUUIDItem(Player player, int uuidtag, Action action) {
        if(uuidtag==104) {
            if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
                if (FightHandler.hasFight(player)) {
                    if (compasCooldown.isAbleToUse()) {
                        if (player.getMaxHealth() > 1) {
                            this.addBonus(new Bonus(BonusType.Heart, -1));
                            this.addBonus(new Bonus(BonusType.Force, 5));
                            this.addBonus(new Bonus(BonusType.Speed, 5));
                            player.sendMessage(Main.UHCTypo + "Vous avez échangé 1/2♥ contre 5% de force et de vitesse");
                            compasCooldown.setUseNow();
                        } else {
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"kill "+Bukkit.getPlayer(getOwner()).getName());
                        }


                    } else
                        player.sendMessage(Main.UHCTypo + "§7Il vous reste encore " + TimeUtility.transform(compasCooldown.getRemainingTime(), "§c") + "§7 avant de pouvoir re-utiliser le 'Compas'");

                } else
                    player.sendMessage(Main.UHCTypo + "§cVous ne pouvez pas utiliser cet objet en dehors d'un fight.");

            }
        }
    }
}
