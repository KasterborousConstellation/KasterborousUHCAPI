package fr.supercomete.head.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;

import fr.supercomete.head.GameUtils.Scenarios.Scenarios;
import fr.supercomete.head.core.Main;

record FurnaceBurnListener(Main main) implements Listener {

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent e) {
        Block block = e.getBlock();
        if (Main.currentGame.getScenarios().contains(Scenarios.FastSmelting)) {
            Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                @Override
                public void run() {
                    if (block.getType() == Material.AIR) {
                        return;
                    }
                    Furnace furnace = (Furnace) block.getState();
                    if (furnace.getBurnTime() <= 10) {
                        return;
                    }
                    if (furnace.getCookTime() <= 0) {
                        Bukkit.getScheduler().runTaskLater(main, this, 5);
                        return;
                    }
                    short newCookTime = (short) (furnace.getCookTime() + 10);
                    if (newCookTime >= 200) {
                        newCookTime = 199;
                    }
                    furnace.setCookTime(newCookTime);
                    furnace.update();
                    Bukkit.getScheduler().runTaskLater(main, this, 2);
                }
            }, 1);
        }
    }

}
