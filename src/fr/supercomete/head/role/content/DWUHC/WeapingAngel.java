package fr.supercomete.head.role.content.DWUHC;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.Fights.Fight;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.*;
import fr.supercomete.head.role.Triggers.Trigger_OnFightEnd;
import fr.supercomete.head.role.Triggers.Trigger_OnInteractWithUUIDItem;
import fr.supercomete.head.role.Triggers.Trigger_WhileAnyTime;
import fr.supercomete.head.role.Triggers.Trigger_onFightBegin;
import fr.supercomete.nbthandler.NbtTagHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public final class WeapingAngel extends DWRole implements Trigger_WhileAnyTime, Trigger_onFightBegin,Trigger_OnFightEnd, Trigger_OnInteractWithUUIDItem {
    private final HashMap<UUID, Location> map = new HashMap<>();
    public CoolDown coolDown = new CoolDown(0,8*60);
    public WeapingAngel(UUID owner) {
        super(owner);
    }

    @Override
    public Status[] AskStatus() {
        return new Status[]{};
    }

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Retour temporel utilisable: "+ Main.getCheckMark(coolDown.isAbleToUse())};
    }

    @Override
    public String askName() {
        return "Ange Pleureur";
    }

    @Override
    public Camps getDefaultCamp() {
        return Camps.Division;
    }

    @Override
    public List<String> askRoleInfo() {
        return Arrays.asList(
                "Vous connaissez le nombre de personne autour de vous.",
                "Lorque plus de 4 joueurs se trouve a moins de 30 blocs de vous, vous obtenez l'effet lenteur.",
                "Lorsque vous débutez un combat avec un joueur vous recevez un message: 'Marque temporelle Activé sur <Joueur>'.",
                "A la fin d'un combat (20s après le dernier coup infligé a un joueur), votre marque temporelle sur le joueur combattu disparait.",
                "Quand un joueur a une marque temporelle active, alors vous pouvez utilisé votre item 'Retour Temporel' pour vous renvoyez vous et le joueur visé à l'endroit ou vous aviez activer la marque temporelle.",
                "Lors d'un retour temporel vous obtenez temporairement §cforce§7 et §bvitesse 2§7 pendant 45s et vous pouvez utiliser de nouveau votre 'Retour Temporel' 8 min après sa dernière utilisation."
        );
    }

    @Override
    public ItemStack[] askItemStackgiven() {
        ItemStack stack = InventoryUtils.getItem(Material.NETHER_STAR,"§6Retour temporel",Main.SplitCorrectlyString("Cliquez dans la direction d'un joueur pour le faire retourner vous et lui à sa marque temporelle",40,"§6"));
        stack = NbtTagHandler.createItemStackWithUUIDTag(stack,15);
        return new ItemStack[]{stack};
    }

    @Override
    public boolean AskIfUnique() {
        return false;
    }

    @Override
    public String AskHeadTag() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzBkODA2ZjE5YTc1MjZmYzZmYTQ4ZjQyZjcwM2I5MGFkZDgxNDJkYWM3ZjM3YWQxYmUxYjRlZGNhYzViYTgzIn19fQ==";
    }

    @Override
    public void WhileAnyTime(Player player) {
        int players = 0;
        for(final Player p: Bukkit.getOnlinePlayers()){
            if(Main.playerlist.contains(p.getUniqueId())&& p.getUniqueId()!=player.getUniqueId()){
                if(p.getWorld().equals(player.getWorld())){
                    if(p.getLocation().distance(player.getLocation())<30){
                        players++;
                    }
                }
            }
        }
        PlayerUtility.sendActionbar(player,"Joueurs autour de vous: §4"+players);
        if(players>4){
            PlayerUtility.addProperlyEffect(player,new PotionEffect(PotionEffectType.SLOW,20*3,0,false,false));
        }
    }
    private String getOther(Fight fight){
        final UUID other = (fight.getFirst()==getOwner())?fight.getSecond():fight.getFirst();
        final Player player = Bukkit.getPlayer(other);
        return (player==null)?"§cDéconnecté§7":player.getName();

    }
    private UUID getOtherUUID(Fight fight){
        return (fight.getFirst()==getOwner())?fight.getSecond():fight.getFirst();
    }
    @Override
    public void onFightBegin(Fight fight) {
        if(RoleHandler.isOnlineAndHaveRole(getOwner())){
            Bukkit.getPlayer(getOwner()).sendMessage(Main.UHCTypo+"Vous avez placé une marque temporelle sur "+getOther(fight));
            map.put(getOtherUUID(fight), Bukkit.getPlayer(getOwner()).getLocation());
        }
    }
    public Location getLocationOfTemporalMark(final Player player){
        for(Map.Entry<UUID,Location>entry:map.entrySet()){
            if(entry.getKey()==player.getUniqueId()){
                return entry.getValue();
            }
        }
        return null;
    }
    public boolean HasTemporalMark(Player player){
        for(Map.Entry<UUID,Location>entry:map.entrySet()){
            if(entry.getKey()==player.getUniqueId()){
                return true;
            }
        }
        return false;
    }
    @Override
    public void onFightEnd(Fight fight) {
        if(RoleHandler.isOnlineAndHaveRole(getOwner())){
            Bukkit.getPlayer(getOwner()).sendMessage(Main.UHCTypo+"La marque de "+getOther(fight)+" vient de disparaître.");
            map.remove(getOtherUUID(fight));
        }
    }

    @Override
    public void OnInteractWithUUIDItem(Player player, int uuidtag, Action action) {
        if(uuidtag==15&&RoleHandler.IsRoleGenerated()){
            if(action==Action.RIGHT_CLICK_AIR||action==Action.RIGHT_CLICK_BLOCK){
                final Role role = RoleHandler.getRoleOf(player);
                if(role instanceof WeapingAngel){
                    WeapingAngel angel =(WeapingAngel)role;
                    Player target =PlayerUtility.getTarget(player,20);
                    if(target==null){
                        player.sendMessage(Main.UHCTypo + "§cLe joueur n'est pas correctement indiqué.");
                    }else{
                        if(angel.HasTemporalMark(target)){
                            Location location = angel.getLocationOfTemporalMark(target);
                            if(angel.coolDown.isAbleToUse()){
                                angel.coolDown.setUseNow();
                                player.teleport(location);
                                target.teleport(location);
                                player.sendMessage(Main.UHCTypo+"Vous êtes arrivé a la marque temporelle de "+target.getName()+" vous avez désormais pour 2min l'effet §cforce§7 et §bvitesse 2");
                                PlayerUtility.addProperlyEffect(player,new PotionEffect(PotionEffectType.SPEED,20*60*2,1,false,false));
                                PlayerUtility.addProperlyEffect(player,new PotionEffect(PotionEffectType.INCREASE_DAMAGE,20*60*2,0,false,false));
                            }else player.sendMessage(Main.UHCTypo+"Vous devez attendre encore "+ TimeUtility.transform(angel.coolDown.getRemainingTime(),"§4"));
                        }else{
                            player.sendMessage(Main.UHCTypo + "§cLe joueur n'a pas de marque temporelle.");
                        }
                    }
                }
            }
        }
    }
}