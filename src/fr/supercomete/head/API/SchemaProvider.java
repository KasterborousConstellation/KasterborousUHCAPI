package fr.supercomete.head.API;

import fr.supercomete.head.schema.utility.SchemaCondition;
import fr.supercomete.head.schema.utility.SchemaVariable;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public interface SchemaProvider {
    void register(String name,SchemaVariable variable);
    void register(String name, SchemaCondition line_condition);
    File loadFromRessources(String name, JavaPlugin loaded_from);
}
