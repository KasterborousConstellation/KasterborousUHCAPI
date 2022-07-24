package fr.supercomete.head.GameUtils;
import java.util.Map;
public final class MyEntry<PlayerInventory, Location> implements Map.Entry<PlayerInventory, Location> {
	private final PlayerInventory key;
	private Location value;
	public MyEntry(PlayerInventory key, Location value) {
	this.key = key;
	this.value = value;
	}
	    @Override
	    public PlayerInventory getKey() {
	        return key;
	    }
	    @Override
	    public Location getValue() {
	        return value;
	    }

	    @Override
	    public Location setValue(Location value) {
	    	Location old = this.value;
	        this.value = value;
	        return old;
	    }
	}
