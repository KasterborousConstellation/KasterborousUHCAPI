package fr.supercomete.head.role.content.DWUHC;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.RoleModifier.HasAdditionalInfo;
import fr.supercomete.head.role.RoleModifier.PreAnnouncementExecute;
import fr.supercomete.head.role.Triggers.Trigger_OnOwnerDeath;
import fr.supercomete.head.role.Triggers.Trigger_WhileAnyTime;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.RoleModifier.BonusHeart;
import fr.supercomete.head.role.Status;
import fr.supercomete.nbthandler.NbtTagHandler;
import fr.supercomete.tasks.FluxTask;
import fr.supercomete.tasks.particles.FluxParticle;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public final class Tecteun extends DWRole implements BonusHeart, Trigger_OnOwnerDeath, Trigger_WhileAnyTime, PreAnnouncementExecute, HasAdditionalInfo {
    private boolean CanRevive= true;
    private boolean flux=false;
    private ArrayList<UUID>ally=new ArrayList<>();
    public Tecteun(UUID owner) {
        super(owner);
    }
    @Override
    public Status[] AskStatus() {
        return new Status[]{Status.TimeTraveller};
    }

    @Override
    public String[] AskMoreInfo() {
        return new String[]{"Revive: "+Main.getCheckMark(CanRevive),"Flux utilisé: "+Main.getCheckMark(flux)};
    }

    @Override
    public String askName() {
        return "Tecteun";
    }

    @Override
    public Camps getDefaultCamp() {
        return Camps.Division;
    }

    @Override
    public List<String> askRoleInfo() {
        return Arrays.asList(
                "Vous avez 12♥ permanent",
                "Vous avez l'effet "+ ChatColor.DARK_GRAY +"résistance§7 permanent.",
                "Vous avez un item appelée 'Flux', qui vous permet de créer une zone de 30 blocs dans laquelle tout les joueurs sauf vous et les anges pleureurs prennent 1♥ de dégat toute les 5s. Cette item est utilisable uniquement une fois et la zone est permanente, cependant l'activation vous fait perdre 2♥.",
                "Quand vous avez une vie supplémentaire. Lors de votre première mort, vous perdrez 3♥ permanent.",
                "Vous avez la liste des autres membres de La Division."
        );
    }

    @Override
    public ItemStack[] askItemStackgiven() {
        ItemStack stack = InventoryUtils.getItem(Material.NETHER_STAR,"§6Flux", Collections.singletonList("§6Cliquez ici pour activer la zone du Flux"));
        stack = NbtTagHandler.createItemStackWithUUIDTag(stack,14);
        return new ItemStack[]{stack};
    }
    @Override
    public boolean AskIfUnique() {
        return true;
    }
    @Override
    public String AskHeadTag() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDI3YTRiOGQxNmQ1ZDJlMzNkMWIxMDcwZDhmZDY4Y2I1ODFjZDJmNjNlNWEyYmYwZDllY2ZiNDgzMGI2ODI5ZSJ9fX0=";
    }
    @Override
    public int getHPBonus() {
        return 4;
    }

    @Override
    public boolean onOwnerDeath(Player player, Player damager) {
        if(this.CanRevive){
            CanRevive=false;
            this.addBonus(new Bonus(BonusType.Heart,-6));
            player.sendMessage(Main.UHCTypo+"§6Vous êtes revenu à la vie. Si vous venez à mourir, cette fois ci vous serez mort définitivement.");
            return true;
        }
        return false;
    }
    @Override
    public void WhileAnyTime(Player player) {
        PlayerUtility.addProperlyEffect(player,new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20*3,0,false,false));
    }
    private byte pick(){
        byte[] shorts = new byte[]{1,1,1,1,4,4,4,4,4,4,4,4,4,14,14,14,15};
        int random = (int)(Math.random()*((double)shorts.length));
        return shorts[random];
    }
    public void createFluxZone(final Player player,final Main main){
        flux=true;
        this.addBonus(new Bonus(BonusType.Heart,-4));
        final int range = 30;
        final World world = player.getWorld();
        final Location location =player.getLocation();
        for(int x=-range;x<range;x++){
            for(int y=-range;y<range;y++){
                for(int z=-range;z<range;z++){
                    final Block block = world.getBlockAt(player.getLocation().add(x,y,z));
                    if(!block.getType().equals(Material.AIR)&& block.getLocation().distance(location)<30){
                        block.setType(Material.STAINED_CLAY);
                        block.setData(pick());
                    }
                }
            }
        }
        final FluxTask task = new FluxTask(location);
        task.runTaskTimer(main,0,100);
        final FluxParticle particle = new FluxParticle(location);
        particle.runTaskTimer(main,0,3);
    }

    @Override
    public void PreAnnouncement() {
        for(final Role role : RoleHandler.getRoleList().values()){
            if(role.getCamp()==Camps.Division){
                ally.add(role.getOwner());
            }
        }
    }

    @Override
    public String[] getAdditionnalInfo() {
        final ArrayList<String> array = new ArrayList<>();
        array.add("§7Liste des membres de "+ ChatColor.DARK_PURPLE+"La Division");
        for(final UUID uu: ally){
            if(Bukkit.getPlayer(uu)!=null){
                array.add("    §7"+Bukkit.getPlayer(uu).getName());
            }
        }
        return Main.convertArrayToTable(String.class,array);
    }
}