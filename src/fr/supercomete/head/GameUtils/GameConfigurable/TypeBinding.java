package fr.supercomete.head.GameUtils.GameConfigurable;

public class TypeBinding extends Binding {
	private final BindingType type;
	public TypeBinding(BindingType type) {
		this.type=type;
	}
	@Override
	public String getBinding() {
		return type.getName();
	}

}
