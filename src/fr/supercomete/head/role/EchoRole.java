package fr.supercomete.head.role;
import java.util.UUID;

public abstract class EchoRole extends Role{

    public EchoRole(UUID owner) {
        super(owner);
    }
    public abstract BranlusqueLevel getBranlusqueLevel();
    public abstract float getMangaProbability();
    public static enum BranlusqueLevel{
        Archimage("Archimage"),
        MasterMage("Maitre Mage"),
        MageTeacher("Mage Enseignant"),
        Mage("Mage"),
        Limited("Mage Limité"),
        Human("Humain")
        ;
        private String name;
        BranlusqueLevel(String str){
            this.name=str;
        }
        public String getName(){
            return name;
        }
    }
}
