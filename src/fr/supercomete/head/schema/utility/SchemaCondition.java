package fr.supercomete.head.schema.utility;

import org.bukkit.entity.Player;

public interface SchemaCondition {
    boolean evaluate(Player player);
}
