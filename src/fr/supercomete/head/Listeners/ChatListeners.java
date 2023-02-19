package fr.supercomete.head.Listeners;
import fr.supercomete.head.PlayerUtils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.GameConfigurable.Configurable;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.core.Main;
import fr.supercomete.tasks.Cycle;
final class ChatListeners implements Listener{

	@EventHandler(priority = EventPriority.LOWEST)
	public void onChatMessage(AsyncPlayerChatEvent event) {
		final Player sender = event.getPlayer();
		final boolean IsSenderDead = !(Main.currentGame.getGamestate()==Gstate.Waiting) && !Main.getPlayerlist().contains(sender.getUniqueId());
		final boolean IsChatDisabled = Configurable.ExtractBool(Configurable.LIST.AutoChatDisable) && ( Main.currentGame.getTimer(Timer.PvPTime).getData()< Main.currentGame.getTime()||Cycle.hasPvpForced) && !(Main.currentGame.getGamestate()==Gstate.Waiting) ;
		if((Configurable.ExtractBool(Configurable.LIST.Chat) && !IsChatDisabled) && !IsSenderDead){
			event.setCancelled(false);
			//Autorise le joueur a parler car il est en vie et le chat est activer
		}else if(IsChatDisabled && !IsSenderDead){
			sender.sendMessage("§cLe Chat est désactivé");
			event.setCancelled(true);
			//Le joueur ne peut pas parler car il n'est pas mort et que le chat est désactiver
		}else if(IsSenderDead) {
			if(Configurable.ExtractBool(Configurable.LIST.DeathChat)) {
				if(Configurable.ExtractBool(Configurable.LIST.DeathChatVisible)) {
					event.setCancelled(false);
				}else {
					//Envoie correctement le message au autres mort
					event.setCancelled(true);
					for(final Player player :Bukkit.getOnlinePlayers()) {
                        if(Main.getPlayerlist().contains(player.getUniqueId())){
                            player.sendMessage("§7[Mort] " +sender.getName()+" §7 "+event.getMessage());
                        }
					}
				}
			}else {
				sender.sendMessage("§cLe Chat des morts est désactivé");
				event.setCancelled(true);
				//Le joueur ne peut pas parler car il est mort et que le tchat des mort est désactivé
			}
		}else {
			sender.sendMessage("§cLe Chat n'est pas activé");
			event.setCancelled(true);
		}
	}
}