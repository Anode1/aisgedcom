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


public class TestTranscode extends TestCase{

	public TestTranscode(String name){
	    super(name);
	}

	/*
	public void testTranscode() throws Exception{
		
		//System.out.println(Charset.availableCharsets()); 
		
		FileInputStream fis = new FileInputStream("conf/relations_ru_Windows-1251.lang");
		//UTF-8 ISO-8859-1 KOI8-R
		FileOutputStream fos = new FileOutputStream("test.txt");
		//Transcode.transcode(fis, fos, "windows-1251", "UTF-8");
		//Transcode.transcode(fis, fos, "windows-1251", "KOI8-R");
		fis.close();
		fos.close();
		
		
		//PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test.txt"), CodeMap.getCode())));
		//out.println(strings.get("mother"));
		//out.close();
	}
	*/
	
	
	public void test(){
		//Main.main(new String[]{"conf/relations_ru_Windows-1251.lang", "--transcode", "-e1", "windows-1251", "-e2", "translit"});
		Main.main(new String[]{"conf/relations_ru_Windows-1251.lang", "--transcode", "-e1", "windows-1251", "-e2", "ISO-8859-1", "--translit"});
	}
		
	
	/* Print out all suspected Cyrillic characters from Unicode pages
	public void test() throws Exception{
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test.txt"), "UTF-8")));

		for(int i = 1024; i<=1279; i++){
			out.println(i+" "+(char)i);
			
		}
		
		for(int i = 1280; i<=1327; i++){
			out.println(i+" "+(char)i);
		}

		for(int i = 11744; i<=11775; i++){
			out.println(i+" "+(char)i);
		}

		for(int i = 42560; i<=42655; i++){
			out.println(i+" "+(char)i);
		}				
		out.close();
	}*/

}
