package fr.supercomete.head.structure;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import fr.supercomete.datamanager.FileManager.Fileutils;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.schema.utility.FileUtility;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

public final class StructureHandler {
	public static File structurefile;
	public StructureHandler(Main main) {
		structurefile = new File(main.getDataFolder(),"struture");
	}
	private int calculate_heap(CustomBlock[][][] tridimensionnal_array){
        int heap = 0;
        for (CustomBlock[][] customBlocks : tridimensionnal_array) {
            for (int y = 0; y < tridimensionnal_array[0].length; y++) {
                for (int z = 0; z < tridimensionnal_array[0][0].length; z++) {
                    CustomBlock current = customBlocks[y][z];
                    String id_str = current.getMaterial().getId() + "";
                    heap = Math.max(id_str.length(), heap);
                }
            }
        }
        return heap;
    }
    public void write(Structure structure) {
		final File file = new File(structurefile,structure.getStructurename()+".kstruct");
        StringBuilder content_builder = new StringBuilder();
        CustomBlock[][][] data = structure.getData();
        System.out.println(Arrays.deepToString(data));
        int heap = calculate_heap(data);
        String worldname = structure.getWorldName();
        if(worldname==null){
            worldname="StructureWorld";
        }
        content_builder.append(worldname).append('\\').append(heap).append('\\');
        int x_size = data.length;
        int y_size = data[0].length;
        int z_size = data[0][0].length;
        content_builder.append(x_size).append("\\").append(y_size).append("\\").append(z_size).append("\\#");
        for(int z = 0;z<data[0][0].length;z++){
            for(int y = 0;y<data[0].length;y++){
                for (CustomBlock[][] datum : data) {
                    StringBuilder id_str = new StringBuilder(datum[y][z].getMaterial().getId() + "");
                    while (id_str.length() < heap) {
                        id_str.insert(0, '0');
                    }
                    content_builder.append(id_str);
                }
            }
        }
        if(!file.exists()){
            try{
                Fileutils.createFile(file);
            }catch (IOException e){
                e.printStackTrace();
                return;
            }
        }
        Fileutils.save(file,content_builder.toString());
	}
    public Structure loadKTBSStructureFile(Class<?>pluginClass,String name,boolean from_source) throws IOException {
        String content;
        if(from_source){
            URL url = pluginClass.getClassLoader().getResource(name+".kstruct");
            if (url == null) {
                return null;
            }
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            content = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        }else{
            content = Fileutils.loadContent(new File(structurefile,name+".kstruct"));
        }
        int pointer = 0;
        char pointer_char=' ';
        ArrayList<String> extract = new ArrayList<>();
        StringBuilder builder= new StringBuilder();
        while(pointer_char!='#'){
            while(pointer_char!='\\'){
                pointer_char=content.charAt(pointer);
                builder.append(pointer_char);
                pointer++;
            }
            extract.add(builder.toString());
            builder = new StringBuilder();
            pointer_char=content.charAt(pointer);
        }
        pointer++;
        extract.replaceAll(s -> s.replace("\\", ""));
        int heap = Integer.parseInt(extract.get(1));
        int x_size = Integer.parseInt(extract.get(2));
        int y_size = Integer.parseInt(extract.get(3));
        int z_size = Integer.parseInt(extract.get(4));
        int x_off = Integer.parseInt(extract.get(5));
        int y_off = Integer.parseInt(extract.get(6));
        int z_off = Integer.parseInt(extract.get(7));
        final int pointer_parity = pointer%heap;
        int x=0,y=0,z=0;
        CustomBlock[][][] tri_dimensional_array = new CustomBlock[x_size][y_size][z_size];
        builder = new StringBuilder();
        while(pointer < content.length()){
            int pared_pointer = (pointer-pointer_parity)%heap;
            builder.append(content.charAt(pointer));
            if(pared_pointer == heap-1){
                tri_dimensional_array[x][y][z] = new CustomBlock(Material.getMaterial(Integer.parseInt(builder.toString())),(byte)0);
                builder = new StringBuilder();
                x++;
                if(x==x_size){
                    x=0;
                    y++;
                }
                if(y==y_size){
                    y=0;
                    z++;
                }
            }
            pointer++;
        }
        Structure structure = new Structure(name,extract.get(0),tri_dimensional_array);
        structure.setSpawnLocation(x_off,y_off,z_off);
        return structure;
    }
    public Structure loadKTBSStructureFile(Class<?>pluginClass,String name) throws IOException {
        return loadKTBSStructureFile(pluginClass,name,true);
    }

    public Structure load_legacy(Class<?>pluginClass,String name) throws IOException, URISyntaxException {

        URL url = pluginClass.getClassLoader().getResource(name+".struct");
        if (url == null) {
            return null;
        }
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        final Iterator<String> ip = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)).lines().iterator();

        StringBuilder content = new StringBuilder();
        while(ip.hasNext()){
            content.append(ip.next()).append("\n");
        }
        String json = content.toString();
        json =json.replace("#","null");
        json =json.replace("!","material");
        json =json.replace("?","data");
        return Main.manager.deserializeStructure(json);
    }
	public Structure extractStructure(Class<?>pluginClass,String name) {
        try {
            return loadKTBSStructureFile(pluginClass,name);
        }catch (IOException|NullPointerException e) {
            try{
                Bukkit.getLogger().log(Level.WARNING,"Loading: "+name+".struct -> Loading legacy format");
                return load_legacy(pluginClass,name);
            }catch (IOException | NullPointerException | URISyntaxException e2){
                e2.printStackTrace();
                return null;
            }
        }
	}
}