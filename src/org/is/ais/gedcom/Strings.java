/*
 	Copyright (C) 2011 Vasili Gavrilov

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.is.ais.gedcom;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;

import org.is.ais.gedcom.tworelatives.CodeMap;


public class Strings{
		
	//private UCProperties map;
	private Properties map;
	
	public Strings() throws Exception{
		
		//map=new UCProperties();
		map=new Properties();
		
		//We could do ResourceBundles here but for now this approach is easier and more flexible
		String lang = Parameters.getString(Constants.LANG_KEY);
		if(lang==null)
			lang=getDefault();
		
		String fileName="conf"+File.separator+Constants.LANG_PREFIX+lang+Constants.LANG_SUFFIX;
		//map.load(new BufferedReader(new FileReader(fileName)));
		
		
		BufferedReader reader = null;
		try{
			if(isDefaultEncoding(lang)){
				reader = new BufferedReader(new FileReader(fileName));
			}
			else{
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), CodeMap.getCode(lang)));
			}

			String line=null;
			int lineCounter=0;

			while((line=reader.readLine())!=null){
				lineCounter++;
				
				//line=line.trim();
				if("".equals(line) || line.startsWith("#")){
					continue;
				}
	
				String[] fields=line.split("=", 2);
				if(fields.length<2)
					continue;
			
				map.put(fields[0], fields[1]);
			}
		}
		finally{
			if(reader!=null)try{reader.close();}catch(Exception e){}
		}
		
	}	

	
	public static String getDefault(){
		return "en";
	}
	
	
	public String get(String id){
	    if(id==null)
	    	throw new NullPointerException("Can't use 'null' as a key into the messages map!");
	    String value = map.getProperty(id);
	    if(value==null){
	    	System.out.println(map);
	    	System.out.println("No message found for key="+id);
	    	return null;
	    }
	    
	    return value.trim();
	}
	
	
	/**
	 * Returns formatted message (if template)
	 */
	public String get(String id, Object... args){
		String msg = get(id);
		if(msg==null)
			return null;

	    return String.format(msg, args);
	}
	
	
	public static boolean isDefaultEncoding(String lang){
		return lang==null || lang.trim().equals("") || lang.equals(Strings.getDefault()) || lang.equals("ru_translit");
	}

}
