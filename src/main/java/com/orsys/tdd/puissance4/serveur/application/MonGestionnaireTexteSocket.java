package com.orsys.tdd.puissance4.serveur.application;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.orsy.tdd.puissance4.client.ClientPuissance4;
import com.orsys.tdd.puissance4.serveur.modele.EtatPartie;
import com.orsys.tdd.puissance4.serveur.modele.ModelePuissance4;

public class MonGestionnaireTexteSocket extends TextWebSocketHandler {
	
	private WebSocketSession sessionPremierJoueur = null;
	private WebSocketSession sessionDeuxiemeJoueur = null;
	private ModelePuissance4 modele = null;
	private EtatPartie etatPremierJoueur = null;
	private EtatPartie etatDeuxiemeJoueur = null;
	private static final int NB_COL = 7; 
	
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	if ( session == sessionPremierJoueur ) {
    		sessionPremierJoueur = null;
    	}
    	
    	if ( session == sessionDeuxiemeJoueur ) {
    		sessionDeuxiemeJoueur = null;
    	}
    	
    }
 
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
      
       if ( sessionPremierJoueur == null ) {
    	   modele = new ModelePuissance4();
   		   etatPremierJoueur = new EtatPartie( modele.etatGrille());
    	   sessionPremierJoueur = session;
    	   etatPremierJoueur.setCouleur("J");
     	   etatPremierJoueur.setEtat( ClientPuissance4.CONNECTE_ATTENTE_JOUEUR_ADVERSE);
     	   sessionPremierJoueur.sendMessage(new TextMessage( etatPremierJoueur.toString() ));
    	  
       }
       else if ( sessionDeuxiemeJoueur == null ) {
    	   etatDeuxiemeJoueur = new EtatPartie( modele.etatGrille());
    	   sessionDeuxiemeJoueur = session;
    	   etatDeuxiemeJoueur.setCouleur("R");
    	   etatDeuxiemeJoueur.setEtat( ClientPuissance4.CONNECTE_ATTENTE_COUP_ADVERSE);
    	   sessionDeuxiemeJoueur.sendMessage(new TextMessage( etatDeuxiemeJoueur.toString() ));
    	   
    	   // on renvoie l'état connecte doit jouer 	   
    	   etatPremierJoueur.setEtat( ClientPuissance4.CONNECTE_DOIT_JOUER);
    	   for( int col=1; col< NB_COL; col++)
    	        etatPremierJoueur.setEtatBoutonCol(col, true);
    	   sessionPremierJoueur.sendMessage(new TextMessage( etatPremierJoueur.toString() ));
       }
    }
 
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String message = textMessage.getPayload();
         // Qui envoi ?
        if ( session == sessionPremierJoueur )
        {
        	// Quelle action ?
        	if ( message.startsWith("jouer")) {
        		// ex "jouer 3"
        		int numCol = Integer.valueOf( message.substring(6));
        		modele.lacherJeton("J", numCol);
        		etatPremierJoueur.setGrille( modele.etatGrille());
        		etatPremierJoueur.setEtat(ClientPuissance4.CONNECTE_ATTENTE_COUP_ADVERSE);
        		etatDeuxiemeJoueur.setGrille( modele.etatGrille());
        		etatDeuxiemeJoueur.setEtat(ClientPuissance4.CONNECTE_DOIT_JOUER);
        		for( int col = 1; col <= NB_COL ; col++ )
        		{
        			etatPremierJoueur.setEtatBoutonCol( col , false);
        			etatDeuxiemeJoueur.setEtatBoutonCol(col, !modele.colonnePleine(col));
        		}
        		
        	}
        }
        else if ( session == sessionDeuxiemeJoueur )
        {
        	if ( message.startsWith("jouer")) {
        		int numCol = Integer.valueOf( message.substring(6));
        		modele.lacherJeton("R", numCol);
        		etatDeuxiemeJoueur.setGrille( modele.etatGrille());
        		etatDeuxiemeJoueur.setEtat(ClientPuissance4.CONNECTE_ATTENTE_COUP_ADVERSE);
        		etatPremierJoueur.setGrille( modele.etatGrille());
        		etatPremierJoueur.setEtat(ClientPuissance4.CONNECTE_DOIT_JOUER);
        		for( int col = 1; col <= NB_COL ; col++ )
        		{
        			etatDeuxiemeJoueur.setEtatBoutonCol( col , false);
        			etatPremierJoueur.setEtatBoutonCol(col, !modele.colonnePleine(col));
        		}       		
        	}
        }
        sessionPremierJoueur.sendMessage(new TextMessage( etatPremierJoueur.toString() ));
        sessionDeuxiemeJoueur.sendMessage(new TextMessage( etatDeuxiemeJoueur.toString() ));
    }
}
