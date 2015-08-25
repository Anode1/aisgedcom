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

import java.util.Hashtable;
import java.util.regex.*;

/**
 * Caches patterns
 */
public class RegMatcher {

	private static Hashtable cache = new Hashtable();
	
	
	/**
     * We use null for 'All' - not to make matching with ".*" - when no need   
     */
    public static boolean matches(String string, String pattern) throws Exception{
        
    	if(pattern==null) //we use null to match with everything for efficiency 
        	return true;  //('All' - the most frequent value been transformed to null)
        		
        Pattern p = (Pattern)cache.get(pattern);
        if(p==null){
        	p = Pattern.compile(pattern);
        	cache.put(pattern, p);
        }
       
        return p.matcher(string).matches();
    }
    
}
