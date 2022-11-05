package fr.supercomete.enums;

public enum Gstate {
    /*
    Enumération
    Description: Décrit les différentes étapes de la partie

    Waiting: Etat avant le démarrage de la partie
    Starting: Durant la téléportation des joueurs
    Playing: Etape après la téléportation et avant le début du jour
    Day: Durant le jour
    Night: Durant la nuit
    Finish: Pendant la fin de la partie après l'annonce de la victoire.
     */
	Waiting,Starting,Playing,Day,Night,Finish
}