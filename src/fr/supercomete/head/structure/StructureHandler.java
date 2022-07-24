package fr.supercomete.head.structure;
import java.io.File;
import java.io.IOException;
import fr.supercomete.datamanager.FileManager.Fileutils;
import fr.supercomete.head.core.Main;
public class StructureHandler {
	public static File structurefile;
	public StructureHandler(Main main) {
		structurefile = new File(main.getDataFolder(),"struture");
	}
	public void write(Structure structure) {
		final File file = new File(structurefile,structure.getStructurename()+".struct");
		final String jsoncontent = Main.manager.serializeStructure(structure);
		if(!file.exists()) {
			try {
				Fileutils.createFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Fileutils.save(file, jsoncontent);
	}
	public Structure extractStructure(String name) {
		final File file = new File("/var/games/minecraft/Library/Structure/"+name+".struct");
		System.out.println(file.exists());
		final Structure structure = Main.manager.deserializeStructure(Fileutils.loadContent(file));
		System.out.println(structure.getStructurename());
		return structure;
	}
}