package fr.supercomete.head.role;
import java.util.*;

public abstract class RoleGenerator {
    protected final Random random=new Random(System.currentTimeMillis());
    public abstract HashMap<UUID, Class<?>> map(HashMap<Class<?>,Integer>roles, LinkedList<UUID> players);
    public abstract ArrayList<String> displayCompo(ArrayList<Role> roles);
}