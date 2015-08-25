package org.is.ais.gedcom.tworelatives;


import org.is.ais.gedcom.Constants;
import org.is.ais.gedcom.Parameters;


public class RelationsMap {
	
	
	public String[] get(String path1, String path2) throws Exception{
		//delegate to plugins (implementing different languages relations logic) 
		//(for now we just do simple if. Class.forName() will be used - when more languages will be added and class mapping)
		RelationsBase relations;
		
		String lang = Parameters.getString(Constants.LANG_KEY);
		if(lang!=null && lang.startsWith("ru")){
			relations=new Relations_ru();
		}
		else
			relations=new Relations_en();
		
		return relations.get(path1, path2);
	}
	
	
	/**
	 * Convenience method for unit tests 
	 */
	public String getString(String path1, String path2) throws Exception{
		String[] relations=get(path1, path2);
		
		return relations[0]+", "+relations[1];
	}	
	
	
}
