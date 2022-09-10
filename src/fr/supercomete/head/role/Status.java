package fr.supercomete.head.role;

public enum Status {
	Companion("§bCompagnon"),
	TimeTraveller("§eVoyageur du Temps"),
	Clone("§6Clone"),
	Humain("§4Humain")
	;
	private final String name;
	Status(String name){
		this.name=name;
	}
	public String getName() {
		return name;
	}
	
}
