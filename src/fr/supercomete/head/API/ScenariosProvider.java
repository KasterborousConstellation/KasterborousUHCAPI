package fr.supercomete.head.API;
import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;
import java.util.ArrayList;
public interface ScenariosProvider {
    ArrayList<KasterborousScenario> getRegisteredScenarios();
    void RegisterScenarios(KasterborousScenario... scenarios);
    void RegisterScenarios(ArrayList<KasterborousScenario> scenarios);
    boolean IsScenarioActivated(String name);
    KasterborousScenario getActivatedScenario(String name);
}
