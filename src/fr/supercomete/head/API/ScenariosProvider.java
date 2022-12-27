package fr.supercomete.head.API;
import fr.supercomete.head.GameUtils.Scenarios.KasterborousScenario;
import java.util.ArrayList;
public interface ScenariosProvider {
    ArrayList<KasterborousScenario> getScenarios();
    void RegisterScenarios(KasterborousScenario... scenarios);
    void RegisterScenarios(ArrayList<KasterborousScenario> scenarios);
}
