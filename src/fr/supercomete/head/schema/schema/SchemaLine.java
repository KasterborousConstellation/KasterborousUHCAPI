package fr.supercomete.head.schema.schema;

import fr.supercomete.head.Exception.MalformedSchemaException;
import fr.supercomete.head.schema.utility.SchemaEnvironnement;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

public class SchemaLine {
    final LineCondition linecondition;
    String[] tokens;
    final HashMap<Integer,String> replacement;
    private int remember;
    public SchemaLine(String line) throws MalformedSchemaException{
        replacement=new HashMap<>();
        final ArrayList<String> tokens_current = new ArrayList<>();
        final StringBuilder evaluating = new StringBuilder();
        int parse_position = 0;
        boolean inspecialtoken=false;
        while(parse_position<line.length()){
            if(enter_char(line,parse_position)){
                if(inspecialtoken) {
                    throw new MalformedSchemaException("Malformed SHMA. You haven't closed your last variable declaration. If you mean't to use the '{' character, use '\\{' instead");
                }else{
                    inspecialtoken=true;
                    reset(tokens_current,evaluating);
                }
            }
            evaluating.append(line.charAt(parse_position));
            if(exit_char(line,parse_position)){
                //Reset
                //Begin new token identification
                if(inspecialtoken){
                    inspecialtoken=false;
                    parse_position++;
                    final String eval = sub_evaluate(evaluating);
                    replacement.put(tokens_current.size(),eval);
                    reset(tokens_current,evaluating);
                }else{
                    throw new MalformedSchemaException("Malformed SHMA at the "+parse_position+" position");
                }
            }else{
                parse_position++;
            }
        }
        reset(tokens_current,evaluating);
        final String[]parser_result = new String[tokens_current.size()];
        for(int i = 0;i<tokens_current.size();i++){
            parser_result[i]=tokens_current.get(i);
        }
        tokens=parser_result;
        for(int i =1;i< tokens.length;i++){
            if(tokens[i].length()>=2&&tokens[i].charAt(0)=='?'&&tokens[i].charAt(tokens[i].length()-1)=='?'){
                throw new MalformedSchemaException("MALFORMED SHMA. Detected line condition but it's not at the beginning of the line. A line condition must be at the beginning of the line");
            }
        }
        if(tokens.length>0){
            linecondition = new LineCondition(tokens[0]);
        }else{
            linecondition = new LineCondition("true",false);
        }

        if(!linecondition.isdefault()){
            replacement.remove(0);
            replacement.put(0,"");
        }
    }
    public String evaluate(Player player){
        final String[] to_eval = tokens.clone();
        for(Map.Entry<Integer,String>entry : replacement.entrySet()){
            String eval= SchemaEnvironnement.get_variable(entry.getValue(),player);
            if(eval !=null){
                to_eval[entry.getKey()] = eval;
            }else{
                to_eval[entry.getKey()]= "{"+entry.getValue()+"}";
                System.out.println("This variable: {"+ entry.getValue()+"} can't be found. Consider adding it to the SchemaEnvironnement or maybe you made a typo ?");
            }

        }
        return String.join("", to_eval);
    }
    public LineCondition getCondition(){
        return linecondition;
    }
    private static String sub_evaluate(StringBuilder b){
        if(b.length()==0){
            return "";
        }
        if(b.charAt(0)=='{'){
            return b.substring(1,b.length()-1).replace("\\{","{").replace("\\}","}");
        }else{
            return b.toString().replace("\\{","{").replace("\\}","}");
        }
    }
    private static void reset(ArrayList<String>strl,StringBuilder builder){
        if(builder.length()!=0){
            strl.add(sub_evaluate(builder));
            builder.delete(0,builder.length());
        }
    }
    private static boolean enter_char(String line,int position){
        return (position==0&&line.charAt(position)=='{')||line.charAt(position)=='{'&&line.charAt(position-1)!='\\';
    }
    private static boolean exit_char(String line,int position){
        return (position==0&&line.charAt(position)=='}')||line.charAt(position)=='}'&&line.charAt(position-1)!='\\';
    }
}
