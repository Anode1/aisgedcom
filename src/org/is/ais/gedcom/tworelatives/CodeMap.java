package org.is.ais.gedcom.tworelatives;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.is.ais.gedcom.Constants;
import org.is.ais.gedcom.Parameters;
import org.is.ais.gedcom.Strings;

public class CodeMap {

	private static LinkedHashMap nameCode=new LinkedHashMap();
	
	static{
		nameCode.put("en", new CodePage("ASCII","English, ASCII"));
		nameCode.put("ru", new CodePage("UTF8", "UTF-8"));
		nameCode.put("ru_Windows-1251", new CodePage("Cp1251", "Russian Windows-1251"));
		nameCode.put("ru_translit", new CodePage("ASCII", "Russian, translit"));
		
		//add here
	}
	
	
	
	public static String getCode(String name){
		CodePage codePage=(CodePage)nameCode.get(name);
		if(codePage==null){
			throw new NullPointerException("Code page is not supported!");
		}
		return codePage.codeName;
	}
	
	
	/**
	 * Convenience method using current lang
	 */
	public static String getCode(){
		String lang = Parameters.getString(Constants.LANG_KEY);
		if(lang==null)
			lang=Strings.getDefault();
		return getCode(lang);
	}
	
	
	public static String[] codePagesSupported(){
		ArrayList list=new ArrayList();
		
		
		
		String[] result=new String[list.size()];
		list.toArray(result);
		return result; 
	}
	

}
