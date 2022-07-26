package fr.supercomete.datamanager.ModuleLoader.ModuleException;

public class NotAModuleException extends Exception{

    public NotAModuleException(String module){
        super("The module path: " +module +" lead to a class that is not an instance of Module object.");
    }
}
