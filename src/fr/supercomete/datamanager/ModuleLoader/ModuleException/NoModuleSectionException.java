package fr.supercomete.datamanager.ModuleLoader.ModuleException;

public class NoModuleSectionException extends Exception{

    public NoModuleSectionException(String module){
        super("In module.yml of "+module+", their is no module section. It need a module section for the module to function and it need to specify the classpath of the main class that have to extend module object.");
    }
}
