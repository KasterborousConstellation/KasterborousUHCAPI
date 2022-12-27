package fr.supercomete.head.API;

import fr.supercomete.head.core.KasterborousRunnable;

import java.util.ArrayList;

public interface KTBSRunnableProvider {
    void RegisterRunnable(ArrayList<KasterborousRunnable> ktbs_runnables);
    ArrayList<KasterborousRunnable> getRunnables();
}
