package fr.supercomete.head.GameUtils.GameConfigurable;

import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;

import java.lang.reflect.InvocationTargetException;

public class ModeBinding extends Binding {
	private final Mode mode;
	public ModeBinding(Mode mode) {
		this.mode=mode;
	}
    public ModeBinding(Class<?>modeClass)  {
        try {
            this.mode = (Mode) modeClass.getConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
	@Override
	public String getBinding() {
		return mode.getName();
	}
}