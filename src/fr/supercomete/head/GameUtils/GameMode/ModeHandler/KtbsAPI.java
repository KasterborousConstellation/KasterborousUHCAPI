package fr.supercomete.head.GameUtils.GameMode.ModeHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import com.sun.istack.internal.NotNull;
import fr.supercomete.head.API.*;
import fr.supercomete.head.Exception.AlreadyRegisterdScenario;
import fr.supercomete.head.Exception.AlreadyRegisteredConfigurable;
import fr.supercomete.head.GameUtils.GameConfigurable.KasterBorousConfigurable;
import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;
import fr.supercomete.head.core.KasterborousRunnable;


public class KtbsAPI {
    private final KtbsProvider provider;
    public KtbsAPI(){
        provider= new KtbsProvider();
    }


    public HostProvider getHostProvider(){
        return provider;
    }
    public GameProvider getGameProvider(){
        return provider;
    }
    public MapProvider getMapProvider(){return provider;}
    public ConfigurableProvider getConfigurableProvider(){
        return provider;
    }
    public ModeProvider getModeProvider(){
        return provider;
    }
    public ScenariosProvider getScenariosProvider(){
        return provider;
    }
    public RoleProvider getRoleProvider(){
        return provider;
    }
    public KTBSRunnableProvider getKTBSRunnableProvider(){
        return provider;
    }
}