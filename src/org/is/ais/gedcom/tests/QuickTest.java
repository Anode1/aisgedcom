package org.is.ais.gedcom.tests;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import junit.framework.TestCase;

import org.is.ais.gedcom.Constants;
import org.is.ais.gedcom.Main;
import org.is.ais.gedcom.Parameters;
import org.is.ais.gedcom.tworelatives.CodeMap;
import org.is.ais.gedcom.tworelatives.RelationsMap;


public class QuickTest extends TestCase{

	public QuickTest(String name){
	    super(name);
	}
	
	
	public void test(){
		//Main.main(new String[]{"/home/sourcer/11086.ged", "@I360@"/*, "--living"*/});
		Main.main(new String[]{"test3.ged", "-i", "@I1@", "-i", "@I7@" , "--atreenoprefix"});
		
	}

	
}
