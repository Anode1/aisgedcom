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

import java.util.HashMap;
import java.util.Stack;


public class Ascending {

	
	public static HashMap findAllDirect(Tree tree) throws Exception{
		HashMap directAncestors = new HashMap();

		String id = Parameters.getString(Constants.START_ID_KEY); //we have validated the existence already
		
		Node node=(Node)tree.indiIds.get(id);
		if(node==null)
			return null;
		
		//iterative preorder tree traversing
		Stack stack = new Stack();
		stack.push(node);
		String gender=Gedcom.getGender(node);
		if(gender==null)
			node.setPath("U");
		else
			node.setPath(gender);
		
		while(!stack.empty()){
			node=(Node)stack.pop();
			
			Node left = Gedcom.getParent(tree, node, false);
			if(left!=null){
				stack.push(left);
				left.setPath(node.getPath()+"F");
			}	
			
			Node right = Gedcom.getParent(tree, node, true);
            if(right!=null) {
            	stack.push(right);
            	right.setPath(node.getPath()+"M");
            }
		
			//System.out.println(node.path + " " + node);
            directAncestors.put(node.getPath(), node.getId());
		}//while
		
		return directAncestors;
	}

	
}
