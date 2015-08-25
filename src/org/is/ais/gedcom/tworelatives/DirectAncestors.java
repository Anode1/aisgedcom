package org.is.ais.gedcom.tworelatives;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.is.ais.gedcom.Node;
import org.is.ais.gedcom.Tree;


/**
 * Adds additional index to direct ancestors (by Id) 
 */
public class DirectAncestors {
	
	private HashMap path2Node;  //MFFMMMF->id
	private HashMap ids=new HashMap();  //id->Node
	
	
	public DirectAncestors(Tree tree, HashMap path2Node){
		this.path2Node=path2Node;
		
		Iterator itr=path2Node.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry pairs = (Map.Entry)itr.next();
			String id = (String)pairs.getValue();
	    	Node node = (Node)tree.indiIds.get(id); //not null since we already extracted id from Node before
			ids.put(id, node);
		}
	}

	
	public Node get(String path){
		return (Node)path2Node.get(path);
	}
	

	public Node getById(String id){
		return (Node)ids.get(id);
	}
	
	
	public HashMap getPath2Node(){
		return path2Node;
	}
	
}
