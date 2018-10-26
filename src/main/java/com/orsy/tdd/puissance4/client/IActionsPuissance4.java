package com.orsy.tdd.puissance4.client;

public interface IActionsPuissance4 {
	public void positionner( int x, int y);
	public void seConnecter();
	public void seDeconnecter();
	public void abandonner();
	public void jouerJeton( int indexColonne );
	public String lireStatut();
	public void fermer();
	public String lireTexteBoutonAction();
	public boolean verifierImagePlateauVide();
	public boolean accessibiliteBoutonAjoutColonne(int indexColonne);
	void attendre(long duree);
}
