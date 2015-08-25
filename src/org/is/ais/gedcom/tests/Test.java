package org.is.ais.gedcom.tests;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.is.ais.gedcom.Constants;
import org.is.ais.gedcom.Counter;
import org.is.ais.gedcom.Main;
import org.is.ais.gedcom.Parameters;
import org.is.ais.gedcom.Statistics;
import org.is.ais.gedcom.Transcode;

import junit.framework.TestCase;


public class Test extends TestCase{

	public Test(String name){
	    super(name);
	}
	
	
	public void test(){
		//Main.main(new String[]{"/home/sourcer/11086.ged", "@I360@"/*, "--living"*/});
		Main.main(new String[]{"test2.ged", "-i", "@I1@", "-i", "@I13@", "--lang", "ru"});
		
	}

	
}
