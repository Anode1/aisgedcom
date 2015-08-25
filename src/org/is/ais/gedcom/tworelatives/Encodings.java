package org.is.ais.gedcom.tworelatives;

import java.io.File;
import java.util.ArrayList;
import org.is.ais.gedcom.Constants;


public class Encodings {

	public static String[] loadSupportedEncoding() throws Exception{
		ArrayList arrayList=new ArrayList();
		
		String dirName="conf";
		File dir = new File(dirName);
		String[] entries = dir.list();
	    for(int i = 0; i < entries.length; i++){
	    	String filePath=dirName + File.separator + entries[i];
	        File aFile = new File(filePath);
	        if(aFile.isDirectory()){
	        	continue; //skip directories
	        }
        	else if(aFile.isFile()){
        		if(entries[i].startsWith(Constants.LANG_PREFIX) && entries[i].endsWith(Constants.LANG_SUFFIX)){
       			
        			String l=entries[i].substring(Constants.LANG_PREFIX.length(), entries[i].length()-Constants.LANG_SUFFIX.length());
        			
        			arrayList.add(l);
        		}
        	}
	    }
		
		String[] list=new String[arrayList.size()];
		arrayList.toArray(list);
		return list;
	}
}
