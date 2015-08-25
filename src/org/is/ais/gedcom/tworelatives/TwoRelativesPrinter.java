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
package org.is.ais.gedcom.tworelatives;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.is.ais.gedcom.Constants;
import org.is.ais.gedcom.Gedcom;
import org.is.ais.gedcom.Node;
import org.is.ais.gedcom.Parameters;
import org.is.ais.gedcom.PrintUtil;
import org.is.ais.gedcom.Strings;
import org.is.ais.gedcom.Tree;
import org.is.ais.gedcom.Util;


public class TwoRelativesPrinter {

	
	private static final String DELIM=", ";	
	
	
	public static void print(Tree tree, Node node1, Node node2, Collected collected) throws Exception{
	
		boolean noPathPrefix=Parameters.getAsBoolean(Constants.ATREE_NO_PREFIX_KEY);
		boolean printNames=!Parameters.getAsBoolean(Constants.NO_NAMES_IN_RELATIONS);
		String outputFile=Parameters.getString(Constants.OUTUT_FILE_KEY);
		if(outputFile==null){
			outputFile=Constants.TWO_RELATIVES_FILE;
		}		
		
		RelationsMap names=new RelationsMap();
		
		//print report
		PrintWriter out = null;
		try{
			
			String lang=Parameters.getString(Constants.LANG_KEY);
			if(Strings.isDefaultEncoding(lang)){
				out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, false)));
			}
			else{
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile),CodeMap.getCode())));
			}
				
	    	out.print(Gedcom.getName(node1));
	    	PrintUtil.printDates(node1, out);
	    	out.print(" vs ");
	    	out.print(Gedcom.getName(node2));
	    	PrintUtil.printDates(node2, out);
	    	out.println();
	    	
			//HashMap path2NodeMap = directAncestors.getPath2Node();
			//ArrayList view = new ArrayList(path2NodeMap.keySet());
			//Collections.sort(view, new FamilyLinesComparator());
			ArrayList view = collected.connections;
			
		    for(int i=0; i<view.size(); i++){
		    	
		    	Connection con=(Connection)view.get(i);
		    	Node ancestotNodeRelatively2First = con.getAncestor();
		    	Relation relative = con.getRelative();
		    	
		    	String path1=ancestotNodeRelatively2First.getPath();
		    	String path2=relative.getPath();
		    	
		    	String[] relationNames=names.get(path1,path2);

		    	String path1ToPrint=path1;
		    	String path2ToPrint=path2;
		    	
		    	if(noPathPrefix){
		    		path1ToPrint=Util.familyLineWithoutSubject(path1);
		    		path2ToPrint=Util.familyLineWithoutSubject(path2);
		    	}
		    	
				//just verification check (instead of assert):
				if(ancestotNodeRelatively2First.getId()!=con.getAncestor().getId()){
					throw new Exception("The same ancestor has different IDs? "+ancestotNodeRelatively2First+" vs " + relative.node);
				}

				
		    	if(printNames){
		    		out.println();
		    		
		    		out.print(relationNames[0]);
		    		out.print(DELIM);
			    	out.println(path1ToPrint);

			    	Node[] nodes1 = Gedcom.getAncestorsNamesByPath(tree, node1, path1);
			    	for(int j=0; j<nodes1.length; j++){
			    		if(noPathPrefix && j==0 && nodes1.length!=1) //skip the first row
			    			continue;
			    		
			    		out.print("\t"+Gedcom.getName(nodes1[j]));
			    		PrintUtil.printDates(nodes1[j], out);
			    		out.println();
			    	}

			    	out.print(relationNames[1]);
		    		out.print(DELIM);
			    	out.println(path2ToPrint);
			    	
			    	Node[] nodes2 = Gedcom.getAncestorsNamesByPath(tree, node2, path2);
			    	for(int j=0; j<nodes2.length; j++){
			    		if(noPathPrefix && j==0 && nodes2.length!=1) //skip the first row
			    			continue;
			    		
			    		out.print("\t"+Gedcom.getName(nodes2[j]));
			    		PrintUtil.printDates(nodes2[j], out);
			    		out.println();
			    	}
  		
		    	}
		    	else{//short report
		    		out.print(relationNames[0]);
			    	out.print(DELIM);
			    	out.print(relationNames[1]);
			    	out.print(DELIM);			    		
			    	out.print(path1ToPrint);
			    	out.print(DELIM);
					out.print(path2ToPrint);
					out.print(DELIM);
					out.print(Gedcom.getName(ancestotNodeRelatively2First));
			    	PrintUtil.printDates(ancestotNodeRelatively2First, out);
			    	out.println();
		    	}

		    }//for
			
		}
		finally{
			if(out!=null)try{out.close();}catch(Exception ex){}
		}		
	}
	
	
	
}
