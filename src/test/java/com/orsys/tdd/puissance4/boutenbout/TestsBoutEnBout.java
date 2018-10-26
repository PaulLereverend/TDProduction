package com.orsys.tdd.puissance4.boutenbout;

import org.junit.FixMethodOrder;
import org.junit.Test;

import com.orsy.tdd.puissance4.client.ClientPuissance4;
import com.orsy.tdd.puissance4.client.IActionsPuissance4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.runners.MethodSorters;

import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestsBoutEnBout {
	
	private static final long TIMEOUT = 2;
	static private IActionsPuissance4 ihmJoueur1 = null;
	static private IActionsPuissance4 ihmJoueur2 = null;

	
	
	
	@Test
	public void test01ConnexionDeconnexionPremierJoueur() {
		
		ihmJoueur1 = new ClientPuissance4();
		ihmJoueur1.seConnecter();	
		await().atMost(TIMEOUT, SECONDS).until( () ->  !ihmJoueur1.lireStatut()
				.equals(ClientPuissance4.STATUT_INITIAL));
		assertEquals( ClientPuissance4.STATUT_CONNECTE_ATTENTE_ADVERSAIRE , ihmJoueur1.lireStatut() );
	    String texteBouton = ihmJoueur1.lireTexteBoutonAction();
	    assertEquals( ClientPuissance4.ACTION_DECONNECTER_SERVEUR, texteBouton );
		ihmJoueur1.seDeconnecter();
		await().atMost(TIMEOUT, SECONDS).until( () -> !ihmJoueur1.lireStatut()
				.equals(ClientPuissance4.STATUT_CONNECTE_ATTENTE_ADVERSAIRE));
	    assertEquals( ClientPuissance4.STATUT_INITIAL, ihmJoueur1.lireStatut() );
	    texteBouton = ihmJoueur1.lireTexteBoutonAction();
	    assertEquals( ClientPuissance4.ACTION_CONNECTER_SERVEUR, texteBouton );
	    ihmJoueur1.fermer();
	
	}
	
	@Test
	public void test02AffichagePlateauVidePremierJoueurEtAttenteDeuxiemeJoueur() {
		ihmJoueur1 = new ClientPuissance4();
		ihmJoueur1.positionner(10, 10);
		ihmJoueur1.seConnecter();	
	    assertTrue( ihmJoueur1.verifierImagePlateauVide());
	    ihmJoueur2 = new ClientPuissance4();
	    ihmJoueur2.positionner(400, 10);
	    ihmJoueur2.seConnecter();
		await().atMost(TIMEOUT, SECONDS).until( () ->  !ihmJoueur1.lireStatut()
				.equals(ClientPuissance4.STATUT_CONNECTE_ATTENTE_ADVERSAIRE));
	    assertEquals( ClientPuissance4.STATUT_CONNECTE_DOIT_JOUER, ihmJoueur1.lireStatut() );
		ihmJoueur1.seDeconnecter();
		ihmJoueur2.seDeconnecter();
		ihmJoueur1.fermer();
		ihmJoueur2.fermer();
	}
	
	
	@Test 
	public void test03BoutonAjoutColonneInaccessibleLorsqueColonnePleine() {
		ihmJoueur1 = new ClientPuissance4();
		ihmJoueur1.positionner(10, 10);
		ihmJoueur1.seConnecter();	
	 
		ihmJoueur2 = new ClientPuissance4();
	    ihmJoueur2.positionner(400, 10);
	    ihmJoueur2.seConnecter();
	
	    for( int i=0; i<3; i++) {
	      ihmJoueur1.attendre(500);
	      ihmJoueur1.jouerJeton(1);
	      ihmJoueur2.attendre(500);
	      ihmJoueur2.jouerJeton(1);
	    }
	    ihmJoueur1.attendre(500);
	    assertFalse( ihmJoueur1.accessibiliteBoutonAjoutColonne(1) );
	    assertTrue(  ihmJoueur1.accessibiliteBoutonAjoutColonne(2) );
	    ihmJoueur1.seDeconnecter();
		ihmJoueur2.seDeconnecter();
		ihmJoueur1.fermer();
		ihmJoueur2.fermer();
	    
	    
	}

}
