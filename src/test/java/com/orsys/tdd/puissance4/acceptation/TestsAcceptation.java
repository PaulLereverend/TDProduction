package com.orsys.tdd.puissance4.acceptation;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

import com.orsys.tdd.puissance4.serveur.modele.ConstructeurModelePuissance4;
import com.orsys.tdd.puissance4.serveur.modele.ModelePuissance4;

public class TestsAcceptation {
	private ModelePuissance4 modele = null;
	private ConstructeurModelePuissance4 fabrique = new ConstructeurModelePuissance4();
    
	
	@Before
	public void creationModele() {
		modele = new ModelePuissance4();
	}

	@Test
	public void testCoupGagnantAvecAlignementHorizontal() {
		modele.lacherJeton("R", 1);
		modele.lacherJeton("R", 4);
		modele.lacherJeton("R", 3);
		modele.lacherJeton("R", 2);
		assertTrue(modele.dernierCoupGagnant());
	}

	@Test
	public void testCoupGagnantAvecAlignementVertical() {
		modele.lacherJeton("R", 1);
		modele.lacherJeton("R", 1);
		modele.lacherJeton("R", 1);
		modele.lacherJeton("R", 1);
		assertTrue(modele.dernierCoupGagnant());
	}

	@Test
	public void testCoupGagnantAvecAlignementDiagonal() {
		for (int col = 1; col <= 4; col++)
			for (int coup = 1; coup <= col; coup++)
				if (coup == col)
					modele.lacherJeton("R", col);
				else
					modele.lacherJeton("J", col);

		assertTrue(modele.dernierCoupGagnant());
	}

	@Test
	public void testPartieGagnanteJoueurJaune() {
        for( int col=1; col<4; col++) {
        	modele.lacherJeton(ModelePuissance4.JETON_JAUNE, col );
        	modele.lacherJeton(ModelePuissance4.JETON_ROUGE, col);
        }
        modele.lacherJeton(ModelePuissance4.JETON_JAUNE, 4 );
     	assertTrue( modele.couleurGagante() == ModelePuissance4.JETON_JAUNE );
	}

	@Test
	public void testPartieGagnanteJoueurRouge() {
        for( int col=1; col<4; col++) {
        	modele.lacherJeton(ModelePuissance4.JETON_ROUGE, col );
        	modele.lacherJeton(ModelePuissance4.JETON_JAUNE, col);
        }
        modele.lacherJeton(ModelePuissance4.JETON_ROUGE, 4 );
     	assertTrue( modele.couleurGagante() == ModelePuissance4.JETON_ROUGE );
	}
	
	@Test
	public void testPartieNulle() throws ClassNotFoundException, 
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        modele = fabrique.dansUnModeleVide().pourToutesLesColonnesFaire()
        			.lacher(3).jetons().changerLaCouleur().lacher(3).jetons()
        			.construire();
     	assertTrue( modele.partieNulle());
	}
	
	
	
	
	
}
