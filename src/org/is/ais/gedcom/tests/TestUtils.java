package org.is.ais.gedcom.tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Utils used only in unit tests 
 */
public class TestUtils {

	
	public static String file2String(String filePath) throws Exception{
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(filePath));
			String line;
			while((line = reader.readLine())!=null){
				sb.append(line);
				sb.append(System.getProperty("line.separator"));
			}
		}
		finally{
			reader.close();
		}
		return sb.toString();
	}
	
	
	public static void string2File(String filePath, String string) throws Exception{
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
		out.print(string);
		out.close();
	}
	
}
