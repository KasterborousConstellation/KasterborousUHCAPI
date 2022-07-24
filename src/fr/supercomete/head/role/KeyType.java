package fr.supercomete.head.role;

public enum KeyType {
	Key1("1ère Clef"),
	Key2("2ème Clef"),
	Key3("3ème Clef"),
	Key4("4ème Clef"),
	Key5("5ème Clef"),
	
	;
	private final String name;
	KeyType(String name){
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

}
