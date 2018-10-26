package com.orsy.tdd.puissance4.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.websocket.ClientEndpoint;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orsys.tdd.puissance4.serveur.modele.EtatPartie;

@ClientEndpoint
public class ClientPuissance4 extends JFrame implements IActionsPuissance4 {
	
	
	private static final int NB_LIGS = 6;
	private static final int NB_COLS = 7;
	private final int xCOINSG = 10;
	private final int yCOINSG = 60;
	private final int dIAMETREJETON = 50;
	private final int eCARTENTREJETON = 3;

		
	static final public int DECONNECTE = 0;
	static final public int CONNECTE_ATTENTE_JOUEUR_ADVERSE = 1;
	static final public int CONNECTE_DOIT_JOUER = 2;
	static final public int CONNECTE_ATTENTE_COUP_ADVERSE = 3;
	
	public static final String ACTION_CONNECTER_SERVEUR = "Connecter serveur Puissance 4";
	public static final String ACTION_DECONNECTER_SERVEUR = "Déconnecter";
	
	public static final String STATUT_INITIAL = "Aucune partie en cours";
	public static final String STATUT_CONNECTE_ATTENTE_ADVERSAIRE = "Connecté, attente adversaire";
	public static final String STATUT_CONNECTE_DOIT_JOUER = "A vous de jouer";
	public static final String STATUT_CONNECTE_ATTENTE_COUP_ADVERSAIRE = "Attente coup adversaire";
	
		
	private int etat = DECONNECTE;
	private JLabel lblInformation = new JLabel();
	
	private JTextField txtHostAdressePort = new JTextField("localhost:8080");
	private JButton btnAction;
	
	// Les boutons pour lacher dans une colonne
	private JButton[] btnColonnes = new JButton[7];
	
	
    // Etat partie 
	private EtatPartie etatPartieCourante = new EtatPartie();
	// Convertisseur String JSON to Java
	ObjectMapper mapper = new ObjectMapper();
	// Pour créer la connexion Web Socket entre le client et le serveur
    private WebSocketClient client;
	
	
	
	public void handleServerMessage(String message) throws JsonParseException, JsonMappingException, IOException {
		etatPartieCourante = mapper.readValue( message , EtatPartie.class);
		int i=0;
		for( boolean b : etatPartieCourante.getEtatBoutonsCol())
			btnColonnes[i++].setEnabled(b);
		switch( etatPartieCourante.getEtat()) {
		case DECONNECTE :
			setInformation(STATUT_INITIAL);
			etat = DECONNECTE;
			btnAction.setText( ACTION_CONNECTER_SERVEUR);
			break;
		case CONNECTE_ATTENTE_JOUEUR_ADVERSE :
			etat = CONNECTE_ATTENTE_JOUEUR_ADVERSE;
			btnAction.setText( ACTION_DECONNECTER_SERVEUR);
			setInformation(STATUT_CONNECTE_ATTENTE_ADVERSAIRE);
			break;
		case CONNECTE_ATTENTE_COUP_ADVERSE :
			etat = CONNECTE_ATTENTE_COUP_ADVERSE;
			btnAction.setText( ACTION_DECONNECTER_SERVEUR);
			setInformation(STATUT_CONNECTE_ATTENTE_COUP_ADVERSAIRE);
			break;
		case CONNECTE_DOIT_JOUER :
			etat = CONNECTE_DOIT_JOUER;
			setInformation(STATUT_CONNECTE_DOIT_JOUER);
			break;
		}
		this.invalidate();
		this.repaint();
	}
	
	
	
	private void gestionnnaireBoutonAction()  {
		
		if ( etat == DECONNECTE )  { 
  		  client = new WebSocketClient( txtHostAdressePort.getText() , this );
  		}
		else { 
		  client.disconnectServer();
		  etat = DECONNECTE;
		  setInformation(STATUT_INITIAL);
		  btnAction.setText( ACTION_CONNECTER_SERVEUR);
  	    }
	}
	
