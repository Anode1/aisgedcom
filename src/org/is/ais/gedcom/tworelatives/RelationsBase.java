package org.is.ais.gedcom.tworelatives;

import org.is.ais.gedcom.Strings;

public abstract class RelationsBase {
	
	protected Strings strings;
	
	
	public RelationsBase() throws Exception{
		strings=new Strings();
	}
	
	public abstract String[] get(String path1, String path2);
	
}
