package fr.supercomete.head.API;

import fr.supercomete.head.GameUtils.GameConfigurable.KasterBorousConfigurable;

import java.util.ArrayList;
import java.util.LinkedList;

public interface ConfigurableProvider {
    LinkedList<KasterBorousConfigurable> getConfigurables();
    void RegisterConfigurable(KasterBorousConfigurable... configurable);
    void RegisterConfigurable(ArrayList<KasterBorousConfigurable> configurable);
    int getDataFrom(KasterBorousConfigurable configurable);
    boolean getBooleanDataFrom(KasterBorousConfigurable configurable);
    boolean translateDataToBoolean(int data);
    int getDataFrom(String name);
    void setDataOf(KasterBorousConfigurable kasterBorousConfigurable,int data);
    void setDataOf(String name,int data);
}
