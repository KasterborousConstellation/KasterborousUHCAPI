package fr.supercomete.head.schema.schema;



import fr.supercomete.head.schema.utility.SchemaEnvironnement;
import org.bukkit.entity.Player;

import java.util.Objects;

public final class LineCondition {
    private final boolean inverted;
    private final String condition;
    public LineCondition(String first_token){
        if(first_token.length()==0){
            condition=default_condition;
            inverted=false;
        }else{
                //Special token
                if(first_token.length()>=2){
                    if(first_token.charAt(0)=='?'&&first_token.charAt(first_token.length()-1)=='?'){
                        //DETECTED CONDITION
                        if(first_token.charAt(1)=='!'){
                            //Inverted
                            inverted=true;
                            condition = first_token.substring(2,first_token.length()-1);
                        }else{
                            //Not inverted
                            inverted=false;
                            condition = first_token.substring(1,first_token.length()-1);
                        }
                    }else{
                        condition = default_condition;
                        inverted=false;
                    }
                }else{
                    condition=default_condition;
                    inverted=false;
                }
        }
    }
    public boolean isdefault(){
        return Objects.equals(condition, "true");
    }
    public boolean evaluate(Player player){
        if(!inverted){
            return SchemaEnvironnement.get_condition(condition,player);
        }else{
            return !SchemaEnvironnement.get_condition(condition,player);
        }
    }
    public LineCondition(String condition,boolean inverted){
        this.condition=condition;
        this.inverted=inverted;
    }
    private static final String default_condition = "true";

}