package fr.supercomete.enums;

public enum ExeptionType {
	BadGameNameException(ExceptionLevel.Low),
	UnableToCreateFile(ExceptionLevel.Warning),
	
	;
	private ExceptionLevel lvl;
	ExeptionType(ExceptionLevel level) {
		this.setLvl(level);

	}
	public ExceptionLevel getLvl() {
		return lvl;
	}
	public void setLvl(ExceptionLevel lvl) {
		this.lvl = lvl;
	}
}
