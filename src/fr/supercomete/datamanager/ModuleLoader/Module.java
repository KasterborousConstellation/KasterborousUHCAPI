package fr.supercomete.datamanager.ModuleLoader;

import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.UUID;

class Module {
    private String author;
    private String version;
    private ArrayList<Listener> listeners;
    private UUID uuid;
    public Module(String author,String version, ArrayList<Listener>listeners,UUID uuid){
        this.author=author;
        this.version=version;
        this.listeners=listeners;
        this.uuid=uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    public ArrayList<Listener> getListeners() {
        return listeners;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }
}
