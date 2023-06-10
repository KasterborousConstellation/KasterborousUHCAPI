package fr.supercomete.head.schema.utility;
import com.sun.media.sound.InvalidDataException;
import fr.supercomete.head.schema.utility.SchemaCondition;
import fr.supercomete.head.schema.utility.SchemaVariable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

public class SchemaEnvironnement {
    private final static HashMap<String, SchemaVariable> variables = new HashMap<>(1024);
    private final static HashMap<String, SchemaCondition> conditions = new HashMap<>(1024);
    public static void register(String string, SchemaCondition conditon){
        if(string.length()==0){
            try{
               throw new InvalidDataException("The condition identifier can't be empty.");
            }catch (InvalidDataException e){
                e.printStackTrace();
                return;
            }
        }
        if(string.length()>=2){
            if(string.charAt(0)=='{'&&string.charAt(string.length()-1)=='}'){
                string = string.substring(1,string.length()-2);
            }
        }
        conditions.put(
                string,
                conditon
        );
    }
    public static void register(String string, SchemaVariable variable) {
        if(string.length()==0){
            try{
                throw new InvalidDataException("The varibalbe identifier can't be empty.");
            }catch (InvalidDataException e){
                e.printStackTrace();
                return;
            }
        }
        if(string.length()>=2){
            if(string.charAt(0)=='{'&&string.charAt(string.length()-1)=='}'){
                string = string.substring(1,string.length()-2);
            }
        }
        variables.put(
                string,
                variable
        );
    }
    public static boolean get_condition(String linecondition, Player player){
        if(Objects.equals(linecondition, "true")){
            return true;
        }
        if(conditions.containsKey(linecondition)){
            return conditions.get(linecondition).evaluate(player);
        }else return false;
    }
    public static String get_variable(String token,Player player){
        if(Objects.equals(token, "")){
            return "";
        }
        if(!variables.containsKey(token)){
            return "{NotFound}";
        }
        return variables.get(token).evaluate(player);
    }
}
