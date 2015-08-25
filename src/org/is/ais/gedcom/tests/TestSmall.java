package org.is.ais.gedcom.tests;

import junit.framework.TestCase;

import org.is.ais.gedcom.Constants;
import org.is.ais.gedcom.Main;


public class TestSmall extends TestCase{


	public TestSmall(String name){
	    super(name);
	}
	
	
	public void testSmall() throws Exception{
		
		Main.main(new String[]{"test2.ged", "@I1@", "--stat"}); //will generate atree.txt
	}
	

}
