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

import java.io.BufferedWriter;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Stack;

import org.is.ais.gedcom.Constants;
import org.is.ais.gedcom.FamilyLinesComparator;
import org.is.ais.gedcom.Gedcom;
import org.is.ais.gedcom.Node;
import org.is.ais.gedcom.Parameters;
import org.is.ais.gedcom.PrintUtil;
import org.is.ais.gedcom.Result;
import org.is.ais.gedcom.Strings;
import org.is.ais.gedcom.Util;
import org.is.ais.gedcom.tworelatives.CodeMap;
import org.is.ais.gedcom.tworelatives.Connection;
import org.is.ais.gedcom.tworelatives.Relation;

public class AtreeProcessor {

	public static void process(String filePath, Result result) throws Exception{
		
		AtreeParser parser=new AtreeParser();
		
		HashMap atree=parser.parse(filePath, result);
		
		//determine - whether we have the first node been cut off (Mich Glich format), i.e. we have 2 roots
		if((atree.get("M")!=null) && atree.get("F")!=null){
			result.criticalMessage="Cannot create gedcom from atree which has no root node. Please regenerate atree with prefix";
			return;
		}
		//no root at all
		if((atree.get("M")==null) && atree.get("F")==null){
			result.criticalMessage="Cannot create gedcom from atree which has no root node";
			return;
		}		
		
		//fill missing linking nodes by dummy (empty) nodes
		fillMissing(atree);
		
		//here we have a connected binary tree (not forest)
		
		GedcomAtree gedcomAtree=new GedcomAtree();
		ArrayList list=new ArrayList(atree.keySet());
		
		Collections.sort(list, new FamilyLinesComparator());
		


		//go through all indis and create families
	    Iterator itr = list.iterator();
	    while(itr.hasNext()){
	    	String path=(String)itr.next();
	    	Data childData=(Data)atree.get(path);
	    	if(childData.indiId==null){ //not been added yet
	    		childData.gender=path.substring(path.length()-1);
	    		gedcomAtree.addIndi(childData);
	    	}
	    	
			String fatherPath = path+"M";
			Data father=(Data)atree.get(fatherPath);
			if(father!=null){
				father.gender=fatherPath.substring(fatherPath.length()-1);
	    		gedcomAtree.addIndi(father);
			}		    	
	    	
			String motherPath = path+"F";
			Data mother=(Data)atree.get(motherPath);
			if(mother!=null){
				mother.gender=motherPath.substring(motherPath.length()-1);
	    		gedcomAtree.addIndi(mother);
			}
			
			if(mother!=null){
				GedcomFam fam=gedcomAtree.addFam(childData.indiId);
				childData.famcId=fam.getFamId();
				fam.setWifeId(mother.indiId);
				mother.famsId=fam.getFamId();
				if(father!=null){
					fam.setHusbId(father.indiId);
					father.famsId=fam.getFamId();
				}
				else{ //father==null
					//nothing
				}
			}
			else{ //mother==null
				if(father!=null){
					GedcomFam fam=gedcomAtree.addFam(childData.indiId);
					childData.famcId=fam.getFamId();
					fam.setHusbId(father.indiId);
					father.famsId=fam.getFamId();
				}
				else{ //father==null
					//do not create FAM at all
				}
			}
			
	    }
			
	   /*
		Data root;
		String path;
		if((root=(Data)atree.get("M"))!=null)
			path="M";
		else
			path="F";

		Stack stack = new Stack();
		stack.push(path);

		while(!stack.empty()){
			path=(String)stack.pop();
			
			String motherPath = path+"F";
			Data left=(Data)atree.get(motherPath);
			if(left!=null){
				stack.push(motherPath);
			}
			
			String fatherPath = path+"M";
			Data right=(Data)atree.get(fatherPath);
			if(right!=null){
				stack.push(fatherPath);
			}			
			
			System.out.println(path);
            //directAncestors.put(node.getPath(), node.getId());
		}//while
       */
		
		GedcomPrinter.print(gedcomAtree);
		
		result.infoMessage="File successfully created in directory: " + System.getProperty("user.dir");
	}
	
	
	/**
	 * For all already existing nodes - check whether intermediate nodes are existing) 
	 */
	public static void fillMissing(HashMap hash){
		ArrayList mising=new ArrayList();
		
	    Iterator itr = hash.keySet().iterator();
	    while(itr.hasNext()){
	    	String path=(String)itr.next();
	    	int length=path.length();
	    	for(int i=1; i<length; i++){
	    		String childPath=path.substring(0, i);
	    		if(hash.get(childPath)==null){
	    			mising.add(childPath);
	    		}
	    	}
	    }
	    
	    for(int i=mising.size()-1; i>=0; i--){
	    	hash.put((String)mising.get(i), new Data());
	    }
	}
	
}
