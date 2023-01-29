package fr.supercomete.head.API;

import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;
import fr.supercomete.head.role.KasterBorousCamp;
import fr.supercomete.head.role.Role;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public interface RoleProvider {
    void registerRole(Mode mode, Class<?> role);
    Class<?> getRoleClassByName(String name);
    Role getRoleByClass(Class<?> clz);
    CopyOnWriteArrayList<Class<?>> getRolesByCamp(Mode mode, KasterBorousCamp camp);
    Role getRoleOf(Player player);
    Role getRoleOf(UUID uuid);
    void DisplayRole(Player player);
    boolean IsCompoHiden();
    void setCompoHiden(boolean hide);
    UUID getWhoHaveRole(Class<?> clz);
    String FormalizedWhoHaveRole(Class<?> clz);
    boolean isRoleGenerated();
    HashMap<UUID,Role> getRoleMap();
}
