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

import java.io.File;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.is.ais.gedcom.atree.AtreeProcessor;
import org.is.ais.gedcom.tworelatives.TwoRelativesProcessor;


public class Processor {
	
	
	public static Tree parse(Result result) throws Exception{

		//check file existence (required in all processors) 
		
		String filePath=Parameters.getString(Constants.INPUT_FILE_KEY);
		
		if(filePath==null){
			result.criticalMessage=("File not passed");
			return null;
		}
    	File file = new File(filePath);
		if(!file.exists() || !file.canRead()){
			result.criticalMessage="File does not exist or can't read: " + file;
			return null;
		}
		
		if(file.isDirectory()){
			result.criticalMessage="Please provide the name of the file - not directory";
			return null;
		}
		
		//
		//generic filters:
		//
		
		if(Parameters.getAsBoolean(Constants.TRANSCODE)){
			Transcode.transcode(filePath, result);
			return null;
		}
		
		//
		//processors not requiring Tree (parsing)
		//
		
		if(Parameters.getAsBoolean(Constants.ATREE2GEDCOM)){
			AtreeProcessor.process(filePath, result);
			return null;
		}
		
		//
		//processors parsing GEDCOM into Tree:
		//
		
		Tree tree=new Tree();
		Parser parser = new Parser();
		parser.parse(filePath, tree);
		
		if(tree.indiIds.size()<1){
			result.criticalMessage="Empty genealogy";
			return null;
		}
		return tree;
	}
	
	
	public static void process(Result result) throws Exception{
		
		Tree tree = parse(result);
		if(tree==null)
			return;
		
		//
		//validate starting ID and fix it - if necessary:
		//
		String id = Parameters.getString(Constants.START_ID_KEY);
		if(id==null){
			result.criticalMessage="Gedcom ID was not passed";
			return;
		}			
		
		String wrappedId = Util.wrapIdIfNecessary(id);
		if(wrappedId!=null){
			Parameters.getInstance().addProperty(Constants.START_ID_KEY, wrappedId);
			id=wrappedId;
		}
		if(tree.indiIds.get(id)==null){
			result.criticalMessage="ID " + Parameters.getString(Constants.START_ID_KEY) +" is not found in file " + Parameters.getString(Constants.INPUT_FILE_KEY);
			return;
		}
		
		HashMap directAncestors = Ascending.findAllDirect(tree);
		if(directAncestors==null){
			result.criticalMessage="No direct ancestors";
			return; //something wrong - exiting (can't do anyway)
		}

		//System.out.println("INDI:"+tree.indiIds);
		//System.out.println("FAM:"+tree.famIds);
		
		if(Parameters.getString(Constants.SECOND_ID_KEY)!=null){ //so we don't want other reports
			if(!TwoRelativesProcessor.process(tree, directAncestors, result)){
				return;
			}
		}
		else{
			AtreePrinter.printAtree(tree, directAncestors);
			
			HashMap y=new HashMap();
			HashMap mito=new HashMap();
			
			DescendingLines.findFamilyLines(tree, directAncestors, y, mito);
			
			DescendingReport.printDescendingLines(tree, y, mito);
	
			if(Parameters.getAsBoolean(Constants.GENERATE_STATISTICS))
				new Statistics().print(tree);
		}

		result.infoMessage="Files successfully created in directory: " + System.getProperty("user.dir");
	}

	
	/**
	 * We don't need haplogroups-specific structures, and we need a flat list from now on, so we'll move data 
	 */
	private static void moveNodes(HashMap half, HashMap all){
		Iterator itr = half.keySet().iterator();
		while(itr.hasNext()){
			String familyLinePath=(String)itr.next();
			HashMap ids=(HashMap)half.get(familyLinePath);
			
			Iterator itr3=ids.entrySet().iterator();
			while(itr3.hasNext()){
				Map.Entry pairs = (Map.Entry)itr3.next();
				Node node=(Node)pairs.getValue();
				all.put(pairs.getKey(), node);
			}
		}
	}
	

	
}
