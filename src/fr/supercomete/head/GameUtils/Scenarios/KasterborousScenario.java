package fr.supercomete.head.GameUtils.Scenarios;

import fr.supercomete.head.core.KasterborousRunnable;
import org.bukkit.Material;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public interface KasterborousScenario {
    String getName();
    Compatibility getCompatiblity();
    Material getMat();
    @Nullable
    List<KasterborousRunnable> getAttachedRunnable();
}