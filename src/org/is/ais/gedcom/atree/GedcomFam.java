package org.is.ais.gedcom.atree;

public class GedcomFam {

	private String famId;
	private String husbId;
	private String wifeId;
	private String childId;
	
	
	GedcomFam(String childId, Sequence seq){
		famId=seq.getNextFamId();
		this.childId=childId;
	}

	
	public String getFamId() {
		return famId;
	}


	public String getHusbId() {
		return husbId;
	}


	public void setHusbId(String husbId) {
		this.husbId = husbId;
	}


	public String getWifeId() {
		return wifeId;
	}


	public void setWifeId(String wifeId) {
		this.wifeId = wifeId;
	}


	public String getChildId() {
		return childId;
	}

	
}
