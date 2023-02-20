package fr.supercomete.head.role;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import fr.supercomete.head.core.Main;
public class RoleBuilder {
	public static Role Build(Class<?> claz,UUID owner) {
		try {
			return (Role) claz.getConstructors()[0].newInstance(owner);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
}