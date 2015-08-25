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

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DescendingLines {

	
	public static void findFamilyLines(Tree tree, HashMap directAncestors, HashMap y, HashMap mito) throws Exception{
		
	    Iterator itr = directAncestors.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry pairs = (Map.Entry)itr.next();
	    	String id=(String)pairs.getValue();
	    	Node node = (Node)tree.indiIds.get(id);
	    	String familyLine = Util.path2familyLine(node.getPath());
	    	HashMap map2use;
	    	boolean male;
	    	if(familyLine.endsWith("M")){
	    		map2use=y;
	    		male=true;
	    	}
	    	else{
	    		map2use=mito;
	    		male=false;
	    	}
	    	
	    	HashMap ids = (HashMap)map2use.get(familyLine);
			if(ids==null){ //the first one
				ids = new HashMap();
				map2use.put(familyLine, ids);
			}
			ids.put(id, node); //add all direct ancestors first
			node.setDistance(node.getPath().length()-1);
	    }//while
	    //System.out.println(y);
	    //System.out.println(mito);
	    
	    populateAll(tree, y, true);
	    populateAll(tree, mito, false);
	}
	
	
	private static void populateAll(Tree tree, HashMap map, boolean isMale) throws Exception{
		
		Iterator itr = map.keySet().iterator(); //family lines
		while(itr.hasNext()){ //all direct ancestors
			String familyLinePath=(String)itr.next();
			HashMap ancestorsIds=(HashMap)map.get(familyLinePath); //ids of ancestors along the family line
		    Iterator itr2 = ancestorsIds.keySet().iterator();
		    ArrayList foundChildrenIds=new ArrayList();
		    while(itr2.hasNext()){
		    	String directAncestorId=(String)itr2.next();
		    	Node node = (Node)tree.indiIds.get(directAncestorId);
		    	ArrayList collectedDescendants=new ArrayList();
		    	Gedcom.getAllDescendants(tree, node, node, isMale, collectedDescendants, 1); //this will return children, children of children etc i.e. the whole descending subtree
	    		foundChildrenIds.addAll(collectedDescendants);
		    }
		    //add all into the set, overwriting redundant
		    for(int i=foundChildrenIds.size()-1; i>=0; i--){
		    	String id=(String)foundChildrenIds.get(i);
		    	Node node = (Node)tree.indiIds.get(id);
		    	if(ancestorsIds.get(id)==null){ //ignore existing ones direct ancestors
		    		ancestorsIds.put(id,node); //add ALL descendants of the direct ancestor
		    	}
		    }
		}

	}
	
	

	

}
