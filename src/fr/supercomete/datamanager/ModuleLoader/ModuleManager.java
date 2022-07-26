package fr.supercomete.datamanager.ModuleLoader;

import com.sun.org.apache.xpath.internal.operations.Mod;
import fr.supercomete.datamanager.FileManager.Fileutils;
import fr.supercomete.datamanager.ModuleLoader.ModuleException.NoModuleSectionException;
import fr.supercomete.datamanager.ModuleLoader.ModuleException.NoModuleYmlException;
import fr.supercomete.datamanager.ModuleLoader.ModuleException.NotAModuleException;
import fr.supercomete.head.core.Main;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Seekable;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleManager {
    private static File moduleFolder;
    public static void init(Main main){
        Bukkit.broadcastMessage("[Module initialisation]");
        moduleFolder= new File(main.getDataFolder(),"ModuleFolder/");
        if(!moduleFolder.exists()){
            moduleFolder.mkdirs();
        }
        for(File module: Objects.requireNonNull(moduleFolder.listFiles())){
            try{
                URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{module.toURL()},System.class.getClassLoader());
                InputStream is = urlClassLoader.getResourceAsStream("module.yml");
                Yaml yml= new Yaml();

                Map<String,Object> mapYaml = yml.load(is);
                if(yml==null){
                    try{
                        throw new NoModuleYmlException(module.getName());
                    }catch (NoModuleYmlException e){
                        e.printStackTrace();
                        continue;
                    }
                }
                final String classPath =(String)mapYaml.get("module");
                if(classPath==null){
                    try{
                        throw new NoModuleSectionException(module.getName());
                    }catch (NoModuleSectionException e){
                        e.printStackTrace();
                        continue;
                    }
                }
                final Class<?> moduleclass = urlClassLoader.loadClass(classPath);
                if(moduleclass.getSuperclass().equals(Module.class)){
                    try{
                        final Module importedmodule = (Module) moduleclass.getConstructor().newInstance();
                        Bukkit.broadcastMessage(importedmodule.getAuthor()+" loaded");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    try{
                        throw new NotAModuleException(classPath);
                    }catch(NotAModuleException e){
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