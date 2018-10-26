package com.orsys.tdd.puissance4.serveur.modele;

public class Direction {
	
	public static final Direction NORD = new Direction(-1,0);
	public static final Direction NORD_OUEST = new Direction(-1,1);
	public static final Direction OUEST = new Direction(0,1);
	public static final Direction SUD = directionOpposee( NORD );
	public static final Direction SUD_EST = directionOpposee( NORD_OUEST );
	public static final Direction EST = directionOpposee( OUEST );
	
	
	public static Direction[] lesDirections = { NORD , NORD_OUEST , OUEST };
	
	private int dLig = 0;
	private int dCol = 0;
	
	
	public Direction( int dLig , int dCol ) {
		this.dLig = dLig;
		this.dCol = dCol;
	}
	
	public static Direction directionOpposee( Direction dir ) {
		return new Direction( -dir.dLig , -dir.dCol);
	}

	public int dLig() { return this.dLig; }
	public int dCol() { return this.dCol; }
}