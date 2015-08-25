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
import org.is.ais.gedcom.atree.AtreeParser;
import org.is.ais.gedcom.atree.Data;
import org.is.ais.gedcom.atree.Dates;
import org.is.ais.gedcom.atree.Sequence;
import org.is.ais.gedcom.tworelatives.CodeMap;
import org.is.ais.gedcom.tworelatives.RelationsMap;


public class OneTest extends TestCase{

	public OneTest(String name){
	    super(name);
	}
	
	
	public void test() throws Exception{
		Data data=AtreeParser.parseData("/Surname/ LastName");
		assertEquals("/Surname/ LastName", data.name);
		assertEquals(null, data.bPlace);
		
		data=AtreeParser.parseData("FirstName /Surname/ , place");
		assertEquals("FirstName /Surname/", data.name);
		assertEquals(null, data.bDate);
		assertEquals("place", data.bPlace);
	}
	

	
}
