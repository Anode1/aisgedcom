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
package org.is.ais.gedcom.atree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import org.is.ais.gedcom.Constants;
import org.is.ais.gedcom.Parameters;
import org.is.ais.gedcom.Result;
import org.is.ais.gedcom.Strings;
import org.is.ais.gedcom.tworelatives.CodeMap;


public class AtreeParser {


	public HashMap parse(String filePath, Result result) throws Exception{

		HashMap hashMap=new HashMap();
		BufferedReader reader = null;
		try{
			File file = new File(filePath);
			if(!file.exists() || !file.canRead()){
				System.out.println("Cannot read file");
				System.out.println();
			}

			String lang=Parameters.getString(Constants.LANG_KEY);
			if(Strings.isDefaultEncoding(lang)){
				reader = new BufferedReader(new FileReader(file));
			}
			else{
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), CodeMap.getCode(lang)));
			}
			
			String line=null;
			int lineCounter=0;

			while((line=reader.readLine())!=null){
				
				lineCounter++;
				line=line.trim();
				if(line.startsWith("#"))
					continue;
				
				String[] fields=line.split("[ \t]", 2);
				if(fields.length<2)
					continue;

				String path=fields[0].trim();
				
				//validate path
				boolean valid=true;
				for(int i=path.length()-1; i>=0; i--){
					char c=path.charAt(i);
					if(c!='M' && c!='F'){
						System.out.println("WARN: Invalid path in line " + lineCounter);
						valid=false;
						break;
					}
				}
				if(!valid)
					continue;
				
				Data data=null;
				//try{
					data=parseData(fields[1]);
				//}
				//catch(Exception e){ //parsing errors
				//	result.criticalMessage="Errors parsing line "+lineCounter+" :" + e.getMessage();
				//}

				//check duplicates
				if(hashMap.get(path)!=null){
					result.criticalMessage="Duplicate in atree in line "+lineCounter;
					break;
				}
				hashMap.put(path, data);

			}//while lines
			
			return hashMap;

		}
		finally{
			if(reader!=null)try{reader.close();}catch(Exception e){}
		}
	}	


	/**
	 * grammar is too simple - to use parser generators such as antlr or javacc. 
	 * Even StreamTokenizer is not much useful here - where indexof is easy
	 */
	public static Data parseData(String string){
		Data data=new Data();

		//find valid dates (use them as the delimiter)
		
		//String[] toks=string.split("[()]");
		/*
	    StringTokenizer st = new StringTokenizer(string, "()", true);
	    while (st.hasMoreTokens()) {
	    	String tok = st.nextToken();
	        System.out.println(tok);
	    }*/

		int firstSlashIndex=string.indexOf("/");
		int secondSlashIndex=-1;
		if(firstSlashIndex+1<string.length()){
			secondSlashIndex=string.indexOf("/", firstSlashIndex+1);
		}		
		
	    int startDate=string.indexOf("(");
	    int endDate=-1;
	    while(startDate!=-1){
	    	endDate=string.indexOf(")", startDate);
	    	if(endDate==-1){ //no paired bracket - error -- just ignore it
	    		startDate=-1;
	    		break;
	    	}
	    	
			//check - whether (?) is inside / /
			if(firstSlashIndex!=-1 && firstSlashIndex<startDate){ //there is slash and it is on the left
				if(secondSlashIndex!=-1 && endDate<secondSlashIndex){ //no right slashes at all
					startDate=string.indexOf("(", startDate+1); //next candidate
					continue; //skip (?) if it is inside surnames been delimited by / / (so it is not a date but a maidenName)
				}
			}
			
	    	String potentialDate=string.substring(startDate+1, endDate);
	    	if(parseDates(potentialDate, data)){
	    		break;
	    	}
	    	
	    	if(startDate<string.length()-1)
	    		startDate=string.indexOf("(", startDate+1); //next candidate
	    	else
	    		break;
	    }//while
	    
	    if(startDate!=-1){ //date has been found
	    	data.name=string.substring(0, startDate).trim();
	    	String theRest=string.substring(endDate+1).trim();
	    	if(!theRest.equals("")){
	    		if(theRest.startsWith(",")){ //remove ","
	    			theRest=theRest.substring(1).trim();
	    		}
	    		data.bPlace=theRest;
	    	}
	    }
	    else{ // dates (main delimiter) not found, so use other delimiters
			//comma after the last / (delimiter for a place), allowing commas in names
	    	int indexOfComma=string.indexOf(",");
	    	if(indexOfComma!=-1 && secondSlashIndex!=-1 && indexOfComma>secondSlashIndex){ 
	    		data.name=string.substring(0, indexOfComma).trim();
	    		String theRest=string.substring(indexOfComma+1).trim();
		    	if(!theRest.equals("")){
		    		data.bPlace=theRest;
		    	}
	    	/*
			if(secondSlashIndex!=-1 && secondSlashIndex+1<string.length()){
				data.name=string.substring(0, secondSlashIndex+1).trim();
				String theRest=string.substring(secondSlashIndex+1).trim();
		    	if(!theRest.equals("")){
		    		if(theRest.startsWith(",")){ //remove ","
		    			theRest=theRest.substring(1).trim();
		    		}
		    		data.bPlace=theRest;
		    	}*/
			}
			else{
	    	   	data.name=string.trim();
			}
	    }
		
		return data;		
	}
	
	
	public static boolean parseDates(String string, Data data){
		/*
		if(string.endsWith(")"))
			string=string.substring(0, string.length()-1);
		if(string.startsWith("(")){
			string=string.substring(1);
		}*/

		String dates[] = string.split("-", 2);
		if(dates.length==1){ //only birth
			String date=dates[0].trim();
			if(Dates.isGoodDate(date)){
				data.bDate=date;
				return true;
			}
			else{
				return false;
			}
		}
		else{
			String date1=dates[0].trim();
			String date2=dates[1].trim();
			
			if(Dates.isGoodDate(date1) && Dates.isGoodDate(date2)){
				data.bDate=date1;
				data.dDate=date2;
				return true;
			}
			else{
				return false;
			}
		}
	}
	
}
