package org.is.ais.gedcom.tests;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Used only in Unit tests
 */
public class Utils {

	public static String[] getFields(String filePath, int lineNumRequired) throws Exception{
		
		BufferedReader reader = null;
		try{
			int lineNum = 0;
			String line;
			reader = new BufferedReader(new FileReader(filePath));
			while((line = reader.readLine()) != null) {
				lineNum++;
				if(lineNumRequired!=lineNum)
					continue;
				String[] fields = line.split(",", -1);
				return fields;
			}
			return null;
		}
		finally{
			if(reader!=null)try{reader.close();}catch(Exception e){}
		}
	}
}
