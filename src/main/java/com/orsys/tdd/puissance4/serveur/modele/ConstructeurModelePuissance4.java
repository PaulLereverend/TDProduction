package com.orsys.tdd.puissance4.serveur.modele;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class ConstructeurModelePuissance4 {
	
	private ModelePuissance4 modele;
	private int colonneCourante = 1;
	private String couleurCourante = ModelePuissance4.JETON_JAUNE;
	private int nbJetonsCourant;
	private boolean pourToutesLesColonnes = false;
	
	private List<Method> instructions = new ArrayList<Method>();
	private List<Integer> parametresNombreDeJetons = new ArrayList<Integer>();
	private Class<?> classe;
	
	public Method lireMethodeParNom( String nom ) throws ClassNotFoundException {
		classe = Class.forName("com.orsys.tdd.puissance4.serveur.modele.ConstructeurModelePuissance4");
		Method[] lesMethodes = classe.getMethods();
		for( Method m : lesMethodes ) {
			if ( m.getName().equals( nom ))
				return m;
		}
		return null;
	}
	
	
	public ConstructeurModelePuissance4 dansUnModeleVide() {
		modele = new ModelePuissance4();
		return this;
	}
	
	public ConstructeurModelePuissance4 dansLeModeleCourant() {
		return this;
	}
	
	
	public ConstructeurModelePuissance4 dansLaColonne( int col ) {
		pourToutesLesColonnes = false;
		colonneCourante = col;
		return this;
	}
	
	public ConstructeurModelePuissance4 pourToutesLesColonnesFaire( ) {
		pourToutesLesColonnes = true;
		return this;
	}
	
	public ConstructeurModelePuissance4 jetonsJaunes( ) throws ClassNotFoundException {
		if ( pourToutesLesColonnes ) {
	        instructions.add( lireMethodeParNom( "jetonsJaunes"));
	        parametresNombreDeJetons.add( null );
	        return this;
		}
		couleurCourante = ModelePuissance4.JETON_JAUNE;
		lacherJetons();
		return this;
	}
	
	public ConstructeurModelePuissance4 jetonsRouges( ) throws ClassNotFoundException {
		if ( pourToutesLesColonnes ) {
	        instructions.add( lireMethodeParNom( "jetonsRouges"));		 
	        parametresNombreDeJetons.add( null );
	        return this;
		}
		couleurCourante = ModelePuissance4.JETON_ROUGE;
		lacherJetons();
		return this;
	}
	
	public ConstructeurModelePuissance4 jetons( ) throws ClassNotFoundException {
		if ( pourToutesLesColonnes ) {
	        instructions.add( lireMethodeParNom( "jetons"));		 
	        parametresNombreDeJetons.add( null );
	        return this;
		}
		lacherJetons();
		return this;
	}
	
	
	public ConstructeurModelePuissance4 lacher( int nbJetons ) throws ClassNotFoundException {
		if ( pourToutesLesColonnes ) {
	        instructions.add( lireMethodeParNom( "lacher"));		 
	        parametresNombreDeJetons.add( nbJetons );
	        return this;
		}
		nbJetonsCourant = nbJetons;
		return this;
	}
	
	
	public ConstructeurModelePuissance4 changerLaCouleur() throws ClassNotFoundException {
		if ( pourToutesLesColonnes ) {
	        instructions.add( lireMethodeParNom( "changerLaCouleur"));		 
	        parametresNombreDeJetons.add( null );
	        return this;
		}
		if ( couleurCourante.equals( ModelePuissance4.JETON_JAUNE))
		  couleurCourante = ModelePuissance4.JETON_ROUGE;
		else
		  couleurCourante = ModelePuissance4.JETON_JAUNE; 
		return this;
	}
	
	
	private void lacherJetons() {
		for( int i=0; i<nbJetonsCourant; i++ ) {
			modele.lacherJeton(couleurCourante, colonneCourante);
		}
	}
	
    public ModelePuissance4 construire() throws IllegalAccessException, 
    IllegalArgumentException, InvocationTargetException {
    	if ( pourToutesLesColonnes ) {
    		pourToutesLesColonnes = false;
    		for( int col=1; col<=ModelePuissance4.NB_COL; col++) {
    			colonneCourante = col;
    			int numPar = 0;
    			for( Method m : instructions ) {
    	           	Object args = parametresNombreDeJetons.get( numPar );
    	           	if ( args != null )
    	         	   m.invoke(this, args);
    	           	else
    	           	   m.invoke(this);
    	           	numPar++;
    			}
    		}
    	}
    	return modele;
    }
    
    
    
}
