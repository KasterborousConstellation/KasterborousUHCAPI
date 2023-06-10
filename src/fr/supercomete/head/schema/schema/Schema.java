package fr.supercomete.head.schema.schema;

import fr.supercomete.head.schema.exception.BadExtensionException;
import fr.supercomete.head.schema.exception.MalformedSchemaException;
import fr.supercomete.head.schema.utility.FileUtility;
import org.bukkit.entity.Player;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Schema {
    final SchemaLine[] lines;
    String[] identifiers= new String[]{"§1","§2","§3","§4","§5","§6","§7","§8","§9","§a","§b","§c","§d","§r"};
    boolean uniquelines=false;
    public void setUniqueLinesBehavior(boolean condition){
        uniquelines=condition;
    }
    public Schema(final File file) throws IOException, BadExtensionException, MalformedSchemaException {
        if(file.exists()){
            if(!FileUtility.is_extension(file,"shma")){
                throw new BadExtensionException("The file extension sould be '.shma'. Maybe it's the wrong file or the wrong extension ?");
            }
            final String content = FileUtility.load(file);
            final String[] lines = content.split("\n");
            final ArrayList<String> effectives = new ArrayList<>();
            for(final String line: lines){
                if(line.length()==0||line.charAt(0)!='#'){
                    effectives.add(line);
                }
            }
            this.lines = new SchemaLine[effectives.size()];
            for(int i =0;i<effectives.size();i++){
                this.lines[i]=new SchemaLine(effectives.get(i));
            }
        }else{
            throw new FileNotFoundException("The file: "+file.getPath()+" doesn't exist");
        }
    }
    private boolean isUnique(ArrayList<String> list, String tested_elm){
        for(String sub:list){
            if(sub.equals(tested_elm)){
                return false;
            }
        }
        return true;
    }
    public String[] evaluate(int line_length, Player player){
        if(line_length==0){
            line_length=0xfffffff;
        }
        final ArrayList<String> rtn_strl = new ArrayList<>();
        for (SchemaLine line : lines) {
            final String evaluated = line.evaluate(player);
            final LineCondition condition = line.getCondition();
            if (condition.evaluate(player)) {
                String to_build =evaluated.substring(0, Math.min(line_length + 1, evaluated.length()));
                int e = 0;
                String tmp = to_build;
                while(!isUnique(rtn_strl,tmp)){
                    if(e==identifiers.length){
                        if(tmp.length()+2>line_length){
                            break;
                        }else{
                            to_build = to_build+identifiers[new Random().nextInt(identifiers.length)];
                            e=e%identifiers.length;
                        }
                    }
                    if(tmp.length()+2< line_length+1){
                        tmp = to_build+identifiers[e];
                    }
                    e++;
                }
                if(isUnique(rtn_strl,tmp)){
                    rtn_strl.add(tmp);
                }
            }
        }
        final String[] rtn = new String[rtn_strl.size()];
        for(int i = 0;i<rtn_strl.size();i++){
            rtn[i]=rtn_strl.get(i);
        }
        return rtn;
    }
}