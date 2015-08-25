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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.is.ais.gedcom.tworelatives.CodeMap;


public class Parser {

	private String prevPrefix="";
	private int prevLevel;
	private String prevLevel0recordId;
	boolean headFound;
	boolean inHead=false;
	
	
	public void parse(String filePath, Tree tree) throws Exception{

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

			boolean end=false;
			
			while((line=reader.readLine())!=null){
				
				lineCounter++;
				line=line.trim();
				if("".equals(line)){
					continue;
				}
				if(end){
					throw new Exception("Invalid GEDCOM: TRLR tag must be the last record");
				}
				
				//System.out.println(lineCounter+": "+line);				
				
				String[] fields=line.split(" ", 3);
				int level=0;
				try{
					level=Integer.parseInt(fields[0]);
				}
				catch(Exception e){
					throw new Exception("Invalid GEDCOM: Invalid level");
				}
				
				String pointer=null;
				String tag=null;
				String data=null;
				//we may have either one:
				//1 BIRT
				//2 DATE bla
				//1 @id@ INDI
				//1 @id@ TAG value
				
				fields[1]=fields[1].trim();
				
				if(fields.length==2){ //tag must exist anyway, so the second is TAG and no pointer
					tag=fields[1];
				}
				else{
					if(Util.isPointer(fields[1])){ //so the tag is 3rd
						pointer=fields[1];
						String[] tagAndData=fields[2].split(" ", 2);
						
						tag=tagAndData[0].trim();
						if(tagAndData.length>1){
							data=tagAndData[1].trim();
						}
					}
					else{ //no pointer
						tag=fields[1].trim();
						data=fields[2].trim();
					}
				}
				
				onLevel(level, pointer, tag, data, tree);
				
				if("TRLR".equals(tag)){
					end=true;
				}
			}
			
			if(!end)
				throw new Exception("Invalid GEDCOM: no TRLR tag in the file");
		}
		finally{
			if(reader!=null)try{reader.close();}catch(Exception e){}
		}
	}	
	
	
	/**
	 * Callback 
	 */
	private void onLevel(int currentLevel, String pointer, String tag, String theRest, Tree tree) throws Exception{

		String currentPrefix=null;
		if(currentLevel<=prevLevel){
			if(currentLevel==0){
				currentPrefix=tag;
				if(inHead){ //second 0-level after HEAD
					inHead=false;
				}
			}
			else{
				currentPrefix=Util.removeSuffixes(prevPrefix, currentLevel)+Constants.DELIM+tag;
			}
		}else{ // next level
			if(currentLevel-prevLevel>1)
				throw new Exception("Invalid GEDCOM: difference between subsequent level and the previous - more than one");
			currentPrefix=prevPrefix+Constants.DELIM+tag;
		}

		if(!headFound){ //the very first 0 record
			
			if(!"HEAD".equals(tag) || currentLevel!=0){ //must be HEAD
				throw new Exception("First tag is different from HEAD");
			}
			headFound=true;
			inHead=true;
		}		

		if(currentLevel==0){
			if(pointer!=null) prevLevel0recordId=pointer;
		}		
		
		if(!inHead) //do not save HEAD
			tree.addNode(currentLevel, prevLevel0recordId, currentPrefix, pointer, theRest);

	
		//System.out.println(currentPrefix + "	");
		
		prevLevel=currentLevel;
		prevPrefix=currentPrefix;
	
	}

	
}
