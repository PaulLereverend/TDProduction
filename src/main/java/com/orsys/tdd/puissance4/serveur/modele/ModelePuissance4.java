package com.orsys.tdd.puissance4.serveur.modele;

import java.util.Arrays;

public class ModelePuissance4 {

	public final static int NB_LIG = 6;
	public final static int NB_COL = 7;
	
	public final static String JETON_ROUGE = "R";
	public final static String JETON_JAUNE = "J";
	private static final String AUCUNE = "AUCUNE";
	private static final String PARTIE_NULLE = "NULLE";
	
	
	private int ligDernierCoup = 0;
	private int colDernierCoup = 0;
	private String couleurDernierCoup = "R";
	
	
	private boolean partieGagnee = false;
	private String[][] grille = new String[NB_LIG][NB_COL];

	public ModelePuissance4() {
		for (int i = 0; i < NB_LIG; i++) {
			for (int j = 0; j < NB_COL; j++) {
				grille[i][j] = "V";
			}

		}
	}
	
	public String[][] etatGrille() {
		// SECURITE !!!
		return grille.clone();
	}
	
	
	public boolean vide() {
		for( String tab[] : Arrays.asList( grille ))
	         for( String s : tab ) 
	        	  if ( ! "V".equals(s) ) return false;
		return true;
	}

	public void lacherJeton(String couleur, int colIndex) {
		
		int hauteur = lireHauteurColonne( colIndex);
		if ( hauteur < NB_LIG ) {
			 colDernierCoup = colIndex - 1;
		     ligDernierCoup = NB_LIG - hauteur - 1;
		     couleurDernierCoup = couleur;
		     grille[ ligDernierCoup ][ colDernierCoup ] = couleur;
		     partieGagnee = dernierCoupGagnant();
		}
	}

	public int lireHauteurColonne(int colIndex) {
		int hauteur = 0;
		int ligne = NB_LIG-1;
		while(  ligne >= 0 && !"V".equals(grille[ligne][colIndex-1] ) ) { 
			ligne--;
			hauteur++;
		}
		return hauteur;
	}

	public boolean colonnePleine(int colIndex) {
		return lireHauteurColonne(colIndex) == NB_LIG;
	}
	
	
	public boolean positionValide( int lig, int col ) {
		return lig>=0 && lig < NB_LIG && col>=0 && col < NB_COL;
	}
	
	public int compterPionsContigusDepuisPositionDansDirection( String couleur , int lig, int col , Direction dir )
	{
	   int nbPions = 0;
	   while ( positionValide( lig , col ) && grille[lig][col].equals( couleur )) {
		   nbPions++;
		   lig+=dir.dLig();
		   col+=dir.dCol();
	   }
	   return nbPions;
	}
	
	public boolean coupGagnant( int lig , int col , String couleur ) {
		
		for( Direction dir : Direction.lesDirections ) {
			int nbPions = compterPionsContigusDepuisPositionDansDirection(couleur, lig, col, dir)
			+ compterPionsContigusDepuisPositionDansDirection(
					couleur, lig, col, Direction.directionOpposee(dir));
			// Gros piege : on compte 2 fois le premier pion ( lig , col )
			nbPions--;
			if ( nbPions >= 4 ) return true;
		}
		return false;
	}

	public boolean dernierCoupGagnant() {
		return coupGagnant(  ligDernierCoup , colDernierCoup , couleurDernierCoup  );
	}

	public String couleurGagante() {
		// TODO Auto-generated method stub
		if ( dernierCoupGagnant() )
			return couleurDernierCoup;
		return ModelePuissance4.AUCUNE;
	}

	public boolean partieNulle() {
		
		for( int col=1; col <= NB_COL ; col++ ) {
			if (!colonnePleine( col ))
				return false;
		}
		return !partieGagnee;
	}
	
	
	
	
}
