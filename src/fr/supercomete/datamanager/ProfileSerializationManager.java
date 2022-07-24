package fr.supercomete.datamanager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.supercomete.head.GameUtils.Game;
import fr.supercomete.head.structure.Structure;
public class ProfileSerializationManager {
	private final Gson gson;
	public ProfileSerializationManager() {
		this.gson=createGsonInstance();
	}
	private Gson createGsonInstance() {
		return new GsonBuilder()
				.setPrettyPrinting()
				.serializeNulls()
				.disableHtmlEscaping()
				.create();
	}
	public String serialize(Game profile) {
		return gson.toJson(profile);
	}	
	public Game deserialize(String json){
		return this.gson.fromJson(json, Game.class);
	}
	
	public String serializeStructure(Structure profile) {
		return gson.toJson(profile);
	}	
	public Structure deserializeStructure(String json){
		return this.gson.fromJson(json, Structure.class);
	}
}
