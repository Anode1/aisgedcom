package org.is.ais.gedcom.tworelatives;

import org.is.ais.gedcom.Node;

public class Connection {
	
	private Node ancestor; //relatively to the first person
	private Relation relative; //relatively to the second person
	
	
	public Connection(Node ancestor, Relation relative){
		
		this.ancestor=ancestor;
		this.relative=relative;
	}

	
	public Node getAncestor(){
		return ancestor;
	}

	
	public Relation getRelative(){
		return relative;
	}
	
	
}
