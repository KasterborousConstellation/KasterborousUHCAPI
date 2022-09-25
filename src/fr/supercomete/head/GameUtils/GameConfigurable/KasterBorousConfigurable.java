package fr.supercomete.head.GameUtils.GameConfigurable;

import org.bukkit.Material;

public interface KasterBorousConfigurable {
    Binding getBind();

    Bound getBound();

    AddingRule getRule();

    ConfigurableType getType();

    int getBaseData();

    String getDescription();

    String getName();

    Material getMat();
}
