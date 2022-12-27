package fr.supercomete.head.API;

import fr.supercomete.head.GameUtils.GameConfigurable.KasterBorousConfigurable;

import java.util.ArrayList;
import java.util.LinkedList;

public interface ConfigurableProvider {
    LinkedList<KasterBorousConfigurable> getConfigurables();
    void RegisterConfigurable(KasterBorousConfigurable... configurable);
    void RegisterConfigurable(ArrayList<KasterBorousConfigurable> configurable);
}
