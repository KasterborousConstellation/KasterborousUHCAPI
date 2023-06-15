package fr.supercomete.head.GameUtils.GameMode.ModeModifier;
public interface DelayedDeath extends GameModeModifier{
	int getDeathDelay();
	void onSecondtick(int second);
}
