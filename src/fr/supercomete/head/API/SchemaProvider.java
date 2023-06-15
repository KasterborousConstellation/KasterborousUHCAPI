package fr.supercomete.head.API;

import fr.supercomete.head.schema.utility.SchemaCondition;
import fr.supercomete.head.schema.utility.SchemaVariable;

public interface SchemaProvider {
    void register(String name,SchemaVariable variable);
    void register(String name, SchemaCondition line_condition);
}
