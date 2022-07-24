package fr.supercomete.head.GameUtils.GameConfigurable;

import fr.supercomete.head.GameUtils.GameMode.Modes.Mode;

public class ModeBinding extends Binding {
	private final Mode mode;
	public ModeBinding(Mode mode) {
		this.mode=mode;
	}
	@Override
	public String getBinding() {
		return mode.getName();
	}
}