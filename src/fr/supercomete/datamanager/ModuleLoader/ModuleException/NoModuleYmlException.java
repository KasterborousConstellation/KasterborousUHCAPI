package fr.supercomete.datamanager.ModuleLoader.ModuleException;

public class NoModuleYmlException extends Exception{
    public NoModuleYmlException(String module){
        super("The module: "+module+" has no module.yml. Add one or check if it place at the root of the module and not in a package.");
    }
}
