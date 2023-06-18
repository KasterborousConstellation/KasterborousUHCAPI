package fr.supercomete.head.schema.utility;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileUtility {
    public static boolean is_extension(File file,String extension){
        final String filename = file.getName();
        StringBuilder read = new StringBuilder();
        boolean found = false;
        int reverse_index=0;
        char reading = ' ';
        while(reading!='.'&&reverse_index<filename.length()){
            reading = filename.charAt(filename.length()-1-reverse_index);
            if(reading!='.'){
                read.append(reading);
            }else{
                found = true;
            }
            reverse_index++;
        }
        return found&&read.reverse().toString().equals(extension);
    }
    public static void createFile(File file) throws IOException {
        file.getParentFile().mkdirs();
        file.createNewFile();
    }
    public static void write(File file, String content) throws IOException {
        if(file.exists()){
            Files.write(Paths.get(file.getPath()), Arrays.asList(content.split("\n")));
        }else{
            createFile(file);
        }
    }
    public static String load(final File file) throws IOException {
        final StringBuilder builder = new StringBuilder();
        final List<String> content = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
        for(final String str:content){
            builder.append(str).append("\n");
        }
        return builder.substring(0,builder.length()-1);
    }
}
