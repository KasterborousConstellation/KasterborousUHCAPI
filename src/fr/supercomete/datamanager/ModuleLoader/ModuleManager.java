package fr.supercomete.datamanager.ModuleLoader;

import fr.supercomete.datamanager.FileManager.Fileutils;
import fr.supercomete.datamanager.ModuleLoader.ModuleException.NoModuleSectionException;
import fr.supercomete.datamanager.ModuleLoader.ModuleException.NoModuleYmlException;
import fr.supercomete.datamanager.ModuleLoader.ModuleException.OutOfSpaceforModuleFolder;
import fr.supercomete.head.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ModuleManager {
    private static File moduleFolder;

    public static void init(Main main){
        Bukkit.broadcastMessage("[Module initialisation]");
        moduleFolder= new File(main.getDataFolder(),"ModuleFolder");
        if(!moduleFolder.exists())
        {
            try {
                Fileutils.createFile(moduleFolder);
            } catch (IOException e) {
                /*
                Throw a more specified exception for debugging.
                 */
                try {
                    throw new OutOfSpaceforModuleFolder();
                } catch (OutOfSpaceforModuleFolder ex) {
                    ex.printStackTrace();
                }
                return;
            }
            moduleFolder.mkdir();
        }
        for(File module: Objects.requireNonNull(moduleFolder.listFiles())){
            try{
                final YamlConfiguration yml = YamlConfiguration.loadConfiguration(new File(module,"module.yml"));
                if(yml==null){
                    try{
                        throw new NoModuleYmlException(module.getName());
                    }catch (NoModuleYmlException e){
                        e.printStackTrace();
                        continue;
                    }
                }
                final String classPath = (String)yml.get("module");
                if(classPath==null){
                    try{
                        throw new NoModuleSectionException(module.getName());
                    }catch (NoModuleSectionException e){
                        e.printStackTrace();
                        continue;
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}