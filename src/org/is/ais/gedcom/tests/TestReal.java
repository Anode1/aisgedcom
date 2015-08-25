package org.is.ais.gedcom.tests;

import junit.framework.TestCase;
import org.is.ais.gedcom.Main;


public class TestReal extends TestCase{


	public TestReal(String name){
	    super(name);
	}
	
	
	public void testBigWithISO1251(){
		Main.main(new String[]{"/home/sourcer/2011-12-18.ged", "@I360@", "--stat", "--centuries"});
		//Main.main(new String[]{"/home/sourcer/2011-12-06.ged", "@I360@", "--stat", "--centuries", "-a"});
		//Main.main(new String[]{"/home/sourcer/2011-12-06.ged", "-i", "@I360@", "-i", "@I34033@", "-l", "ru_Windows-1251", "--atreenoprefix"});
	}

	

}
