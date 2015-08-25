package org.is.ais.gedcom;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Here we have mainly - GEDCOM traversing routines  
 *
 */
public class Gedcom {
	
	
	public static Node getParent(Tree tree, Node node, boolean father) throws Exception{
		
		ArrayList famc=(ArrayList)node.data.get("INDI.FAMC");
		if(famc==null || famc.size()==0)
			return null;
		
		if(famc.size()>1){
			System.out.println("WARN: child " + node.getId()+ " has more than one FAMC which are PEDI birth. Has no idea - which path to pick. First is taken");
		}
		
		String famId = (String)famc.get(0);
		Node family = (Node)tree.famIds.get(famId);

		if(family==null){
			System.out.println("GEDCOM invalid: there is no FAM with id " + famId);
			return null;
		}
		
		String tag;
		if(father)
			tag="FAM.HUSB";
		else
			tag="FAM.WIFE";
		
		String parentId=(String)family.data.get(tag);
		if(parentId==null)
			return null; //no father in the family
		
		Node parentNode = (Node)tree.indiIds.get(parentId);
		
		return parentNode;
	}
	
	
	public static void getAllDescendants(Tree tree, Node commonAncestor, Node node, boolean male, ArrayList collectedDescendants, int levelDown) throws Exception{
		
		ArrayList fams=(ArrayList)node.data.get("INDI.FAMS");
		if(fams==null)
			return;
		
		for(int i=0; i<fams.size(); i++){
			String famId = (String)fams.get(i);
			Node family = (Node)tree.famIds.get(famId);
			if(family==null){
				throw new Exception("GEDCOM invalid: there is no FAM with id " + famId);
			}
			
			ArrayList childrenIds=(ArrayList)family.data.get("FAM.CHIL");
			if(childrenIds==null)
				continue; //no children - next family
			
			for(int j=0; j<childrenIds.size(); j++){
				String childId=(String)childrenIds.get(j);
				Node childNode = (Node)tree.indiIds.get(childId);
				if(childNode==null){
					throw new Exception("Invalid GEDCOM: child not found. Id="+childId);
				}

				String gender=getGender(childNode);
				
				if(!childNode.isDirectAncestor()){
					childNode.setDistance(node.getDistance()+1);
				}				
				
				//if(!childNode.isDirectAncestor())
				//	childNode.commonAncestor=commonAncestor; //set pointer to the most distant common ancestor
				if(gender==null)
					continue;
				
				if(male && gender.equals("F")){
					continue; 
				}
				
				if(!male && gender.equals("M")){
					collectedDescendants.add(childId); //add males in 1 generation - they can also be tested on mito
					continue;
				}

				if(childNode.isDirectAncestor()){
					continue;
				}
				
				//System.out.println("found child: "+childId+" " + gender+" "+childNode.getPath());
				
				collectedDescendants.add(childId);
				
				getAllDescendants(tree, commonAncestor, childNode, male, collectedDescendants, levelDown+1); //recursive call
			}
		}
	}
		
	
	public static String getName(Node node){
		ArrayList names = (ArrayList)node.data.get("INDI.NAME");
		if(names==null){
			return "";
		}
		
		StringBuffer sb=new StringBuffer();
		for(int i=0; i<names.size(); i++){
			if(i>0)sb.append(" ");
			sb.append(names.get(i));
		}
		return sb.toString();
	}
	
	
	public static String getGender(Node node) throws Exception{
		String startNodeGender=(String)node.data.get("INDI.SEX");
		if(startNodeGender==null){
			throw new Exception("Invalid GEDCOM: no INDI.SEX in the start node");
		}else if(startNodeGender.equals("M")){
			return "M";
		}else if(startNodeGender.equals("F")){
			return "F";
		}else{
			//System.out.println("Invalid GEDCOM: INDI.SEX contains unknown value: "+startNodeGender);
			return null;
		}
	}
	
	
	public static boolean isLiving(Node node){
		String deathDate = (String)node.data.get("INDI.DEAT.DATE");
		if(deathDate!=null) //known death date
			return false;
		//calculate age. If more than 100 years - ignore
		String birthDate = (String)node.data.get("INDI.BIRT.DATE");
		if(birthDate==null){
			String path=node.getPath();
			if(path!=null && path.length()>Constants.NUM_GENERATIONS_LIKELY_NOT_LIVING) //more than 5 generations
				return false; //not likely
			
			return true; //can't say anything
		}
		
		int years=DateUtil.thisYear-DateUtil.parseYear(birthDate, false);
	
		if(years>Constants.YEARS_THRESHOLD_FOR_LIKELY_LIVING){
			return false; //likely not
		}

		return true;
	}
	

	public static Node[] getAncestorsNamesByPath(Tree tree, Node from, String path) throws Exception{
		int n=path.length();
		Node[] nodes=new Node[n];
		nodes[0]=from;
		for(int i=1; i<n; i++){
			char gender=path.charAt(i);
	
			if(gender=='F')
				from=getParent(tree, from, false);
			else if(gender=='M')
				from=getParent(tree, from, true);
			else throw new Exception("Do not know gender!");
			nodes[i]=from;			
		}
		return nodes;
	}

}
