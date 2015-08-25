package org.is.ais.gedcom.tests;

import junit.framework.TestCase;

import org.is.ais.gedcom.Main;


public class TestAtree extends TestCase{


	public TestAtree(String name){
	    super(name);
	}
	
	
	public void testSmall(){
		Main.main(new String[]{"atree.txt", "--atree2gedcom"});
	}
	

}
