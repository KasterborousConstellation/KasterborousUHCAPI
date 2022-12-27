package fr.supercomete.head.core;

import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.Time.Timer;

public interface KasterborousRunnable {
    String name();
    void onAPILaunch(KtbsAPI api);
    void onAPIStop(KtbsAPI api);
    void onGameLaunch(KtbsAPI api);
    void onGameEnd(KtbsAPI api);
    void onTick(Gstate gstate, KtbsAPI api);
    void onTimer(Timer timer);
}