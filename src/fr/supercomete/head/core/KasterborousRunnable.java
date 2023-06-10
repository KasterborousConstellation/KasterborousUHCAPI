package fr.supercomete.head.core;
import fr.supercomete.enums.Gstate;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.Time.Timer;
public interface KasterborousRunnable {
    String name();
    default void onAPILaunch(KtbsAPI api){

    }
    default void onAPIStop(KtbsAPI api){

    }
    default void onGameLaunch(KtbsAPI api){

    }
    default void onGameEnd(KtbsAPI api){

    }
    default void onTick(Gstate gstate, KtbsAPI api){

    }
    default void onTimer(Timer timer){

    }
    default void onSecond(KtbsAPI api){

    }
    default void onScoreBoardUpdate(KtbsAPI api){

    }
}