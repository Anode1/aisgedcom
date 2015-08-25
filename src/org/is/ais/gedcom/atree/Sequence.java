package org.is.ais.gedcom.atree;

public class Sequence {

	private int lastIndi;
	private int lastFam;
	
	
	public String getNextIndiId(){
		return "@I"+(++lastIndi)+"@";
	}
	
	
	public String getNextFamId(){
		return "@F"+(++lastFam)+"@";
	}
	
	
}
