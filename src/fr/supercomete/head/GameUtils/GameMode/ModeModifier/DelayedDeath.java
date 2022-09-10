package fr.supercomete.head.GameUtils.GameMode.ModeModifier;

import java.util.HashMap;
import java.util.UUID;

public interface DelayedDeath extends GameModeModifier{
	int getDeathDelay();
	void onSecondtick(int second);
}
