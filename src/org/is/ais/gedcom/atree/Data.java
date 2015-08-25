package org.is.ais.gedcom.atree;


public class Data {
	
	public String name;
	public String bDate;
	public String dDate;
	public String bPlace;
	
	public String indiId;
	public String gender;
	public String famcId;
	public String famsId;
	
	
	
	/**
	 * For debugging purposes only
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("name="+name);
		sb.append(", bDate="+bDate);
		sb.append(", dDate="+dDate);
		sb.append(", bPlace="+bPlace);

		return sb.toString();
	}
}
