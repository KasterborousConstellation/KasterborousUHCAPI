package fr.supercomete.autoupdater;

import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {
    public static final String PLUGIN_URL = "https://spigotmc.org/resources/108015";
    public static final String VERSION = "1.0";
    private static final int ressourceid=108015;
    public static void getLasterVersion(Consumer<String> consumer){
        Bukkit.getScheduler().runTaskAsynchronously(Main.INSTANCE,()->{
           try(InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + ressourceid).openStream(); Scanner scanner = new Scanner(inputStream)){
               if (scanner.hasNext()) {
                   consumer.accept(scanner.next());
               }
           }catch (IOException e){
               Main.INSTANCE.getLogger().info("Cannot look for updates: "+e.getMessage());
           }
        });
    }
}