	// Placement des composants et ajout des gestionnaires
	private void initComposants()  {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        
        // label Serveur host name
        // Champ de saisie
        txtHostAdressePort.setBounds(5,355,150,30);
        add( txtHostAdressePort);        
        // BOUTON ACTION ( Connecter / Deconnecter )
        btnAction = new JButton(ACTION_CONNECTER_SERVEUR);
        btnAction.setName("btnAction");
        btnAction.setBounds(5,385,375,30);
        btnAction.addActionListener(
        		new ActionListener() {
                      public void actionPerformed(ActionEvent e) { 
                    	  gestionnnaireBoutonAction();
                      }
        		}
        );
        
        add( btnAction );
        
        
        // LABEL INFORMATION 
        lblInformation.setText(STATUT_INITIAL);
        lblInformation.setName("lblInformation");
        lblInformation.setBounds(157,355,224,30);
        lblInformation.setForeground(Color.blue);
        lblInformation.setBorder(new EtchedBorder());
        add( lblInformation );
        
        
        // BOUTONS COLONNES
        for( int col=0; col < NB_COLS; col++ ) {
        	btnColonnes[col] = new JButton();
        	btnColonnes[col].setName("col"+(col+1));
        	btnColonnes[col].setEnabled( true );
        	btnColonnes[col].setBounds(xCOINSG - eCARTENTREJETON + col*(dIAMETREJETON+eCARTENTREJETON), 
        			                   yCOINSG - eCARTENTREJETON - 50, dIAMETREJETON, 20);
        	add(btnColonnes[col]);
        	btnColonnes[col].addActionListener(
            		new ActionListener() {
                          public void actionPerformed(ActionEvent e) { 
                        	  gestionnnaireBoutonsColonne(e);
                          }
            		}
            );
        }
    
        // Taille fixe
        setSize(390,450);
        setResizable(false);
        setMinimumSize(getSize());
       
        
	}
	
	
	protected void gestionnnaireBoutonsColonne(ActionEvent e) {
		// TODO Auto-generated method stub
	    String nomBouton = ((JButton)(e.getSource())).getName();
		String sNumCol = nomBouton.substring(3);
		client.sendMessage("jouer "+sNumCol);
	}


	public void setInformation( String info ) {
		 lblInformation.setText( info );
		 lblInformation.repaint();
	}
	
	
	@Override
	public void paint( Graphics g )
	{
	   super.paint(g);
	   g.setColor( Color.BLUE );
	   g.fillRect(xCOINSG - eCARTENTREJETON, yCOINSG - eCARTENTREJETON,
			      NB_COLS*(dIAMETREJETON+eCARTENTREJETON) + eCARTENTREJETON,
			      NB_LIGS*(dIAMETREJETON+eCARTENTREJETON) + eCARTENTREJETON);
	   
	   for( int lig=0; lig < NB_LIGS; lig++ )
		   for( int col=0; col < NB_COLS; col++ )  {
			   
			   switch( etatPartieCourante.getGrille()[lig][col]) {
			   		case "V" : g.setColor(Color.LIGHT_GRAY);break;
			   		case "R" : g.setColor(Color.RED);break;
			   		case "J" : g.setColor(Color.YELLOW);break;
			   }
			   
			   g.fillOval(xCOINSG + col*(dIAMETREJETON+eCARTENTREJETON), 
					      yCOINSG + lig*(dIAMETREJETON+eCARTENTREJETON), 
					      dIAMETREJETON, dIAMETREJETON);
		   }
				   
	}
	
	
	
	public ClientPuissance4() {
		super("Client Puissance 4");
		initComposants();
		setVisible(true);
		
		 
	}
	
	public static void main(String[] args) {
		new ClientPuissance4();	
	}



	//*************** API de pilotage ****************


	@Override
	public void seConnecter() {
		// TODO Auto-generated method stub
		btnAction.doClick();
	}



	@Override
	public void seDeconnecter() {
		// TODO Auto-generated method stub
		btnAction.doClick();
	}



	@Override
	public void abandonner() {
		btnAction.doClick();
		
	}



	@Override
	public void jouerJeton(int indexColonne) {	
		btnColonnes[indexColonne-1].doClick();
	}



	@Override
	public String lireStatut() {
		// TODO Auto-generated method stub
		return lblInformation.getText();
	}



	@Override
	public void fermer() {
		// Fait crasher ou bloque la jvm !
		//this.dispatchEvent(new WindowEvent( this, WindowEvent.WINDOW_CLOSING));
		
		// La solution :
		dispose();
		
	}



	@Override
	public String lireTexteBoutonAction() {
		return btnAction.getText();
	}



	@Override
	public boolean verifierImagePlateauVide() {
		// TODO Auto-generated method stub
		return true;
	}



	@Override
	public void positionner(int x, int y) {
		// TODO Auto-generated method stub
		setLocation(x,y);
	}



	@Override
	public boolean accessibiliteBoutonAjoutColonne(int indexColonne) {
		// TODO Auto-generated method stub
		return btnColonnes[ indexColonne-1].isEnabled();
	}
	
	@Override
	public void attendre( long duree ) {
		long debut = System.currentTimeMillis();
		while( System.currentTimeMillis() - debut < duree );
	}
	
}
