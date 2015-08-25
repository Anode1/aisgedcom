/*
 	Copyright (C) 2009 Vasili Gavrilov

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
package org.is.ais.gedcom.gui;


import org.is.ais.gedcom.Constants;
import org.is.ais.gedcom.Parameters;


/**
 * User-specific (saved) properties. If not found - delegating lookup of a 
 * property to main properties (Config properties)
 * 
 * @since jdk1.0
 */
public class UserParameters extends java.util.Properties{
	
	public static final String USER_DEFINED_PROPERTIES_FILENAME=".properties";	
	
	/**
	 * The single instance
	 */
	private static UserParameters instance=new UserParameters();

	/**
	 * Returns single instance of this class
	 */
	static UserParameters getInstance(){

		return instance;
	}

	/**
	 * Returns a property by key as String
	 */
	static String getString(String key){

		String value=instance.getProperty(key);
		if(value==null) //delegate to default
			return Parameters.getString(key);

		return value;
	}


	/**
	 * Adds a property overriding the old value if the value with the same key exists
	 * already
	 */
	static void addProperty(String key, String value){

		if(key==null || value==null)
			return;

		//save only when not the default (i.e. actually changed by user)
		//IMPLEMENT ME - if more properties will be supported!
		//if(!key.equals(Parameters.getString(key))

		instance.put(key, value);
	}


	static String getInputFile()throws Exception{
		String value = getString(Constants.INPUT_FILE_KEY);
		if(value!=null)
			return value;

		//default
		return Config.getDefaultInputFile();
	}


	static void saveInputFile(String value){
		if(value==null)
			return;
		value=value.trim();
		//if not defaults (either coming from config file or hardcoded) - save 
		if(!value.equals(Config.getDefaultInputFile())){
			instance.put(Constants.INPUT_FILE_KEY, value);
		}
		else{
			instance.remove(Constants.INPUT_FILE_KEY); //clear 
		}
	}
	
	
	static String getId()throws Exception{
		String value = getString(Constants.START_ID_KEY);
		if(value!=null)
			return value;

		//default
		return Config.getDefaultId();
	}


	static void saveId(String value){
		if(value==null)
			return;
		value=value.trim();
		//if not defaults (either coming from config file or hardcoded) - save 
		if(!value.equals(Config.getDefaultId())){
			instance.put(Constants.START_ID_KEY, value);
		}
		else{
			instance.remove(Constants.START_ID_KEY); //clear 
		}
	}	
	
}
