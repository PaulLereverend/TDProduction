package com.orsys.tdd.puissance4.unitaire;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.orsys.tdd.puissance4.serveur.modele.Direction;
import com.orsys.tdd.puissance4.serveur.modele.ModelePuissance4;

public class TestsModelePuissance4 {
	
	private ModelePuissance4 modele = null;
	
	
	@Before
	public void creationModele() {
		modele = new ModelePuissance4();
	}
	
	@Test
	public void testModeleVideApresCreation() {
		assertTrue( modele.vide() );
	}
	
	@Test 
	public void testHauteurNulleApresCreation() {
		assertEquals( 0 , modele.lireHauteurColonne(1));
	}
	
	@Test 
	public void testPositionJetonApresInsertionDansGrilleVide() {
		modele.lacherJeton( "J" , 1 );
		assertEquals( 1 , modele.lireHauteurColonne(1) );
	}

	@Test 
	public void testPositionJetonApres2InsertionDansGrilleVide() {
	   
		modele.lacherJeton( "J" , 1 );
		modele.lacherJeton( "R" , 1 );
		assertEquals( 2 , modele.lireHauteurColonne(1) );
	}

	@Test 
	public void testColonnePleine() {
		for( int i=1; i<=ModelePuissance4.NB_LIG; i++) 
			modele.lacherJeton("R", ModelePuissance4.NB_COL);
		assertTrue( modele.colonnePleine( ModelePuissance4.NB_COL));
	}
	
	@Test
	public void testComptagePionsDansDirectionOuest() {
		modele.lacherJeton("R", 1);
		assertEquals( 1 , modele.compterPionsContigusDepuisPositionDansDirection("R", 
				ModelePuissance4.NB_LIG-1, 0, Direction.OUEST));
		modele.lacherJeton("R", 2);
		modele.lacherJeton("R", 3);
		modele.lacherJeton("R", 4);
		assertEquals( 3 , modele.compterPionsContigusDepuisPositionDansDirection("R", 
				ModelePuissance4.NB_LIG-1, 1, Direction.OUEST));
		
	}
	
}
