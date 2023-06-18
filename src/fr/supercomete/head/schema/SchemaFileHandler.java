package fr.supercomete.head.schema;
import fr.supercomete.head.Exception.BadExtensionException;
import fr.supercomete.head.Exception.MalformedSchemaException;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.schema.schema.Schema;
import fr.supercomete.head.schema.utility.FileUtility;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;
public class SchemaFileHandler {
    public static File getSchemafolder(JavaPlugin plugin) throws IOException {
        final File datafolder =plugin.getDataFolder();
        final File schemaFolder = new File(datafolder,"schemas");
        if(!schemaFolder.exists()){

            Files.createDirectories(schemaFolder.toPath());
        }
        return schemaFolder;
    }
    public static File getFile(final String name,JavaPlugin plugin){
        try {
            File f=new File(getSchemafolder(plugin),name);
            check(f,plugin);
            return f;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void check(final File file,JavaPlugin plugin) throws IOException {
        if(!file.exists()){
            final Iterator<String> ip = new BufferedReader(new InputStreamReader(plugin.getResource(file.getName()), StandardCharsets.UTF_8)).lines().iterator();
            StringBuilder content = new StringBuilder();
            while(ip.hasNext()){
                content.append(ip.next()).append("\n");
            }
            FileUtility.createFile(file);
            FileUtility.write(file,content.toString());
        }
    }
    public static Schema load(final String name, final JavaPlugin plugin) throws MalformedSchemaException, IOException, BadExtensionException {
        final File file = new File(getSchemafolder(plugin),name);
        check(file,plugin);
        return new Schema(file,Main.scoreboardEnvironment);
    }
    public static Schema load(String name) throws IOException, MalformedSchemaException, BadExtensionException {
        return load(name,Main.INSTANCE);
    }
}
