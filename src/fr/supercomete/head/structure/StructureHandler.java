package fr.supercomete.head.structure;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

import fr.supercomete.datamanager.FileManager.Fileutils;
import fr.supercomete.head.core.Main;
import jdk.nashorn.internal.parser.JSONParser;
import org.bukkit.Bukkit;

public class StructureHandler {
	public static File structurefile;
	public StructureHandler(Main main) {
		structurefile = new File(main.getDataFolder(),"struture");
	}
	public void write(Structure structure) {
		final File file = new File(structurefile,structure.getStructurename()+".struct");
        String jsoncontent = Main.manager.serializeStructure(structure);
        jsoncontent = jsoncontent.replace("null","#");
        jsoncontent =jsoncontent.replace("material","!");
        jsoncontent =jsoncontent.replace("data","?");
        jsoncontent = jsoncontent.replace("\n","");
		if(!file.exists()) {
			try {
				Fileutils.createFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Fileutils.save(file, jsoncontent);
	}
	public Structure extractStructure(Class<?>pluginClass,String name) {
        Bukkit.getLogger().log(Level.FINE,"Loading: "+name+".struct");
        try {
            String json = Fileutils.readFileFromResources(pluginClass,name+".struct");
            json =json.replace("#","null");
            json =json.replace("!","material");
            json =json.replace("?","data");
            final Structure structure = Main.manager.deserializeStructure(json);
            return structure;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
}