package fr.supercomete.head.API;

import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.role.KasterBorousCamp;
import fr.supercomete.head.role.Role;

import java.util.concurrent.CopyOnWriteArrayList;

public interface RoleRegisterer {
    void registerRole(Mode mode, Class<?> role);
    Class<?> getRoleClassByName(String name);
    Role getRoleByClass(Class<?> clz);
    CopyOnWriteArrayList<Class<?>> getRolesByCamp(Mode mode, KasterBorousCamp camp);
}
