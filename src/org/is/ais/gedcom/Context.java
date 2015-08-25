package org.is.ais.gedcom;

import java.util.HashMap;

public class Context {

	HashMap directAncestors;
	
	HashMap all=new HashMap(); //all direct relatives
	
	//the same as above but grouped by male or female lineage
	HashMap y=new HashMap(); 
	HashMap mito=new HashMap();
	
}
