package org.is.ais.gedcom.tworelatives;

import org.is.ais.gedcom.Node;

public class Relation {


	public Node node;
	private String path="";
	
	
	public Relation(Node node){
		this.node=node;
	}
	
	
	public String getPath(){
		return path;
	}
	
	
	public void setPath(String path){
		this.path=path;
	}
	
}
