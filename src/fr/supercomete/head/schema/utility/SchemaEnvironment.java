package fr.supercomete.head.schema.utility;

import com.sun.media.sound.InvalidDataException;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;


public class SchemaEnvironment {

    private final  HashMap<String, SchemaVariable> variables = new HashMap<>(1024);
    private final  HashMap<String, SchemaCondition> conditions = new HashMap<>(1024);
    public void register(String string, SchemaCondition conditon){
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
    public void register(String string, SchemaVariable variable) {
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
    public boolean get_condition(String linecondition, Player player){
        if(Objects.equals(linecondition, "true")){
            return true;
        }
        if(conditions.containsKey(linecondition)){
            return conditions.get(linecondition).evaluate(player);
        }else return false;
    }
    public String get_variable(String token,Player player){
        if(Objects.equals(token, "")){
            return "";
        }
        if(!variables.containsKey(token)){
            return "{NotFound}";
        }
        return variables.get(token).evaluate(player);
    }
}
