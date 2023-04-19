package fr.supercomete.head.GameUtils.GameMode.ModeHandler;

import fr.supercomete.head.API.*;



public class KtbsAPI {
    private final KtbsProvider provider;
    private final Helper helper;
    public KtbsAPI(){
        provider= new KtbsProvider();
        helper= new Helper();
    }
    public PlayerProvider getPlayerHelper(){
        return provider;
    }
    public PotionEffectProvider getPotionEffectProvider(){
        return provider;
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
    public FightProvider getFightProvider(){
        return provider;
    }
    public TeamProvider getTeamProvider(){
        return provider;
    }
    public CoordinateHelper getCoordinateHelper(){
        return helper;
    }
}