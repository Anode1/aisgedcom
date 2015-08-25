package org.is.ais.gedcom.atree;

import java.util.ArrayList;

public class GedcomAtree {

	Sequence seq=new Sequence();
	
	private ArrayList indis=new ArrayList();
	private ArrayList fams=new ArrayList();
	

	public void addIndi(Data data){
		data.indiId=seq.getNextIndiId();
		indis.add(data);
	}
	
	
	public GedcomFam addFam(String childId){
		GedcomFam fam=new GedcomFam(childId, seq);
		fams.add(fam);
		return fam;
	}
	
	
	public ArrayList getIndis() {
		return indis;
	}
	
	
	public ArrayList getFams() {
		return fams;
	}
	
	
}
