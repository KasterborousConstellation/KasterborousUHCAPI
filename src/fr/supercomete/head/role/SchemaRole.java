package fr.supercomete.head.role;
import fr.supercomete.head.Exception.BadExtensionException;
import fr.supercomete.head.Exception.MalformedSchemaException;
import fr.supercomete.head.schema.schema.Schema;
import fr.supercomete.head.schema.utility.SchemaCondition;
import fr.supercomete.head.schema.utility.SchemaEnvironment;
import fr.supercomete.head.schema.utility.SchemaVariable;
import org.bukkit.Bukkit;
import java.io.File;
import java.io.IOException;
import java.util.*;
public abstract class SchemaRole extends Role  {
    private Schema schema;
    private final SchemaEnvironment environnement = new SchemaEnvironment();
    public SchemaRole(UUID owner) {
        super(owner);
    }
    protected abstract File askSchemaFile();
    protected void register(String text, SchemaVariable variable){
        environnement.register(text,variable);
    }
    protected void register(String text, SchemaCondition condition){
        environnement.register(text,condition);
    }
    @Override
    public final List<String> askRoleInfo(){
        if(schema==null){
            try {
                schema = new Schema(askSchemaFile(),environnement);
                schema.setUniqueLinesBehavior(false);
            } catch (IOException | BadExtensionException | MalformedSchemaException e) {
                e.printStackTrace();
                return new LinkedList<>();
            }
        }
        return new LinkedList<>(Arrays.asList(schema.evaluate(0, Bukkit.getPlayer(getOwner()))));
    }
}