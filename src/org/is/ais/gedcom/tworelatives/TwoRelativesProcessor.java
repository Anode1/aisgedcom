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

import java.util.HashMap;
import java.util.Stack;

import org.is.ais.gedcom.Constants;
import org.is.ais.gedcom.Gedcom;
import org.is.ais.gedcom.Node;
import org.is.ais.gedcom.Parameters;
import org.is.ais.gedcom.Result;
import org.is.ais.gedcom.Tree;
import org.is.ais.gedcom.Util;


public class TwoRelativesProcessor {

	
	public static boolean process(Tree tree, HashMap directAncestorsOfFirst, Result result) throws Exception{

		Collected collected=new Collected(); //result of the processing		
		String id1 = Parameters.getString(Constants.START_ID_KEY); //we have validated the existence already
		Node node1=(Node)tree.indiIds.get(id1);
		
		DirectAncestors directAncestorsOfFirstId=new DirectAncestors(tree, directAncestorsOfFirst); //re-index by ID
		
		String id2 = Parameters.getString(Constants.SECOND_ID_KEY); //we have validated the existence already
		String wrappedId = Util.wrapIdIfNecessary(id2);
		if(wrappedId!=null){
			Parameters.getInstance().addProperty(Constants.SECOND_ID_KEY, wrappedId);
			id2=wrappedId;
		}		
		if(id2.equals(id1)){
			result.criticalMessage="The same ID been passed twice. Please specify different IDs";
			return false; //something wrong - exiting (can't do anyway)
		}
		Node node2=(Node)tree.indiIds.get(id2);
		if(node2==null){
			result.criticalMessage="ID" + id2 + " has no direct ancestors. Type a valid ID for the second person.";
			return false; //something wrong - exiting (can't do anyway)
		}
		
		findAllCommon(tree, node2, directAncestorsOfFirstId, collected);
		
		TwoRelativesPrinter.print(tree, node1, node2, collected);
		return true;
	}
	
	
	/**
	 * directAncestorsOfFirst is null - during the first run, so it is the indicator
	 */
	private static void findAllCommon(Tree tree, Node node, DirectAncestors directAncestorsOfFirst, Collected collected) throws Exception{
		
		//iterative preorder tree traversing
		Stack stack = new Stack();
		Relation relative = new Relation(node);
		stack.push(relative);
		String gender=Gedcom.getGender(node);
		if(gender==null)
			relative.setPath("U");
		else
			relative.setPath(gender);
		
		while(!stack.empty()){
			relative=(Relation)stack.pop();

			Node commonAncestor = directAncestorsOfFirst.getById(relative.node.getId());
			if(commonAncestor!=null){ //found the same relative
				Connection con = new Connection(commonAncestor, relative);
				collected.connections.add(con);
				continue;
			}			
			
			Node left = Gedcom.getParent(tree, relative.node, false);
			if(left!=null){
				Relation mother=new Relation(left);
				stack.push(mother);
				mother.setPath(relative.getPath()+"F");
			}	
			
			Node right = Gedcom.getParent(tree, relative.node, true);
            if(right!=null){
            	Relation father=new Relation(right);
            	stack.push(father);
            	father.setPath(relative.getPath()+"M");
            }
          
		}//while

	}


}
