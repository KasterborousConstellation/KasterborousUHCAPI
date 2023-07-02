package fr.supercomete.head.Listeners;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.RoleHandler;
import fr.supercomete.head.role.Triggers.Trigger_OnConsume;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
final class PlayerItemConsumeListener implements Listener {
	@EventHandler
	public void OnConsume(PlayerItemConsumeEvent e) {
		ItemStack item = e.getItem();
		Player player = e.getPlayer();
        if(RoleHandler.IsRoleGenerated()){
            final Role role = RoleHandler.getRoleOf(player);
            if(role instanceof Trigger_OnConsume){
                ((Trigger_OnConsume)role).onConsume(player,e);
            }
        }
	}
}