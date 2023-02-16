package fr.supercomete.commands;

import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.Modes.Null_Mode;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.GameUtils.Time.TimerType;
import fr.supercomete.head.PlayerUtils.EffectHandler;
import fr.supercomete.head.PlayerUtils.KTBSEffect;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeLeftCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main main;
	public TimeLeftCommand(Main main) {
		this.main=main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg3) {
		if(sender instanceof Player){
		    Player player =(Player)sender;
		    if(Main.currentGame.getMode() instanceof Null_Mode){
		        return false;
            }
            if(cmd.getName().equalsIgnoreCase("timeleft") ) {
                player.sendMessage("§1------------------------");
				for(final Timer timer: Timer.values()){
				    if(timer.getType().equals(TimerType.TimeDependent)&&timer.getCompatibility().IsCompatible(Main.currentGame.getMode())){
				        int amount = Main.currentGame.getTimer(timer).getData()-Main.currentGame.getTime();
				        if(amount>0){
				            player.sendMessage("  §b"+timer.getName()+": §r"+ TimeUtility.transform(amount,"§b"));
                        }
                    }
                }
                player.sendMessage("§1------------------------");
            }
		}
		return false;
	}
}