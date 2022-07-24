package fr.supercomete.enums;
import fr.supercomete.head.role.Role;
import fr.supercomete.head.role.content.DWUHC.RoseTyler;
public enum Choice {
	None(Role.class,"","")
	,Humaine(RoseTyler.class,"Humaine","Vous possèderez l'effet speed 1 permanent, ainsi que 15 coeurs permanent")
	,BadWolf(RoseTyler.class,"BadWolf","Vous possèderez l'effet résistance 1 permanent et si vous arrivez à tuer un dalek, vous possèderez 2 coeurs permanent du Pseudo du Docteur")
;
	private final Class<?> roleClass;
	private final String des;
	private final String name;
	Choice(Class<?> rt,String name,String Description){
		this.roleClass=rt;
		this.des=Description;
		this.name=name;
	}
	public Class<?> getConcernedClass(){
		return this.roleClass;
	}
	public String getDescription() {
		return this.des;
	}
	public String getName() {
		return this.name;
	}
}	

